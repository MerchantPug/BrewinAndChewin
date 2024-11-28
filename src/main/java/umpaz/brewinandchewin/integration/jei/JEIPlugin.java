package umpaz.brewinandchewin.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.client.gui.ScreenUtils;
import net.minecraftforge.fluids.FluidStack;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.gui.KegScreen;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.integration.jei.category.CheeseRecipeCategory;
import umpaz.brewinandchewin.integration.jei.category.FermentingRecipeCategory;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
    private static final ResourceLocation ID = new ResourceLocation(BrewinAndChewin.MODID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new FermentingRecipeCategory(registry.getJeiHelpers().getGuiHelper(), registry.getJeiHelpers().getModIdHelper(), registry.getJeiHelpers().getIngredientManager()));
        registry.addRecipeCategories(new CheeseRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        BnCJEIRecipes modRecipes = new BnCJEIRecipes();
        registration.addRecipes(BnCJEIRecipeTypes.FERMENTING, modRecipes.getKegRecipes());
        registration.addRecipes(BnCJEIRecipeTypes.AGING, modRecipes.getCheeseRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BnCItems.KEG.get()), BnCJEIRecipeTypes.FERMENTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
       IIngredientHelper<FluidStack> test = registration.getJeiHelpers().getIngredientManager().getIngredientHelper(ForgeTypes.FLUID_STACK);

       registration.addRecipeClickArea(KegScreen.class, 75, 25, 21, 17, BnCJEIRecipeTypes.FERMENTING);


       Rect2i bounds = new Rect2i(107, 18, 26, 30);

       registration.addGuiContainerHandler(KegScreen.class, new IGuiContainerHandler<>() {

          //          @Override
//          public Collection<IGuiClickableArea> getGuiClickableAreas(KegScreen containerScreen, double mouseX, double mouseY) {
//             return List.of(new IGuiClickableArea() {
//                @Override
//                public Rect2i getArea() {
//                   return bounds;
//                }
//
//                @Override
//                public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
//                   recipesGui.showTypes(recipeTypesList);
//                }
//             });
//          }
//
          @Override
          public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse( KegScreen containerScreen, double mouseX, double mouseY ) {
             if ( bounds.contains((int) mouseX - containerScreen.getGuiLeft(), (int) mouseY - containerScreen.getGuiTop()) ) {
                return Optional.of(new IClickableIngredient<FluidStack>() {
                   @Override
                   public ITypedIngredient<FluidStack> getTypedIngredient() {
                      Optional<ITypedIngredient<FluidStack>> aef = registration.getJeiHelpers().getIngredientManager().createTypedIngredient(ForgeTypes.FLUID_STACK, containerScreen.getMenu().blockEntity.getFluidTank().getFluid());
                      return aef.orElse(null);
                   }

                   @Override
                   public Rect2i getArea() {
                      return bounds;
                   }
                });
             }
             return Optional.empty();
          }
       });
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
       registration.addRecipeTransferHandler(KegMenu.class, BnCMenuTypes.KEG.get(), BnCJEIRecipeTypes.FERMENTING, 0, 5, 6, 36);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
