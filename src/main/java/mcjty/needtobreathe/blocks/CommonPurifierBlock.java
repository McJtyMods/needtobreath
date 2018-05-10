package mcjty.needtobreathe.blocks;

import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.proxy.GuiProxy;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;

public class CommonPurifierBlock<T extends CommonPurifierTileEntity> extends GenericBlock<T, PurifierContainer> {

    public static final PropertyBool WORKING = PropertyBool.create("working");

    public CommonPurifierBlock(String name, Class<T> clazz) {
        super(NeedToBreathe.instance, Material.IRON, clazz, PurifierContainer.class, name, true);
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
        if (te instanceof CommonPurifierTileEntity) {
            probeInfo.progress(((CommonPurifierTileEntity) te).getCoalticks(), ((CommonPurifierTileEntity) te).getMaxCoalTicks());
            DecimalFormat fmt = new DecimalFormat("#.##");
            probeInfo.horizontal().text(LABEL + "Consumption: " + INFO + fmt.format(((CommonPurifierTileEntity) te).getLastCoalPerTick()) + "/t");
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        boolean working = false;
        if (te instanceof CommonPurifierTileEntity) {
            working = ((CommonPurifierTileEntity)te).isWorking();
        }
        return state.withProperty(WORKING, working);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, WORKING);
    }
}
