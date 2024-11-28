package umpaz.brewinandchewin.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import umpaz.brewinandchewin.common.loot.CopyDrinkFunction;

public class BnCLootFunctions {
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS;
    public static final RegistryObject<LootItemFunctionType> COPY_DRINK;

    public BnCLootFunctions() {
    }

    static {
        LOOT_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE.key(), "brewinandchewin");
        COPY_DRINK = LOOT_FUNCTIONS.register("copy_drink", () -> new LootItemFunctionType(new CopyDrinkFunction.Serializer()));
    }
}
