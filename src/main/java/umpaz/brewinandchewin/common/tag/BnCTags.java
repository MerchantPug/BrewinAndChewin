package umpaz.brewinandchewin.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import umpaz.brewinandchewin.BrewinAndChewin;

public class BnCTags {

    public static final TagKey<Item> HORROR_MEATS = modItemTag("horror_meats");
    public static final TagKey<Item> RAW_MEATS = modItemTag("raw_meats");
    public static final TagKey<Item> PIZZA_TOPPINGS = modItemTag("pizza_toppings");
    public static final TagKey<Item> CHEESE_WEDGES = modItemTag("cheese_wedges");
    public static final TagKey<Item> TIPSY_REDUCER = modItemTag("tipsy_reducer");

    public static final TagKey<Block> FREEZE_SOURCES = modBlockTag("freeze_sources");

    public static final TagKey<Fluid> KEG_BLACKLIST = modFluidTag("keg_blacklist");

    public static final TagKey<MobEffect> MILK_BOTTLES_CANNOT_REMOVE = modMobEffectTag("milk_bottles_cannot_remove");
    public static final TagKey<MobEffect> HOT_COCOA_CANNOT_REMOVE = modMobEffectTag("hot_cocoa_cannot_remove");


    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }

    private static TagKey<Fluid> modFluidTag(String path) {
        return FluidTags.create(new ResourceLocation(BrewinAndChewin.MODID, path));
    }

    private static TagKey<MobEffect> modMobEffectTag(String path) {
        return TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(BrewinAndChewin.MODID, path));
    }
}
