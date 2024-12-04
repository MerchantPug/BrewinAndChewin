package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import umpaz.brewinandchewin.BrewinAndChewin;

public class BnCDamageTypes {
    public static final ResourceKey<DamageType> CARDIAC_ARREST = ResourceKey.create(Registries.DAMAGE_TYPE, BrewinAndChewin.asResource("cardiac_arrest"));
}
