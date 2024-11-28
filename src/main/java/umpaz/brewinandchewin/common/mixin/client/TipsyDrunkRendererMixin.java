package umpaz.brewinandchewin.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mixin(LevelRenderer.class)
public class TipsyDrunkRendererMixin {

   @Shadow
   private int ticks;

   @ModifyVariable(method = "renderLevel", at = @At("HEAD"), argsOnly = true)
   private PoseStack renderTipsySpin( PoseStack pProjectionMatrix ) {
      Player player = Minecraft.getInstance().player;
      if ( player.hasEffect(BnCEffects.TIPSY.get()) ) {
         int strength = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier();

         // left and right
         pProjectionMatrix.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, Mth.cos(3 + ticks * 0.0295f) * strength), 0.5f, 0.5f, 0.5f);
         // up and down
         pProjectionMatrix.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 1, Mth.sin(27 + ticks * 0.0132f) * strength), 0.5f, 0.5f, 0.5f);
         // circle
         float xDiff = ( Mth.sin(ticks * 0.00253f) * ( strength / 100F ) );
         float zDiff = ( Mth.cos(ticks * 0.00784f) * ( strength / 100F ) );
         pProjectionMatrix.translate(xDiff, 0, zDiff);
      }
      return pProjectionMatrix;
   }
}
