package umpaz.brewinandchewin.common.mixin.client;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mixin(MouseHandler.class)
public class TipsyMouseHandlerMixin {

   @ModifyExpressionValue(method = "turnPlayer()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
   private boolean brewinandchewin$enableSmoothCamera(boolean original) {
      if (Minecraft.getInstance().player != null) {
         if (Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get())) {
            return true;
         }
      }
      return original;
   }

   @ModifyArg(method = "turnPlayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/SmoothDouble;getNewDeltaValue(DD)D"), index = 1)
   private double brewinandchewin$smoothCameraMovement(double original) {
      if (Minecraft.getInstance().player != null) {
         if (Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get())) {
            return original * (5 - (Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() / 3.0D ));
         }
      }
      return original;
   }
}