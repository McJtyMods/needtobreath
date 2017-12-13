package mcjty.needtobreath.blocks;

import mcjty.lib.base.ModBase;
import mcjty.lib.container.EmptyContainer;
import mcjty.lib.container.GenericBlock;
import mcjty.needtobreath.NeedToBreath;
import net.minecraft.block.material.Material;

public class PurifierBlock extends GenericBlock<PurifierTileEntity, EmptyContainer> {

    public PurifierBlock() {
        super(NeedToBreath.instance, Material.IRON, PurifierTileEntity.class, EmptyContainer.class, "purifier", false);
    }

    @Override
    public int getGuiID() {
        return -1;
    }
}
