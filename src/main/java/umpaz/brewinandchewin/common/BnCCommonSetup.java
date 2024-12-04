package umpaz.brewinandchewin.common;

import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import umpaz.brewinandchewin.common.network.BnCNetworkHandler;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.data.loot.BnCCopyMealFunction;

public class BnCCommonSetup {

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(BnCCommonSetup::registerCompostables);
        event.enqueueWork(BnCCommonSetup::registerLootItemFunctions);
        event.enqueueWork(BnCNetworkHandler::register);
    }

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(BnCItems.KIMCHI.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(BnCItems.PICKLED_PICKLES.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(BnCItems.KIPPERS.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(BnCItems.QUICHE_SLICE.get(), 0.85F);
        ComposterBlock.COMPOSTABLES.put(BnCItems.QUICHE.get(), 1.0F);
    }

   public static void registerLootItemFunctions() {
      LootItemFunctions.register(BnCCopyMealFunction.ID.toString(), new BnCCopyMealFunction.Serializer());
   }
}
