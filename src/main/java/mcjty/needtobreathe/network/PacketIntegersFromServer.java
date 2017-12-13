package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

    public PacketIntegersFromServer(BlockPos pos, int[] integers) {
        this.pos = pos;
        this.integers = integers;
    }

    public static class Handler implements IMessageHandler<PacketIntegersFromServer, IMessage> {
        @Override
        public IMessage onMessage(PacketIntegersFromServer message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketIntegersFromServer message, MessageContext ctx) {
            TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
            if (te instanceof IIntegerRequester) {
                ((IIntegerRequester) te).set(message.integers);
            }
        }

    }
}