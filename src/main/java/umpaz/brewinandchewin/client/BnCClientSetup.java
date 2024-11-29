package umpaz.brewinandchewin.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.gui.IntoxicatedHealthOverlay;
import umpaz.brewinandchewin.client.gui.KegScreen;
import umpaz.brewinandchewin.client.gui.KegTooltip;
import umpaz.brewinandchewin.common.registry.BnCMenuTypes;
import umpaz.brewinandchewin.client.utility.BnCFluidItemDisplayUtils;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BnCClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(BnCMenuTypes.KEG.get(), KegScreen::new));

        IntoxicatedHealthOverlay.init();
        TipsyEffects.init();
    }

    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(KegTooltip.KegTooltipComponent.class, KegTooltip::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BnCFluidItemDisplayUtils.defaultFluidToItems();
    }
}
