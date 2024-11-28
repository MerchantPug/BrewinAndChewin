package umpaz.brewinandchewin.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.client.particle.DrunkBubbleParticle;
import umpaz.brewinandchewin.client.renderer.ItemCoasterRenderer;
import umpaz.brewinandchewin.client.renderer.TankardBlockEntityRenderer;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import umpaz.brewinandchewin.common.registry.BnCParticleTypes;
import vectorwing.farmersdelight.client.particle.SteamParticle;

@Mod.EventBusSubscriber(modid = BrewinAndChewin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BnCClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BnCBlockEntityTypes.TANKARD.get(), TankardBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BnCBlockEntityTypes.ITEM_COASTER.get(), ItemCoasterRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(BnCParticleTypes.FOG.get(), SteamParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BnCParticleTypes.DRUNK_BUBBLE.get(), DrunkBubbleParticle.Factory::new);
    }
}
