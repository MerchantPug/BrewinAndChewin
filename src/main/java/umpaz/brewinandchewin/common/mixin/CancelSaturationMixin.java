package umpaz.brewinandchewin.common.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mixin(FoodData.class)
public class CancelSaturationMixin {

    @Unique
    private LivingEntity brewinandchewin$entity;

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), remap = false)
    private void brewinandchewin$findPlayer(Item pItem, ItemStack pStack, LivingEntity entity, CallbackInfo ci) {
        this.brewinandchewin$entity = entity;
    }

    @ModifyVariable(method = "eat(IF)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private float brewinandchewin$disableSaturation(float value) {
        if (brewinandchewin$entity != null && brewinandchewin$entity.hasEffect(BnCEffects.INTOXICATION.get())) {
            brewinandchewin$entity = null;
            return 0.0F;
        }
        return value;
    }
}
