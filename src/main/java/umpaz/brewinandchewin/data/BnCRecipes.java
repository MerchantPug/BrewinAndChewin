package umpaz.brewinandchewin.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import umpaz.brewinandchewin.data.recipe.*;

import java.util.function.Consumer;

public class BnCRecipes extends RecipeProvider
{
    public BnCRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        KegFermentingRecipes.register(consumer);
        KegPouringRecipes.register(consumer);
        BnCCookingPotRecipes.register(consumer);
        BnCCraftingRecipes.register(consumer);
        BnCCuttingBoardRecipes.register(consumer);
    }
}