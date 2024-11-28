package umpaz.brewinandchewin.common.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import vectorwing.farmersdelight.common.registry.ModEffects;

@Mixin(FoodData.class)
public class HealCancelHungerMixin {

    @Unique
    private Player player;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void findPlayer(Player player, CallbackInfo ci) {
        this.player = player;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"))
    private float disableHungerRemoval(float amount) {
        if (player.hasEffect(BnCEffects.TIPSY.get()) && !player.hasEffect(ModEffects.COMFORT.get())) {
            int amp = player.getEffect(BnCEffects.TIPSY.get()).getAmplifier();
            return amp >= 2 ? 0 : amount;
        }
        return amount;
    }
}
