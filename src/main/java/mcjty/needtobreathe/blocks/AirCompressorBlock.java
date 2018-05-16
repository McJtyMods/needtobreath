package mcjty.needtobreathe.blocks;

import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.proxy.GuiProxy;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;

public class AirCompressorBlock extends GenericBlock<AirCompressorTileEntity, AirCompressorContainer> {

    public AirCompressorBlock() {
        super(NeedToBreathe.instance, Material.IRON, AirCompressorTileEntity.class, AirCompressorContainer.class, "air_compressor", true);
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.HORIZROTATION;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Class<? extends GenericGuiContainer<AirCompressorTileEntity>> getGuiClass() {
        return AirCompressorGui.class;
    }


    @Override
    public int getGuiID() {
        return GuiProxy.GUI_AIRCOMPRESSOR;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            list.add(TextFormatting.WHITE + "This machine compresses air");
            list.add(TextFormatting.WHITE + "out of the environment to fill");
            list.add(TextFormatting.WHITE + "a hazmat chestplate");
            list.add(TextFormatting.YELLOW + "Warning! The block above this");
            list.add(TextFormatting.YELLOW + "machine has to be empty");
        } else {
            list.add(TextFormatting.WHITE + NeedToBreathe.SHIFT_MESSAGE);
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof AirCompressorTileEntity) {
            boolean blocked = ((AirCompressorTileEntity) te).isBlocked();
            if (blocked) {
                probeInfo.text(ERROR + "Please clear space above compressor!");
            }
        }
    }



}
