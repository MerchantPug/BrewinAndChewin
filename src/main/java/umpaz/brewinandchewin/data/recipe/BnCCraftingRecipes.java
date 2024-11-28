package umpaz.brewinandchewin.data.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.tag.BnCTags;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Consumer;

public class BnCCraftingRecipes {

    public static void register(Consumer<FinishedRecipe> consumer) {
        recipes(consumer);

    }

    private static void recipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, BnCItems.HAM_AND_CHEESE_SANDWICH.get(), 2)
                .requires(Items.BREAD)
                .requires(ModItems.SMOKED_HAM.get())
                .requires(BnCItems.FLAXEN_CHEESE_WEDGE.get())
                .requires(Items.BREAD)
                .unlockedBy("has_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.FLAXEN_CHEESE_WEDGE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "ham_and_cheese_sandwich"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.PIZZA.get())
                .pattern(" f ")
                .pattern("ptp")
                .pattern("www")
                .define('w', Items.WHEAT)
                .define('t', ModItems.TOMATO_SAUCE.get())
                .define('p', BnCTags.PIZZA_TOPPINGS)
                .define('f', BnCTags.CHEESE_WEDGES)
                .unlockedBy("has_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.FLAXEN_CHEESE_WEDGE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "pizza"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.QUICHE.get())
                .pattern("blb")
                .pattern("mcm")
                .pattern("eCe")
                .define('b', ModItems.BACON.get())
                .define('l', ModItems.CABBAGE_LEAF.get())
                .define('m', ModItems.MILK_BOTTLE.get())
                .define('c', BnCTags.CHEESE_WEDGES)
                .define('e', Items.EGG)
                .define('C', ModItems.PIE_CRUST.get())
                .unlockedBy("has_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "quiche_from_bacon"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.QUICHE.get())
                .pattern("blb")
                .pattern("mcm")
                .pattern("eCe")
                .define('b', Items.BROWN_MUSHROOM)
                .define('l', ModItems.CABBAGE_LEAF.get())
                .define('m', ModItems.MILK_BOTTLE.get())
                .define('c', BnCTags.CHEESE_WEDGES)
                .define('e', Items.EGG)
                .define('C', ModItems.PIE_CRUST.get())
                .unlockedBy("has_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "quiche_from_mushroom"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BnCItems.ITEM_COASTER.get(), 4)
                .pattern("cc")
                .define('c', ModItems.CANVAS.get())
                .unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "item_coaster"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.PIZZA.get())
                .pattern("pp")
                .pattern("pp")
                .define('p', BnCItems.PIZZA_SLICE.get())
                .unlockedBy("has_slice", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.PIZZA_SLICE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "pizza_from_slices"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.FLAXEN_CHEESE_WHEEL.get())
                .pattern("pp")
                .pattern("pp")
                .define('p', BnCItems.FLAXEN_CHEESE_WEDGE.get())
                .unlockedBy("has_slice", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.FLAXEN_CHEESE_WEDGE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "flaxen_cheese_wheel_from_wedges"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.SCARLET_CHEESE_WHEEL.get())
                .pattern("pp")
                .pattern("pp")
                .define('p', BnCItems.SCARLET_CHEESE_WEDGE.get())
                .unlockedBy("has_slice", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.SCARLET_CHEESE_WEDGE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "scarlet_cheese_wheel_from_wedges"));
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, BnCItems.QUICHE.get())
                .pattern("pp")
                .pattern("pp")
                .define('p', BnCItems.QUICHE_SLICE.get())
                .unlockedBy("has_slice", InventoryChangeTrigger.TriggerInstance.hasItems(BnCItems.QUICHE_SLICE.get()))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "quiche_from_slices"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BnCItems.KEG.get())
                .pattern("ipi")
                .pattern("ihi")
                .pattern("ppp")
                .define('i', Items.IRON_INGOT)
                .define('h', Items.HONEYCOMB)
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_honeycomb", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HONEYCOMB))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "keg"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BnCItems.TANKARD.get(), 4)
                .pattern("p p")
                .pattern("i i")
                .pattern("ppp")
                .define('i', Items.IRON_NUGGET)
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_nugget", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_NUGGET))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "tankard"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BnCItems.HEATING_CASK.get(), 1)
                .pattern("sss")
                .pattern("cCc")
                .pattern("pmp")
                .define('p', ItemTags.PLANKS)
                .define('s', ItemTags.WOODEN_SLABS)
                .define('c', Items.COAL_BLOCK)
                .define('C', Items.BLAZE_POWDER)
                .define('m', Items.MAGMA_BLOCK)
                .unlockedBy("has_powder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "heating_cask"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BnCItems.ICE_CRATE.get(), 1)
                .pattern("pSp")
                .pattern("sis")
                .pattern("psp")
                .define('i', Items.PACKED_ICE)
                .define('S', Items.STRING)
                .define('p', ItemTags.PLANKS)
                .define('s', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_ice", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PACKED_ICE))
                .save(consumer, new ResourceLocation(BrewinAndChewin.MODID, "ice_crate"));

    }
}
