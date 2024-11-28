package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.block.entity.ItemCoasterBlockEntity;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.block.entity.TankardBlockEntity;

public class BnCBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BrewinAndChewin.MODID);

    public static final RegistryObject<BlockEntityType<KegBlockEntity>> KEG = TILES.register("keg",
            () -> BlockEntityType.Builder.of(KegBlockEntity::new, BnCBlocks.KEG.get()).build(null));

    public static final RegistryObject<BlockEntityType<ItemCoasterBlockEntity>> ITEM_COASTER = TILES.register("item_coaster",
            () -> BlockEntityType.Builder.of(ItemCoasterBlockEntity::new, BnCBlocks.ITEM_COASTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TankardBlockEntity>> TANKARD = TILES.register("tankard",
            () -> BlockEntityType.Builder.of(TankardBlockEntity::new, BnCBlocks.TANKARD.get()).build(null));
}
