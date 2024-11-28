package umpaz.brewinandchewin.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

public class DreadNogItem extends BoozeItem {

    public DreadNogItem(Fluid fluid, int potency, int duration, Properties properties) {
        super(fluid, potency, duration, properties);
    }

    public void affectConsumer(LivingEntity consumer) {
        super.affectConsumer(consumer);
        MobEffectInstance badOmenEffect = consumer.getEffect(MobEffects.BAD_OMEN);
        if (!consumer.hasEffect(MobEffects.BAD_OMEN)) {
            consumer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 12000, 0), consumer);
        }
        else if (badOmenEffect.getAmplifier() < 2) {
            consumer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 12000, badOmenEffect.getAmplifier() + 1), consumer);
        }
    }
}
