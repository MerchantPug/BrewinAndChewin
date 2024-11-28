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

public class BnCBlocks {


    public static final BlockBehaviour.Properties TANKARD_PROPERTIES = Block.Properties.copy(Blocks.SPRUCE_PLANKS).instabreak().pushReaction(PushReaction.DESTROY).noOcclusion();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrewinAndChewin.MODID);

    public static final RegistryObject<Block> KEG = BLOCKS.register("keg", () -> new KegBlock(
            BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> COASTER = BLOCKS.register("coaster", CoasterBlock::new);

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
}
