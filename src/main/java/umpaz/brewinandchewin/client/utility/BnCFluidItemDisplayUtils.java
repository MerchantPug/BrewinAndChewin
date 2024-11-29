package umpaz.brewinandchewin.client.utility;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import umpaz.brewinandchewin.common.registry.BnCFluids;
import umpaz.brewinandchewin.common.registry.BnCItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.HashMap;
import java.util.Map;

public class BnCFluidItemDisplayUtils {
    private static final Map<FluidType, ItemStack> FLUID_TYPE_TO_ITEM_MAP = new HashMap<>();

    public static void defaultFluidToItems() {
        setFluidItemDisplay(ForgeMod.WATER_TYPE.get(), Items.POTION);
        setFluidItemDisplay(ForgeMod.MILK_TYPE.get(), ModItems.MILK_BOTTLE.get());
        setFluidItemDisplay(BnCFluids.HONEY_FLUID_TYPE.get(), Items.HONEY_BOTTLE);
        setFluidItemDisplay(BnCFluids.BEER_FLUID_TYPE.get(), BnCItems.BEER.get());
        setFluidItemDisplay(BnCFluids.VODKA_FLUID_TYPE.get(), BnCItems.VODKA.get());
        setFluidItemDisplay(BnCFluids.MEAD_FLUID_TYPE.get(), BnCItems.MEAD.get());
        setFluidItemDisplay(BnCFluids.EGG_GROG_FLUID_TYPE.get(), BnCItems.EGG_GROG.get());
        setFluidItemDisplay(BnCFluids.STRONGROOT_ALE_FLUID_TYPE.get(), BnCItems.STRONGROOT_ALE.get());
        setFluidItemDisplay(BnCFluids.RICE_WINE_FLUID_TYPE.get(), BnCItems.RICE_WINE.get());
        setFluidItemDisplay(BnCFluids.GLITTERING_GRENADINE_FLUID_TYPE.get(), BnCItems.GLITTERING_GRENADINE.get());
        setFluidItemDisplay(BnCFluids.STEEL_TOE_STOUT_FLUID_TYPE.get(), BnCItems.STEEL_TOE_STOUT.get());
        setFluidItemDisplay(BnCFluids.DREAD_NOG_FLUID_TYPE.get(), BnCItems.DREAD_NOG.get());
        setFluidItemDisplay(BnCFluids.KOMBUCHA_FLUID_TYPE.get(), BnCItems.KOMBUCHA.get());
        setFluidItemDisplay(BnCFluids.SACCHARINE_RUM_FLUID_TYPE.get(), BnCItems.SACCHARINE_RUM.get());
        setFluidItemDisplay(BnCFluids.PALE_JANE_FLUID_TYPE.get(), BnCItems.PALE_JANE.get());
        setFluidItemDisplay(BnCFluids.SALTY_FOLLY_FLUID_TYPE.get(), BnCItems.SALTY_FOLLY.get());
        setFluidItemDisplay(BnCFluids.BLOODY_MARY_FLUID_TYPE.get(), BnCItems.BLOODY_MARY.get());
        setFluidItemDisplay(BnCFluids.RED_RUM_FLUID_TYPE.get(), BnCItems.RED_RUM.get());
        setFluidItemDisplay(BnCFluids.WITHERING_DROSS_FLUID_TYPE.get(), BnCItems.WITHERING_DROSS.get());
        setFluidItemDisplay(BnCFluids.FLAXEN_CHEESE_FLUID_TYPE.get(), BnCItems.FLAXEN_CHEESE_WHEEL.get());
        setFluidItemDisplay(BnCFluids.SCARLET_CHEESE_FLUID_TYPE.get(), BnCItems.SCARLET_CHEESE_WHEEL.get());
    }

    public static ItemStack getFluidItemDisplay(Fluid fluid) {
        if (FLUID_TYPE_TO_ITEM_MAP.containsKey(fluid.getFluidType()))
            return FLUID_TYPE_TO_ITEM_MAP.get(fluid.getFluidType());
        if (fluid.getBucket() != Items.AIR)
            return fluid.getBucket().getDefaultInstance();
        return ItemStack.EMPTY;
    }
    
    public static void setFluidItemDisplay(FluidType type, ItemLike item) {
        FLUID_TYPE_TO_ITEM_MAP.put(type, new ItemStack(item));
    }

    public static void setFluidItemDisplay(FluidType type, ItemStack stack) {
        FLUID_TYPE_TO_ITEM_MAP.put(type, stack);
    }
}
