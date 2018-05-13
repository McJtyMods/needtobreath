package mcjty.needtobreathe;

import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.network.IIntegerRequester;
import mcjty.needtobreathe.network.NTBMessages;
import mcjty.needtobreathe.network.PacketIntegersFromServer;
import mcjty.needtobreathe.network.PacketPoisonFromServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class CommandHandler {

    public static final String CMD_REQUESTPOISON = "requestPoison";
    public static final Key<BlockPos> PARAM_POS = new Key<>("pos", Type.BLOCKPOS);

    public static final String CMD_REQUESTINTEGERS = "requestIntegers";

    public static void registerCommands() {
        McJtyLib.registerCommand(NeedToBreathe.MODID, CMD_REQUESTPOISON, (player, arguments) -> {
            BlockPos pos = arguments.get(PARAM_POS);
            DimensionData data = CleanAirManager.getManager().getDimensionData(player.getEntityWorld().provider.getDimension());
            int poison;
            if (data != null) {
                poison = data.getPoison(player.getEntityWorld(), pos);
            } else {
                poison = 0;
            }
            PacketPoisonFromServer msg = new PacketPoisonFromServer(poison);
            NTBMessages.INSTANCE.sendTo(msg, (EntityPlayerMP) player);
            return true;
        });
        McJtyLib.registerCommand(NeedToBreathe.MODID, CMD_REQUESTINTEGERS, (player, arguments) -> {
            BlockPos pos = arguments.get(PARAM_POS);
            TileEntity te = player.getEntityWorld().getTileEntity(pos);
            if (te instanceof IIntegerRequester) {
                PacketIntegersFromServer msg = new PacketIntegersFromServer(pos, ((IIntegerRequester) te).get());
                NTBMessages.INSTANCE.sendTo(msg, (EntityPlayerMP) player);
            }
            return true;
        });
    }
}
