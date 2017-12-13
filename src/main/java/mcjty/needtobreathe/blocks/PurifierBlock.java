package mcjty.needtobreathe.blocks;

import mcjty.lib.container.GenericBlock;
import mcjty.lib.container.GenericGuiContainer;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.proxy.GuiProxy;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PurifierBlock extends GenericBlock<PurifierTileEntity, PurifierContainer> {

    public PurifierBlock() {
        super(NeedToBreathe.instance, Material.IRON, PurifierTileEntity.class, PurifierContainer.class, "purifier", false);
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Class<? extends GenericGuiContainer> getGuiClass() {
        return PurifierGui.class;
    }

    @Override
    public int getGuiID() {
        return GuiProxy.GUI_PURIFIER;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof PurifierTileEntity) {
            probeInfo.progress(((PurifierTileEntity) te).getCoalticks(), ((PurifierTileEntity) te).getMaxCoalTicks());
        }
    }
}
