package umpaz.brewinandchewin.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;


import java.util.*;

public class BnCJEIRecipes {

    private final RecipeManager recipeManager;

    public BnCJEIRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level != null) {
            this.recipeManager = level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft world must not be null.");
        }
    }

    public List<KegFermentingPouringRecipe> getKegRecipes() {

        List<KegFermentingRecipe> ferms = recipeManager.getAllRecipesFor(BnCRecipeTypes.FERMENTING.get());
        List<KegPouringRecipe> pours = recipeManager.getAllRecipesFor(BnCRecipeTypes.KEG_POURING.get());

        List<KegFermentingPouringRecipe> kegRecipes = new ArrayList<>();

        // add all of ferms
        for (KegFermentingRecipe fermentingRecipe : ferms) {
            if (fermentingRecipe.getResultFluid() != null) {
                for (KegPouringRecipe pouringRecipe : pours) {
                    if (pouringRecipe.getRawFluid().isSame(fermentingRecipe.getResultFluid())) {
                        kegRecipes.add(new KegFermentingPouringRecipe(fermentingRecipe, pouringRecipe));
                    }
                }
            }
            else {
                kegRecipes.add(new KegFermentingPouringRecipe(fermentingRecipe, null));
            }
        }


        return kegRecipes;
    }


   public List<CheeseAgingRecipe> getCheeseRecipes() {
      List<CheeseAgingRecipe> cheese = new ArrayList<>();


      cheese.add(new CheeseAgingRecipe(BnCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL.get().asItem(), BnCBlocks.FLAXEN_CHEESE_WHEEL.get().asItem()));
      cheese.add(new CheeseAgingRecipe(BnCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL.get().asItem(), BnCBlocks.SCARLET_CHEESE_WHEEL.get().asItem()));

      // find every instance of Unripe Cheese Wheel block, and call the supplier :)
      return cheese;
   }
}
