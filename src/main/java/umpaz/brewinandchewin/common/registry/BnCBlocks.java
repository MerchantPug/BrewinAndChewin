package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.*;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BnCBlocks {


    public static final BlockBehaviour.Properties TANKARD_PROPERTIES = Block.Properties.copy(Blocks.SPRUCE_PLANKS).instabreak().pushReaction(PushReaction.DESTROY).noOcclusion();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrewinAndChewin.MODID);

    public static final RegistryObject<Block> KEG = BLOCKS.register("keg", () -> new KegBlock(
            BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ITEM_COASTER = BLOCKS.register("item_coaster", ItemCoasterBlock::new);

    public static final RegistryObject<Block> HEATING_CASK = BLOCKS.register("heating_cask", () -> new HeatingCaskBlock(
            BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> ICE_CRATE = BLOCKS.register("ice_crate", () -> new IceCrateBlock(
            BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    //Cheese
    public static final RegistryObject<Block> UNRIPE_FLAXEN_CHEESE_WHEEL = BLOCKS.register("unripe_flaxen_cheese_wheel", () -> new
            UnripeCheeseWheelBlock(BnCBlocks.FLAXEN_CHEESE_WHEEL, Block.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> FLAXEN_CHEESE_WHEEL = BLOCKS.register("flaxen_cheese_wheel", () -> new
            CheeseWheelBlock(BnCItems.FLAXEN_CHEESE_WEDGE, Block.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> UNRIPE_SCARLET_CHEESE_WHEEL = BLOCKS.register("unripe_scarlet_cheese_wheel", () -> new
            UnripeCheeseWheelBlock(BnCBlocks.SCARLET_CHEESE_WHEEL, Block.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> SCARLET_CHEESE_WHEEL = BLOCKS.register("scarlet_cheese_wheel", () -> new
            CheeseWheelBlock(BnCItems.SCARLET_CHEESE_WEDGE, Block.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> PIZZA = BLOCKS.register("pizza", () -> new
            PizzaBlock(Block.Properties.copy(Blocks.CAKE)));

    public static final RegistryObject<Block> FIERY_FONDUE_POT = BLOCKS.register("fiery_fondue_pot", () -> new
            FieryFonduePotBlock(Block.Properties.copy(Blocks.CAULDRON)));

    public static final RegistryObject<Block> QUICHE = BLOCKS.register("quiche", () -> new
            PieBlock(Block.Properties.copy(Blocks.CAKE), BnCItems.QUICHE_SLICE));

    public static final RegistryObject<Block> TANKARD = BLOCKS.register("tankard", () -> new
            TankardBlock(TANKARD_PROPERTIES));

    public static final RegistryObject<Block> TANKARD_MODEL = BLOCKS.register("tankard_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> BEER_MODEL = BLOCKS.register("beer_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> VODKA_MODEL = BLOCKS.register("vodka_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> MEAD_MODEL = BLOCKS.register("mead_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> RICE_WINE_MODEL = BLOCKS.register("rice_wine_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> EGG_GROG_MODEL = BLOCKS.register("egg_grog_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> STRONGROOT_ALE_MODEL = BLOCKS.register("strongroot_ale_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> SACCHARINE_RUM_MODEL = BLOCKS.register("saccharine_rum_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> PALE_JANE_MODEL = BLOCKS.register("pale_jane_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> SALTY_FOLLY_MODEL = BLOCKS.register("salty_folly_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> STEEL_TOE_STOUT_MODEL = BLOCKS.register("steel_toe_stout_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> GLITTERING_GRENADINE_MODEL = BLOCKS.register("glittering_grenadine_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> BLOODY_MARY_MODEL = BLOCKS.register("bloody_mary_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> RED_RUM_MODEL = BLOCKS.register("red_rum_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> WITHERING_DROSS_MODEL = BLOCKS.register("withering_dross_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> DREAD_NOG_MODEL = BLOCKS.register("dread_nog_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));
    public static final RegistryObject<Block> KOMBUCHA_MODEL = BLOCKS.register("kombucha_model", () -> new
            TankardModelBlock(TANKARD_PROPERTIES));

    public static final RegistryObject<Block> SWEET_BERRY_JAM = BLOCKS.register("sweet_berry_jam", () -> new JamBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instabreak().sound(SoundType.LANTERN).instabreak()));
    public static final RegistryObject<Block> GLOW_BERRY_MARMALADE = BLOCKS.register("glow_berry_marmalade", () -> new JamBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instabreak().sound(SoundType.LANTERN).lightLevel(( a ) -> 14).instabreak()));
    public static final RegistryObject<Block> APPLE_JELLY = BLOCKS.register("apple_jelly", () -> new JamBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instabreak().sound(SoundType.LANTERN).instabreak()));
}
