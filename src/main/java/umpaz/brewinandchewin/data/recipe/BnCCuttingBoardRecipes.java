package umpaz.brewinandchewin.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.data.builder.BnCCuttingRecipeBuilder;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Consumer;

public class BnCCuttingBoardRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        // Knife
        cuttingRecipes(consumer);

    }

    private static void cuttingRecipes(Consumer<FinishedRecipe> consumer) {
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.FLAXEN_CHEESE_WHEEL.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), BnCItems.FLAXEN_CHEESE_WEDGE.get(), 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.SCARLET_CHEESE_WHEEL.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), BnCItems.SCARLET_CHEESE_WEDGE.get(), 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.QUICHE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), BnCItems.QUICHE_SLICE.get(), 4)
                .build(consumer);
        BnCCuttingRecipeBuilder.cuttingRecipe(Ingredient.of(BnCItems.PIZZA.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), BnCItems.PIZZA_SLICE.get(), 4)
                .build(consumer);
    }
}
