package umpaz.brewinandchewin.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import umpaz.brewinandchewin.common.tag.BnCTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BnCMobEffectTags extends IntrinsicHolderTagsProvider<MobEffect>
{
    public BnCMobEffectTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.MOB_EFFECT, lookupProvider, mobEffect -> ForgeRegistries.MOB_EFFECTS.getResourceKey(mobEffect).orElseThrow(), BrewinAndChewin.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
    }

    protected void registerModTags() {
        tag(BnCTags.MILK_BOTTLES_CANNOT_REMOVE)
                .add(BnCEffects.INTOXICATION.get());
        tag(BnCTags.HOT_COCOA_CANNOT_REMOVE)
                .add(BnCEffects.INTOXICATION.get());
    }
}
