package umpaz.brewinandchewin.common.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mixin(ForgeGui.class)
public class TipsyDontRenderFoodMixin {
    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true, remap = false)
    private void brewinandchewin$dontRenderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (BnCConfiguration.INTOXICATION_FOOD_OVERLAY.get() && Minecraft.getInstance().player.hasEffect(BnCEffects.INTOXICATION.get()))
            ci.cancel();
    }
}
