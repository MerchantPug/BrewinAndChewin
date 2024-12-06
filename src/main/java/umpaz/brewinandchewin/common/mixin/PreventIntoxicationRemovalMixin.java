package umpaz.brewinandchewin.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import umpaz.brewinandchewin.common.tag.BnCTags;
import vectorwing.farmersdelight.common.item.HotCocoaItem;
import vectorwing.farmersdelight.common.item.MilkBottleItem;

public class PreventIntoxicationRemovalMixin {
    @Mixin(MilkBottleItem.class)
    public static class MilkBottle {
        @ModifyExpressionValue(method = "affectConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isCurativeItem(Lnet/minecraft/world/item/ItemStack;)Z"), remap = false)
        private boolean brewinandchewin$preventIntoxicationRemoval(boolean original, @Local(argsOnly = true) Level level, @Local MobEffectInstance effectInstance) {
            return original && !ForgeRegistries.MOB_EFFECTS.tags().getTag(BnCTags.MILK_BOTTLES_CANNOT_REMOVE).contains(effectInstance.getEffect());
        }
    }

    @Mixin(HotCocoaItem.class)
    public static class HotCocoa {
        @ModifyExpressionValue(method = "affectConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isCurativeItem(Lnet/minecraft/world/item/ItemStack;)Z"), remap = false)
        private boolean brewinandchewin$preventIntoxicationRemoval(boolean original, @Local(argsOnly = true) Level level, @Local MobEffectInstance effectInstance) {
            return original && !ForgeRegistries.MOB_EFFECTS.tags().getTag(BnCTags.HOT_COCOA_CANNOT_REMOVE).contains(effectInstance.getEffect());
        }
    }
}
