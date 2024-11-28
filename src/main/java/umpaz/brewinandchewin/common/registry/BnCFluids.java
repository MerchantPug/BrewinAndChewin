package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.fluid.AlcoholFluidType;
import umpaz.brewinandchewin.common.fluid.CheeseFluidType;
import umpaz.brewinandchewin.common.fluid.HoneyFluidType;
import umpaz.brewinandchewin.common.fluid.MeadFluidType;

public class BnCFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, BrewinAndChewin.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BrewinAndChewin.MODID);

    public static final RegistryObject<FluidType> HONEY_FLUID_TYPE = FLUID_TYPES.register("honey_type", HoneyFluidType::new);
    public static final RegistryObject<FlowingFluid> HONEY_FLUID = FLUIDS.register("honey", () -> new ForgeFlowingFluid.Source(BnCFluids.HONEY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_HONEY_FLUID = FLUIDS.register("flowing_honey", () -> new ForgeFlowingFluid.Flowing(BnCFluids.HONEY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties HONEY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(HONEY_FLUID_TYPE, HONEY_FLUID, FLOWING_HONEY_FLUID);

    public static final RegistryObject<FluidType> BEER_FLUID_TYPE = FLUID_TYPES.register("beer_type", () -> new AlcoholFluidType(0xFFFBB117));
    public static final RegistryObject<FlowingFluid> BEER = FLUIDS.register("beer", () -> new ForgeFlowingFluid.Source(BnCFluids.BEER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BEER = FLUIDS.register("flowing_beer", () -> new ForgeFlowingFluid.Flowing(BnCFluids.BEER_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BEER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(BEER_FLUID_TYPE, BEER, FLOWING_BEER);

    public static final RegistryObject<FluidType> VODKA_FLUID_TYPE = FLUID_TYPES.register("vodka_type", () -> new AlcoholFluidType(0xFFE7FDF6));
    public static final RegistryObject<FlowingFluid> VODKA = FLUIDS.register("vodka", () -> new ForgeFlowingFluid.Source(BnCFluids.VODKA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_VODKA = FLUIDS.register("flowing_vodka", () -> new ForgeFlowingFluid.Flowing(BnCFluids.VODKA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties VODKA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(VODKA_FLUID_TYPE, VODKA, FLOWING_VODKA);

    public static final RegistryObject<FluidType> MEAD_FLUID_TYPE = FLUID_TYPES.register("mead_type", MeadFluidType::new);
    public static final RegistryObject<FlowingFluid> MEAD = FLUIDS.register("mead", () -> new ForgeFlowingFluid.Source(BnCFluids.MEAD_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_MEAD = FLUIDS.register("flowing_mead", () -> new ForgeFlowingFluid.Flowing(BnCFluids.MEAD_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties MEAD_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(MEAD_FLUID_TYPE, MEAD, FLOWING_MEAD);

    public static final RegistryObject<FluidType> EGG_GROG_FLUID_TYPE = FLUID_TYPES.register("egg_grog_type", () -> new AlcoholFluidType(0xFFFFFFFF));
    public static final RegistryObject<FlowingFluid> EGG_GROG = FLUIDS.register("egg_grog", () -> new ForgeFlowingFluid.Source(BnCFluids.EGG_GROG_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_EGG_GROG = FLUIDS.register("flowing_egg_grog", () -> new ForgeFlowingFluid.Flowing(BnCFluids.EGG_GROG_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties EGG_GROG_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(EGG_GROG_FLUID_TYPE, EGG_GROG, FLOWING_EGG_GROG);

    public static final RegistryObject<FluidType> STRONGROOT_ALE_FLUID_TYPE = FLUID_TYPES.register("strongroot_ale_type", () -> new AlcoholFluidType(0xFFBC4A4F));
    public static final RegistryObject<FlowingFluid> STRONGROOT_ALE = FLUIDS.register("strongroot_ale", () -> new ForgeFlowingFluid.Source(BnCFluids.STRONGROOT_ALE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STRONGROOT_ALE = FLUIDS.register("flowing_strongroot_ale", () -> new ForgeFlowingFluid.Flowing(BnCFluids.STRONGROOT_ALE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STRONGROOT_ALE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(STRONGROOT_ALE_FLUID_TYPE, STRONGROOT_ALE, FLOWING_STRONGROOT_ALE);

    public static final RegistryObject<FluidType> RICE_WINE_FLUID_TYPE = FLUID_TYPES.register("rice_wine_type", () -> new AlcoholFluidType(0xFFFFFFFF));
    public static final RegistryObject<FlowingFluid> RICE_WINE = FLUIDS.register("rice_wine", () -> new ForgeFlowingFluid.Source(BnCFluids.RICE_WINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RICE_WINE = FLUIDS.register("flowing_rice_wine", () -> new ForgeFlowingFluid.Flowing(BnCFluids.RICE_WINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RICE_WINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(RICE_WINE_FLUID_TYPE, RICE_WINE, FLOWING_RICE_WINE);

    public static final RegistryObject<FluidType> GLITTERING_GRENADINE_FLUID_TYPE = FLUID_TYPES.register("glittering_grenadine_type", () -> new AlcoholFluidType(0xFFF5A55E));
    public static final RegistryObject<FlowingFluid> GLITTERING_GRENADINE = FLUIDS.register("glittering_grenadine", () -> new ForgeFlowingFluid.Source(BnCFluids.GLITTERING_GRENADINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GLITTERING_GRENADINE = FLUIDS.register("flowing_glittering_grenadine", () -> new ForgeFlowingFluid.Flowing(BnCFluids.GLITTERING_GRENADINE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties GLITTERING_GRENADINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(GLITTERING_GRENADINE_FLUID_TYPE, GLITTERING_GRENADINE, FLOWING_GLITTERING_GRENADINE);

    public static final RegistryObject<FluidType> STEEL_TOE_STOUT_FLUID_TYPE = FLUID_TYPES.register("steel_toe_stout_type", () -> new AlcoholFluidType(0xFF978B8C));
    public static final RegistryObject<FlowingFluid> STEEL_TOE_STOUT = FLUIDS.register("steel_toe_stout", () -> new ForgeFlowingFluid.Source(BnCFluids.STEEL_TOE_STOUT_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_STEEL_TOE_STOUT = FLUIDS.register("flowing_steel_toe_stout", () -> new ForgeFlowingFluid.Flowing(BnCFluids.STEEL_TOE_STOUT_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STEEL_TOE_STOUT_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(STEEL_TOE_STOUT_FLUID_TYPE, STEEL_TOE_STOUT, FLOWING_STEEL_TOE_STOUT);

    public static final RegistryObject<FluidType> DREAD_NOG_FLUID_TYPE = FLUID_TYPES.register("dread_nog_type", () -> new AlcoholFluidType(0xFF25DAB7));
    public static final RegistryObject<FlowingFluid> DREAD_NOG = FLUIDS.register("dread_nog", () -> new ForgeFlowingFluid.Source(BnCFluids.DREAD_NOG_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_DREAD_NOG = FLUIDS.register("flowing_dread_nog", () -> new ForgeFlowingFluid.Flowing(BnCFluids.DREAD_NOG_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties DREAD_NOG_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(DREAD_NOG_FLUID_TYPE, DREAD_NOG, FLOWING_DREAD_NOG);

    public static final RegistryObject<FluidType> KOMBUCHA_FLUID_TYPE = FLUID_TYPES.register("kombucha_type", () -> new AlcoholFluidType(0xFF929238));
    public static final RegistryObject<FlowingFluid> KOMBUCHA = FLUIDS.register("kombucha", () -> new ForgeFlowingFluid.Source(BnCFluids.KOMBUCHA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_KOMBUCHA = FLUIDS.register("flowing_kombucha", () -> new ForgeFlowingFluid.Flowing(BnCFluids.KOMBUCHA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties KOMBUCHA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(KOMBUCHA_FLUID_TYPE, KOMBUCHA, FLOWING_KOMBUCHA);


    public static final RegistryObject<FluidType> SACCHARINE_RUM_FLUID_TYPE = FLUID_TYPES.register("saccharine_rum_type", () -> new AlcoholFluidType(0xFFCD4A7A));
    public static final RegistryObject<FlowingFluid> SACCHARINE_RUM = FLUIDS.register("saccharine_rum", () -> new ForgeFlowingFluid.Source(BnCFluids.SACCHARINE_RUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SACCHARINE_RUM = FLUIDS.register("flowing_saccharine_rum", () -> new ForgeFlowingFluid.Flowing(BnCFluids.SACCHARINE_RUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SACCHARINE_RUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SACCHARINE_RUM_FLUID_TYPE, SACCHARINE_RUM, FLOWING_SACCHARINE_RUM);

    public static final RegistryObject<FluidType> PALE_JANE_FLUID_TYPE = FLUID_TYPES.register("pale_jane_type", () -> new AlcoholFluidType(0xFFD8BEAB));
    public static final RegistryObject<FlowingFluid> PALE_JANE = FLUIDS.register("pale_jane", () -> new ForgeFlowingFluid.Source(BnCFluids.PALE_JANE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PALE_JANE = FLUIDS.register("flowing_pale_jane", () -> new ForgeFlowingFluid.Flowing(BnCFluids.PALE_JANE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties PALE_JANE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(PALE_JANE_FLUID_TYPE, PALE_JANE, FLOWING_PALE_JANE);

    public static final RegistryObject<FluidType> SALTY_FOLLY_FLUID_TYPE = FLUID_TYPES.register("salty_folly_type", () -> new AlcoholFluidType(0xFF38672D));
    public static final RegistryObject<FlowingFluid> SALTY_FOLLY = FLUIDS.register("salty_folly", () -> new ForgeFlowingFluid.Source(BnCFluids.SALTY_FOLLY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SALTY_FOLLY = FLUIDS.register("flowing_salty_folly", () -> new ForgeFlowingFluid.Flowing(BnCFluids.SALTY_FOLLY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SALTY_FOLLY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SALTY_FOLLY_FLUID_TYPE, SALTY_FOLLY, FLOWING_SALTY_FOLLY);

    public static final RegistryObject<FluidType> BLOODY_MARY_FLUID_TYPE = FLUID_TYPES.register("bloody_mary_type", () -> new AlcoholFluidType(0xFF84160D));
    public static final RegistryObject<FlowingFluid> BLOODY_MARY = FLUIDS.register("bloody_mary", () -> new ForgeFlowingFluid.Source(BnCFluids.BLOODY_MARY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BLOODY_MARY = FLUIDS.register("flowing_bloody_mary", () -> new ForgeFlowingFluid.Flowing(BnCFluids.BLOODY_MARY_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties BLOODY_MARY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(BLOODY_MARY_FLUID_TYPE, BLOODY_MARY, FLOWING_BLOODY_MARY);

    public static final RegistryObject<FluidType> RED_RUM_FLUID_TYPE = FLUID_TYPES.register("red_rum_type", () -> new AlcoholFluidType(0xFF521810));
    public static final RegistryObject<FlowingFluid> RED_RUM = FLUIDS.register("red_rum", () -> new ForgeFlowingFluid.Source(BnCFluids.RED_RUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RED_RUM = FLUIDS.register("flowing_red_rum", () -> new ForgeFlowingFluid.Flowing(BnCFluids.RED_RUM_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties RED_RUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(RED_RUM_FLUID_TYPE, RED_RUM, FLOWING_RED_RUM);

    public static final RegistryObject<FluidType> WITHERING_DROSS_FLUID_TYPE = FLUID_TYPES.register("withering_dross_type", () -> new AlcoholFluidType(0xFF191411));
    public static final RegistryObject<FlowingFluid> WITHERING_DROSS = FLUIDS.register("withering_dross", () -> new ForgeFlowingFluid.Source(BnCFluids.WITHERING_DROSS_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_WITHERING_DROSS = FLUIDS.register("flowing_withering_dross", () -> new ForgeFlowingFluid.Flowing(BnCFluids.WITHERING_DROSS_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties WITHERING_DROSS_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(WITHERING_DROSS_FLUID_TYPE, WITHERING_DROSS, FLOWING_WITHERING_DROSS);

    public static final RegistryObject<FluidType> FLAXEN_CHEESE_FLUID_TYPE = FLUID_TYPES.register("flaxen_cheese_type", () -> new CheeseFluidType(true));
    public static final RegistryObject<FlowingFluid> FLAXEN_CHEESE = FLUIDS.register("flaxen_cheese", () -> new ForgeFlowingFluid.Source(BnCFluids.FLAXEN_CHEESE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_FLAXEN_CHEESE = FLUIDS.register("flowing_flaxen_cheese", () -> new ForgeFlowingFluid.Flowing(BnCFluids.FLAXEN_CHEESE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties FLAXEN_CHEESE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(FLAXEN_CHEESE_FLUID_TYPE, FLAXEN_CHEESE, FLOWING_FLAXEN_CHEESE);

    public static final RegistryObject<FluidType> SCARLET_CHEESE_FLUID_TYPE = FLUID_TYPES.register("scarlet_cheese_type", () -> new CheeseFluidType(false));
    public static final RegistryObject<FlowingFluid> SCARLET_CHEESE = FLUIDS.register("scarlet_cheese", () -> new ForgeFlowingFluid.Source(BnCFluids.SCARLET_CHEESE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SCARLET_CHEESE = FLUIDS.register("flowing_scarlet_cheese", () -> new ForgeFlowingFluid.Flowing(BnCFluids.SCARLET_CHEESE_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SCARLET_CHEESE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SCARLET_CHEESE_FLUID_TYPE, SCARLET_CHEESE, FLOWING_SCARLET_CHEESE);
}
