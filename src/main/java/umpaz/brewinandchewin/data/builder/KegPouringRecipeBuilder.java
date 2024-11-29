package umpaz.brewinandchewin.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCRecipeSerializers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KegPouringRecipeBuilder {
    private final ItemStack container;
    private final Fluid fluid;
    private final int amount;
    private final ItemStack output;
    private final boolean strict;
    private final List<ICondition> conditions = new ArrayList<>();

    private KegPouringRecipeBuilder(ItemStack container, Fluid fluid, int amount, ItemStack output, boolean strict) {
        this.container = container;
        this.fluid = fluid;
        this.amount = amount;
        this.output = output;
        this.strict = strict;
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(ItemLike container, Fluid fluid, int amount, ItemStack output, boolean strict) {
        return new KegPouringRecipeBuilder(container.asItem().getDefaultInstance(), fluid, amount, output, strict);
    }

    public static KegPouringRecipeBuilder kegPouringRecipe(ItemLike container, Fluid fluid, int amount, ItemLike output) {
        return new KegPouringRecipeBuilder(container.asItem().getDefaultInstance(), fluid, amount, output.asItem().getDefaultInstance(), false);
    }

    public KegPouringRecipeBuilder withCondition(ICondition condition) {
        conditions.add(condition);
        return this;
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
        consumerIn.accept(new KegPouringRecipeBuilder.Result(id, container, fluid, amount, output, strict, conditions));

        if (ForgeRegistries.ITEMS.getKey(output.getItem()).getNamespace().equals("create"))
            return;

        var fillingBuilder = new ProcessingRecipeBuilder<>(FillingRecipe::new, new ResourceLocation(BrewinAndChewin.MODID, "create/" + id.getPath().replace("pouring/", "")))
                .require(fluid, amount)
                .require(container.getItem())
                .output(output)
                .withCondition(new ModLoadedCondition("create"));

        var emptyingBuilder = new ProcessingRecipeBuilder<>(EmptyingRecipe::new, new ResourceLocation(BrewinAndChewin.MODID, "create/" + id.getPath().replace("pouring/", "")))
                .output(fluid, amount)
                .output(container)
                .withCondition(new ModLoadedCondition("create"));

        if (strict)
            emptyingBuilder.require(StrictNBTIngredient.of(output));
        else
            emptyingBuilder.require(output.getItem());

        for (ICondition condition : conditions) {
            fillingBuilder.withCondition(condition);
            emptyingBuilder.withCondition(condition);
        }

        fillingBuilder.build(consumerIn);
        emptyingBuilder.build(consumerIn);
    }


    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack container;
        private final Fluid fluid;
        private final int amount;
        private final ItemStack output;
        private final boolean strict;
        private final List<ICondition> conditions;

        public Result(ResourceLocation idIn, ItemStack containerIn, Fluid fluidIn, int amountIn, ItemStack outputIn, boolean strict, List<ICondition> conditions) {
            this.id = idIn;
            this.container = containerIn;
            this.fluid = fluidIn;
            this.amount = amountIn;
            this.output = outputIn;
            this.strict = strict;
            this.conditions = conditions;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {

            JsonObject objectContainer = new JsonObject();
            objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(container.getItem()).toString());
            json.add("container", objectContainer);
            if (container.hasTag()) {
                objectContainer.addProperty("nbt", container.getTag().toString());
            }

            JsonObject objectContainer1 = new JsonObject();
            objectContainer1.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            if (output.hasTag()) {
                objectContainer1.addProperty("nbt", output.getTag().toString());
            }
            json.add("output", objectContainer1);

            json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());


            json.addProperty("amount", amount);

            json.addProperty("strict", strict);

            if (!conditions.isEmpty()) {
                JsonArray conditions = new JsonArray();
                this.conditions.forEach(CraftingHelper::serialize);
                json.add("conditions", conditions);
            }
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
