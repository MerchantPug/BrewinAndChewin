package umpaz.brewinandchewin.common.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import umpaz.brewinandchewin.BrewinAndChewin;
import vectorwing.farmersdelight.FarmersDelight;

public class BnCTextUtils {

    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable(BrewinAndChewin.MODID + "." + key, args);
    }
}
