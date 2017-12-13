package mcjty.needtobreathe.network;

import mcjty.lib.network.PacketHandler;
import mcjty.lib.network.PacketIntegerFromServer;
import mcjty.lib.network.PacketRequestIntegerFromServer;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NTBMessages {
    public static SimpleNetworkWrapper INSTANCE;

    public static void registerNetworkMessages(SimpleNetworkWrapper net) {
        INSTANCE = net;

        // Server side
        net.registerMessage(PacketRequestIntegersFromServer.Handler.class, PacketRequestIntegersFromServer.class, PacketHandler.nextID(), Side.SERVER);

        // Client side
        net.registerMessage(PacketSendCleanAirToClient.Handler.class, PacketSendCleanAirToClient.class, PacketHandler.nextID(), Side.CLIENT);
        net.registerMessage(PacketIntegersFromServer.Handler.class, PacketIntegersFromServer.class, PacketHandler.nextID(), Side.CLIENT);
    }
}
