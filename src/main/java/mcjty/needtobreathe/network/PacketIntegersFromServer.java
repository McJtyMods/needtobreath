package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import mcjty.lib.thirteen.Context;
import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketIntegersFromServer implements IMessage {
    private BlockPos pos;
    private int[] integers;

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = NetworkTools.readPos(buf);
        int size = buf.readInt();
        integers = new int[size];
        for (int i = 0 ; i < size ; i++) {
            integers[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, pos);
        buf.writeInt(integers.length);
        for (int i : integers) {
            buf.writeInt(i);
        }
    }

    public PacketIntegersFromServer() {
    }

    public PacketIntegersFromServer(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketIntegersFromServer(BlockPos pos, int[] integers) {
        this.pos = pos;
        this.integers = integers;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            TileEntity te = NeedToBreathe.proxy.getClientWorld().getTileEntity(pos);
            if (te instanceof IIntegerRequester) {
                ((IIntegerRequester) te).set(integers);
            }
        });
        ctx.setPacketHandled(true);
    }
}