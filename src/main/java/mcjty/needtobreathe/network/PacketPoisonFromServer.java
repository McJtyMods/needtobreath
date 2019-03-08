package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.thirteen.Context;
import mcjty.needtobreathe.rendering.NTBOverlayRenderer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

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

    public PacketPoisonFromServer(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketPoisonFromServer(int poison) {
        this.poison = poison;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            NTBOverlayRenderer.setPoison(poison);
        });
        ctx.setPacketHandled(true);
    }
}