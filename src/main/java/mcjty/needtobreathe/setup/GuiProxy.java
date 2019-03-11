package mcjty.needtobreathe.setup;

import mcjty.lib.blocks.GenericBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    public static final int GUI_PURIFIER = 1;
    public static final int GUI_AIRCOMPRESSOR = 2;

    @Override
    public Object getServerGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
//        if (guiid == RFTools.GUI_MANUAL_MAIN) {
//            return null;
//        }

        BlockPos pos = new BlockPos(x, y, z);
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericBlock) {
            GenericBlock genericBlock = (GenericBlock) block;
            TileEntity te = world.getTileEntity(pos);
            return genericBlock.createServerContainer(entityPlayer, te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
//        if (guiid == RFTools.GUI_MANUAL_MAIN) {
//            return new GuiRFToolsManual(GuiRFToolsManual.MANUAL_MAIN);
//        }

        BlockPos pos = new BlockPos(x, y, z);
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericBlock) {
            GenericBlock genericBlock = (GenericBlock) block;
            TileEntity te = world.getTileEntity(pos);
            return genericBlock.createClientGui(entityPlayer, te);
        }
        return null;
    }
}
