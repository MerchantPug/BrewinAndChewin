package umpaz.brewinandchewin.common.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.loot.modifier.BnCSlicingModifier;

public class BnCLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BrewinAndChewin.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SLICING = LOOT_MODIFIERS.register("slicing", BnCSlicingModifier.CODEC);
}
