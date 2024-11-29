package umpaz.brewinandchewin.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import umpaz.brewinandchewin.BrewinAndChewin;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.tag.ModTags;

public class BnCTags {

    public static final TagKey<Item> HORROR_MEATS = modItemTag("horror_meats");
    public static final TagKey<Item> RAW_MEATS = modItemTag("raw_meats");
    public static final TagKey<Item> PIZZA_TOPPINGS = modItemTag("pizza_toppings");
    public static final TagKey<Item> CHEESE_WEDGES = modItemTag("cheese_wedges");
    public static final TagKey<Item> PLACEABLE_ON_COASTER = modItemTag("placeable_on_coaster");

    public static final TagKey<Block> FREEZE_SOURCES = modBlockTag("freeze_sources");

    public static final TagKey<Fluid> KEG_BLACKLIST = modFluidTag("keg_blacklist");


    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }

    private static TagKey<Fluid> modFluidTag(String path) {
        return FluidTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }
}
