package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.thirteen.Context;
import mcjty.needtobreathe.data.ChunkData;
import mcjty.needtobreathe.rendering.NTBOverlayRenderer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketSendCleanAirToClient implements IMessage {
    private Map<Long, ChunkData> cleanAir;

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        cleanAir = new HashMap<>(size);
        for (int i = 0 ; i < size ; i++) {
            long chunkPos = buf.readLong();
            if (buf.readBoolean()) {
                cleanAir.put(chunkPos, new ChunkData(null));
            } else {
                byte[] data = new byte[ChunkData.CHUNK_SIZE];
                buf.readBytes(data);
                cleanAir.put(chunkPos, new ChunkData(data));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(cleanAir.size());
        for (Map.Entry<Long, ChunkData> entry : cleanAir.entrySet()) {
            long chunkPos = entry.getKey();
            ChunkData data = entry.getValue();
            buf.writeLong(chunkPos);
            if (data.isStrong()) {
                buf.writeBoolean(true);
            } else {
                buf.writeBoolean(false);
                buf.writeBytes(data.getData());
            }
        }
    }

    public PacketSendCleanAirToClient() {
    }

    public PacketSendCleanAirToClient(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketSendCleanAirToClient(Map<Long, ChunkData> cleanAir) {
        // No copy because cleanAir is computed for the player already
        this.cleanAir = cleanAir;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            NTBOverlayRenderer.setCleanAir(cleanAir);
        });
        ctx.setPacketHandled(true);
    }
}
