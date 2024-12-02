package umpaz.brewinandchewin.common.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.effects.IntoxicatedEffect;
import umpaz.brewinandchewin.common.effects.SweetHeartEffect;
import umpaz.brewinandchewin.common.effects.TipsyEffect;


public class BnCEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BrewinAndChewin.MODID);

    public static final RegistryObject<MobEffect> SWEET_HEART = EFFECTS.register("sweet_heart", SweetHeartEffect::new);
    public static final RegistryObject<MobEffect> TIPSY = EFFECTS.register("tipsy", TipsyEffect::new);
    public static final RegistryObject<MobEffect> INTOXICATED = EFFECTS.register("intoxicated", IntoxicatedEffect::new);

}
