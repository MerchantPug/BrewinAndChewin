package umpaz.brewinandchewin.integration.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CheeseAgingRecipe implements Recipe<RecipeWrapper> {


   Item before;
   Item after;

   public CheeseAgingRecipe( Item before, Item after ) {
      this.before = before;
      this.after = after;
   }

   @Override
   public boolean matches( RecipeWrapper recipeWrapper, Level level ) {
      return false;
   }

   @Override
   public ItemStack assemble( RecipeWrapper recipeWrapper, RegistryAccess registryAccess ) {
      return null;
   }

   @Override
   public boolean canCraftInDimensions( int i, int i1 ) {
      return false;
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      NonNullList<Ingredient> retVal = NonNullList.create();
      retVal.add(Ingredient.of(this.before));
      return retVal;
   }

   @Override
   public ItemStack getResultItem( RegistryAccess registryAccess ) {
      return new ItemStack(this.after, 1);
   }

   @Override
   public ResourceLocation getId() {
      return null;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return null;
   }

   @Override
   public RecipeType<?> getType() {
      return null;
   }
}
