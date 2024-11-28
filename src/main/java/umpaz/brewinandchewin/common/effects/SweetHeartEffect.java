package umpaz.brewinandchewin.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCEffects;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID)
public class SweetHeartEffect extends MobEffect {

    public SweetHeartEffect() {
        super(MobEffectCategory.BENEFICIAL, 16077186);
    }

    @SubscribeEvent
    public static void sweetHeartEvent(LivingHealEvent event) {
        if (event.getEntity().hasEffect(BnCEffects.SWEET_HEART.get())) {
            event.setAmount(event.getAmount() + (1.0F * event.getEntity().getEffect(BnCEffects.SWEET_HEART.get()).getAmplifier()) + 1.0F);
        }
    }
}
