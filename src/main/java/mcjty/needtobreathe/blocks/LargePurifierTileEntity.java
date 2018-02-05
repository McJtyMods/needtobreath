package mcjty.needtobreathe.blocks;

import mcjty.lib.entity.GenericTileEntity;
import mcjty.needtobreathe.compat.LCSphere;
import mcjty.needtobreathe.compat.LostCitySupport;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.data.LongPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class LargePurifierTileEntity extends GenericTileEntity implements ITickable {

    private BlockPos center = null;
    private float radius;

    @Override
    public void update() {
        if (!world.isRemote) {
            CleanAirManager manager = CleanAirManager.getManager();
            DimensionData data = manager.getDimensionData(world.provider.getDimension());
            if (data == null) {
                // This dimension doesn't need a purifier
                return;
            }

            BlockPos center = getPurifyingSpot();
            float radius = getPurifyingRadius();
            float sqradius = radius*radius;
            float inner = radius * 0.8f;
            float sqinner = inner*inner;

            int r = (int) ((radius-7)/8);

            for (int y = center.getY() - r * 8 ; y <= center.getY() + r * 8 ; y += 8) {
                if (y > 0 && y < 255) {
                    for (int x = center.getX() - r * 8; x <= center.getX() + r * 8; x += 8) {
                        for (int z = center.getZ() - r * 8; z <= center.getZ() + r * 8; z += 8) {
                            BlockPos p = new BlockPos(x, y, z);
                            double sqdist = p.distanceSq(center);
                            if (sqdist < sqradius) {
                                purifyAir(data, p, sqdist < sqinner);
                            }
                        }
                    }
                }
            }
        }
    }

    private void purifyAir(DimensionData data, BlockPos pp, boolean strong) {
        if (strong) {
            data.fillCleanAirStrong(pp);
            return;
        }

        if (data.isValid(world, pp)) {
            data.fillCleanAir(pp.getX(), pp.getY(), pp.getZ());
        }
        BlockPos p2;
        p2 = pp.down();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
        p2 = pp.up();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
        p2 = pp.north();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
        p2 = pp.south();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
        p2 = pp.west();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
        p2 = pp.east();
        if (data.isValid(world, p2)) {
            data.fillCleanAir(p2);
        }
    }

    private BlockPos getPurifyingSpot() {
        if (center != null) {
            return center;
        }
        LCSphere sphere = LostCitySupport.isInSphere(world, pos);
        if (sphere != null) {
            center = sphere.getCenter();

            radius = sphere.getRadius();
        } else {
            center = pos.up();
            radius = 50;
        }
        return center;
    }

    private float getPurifyingRadius() {
        if (center == null) {
            getPurifyingSpot();
        }
        return radius;
    }
}
