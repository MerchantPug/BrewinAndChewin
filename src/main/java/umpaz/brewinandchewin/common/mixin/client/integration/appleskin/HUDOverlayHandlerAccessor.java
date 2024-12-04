package umpaz.brewinandchewin.common.mixin.client.integration.appleskin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(HUDOverlayHandler.class)
public interface HUDOverlayHandlerAccessor {
    @Accessor("flashAlpha")
    static float brewinandchewin$flashAlpha() {
        throw new RuntimeException("");
    }
}
