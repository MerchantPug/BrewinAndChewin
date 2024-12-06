package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.tag.BnCTags;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BnCItemTags extends ItemTagsProvider {

    public BnCItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, BrewinAndChewin.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
    }

    private void registerModTags() {
        tag(BnCTags.CHEESE_WEDGES)
                .add(BnCItems.FLAXEN_CHEESE_WEDGE.get())
                .add(BnCItems.SCARLET_CHEESE_WEDGE.get());
        tag(BnCTags.PIZZA_TOPPINGS)
                .addTag(ForgeTags.COOKED_BACON).addTag(ForgeTags.COOKED_BEEF).addTag(ForgeTags.COOKED_CHICKEN).addTag(ForgeTags.COOKED_MUTTON).addTag(ForgeTags.COOKED_PORK)
                .add(Items.BROWN_MUSHROOM).add(Items.RED_MUSHROOM)
                .add(Items.CARROT).add(Items.BEETROOT).add(ModItems.CABBAGE_LEAF.get()).add(ModItems.ONION.get());
        tag(BnCTags.HORROR_MEATS).addTag(ForgeTags.RAW_BEEF).addTag(ForgeTags.RAW_MUTTON);
        tag(BnCTags.RAW_MEATS).addTag(ForgeTags.RAW_BACON).addTag(ForgeTags.RAW_BEEF).addTag(ForgeTags.RAW_CHICKEN)
                .addTag(ForgeTags.RAW_MUTTON).addTag(ForgeTags.RAW_PORK).add(Items.ROTTEN_FLESH);
        tag(BnCTags.TIPSY_REDUCER)
                .addTag(ForgeTags.MILK);
    }
}