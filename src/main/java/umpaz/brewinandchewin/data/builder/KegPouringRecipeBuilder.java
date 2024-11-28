package umpaz.brewinandchewin.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCRecipeSerializers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class KegPouringRecipeBuilder {
   private final ItemStack container;
    private final Fluid fluid;
    private final int amount;
   private final ItemStack output;

   private KegPouringRecipeBuilder( ItemStack container, Fluid fluid, int amount, ItemStack output ) {
      this.container = container;
        this.fluid = fluid;
        this.amount = amount;
      this.output = output;
   }

   public static KegPouringRecipeBuilder kegPouringRecipe( ItemLike container, Fluid fluid, int amount, ItemStack output ) {
      return new KegPouringRecipeBuilder(container.asItem().getDefaultInstance(), fluid, amount, output);
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(ItemLike container, Fluid fluid, int amount, ItemLike output) {
       return new KegPouringRecipeBuilder(container.asItem().getDefaultInstance(), fluid, amount, output.asItem().getDefaultInstance());
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
       ResourceLocation outputLocation = ForgeRegistries.ITEMS.getKey(output.getItem());
        build(consumerIn, BrewinAndChewin.MODID + ":pouring/" + outputLocation.getPath());
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
       ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(output.getItem());
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Pouring Recipe " + save + " should remove its 'save' argument");
        } else {
            build(consumerIn, new ResourceLocation(save));
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
            consumerIn.accept(new KegPouringRecipeBuilder.Result(id, container, fluid, amount, output));

       new ProcessingRecipeBuilder<>(FillingRecipe::new, new ResourceLocation(BrewinAndChewin.MODID, "create/" + id.getPath().replace("pouring/", "")))
               .require(fluid, amount)
               .require(container.getItem())
               .output(output)
               .withCondition(new ModLoadedCondition("create"))
               .build(consumerIn);

       new ProcessingRecipeBuilder<>(EmptyingRecipe::new, new ResourceLocation(BrewinAndChewin.MODID, "create/" + id.getPath().replace("pouring/", "")))
               .require(output.getItem())
               .output(fluid, amount)
               .output(container)
               .withCondition(new ModLoadedCondition("create"))
               .build(consumerIn);
    }


    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
       private final ItemStack container;
        private final Fluid fluid;
        private final int amount;
       private final ItemStack output;

       public Result( ResourceLocation idIn, ItemStack containerIn, Fluid fluidIn, int amountIn, ItemStack outputIn ) {
            this.id = idIn;
            this.container = containerIn;
            this.fluid = fluidIn;
            this.amount = amountIn;
            this.output = outputIn;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {

            JsonObject objectContainer = new JsonObject();
           objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(container.getItem()).toString());
            json.add("container", objectContainer);
           if ( container.hasTag() ) {
              objectContainer.addProperty("nbt", output.getTag().toString());
           }

            JsonObject objectContainer1 = new JsonObject();
           objectContainer1.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
           if ( output.hasTag() ) {
              objectContainer1.addProperty("nbt", output.getTag().toString());
           }
            json.add("output", objectContainer1);

            json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());


            json.addProperty("amount", amount);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BnCRecipeSerializers.KEG_POURING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
