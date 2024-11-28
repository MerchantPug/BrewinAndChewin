package umpaz.brewinandchewin.data.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.loot.CopyDrinkFunction;

import java.util.HashSet;
import java.util.Set;

public class BnCBlockLoot extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet<>();

    public BnCBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        add(BnCBlocks.KEG.get(), (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyDrinkFunction.builder())))));
        dropSelf(BnCBlocks.HEATING_CASK.get());
        dropSelf(BnCBlocks.ICE_CRATE.get());
        dropSelf(BnCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL.get());
        dropSelf(BnCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL.get());
        dropSelf(BnCBlocks.COASTER.get());

        dropOther(BnCBlocks.FIERY_FONDUE_POT.get(), Blocks.CAULDRON);
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}
