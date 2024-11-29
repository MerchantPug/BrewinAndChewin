package umpaz.brewinandchewin.common.crafting;

import com.google.gson.JsonObject;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.common.registry.BnCRecipeSerializers;
import umpaz.brewinandchewin.common.utility.BnCRecipeWrapper;

import javax.annotation.Nullable;

public class CreatePotionPouringRecipe extends KegPouringRecipe {

    public CreatePotionPouringRecipe(ResourceLocation id, ItemStack container, int amount) {
        super(id, AllFluids.POTION.get().getSource(), container, Items.POTION.getDefaultInstance(), amount, false);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredient = NonNullList.create();
        ingredient.add(DifferenceIngredient.of(Ingredient.of(Items.POTION), StrictNBTIngredient.of(Items.POTION.getDefaultInstance())));
        return ingredient;
    }

    @Override
    public ItemStack assemble(BnCRecipeWrapper recipeWrapper, RegistryAccess registryAccess) {
        ItemStack stack = new ItemStack(Items.POTION);
        FluidStack fluidStack = recipeWrapper.getFluid(0);
        if (fluidStack.getTag() != null) {
            Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(fluidStack.getTag().getString("Potion")));
            if (potion != null)
                PotionUtils.setPotion(stack, potion);
        }
        return stack;
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        FluidStack fluidStack = super.getFluid(container);
        Potion potion = PotionUtils.getPotion(container);
        PotionFluid.addPotionToFluidStack(fluidStack, potion);
        return fluidStack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BnCRecipeSerializers.CREATE_POTION_POURING.get();
    }

    public static class Serializer implements RecipeSerializer<CreatePotionPouringRecipe> {
        public Serializer() {
        }

        @Override
        public CreatePotionPouringRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final int amountIn = GsonHelper.getAsInt(json, "amount", 250);
            final ItemStack containerIn = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true);
            return new CreatePotionPouringRecipe(recipeId, containerIn, amountIn);
        }

        @Nullable
        @Override
        public CreatePotionPouringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int amountIn = buffer.readVarInt();
            ItemStack containerIn = buffer.readItem();
            return new CreatePotionPouringRecipe(recipeId, containerIn, amountIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CreatePotionPouringRecipe recipe) {
            buffer.writeVarInt(recipe.getAmount());
            buffer.writeItem(recipe.getContainer());
        }
    }
}
