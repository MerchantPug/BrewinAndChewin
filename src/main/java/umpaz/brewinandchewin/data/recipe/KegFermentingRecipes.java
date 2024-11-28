package umpaz.brewinandchewin.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import umpaz.brewinandchewin.common.registry.BnCFluids;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.tag.BnCTags;
import umpaz.brewinandchewin.data.builder.KegFermentingRecipeBuilder;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class KegFermentingRecipes {
   public static final int NORMAL_COOKING = 6000;    // 5 minutes
   public static final int SLOW_COOKING = 12000;      // 10 minutes


   public static final float MEDIUM_EXP = 1.0F;
   public static final float LARGE_EXP = 2.0F;

   public static void register( Consumer<FinishedRecipe> consumer ) {
      cookMiscellaneous(consumer);
   }

   private static void cookMiscellaneous( Consumer<FinishedRecipe> consumer ) {
      ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.BEER.get(), 1000, SLOW_COOKING, MEDIUM_EXP)
              .addFluidIngredient(Fluids.WATER, 1000)
              .addIngredient(Items.WHEAT)
              .addIngredient(Items.WHEAT_SEEDS)
              .addIngredient(Items.BROWN_MUSHROOM)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.VODKA.get(), 1000, SLOW_COOKING, MEDIUM_EXP)
              .addFluidIngredient(Fluids.WATER, 1000)
              .addIngredient(ForgeTags.VEGETABLES_POTATO)
              .addIngredient(Items.WHEAT)
              .addIngredient(Items.WHEAT_SEEDS)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.MEAD.get(), 1000, SLOW_COOKING, MEDIUM_EXP)
              .addFluidIngredient(BnCFluids.HONEY_FLUID.get(), 1000)
              .addIngredient(Items.WHEAT)
              .addIngredient(Items.WHEAT_SEEDS)
              .addIngredient(Items.SWEET_BERRIES)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.EGG_GROG.get(), 1000, SLOW_COOKING, MEDIUM_EXP)
              .addFluidIngredient(ForgeMod.MILK.get(), 1000)
              .addIngredient(ForgeTags.EGGS)
              .addIngredient(ForgeTags.CROPS_CABBAGE)
              .addIngredient(Items.SUGAR)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.STRONGROOT_ALE.get(), 1000, NORMAL_COOKING, MEDIUM_EXP)
              .addFluidIngredient(BnCFluids.BEER.get(), 1000)
              .addIngredient(ForgeTags.VEGETABLES_BEETROOT)
              .addIngredient(ForgeTags.VEGETABLES_POTATO)
              .addIngredient(Items.BROWN_MUSHROOM)
              .addIngredient(ForgeTags.VEGETABLES_CARROT)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.RICE_WINE.get(), 1000, SLOW_COOKING, MEDIUM_EXP)
              .addFluidIngredient(Fluids.WATER, 1000)
              .addIngredient(ForgeTags.CROPS_RICE)
              .addIngredient(Items.BROWN_MUSHROOM)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.GLITTERING_GRENADINE.get(), 1000, SLOW_COOKING, MEDIUM_EXP, 2)
              .addFluidIngredient(Fluids.WATER, 1000)
              .addIngredient(Items.GLOW_BERRIES)
              .addIngredient(Items.GLOWSTONE_DUST)
              .addIngredient(Items.GLOW_INK_SAC)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.STEEL_TOE_STOUT.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 1)
              .addFluidIngredient(BnCFluids.STRONGROOT_ALE.get(), 1000)
              .addIngredient(Items.IRON_INGOT)
              .addIngredient(Items.CRIMSON_FUNGUS)
              .addIngredient(Items.NETHER_WART)
              .addIngredient(Items.WHEAT)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.DREAD_NOG.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 1)
              .addFluidIngredient(BnCFluids.EGG_GROG.get(), 1000)
              .addIngredient(ForgeTags.EGGS)
              .addIngredient(Items.TURTLE_EGG)
              .addIngredient(Items.FERMENTED_SPIDER_EYE)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.SACCHARINE_RUM.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 4)
              .addFluidIngredient(BnCFluids.MEAD.get(), 1000)
              .addIngredient(Items.SWEET_BERRIES)
              .addIngredient(Items.SUGAR_CANE)
              .addIngredient(Items.MELON)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.PALE_JANE.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 4)
              .addFluidIngredient(BnCFluids.RICE_WINE.get(), 1000)
              .addIngredient(Items.HONEY_BOTTLE)
              .addIngredient(ModItems.TREE_BARK.get())
              .addIngredient(Items.LILY_OF_THE_VALLEY)
              .addIngredient(Items.SUGAR)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.SALTY_FOLLY.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 2)
              .addFluidIngredient(BnCFluids.VODKA.get(), 1000)
              .addIngredient(Items.SEA_PICKLE)
              .addIngredient(Items.DRIED_KELP)
              .addIngredient(Items.SEAGRASS)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.BLOODY_MARY.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 4)
              .addFluidIngredient(BnCFluids.VODKA.get(), 1000)
              .addIngredient(ForgeTags.CROPS_TOMATO)
              .addIngredient(ForgeTags.CROPS_CABBAGE)
              .addIngredient(Items.SWEET_BERRIES)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.RED_RUM.get(), 1000, NORMAL_COOKING, MEDIUM_EXP, 5)
              .addFluidIngredient(BnCFluids.BLOODY_MARY.get(), 1000)
              .addIngredient(Items.CRIMSON_FUNGUS)
              .addIngredient(Items.NETHER_WART)
              .addIngredient(Items.FERMENTED_SPIDER_EYE)
              .addIngredient(Items.SHROOMLIGHT)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.WITHERING_DROSS.get(), 1000, SLOW_COOKING, LARGE_EXP, 5)
              .addFluidIngredient(BnCFluids.SALTY_FOLLY.get(), 1000)
              .addIngredient(Items.WITHER_ROSE)
              .addIngredient(Items.INK_SAC)
              .addIngredient(Items.NETHER_WART)
              .addIngredient(Items.BONE)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.FLAXEN_CHEESE.get(), 1000, SLOW_COOKING, MEDIUM_EXP, 4)
              .addFluidIngredient(ForgeMod.MILK.get(), 1000)
              .addIngredient(Items.BROWN_MUSHROOM)
              .addIngredient(Items.PUMPKIN_SEEDS)
              .addIngredient(Items.SUGAR)
              .build(consumer);
      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCFluids.SCARLET_CHEESE.get(), 1000, SLOW_COOKING, MEDIUM_EXP, 4)
              .addFluidIngredient(ForgeMod.MILK.get(), 1000)
              .addIngredient(Items.CRIMSON_FUNGUS)
              .addIngredient(Items.NETHER_WART)
              .addIngredient(Items.SUGAR)
              .build(consumer);


      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCItems.JERKY.get(), 3, SLOW_COOKING, MEDIUM_EXP, 4)
              .addIngredient(BnCTags.RAW_MEATS)
              .addIngredient(BnCTags.RAW_MEATS)
              .addIngredient(BnCTags.RAW_MEATS)
              .build(consumer);


      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCItems.KIMCHI.get(), 2, SLOW_COOKING, MEDIUM_EXP, 4)
              .addIngredient(ForgeTags.CROPS_CABBAGE)
              .addIngredient(ForgeTags.VEGETABLES)
              .addIngredient(Items.KELP)
              .build(consumer);

      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCItems.KIPPERS.get(), 3, SLOW_COOKING, MEDIUM_EXP, 4)
              .addIngredient(ForgeTags.RAW_FISHES)
              .addIngredient(ForgeTags.RAW_FISHES)
              .addIngredient(Items.KELP)
              .build(consumer);

      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCItems.PICKLED_PICKLES.get(), 2, SLOW_COOKING, MEDIUM_EXP, 2)
              .addFluidIngredient(BnCFluids.HONEY_FLUID.get(), 250)
              .addIngredient(Items.SEA_PICKLE)
              .addIngredient(Items.SEA_PICKLE)
              .addIngredient(Items.GLOWSTONE_DUST)
              .build(consumer);

      KegFermentingRecipeBuilder.kegFermentingRecipe(BnCItems.COCOA_FUDGE.get(), 1, SLOW_COOKING, MEDIUM_EXP, 2)
              .addFluidIngredient(ForgeMod.MILK.get(), 500)
              .addIngredient(Items.SUGAR)
              .addIngredient(Items.COCOA_BEANS)
              .addIngredient(Items.COCOA_BEANS)
              .build(consumer);
   }


}
