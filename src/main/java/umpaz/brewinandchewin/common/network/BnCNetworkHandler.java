
package umpaz.brewinandchewin.common.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import umpaz.brewinandchewin.BrewinAndChewin;
import umpaz.brewinandchewin.common.network.clientbound.SyncNumbedHeartsClientboundPacket;

public class BnCNetworkHandler {
    private static final String PROTOCOL_VERISON = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            BrewinAndChewin.asResource("main"),
            () -> PROTOCOL_VERISON,
            PROTOCOL_VERISON::equals,
            PROTOCOL_VERISON::equals
    );

    public static void register() {
        int i = 0;
        INSTANCE.registerMessage(i++, SyncNumbedHeartsClientboundPacket.class, SyncNumbedHeartsClientboundPacket::encode, SyncNumbedHeartsClientboundPacket::decode, SyncNumbedHeartsClientboundPacket.Handler::handle);
    }

}
