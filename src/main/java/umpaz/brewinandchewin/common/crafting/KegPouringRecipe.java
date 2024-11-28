package umpaz.brewinandchewin.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.registry.BnCRecipeSerializers;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import javax.annotation.Nullable;

public class KegPouringRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final Fluid fluid;
    private final int amount;
    private final ItemStack container;
    private final ItemStack output;

    public KegPouringRecipe(ResourceLocation id, Fluid fluid, ItemStack container, ItemStack output, int amount) {
        this.id = id;
        this.amount = amount;
        this.fluid = fluid;
        this.container = container;
        this.output = output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredient = NonNullList.create();
        ingredient.add(Ingredient.of(this.container));
        return ingredient;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        return Ingredient.of(this.container).test(inv.getItem(4));
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getContainer(){
        return this.container;
    }

    public ItemStack getOutput(){
        return this.output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output;
    }

    public int getAmount() {
        return this.amount;
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BnCRecipeSerializers.KEG_POURING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BnCRecipeTypes.KEG_POURING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BnCItems.KEG.get());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + container.hashCode();
        //result = 31 * result + output.hashCode();
        result = 31 * result + fluid.hashCode();
        result = 31 * result + amount;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KegPouringRecipe that = (KegPouringRecipe) o;

        if (!getId().equals(that.getId())) return false;
        if (!output.equals(that.output)) return false;
        if (amount != that.amount) return false;
        if (!fluid.equals(that.fluid)) return false;
        return container.equals(that.container);
    }

    public static class Serializer implements RecipeSerializer<KegPouringRecipe> {
        public Serializer() {
        }

        @Override
        public KegPouringRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final Fluid fluidIn = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "fluid")));
            final int amountIn = GsonHelper.getAsInt(json, "amount", 250);
            ItemStack container = GsonHelper.isValidNode(json, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true) : ItemStack.EMPTY;
            final ItemStack outputIn = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
            return new KegPouringRecipe(recipeId, fluidIn, container, outputIn, amountIn);
        }

        @Nullable
        @Override
        public KegPouringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Fluid fluidIn = buffer.readFluidStack().getFluid();
            int amountIn = buffer.readVarInt();
            ItemStack containerIn = buffer.readItem();
            ItemStack outputIn = buffer.readItem();
            return new KegPouringRecipe(recipeId, fluidIn, containerIn, outputIn, amountIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KegPouringRecipe recipe) {
            buffer.writeFluidStack(new FluidStack(recipe.fluid, 1000));
            buffer.writeVarInt(recipe.amount);
            buffer.writeItem(recipe.container);
            buffer.writeItem(recipe.output);
        }
    }
}
