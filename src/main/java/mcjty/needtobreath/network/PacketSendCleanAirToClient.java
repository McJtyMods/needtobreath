package mcjty.needtobreath.network;

import io.netty.buffer.ByteBuf;
import mcjty.needtobreath.NeedToBreath;
import mcjty.needtobreath.NTBOverlayRenderer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class PacketSendCleanAirToClient implements IMessage {
    private Map<Long, Byte> cleanAir;

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readByte();
        cleanAir = new HashMap<>(size);
        for (int i = 0 ; i < size ; i++) {
            cleanAir.put(buf.readLong(), buf.readByte());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(cleanAir.size());
        for (Map.Entry<Long, Byte> entry : cleanAir.entrySet()) {
            buf.writeLong(entry.getKey());
            buf.writeByte(entry.getValue());
        }
    }

    public PacketSendCleanAirToClient() {
    }

    public PacketSendCleanAirToClient(Map<Long,Byte> cleanAir) {
        this.cleanAir = new HashMap<>(cleanAir);
    }

    public static class Handler implements IMessageHandler<PacketSendCleanAirToClient, IMessage> {
        @Override
        public IMessage onMessage(PacketSendCleanAirToClient message, MessageContext ctx) {
            NeedToBreath.proxy.addScheduledTaskClient(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendCleanAirToClient message, MessageContext ctx) {
            NTBOverlayRenderer.setCleanAir(message.cleanAir);
        }
    }

}
