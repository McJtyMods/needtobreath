package mcjty.needtobreathe.blocks;

import mcjty.lib.entity.GenericTileEntity;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.data.LongPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class LargePurifierTileEntity extends GenericTileEntity implements ITickable {

    @Override
    public void update() {
        if (!world.isRemote) {
            CleanAirManager manager = CleanAirManager.getManager();
            DimensionData data = manager.getDimensionData(world.provider.getDimension());
            if (data == null) {
                // This dimension doesn't need a purifier
                return;
            }
            // Depending on how pure it already is we decrease this faster or slower
            BlockPos center = getPurifyingSpot();
            int nr = 4;
            for (int dx = -nr; dx <= nr; dx++) {
                for (int dy = -nr; dy <= nr; dy++) {
                    for (int dz = -nr; dz <= nr; dz++) {
                        BlockPos p = center.add(dx * 8, dy * 8, dz * 8);
                        if (p.getY() >= 0 && p.getY() <= 255) {
                            long pp = p.toLong();
                            if (data.isValid(world, pp)) {
                                purifyAir(data, pp);
                            }
                        }
                    }
                }
            }
        }
    }

    private int purifyAir(DimensionData data, long pp) {
        int workdone = 0;
        workdone += data.fillCleanAir(pp);
        long p2;
        p2 = LongPos.posDown(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        p2 = LongPos.posUp(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        p2 = LongPos.posNorth(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        p2 = LongPos.posSouth(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        p2 = LongPos.posWest(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        p2 = LongPos.posEast(pp);
        if (data.isValid(world, p2)) {
            workdone += data.fillCleanAir(p2);
        }
        return workdone;
    }

    private BlockPos getPurifyingSpot() {
        return pos.up();
    }
}
