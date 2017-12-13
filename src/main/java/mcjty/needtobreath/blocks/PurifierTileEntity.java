package mcjty.needtobreath.blocks;

import mcjty.lib.entity.GenericTileEntity;
import mcjty.needtobreath.data.CleanAirManager;
import net.minecraft.util.ITickable;

public class PurifierTileEntity extends GenericTileEntity implements ITickable {

    @Override
    public void update() {
        if (!world.isRemote) {
            CleanAirManager.getManager().addCleanAir(pos, 1.0f);
        }
    }
}
