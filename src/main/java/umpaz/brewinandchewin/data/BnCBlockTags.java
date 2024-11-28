package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.tag.BnCTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BnCBlockTags extends BlockTagsProvider
{
    public BnCBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BrewinAndChewin.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerBlockMineables();
    }

    protected void registerBlockMineables() {
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                BnCBlocks.KEG.get(),
                BnCBlocks.HEATING_CASK.get(),
                BnCBlocks.ICE_CRATE.get(),
                BnCBlocks.COASTER.get()
        );
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                BnCBlocks.FIERY_FONDUE_POT.get()
        );
        tag(ModTags.MINEABLE_WITH_KNIFE).add(
                BnCBlocks.COASTER.get(),
                BnCBlocks.UNRIPE_FLAXEN_CHEESE_WHEEL.get(),
                BnCBlocks.FLAXEN_CHEESE_WHEEL.get(),
                BnCBlocks.UNRIPE_SCARLET_CHEESE_WHEEL.get(),
                BnCBlocks.SCARLET_CHEESE_WHEEL.get(),
                BnCBlocks.PIZZA.get(),
                BnCBlocks.QUICHE.get()
        );
    }

    protected void registerModTags() {
        tag(BnCTags.FREEZE_SOURCES).add(
                BnCBlocks.ICE_CRATE.get(),
                Blocks.ICE,
                Blocks.PACKED_ICE,
                Blocks.BLUE_ICE
        );

        tag(ModTags.HEAT_SOURCES).add(
                BnCBlocks.HEATING_CASK.get()
        );
    }
}
