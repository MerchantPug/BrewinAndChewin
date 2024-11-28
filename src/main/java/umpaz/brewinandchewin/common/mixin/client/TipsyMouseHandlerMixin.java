package umpaz.brewinandchewin.common.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mixin(MouseHandler.class)
public class TipsyMouseHandlerMixin {

   @Redirect(method = "Lnet/minecraft/client/MouseHandler;turnPlayer()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
   private boolean enableSmoothCamera(Options instance) {
      if (Minecraft.getInstance().player != null) {
         if (Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get())) {
            return true;
         }
      }
      return instance.smoothCamera;
   }

   @Redirect(method = "Lnet/minecraft/client/MouseHandler;turnPlayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/SmoothDouble;getNewDeltaValue(DD)D"))
   private double smoothCameraMovement(SmoothDouble instance, double targetIncrement, double smoothingFactor) {
      if (Minecraft.getInstance().player != null) {
         if (Minecraft.getInstance().player.hasEffect(BnCEffects.TIPSY.get())) {
            return instance.getNewDeltaValue(targetIncrement, smoothingFactor * ( 5 - ( Minecraft.getInstance().player.getEffect(BnCEffects.TIPSY.get()).getAmplifier() / 3.0D ) ));
         }
      }
      return instance.getNewDeltaValue(targetIncrement, smoothingFactor);
   }
}