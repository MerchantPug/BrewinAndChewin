package umpaz.brewinandchewin.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.registry.BnCEffects;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID)
public class IntoxicationEffect extends MobEffect
{
    public IntoxicationEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @SubscribeEvent
    public static void canBeAffected(MobEffectEvent.Applicable event) {
        if (event.getEntity().getMobType() == MobType.UNDEAD) {
            MobEffect mobeffect = event.getEffectInstance().getEffect();
            if (mobeffect == BnCEffects.INTOXICATION.get()) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}