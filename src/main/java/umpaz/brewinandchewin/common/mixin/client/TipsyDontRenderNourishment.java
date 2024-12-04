package umpaz.brewinandchewin.common.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;

@Mixin(NourishmentHungerOverlay.class)
public class TipsyDontRenderNourishment {
    @Inject(method = "drawNourishmentOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void brewinandchewin$dontRenderNourishment(FoodData stats, Minecraft mc, GuiGraphics graphics, int left, int top, boolean naturalHealing, CallbackInfo ci) {
        if (mc.player.hasEffect(BnCEffects.INTOXICATION.get()))
            ci.cancel();
    }
}
