package mcjty.needtobreathe.blocks;

import mcjty.lib.container.GenericBlock;
import mcjty.lib.container.GenericGuiContainer;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.proxy.GuiProxy;
import net.minecraft.block.material.Material;
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


}
