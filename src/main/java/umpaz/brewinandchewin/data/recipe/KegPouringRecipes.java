package umpaz.brewinandchewin.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import umpaz.brewinandchewin.common.registry.BnCFluids;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.data.builder.KegFermentingRecipeBuilder;
import umpaz.brewinandchewin.data.builder.KegPouringRecipeBuilder;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

public class KegPouringRecipes {

    public static void register(Consumer<FinishedRecipe> consumer) {
        cookMiscellaneous(consumer);
    }

    private static void cookMiscellaneous(Consumer<FinishedRecipe> consumer) {
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.BEER.get(), 250, BnCItems.BEER.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.VODKA.get(), 250, BnCItems.VODKA.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.MEAD.get(), 250, BnCItems.MEAD.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.RICE_WINE.get(), 250, BnCItems.RICE_WINE.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.EGG_GROG.get(), 250, BnCItems.EGG_GROG.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.STRONGROOT_ALE.get(), 250, BnCItems.STRONGROOT_ALE.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.SACCHARINE_RUM.get(), 250, BnCItems.SACCHARINE_RUM.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.PALE_JANE.get(), 250, BnCItems.PALE_JANE.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.SALTY_FOLLY.get(), 250, BnCItems.SALTY_FOLLY.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.STEEL_TOE_STOUT.get(), 250, BnCItems.STEEL_TOE_STOUT.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.GLITTERING_GRENADINE.get(), 250, BnCItems.GLITTERING_GRENADINE.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.BLOODY_MARY.get(), 250, BnCItems.BLOODY_MARY.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.RED_RUM.get(), 250, BnCItems.RED_RUM.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.WITHERING_DROSS.get(), 250, BnCItems.WITHERING_DROSS.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.DREAD_NOG.get(), 250, BnCItems.DREAD_NOG.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(BnCItems.TANKARD.get(), BnCFluids.KOMBUCHA.get(), 250, BnCItems.KOMBUCHA.get())
                .build(consumer);

        KegPouringRecipeBuilder.kegPouringRecipe(Items.GLASS_BOTTLE, ForgeMod.MILK.get(), 250, ModItems.MILK_BOTTLE.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.GLASS_BOTTLE, Fluids.WATER, 250, Items.POTION.getDefaultInstance())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.GLASS_BOTTLE, BnCFluids.HONEY_FLUID.get(), 250, Items.HONEY_BOTTLE)
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.BUCKET, Fluids.WATER, 1000, Items.WATER_BUCKET)
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.BUCKET, ForgeMod.MILK.get(), 1000, Items.MILK_BUCKET)
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.BUCKET, Fluids.LAVA, 1000, Items.LAVA_BUCKET)
                .build(consumer);

        KegPouringRecipeBuilder.kegPouringRecipe(Items.HONEYCOMB, BnCFluids.FLAXEN_CHEESE.get(), 1000, BnCItems.UNRIPE_FLAXEN_CHEESE_WHEEL.get())
                .build(consumer);
        KegPouringRecipeBuilder.kegPouringRecipe(Items.HONEYCOMB, BnCFluids.SCARLET_CHEESE.get(), 1000, BnCItems.UNRIPE_SCARLET_CHEESE_WHEEL.get())
                .build(consumer);
    }
}
