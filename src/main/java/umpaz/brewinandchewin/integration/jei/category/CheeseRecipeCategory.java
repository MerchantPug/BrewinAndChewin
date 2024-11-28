package umpaz.brewinandchewin.integration.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;
import umpaz.brewinandchewin.integration.jei.BnCJEIRecipeTypes;
import umpaz.brewinandchewin.integration.jei.CheeseAgingRecipe;
import umpaz.brewinandchewin.integration.jei.KegFermentingPouringRecipe;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheeseRecipeCategory implements IRecipeCategory<CheeseAgingRecipe> {
   public static final ResourceLocation UID = new ResourceLocation(BrewinAndChewin.MODID, "cheese");
   private final Component title;
   private final IDrawable background;
   private final IDrawable icon;


   public CheeseRecipeCategory( IGuiHelper helper ) {
      title = BnCTextUtils.getTranslation("jei.aging");
      ResourceLocation backgroundImage = new ResourceLocation(BrewinAndChewin.MODID, "textures/gui/jei/cheese_ripening.png");
      background = helper.createDrawable(backgroundImage, 0, 0, 118, 58);
      icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BnCItems.FLAXEN_CHEESE_WHEEL.get()));
   }

   @Override
   public RecipeType<CheeseAgingRecipe> getRecipeType() {
      return BnCJEIRecipeTypes.AGING;
   }

   @Override
   public Component getTitle() {
      return this.title;
   }

   @Override
   public IDrawable getBackground() {
      return this.background;
   }

   @Override
   public IDrawable getIcon() {
      return this.icon;
   }

   @Override
   public void setRecipe( IRecipeLayoutBuilder builder, CheeseAgingRecipe recipe, IFocusGroup focusGroup ) {
      builder.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(recipe.getIngredients().get(0).getItems()[0]);
      builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(RecipeUtils.getResultItem(recipe));
   }
}
