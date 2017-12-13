package mcjty.needtobreathe.blocks;

import mcjty.lib.container.EmptyContainer;
import mcjty.lib.container.GenericBlock;
import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.block.material.Material;

public class PurifierBlock extends GenericBlock<PurifierTileEntity, EmptyContainer> {

    public PurifierBlock() {
        super(NeedToBreathe.instance, Material.IRON, PurifierTileEntity.class, EmptyContainer.class, "purifier", false);
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @Override
    public int getGuiID() {
        return -1;
    }
}
