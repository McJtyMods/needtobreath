package mcjty.needtobreathe.network;

import mcjty.lib.network.PacketHandler;
import mcjty.lib.thirteen.ChannelBuilder;
import mcjty.lib.thirteen.SimpleChannel;
import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NTBMessages {
    public static SimpleNetworkWrapper INSTANCE;

    public static void registerMessages(String name) {
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(NeedToBreathe.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net.getNetwork();

        // Server side

        // Client side
        net.registerMessageClient(id(), PacketSendCleanAirToClient.class, PacketSendCleanAirToClient::toBytes, PacketSendCleanAirToClient::new, PacketSendCleanAirToClient::handle);
        net.registerMessageClient(id(), PacketIntegersFromServer.class, PacketIntegersFromServer::toBytes, PacketIntegersFromServer::new, PacketIntegersFromServer::handle);
        net.registerMessageClient(id(), PacketPoisonFromServer.class, PacketPoisonFromServer::toBytes, PacketPoisonFromServer::new, PacketPoisonFromServer::handle);
    }

    private static int id() {
        return PacketHandler.nextPacketID();
    }
}
