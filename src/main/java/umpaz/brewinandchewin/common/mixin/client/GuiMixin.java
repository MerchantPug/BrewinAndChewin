package umpaz.brewinandchewin.common.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import umpaz.brewinandchewin.client.gui.BnCHUDOverlays;

@Mixin(Gui.class)
public class GuiMixin {
    @ModifyExpressionValue(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int brewinandchewin$captureHeartOffset(int original, @Local(ordinal = 11) int heartIndex) {
        BnCHUDOverlays.heartOffset[heartIndex] = original;
        return original;
    }
}
