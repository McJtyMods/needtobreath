package mcjty.needtobreathe.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestIntegersFromServer implements IMessage {

    private BlockPos pos;

    public PacketRequestIntegersFromServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = NetworkTools.readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, pos);
    }

    public PacketRequestIntegersFromServer(BlockPos pos) {
        this.pos = pos;
    }

    public static class Handler implements IMessageHandler<PacketRequestIntegersFromServer, IMessage> {
        @Override
        public IMessage onMessage(PacketRequestIntegersFromServer message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketRequestIntegersFromServer message, MessageContext ctx) {
            TileEntity te = ctx.getServerHandler().player.getEntityWorld().getTileEntity(message.pos);
            if (te instanceof IIntegerRequester) {
                sendReplyToClient(message, ((IIntegerRequester) te).get(), ctx.getServerHandler().player);
            }
       }

        private void sendReplyToClient(PacketRequestIntegersFromServer message, int[] result, EntityPlayerMP player) {
            PacketIntegersFromServer msg = new PacketIntegersFromServer(message.pos, result);
            NTBMessages.INSTANCE.sendTo(msg, player);
        }

    }
}