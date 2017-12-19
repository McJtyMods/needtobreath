package mcjty.needtobreathe.network;

import mcjty.lib.network.PacketHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NTBMessages {
    public static SimpleNetworkWrapper INSTANCE;

    public static void registerNetworkMessages(SimpleNetworkWrapper net) {
        INSTANCE = net;

        // Server side

        // Client side
        net.registerMessage(PacketSendCleanAirToClient.Handler.class, PacketSendCleanAirToClient.class, PacketHandler.nextID(), Side.CLIENT);
        net.registerMessage(PacketIntegersFromServer.Handler.class, PacketIntegersFromServer.class, PacketHandler.nextID(), Side.CLIENT);
        net.registerMessage(PacketPoisonFromServer.Handler.class, PacketPoisonFromServer.class, PacketHandler.nextID(), Side.CLIENT);
    }
}
