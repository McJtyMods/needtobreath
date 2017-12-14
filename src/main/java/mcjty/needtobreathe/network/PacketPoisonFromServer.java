package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import mcjty.needtobreathe.rendering.NTBOverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPoisonFromServer implements IMessage {
    private int poison;

    @Override
    public void fromBytes(ByteBuf buf) {
        poison = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(poison);
    }

    public PacketPoisonFromServer() {
    }

    public PacketPoisonFromServer(int poison) {
        this.poison = poison;
    }

    public static class Handler implements IMessageHandler<PacketPoisonFromServer, IMessage> {
        @Override
        public IMessage onMessage(PacketPoisonFromServer message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketPoisonFromServer message, MessageContext ctx) {
            NTBOverlayRenderer.poison = message.poison;
        }

    }
}