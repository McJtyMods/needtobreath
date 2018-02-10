package mcjty.needtobreathe.blocks;

import mcjty.lib.entity.GenericTileEntity;
import mcjty.needtobreathe.compat.LCSphere;
import mcjty.needtobreathe.compat.LostCitySupport;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.data.LongPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargePurifierTileEntity extends GenericTileEntity implements ITickable {

    private BlockPos center = null;
    private float radius;

    private boolean setup = true;

    @Override
    public void onBlockBreak(World world, BlockPos pos, IBlockState state) {
        super.onBlockBreak(world, pos, state);

        CleanAirManager manager = CleanAirManager.getManager();
        DimensionData data = manager.getDimensionData(world.provider.getDimension());
        if (data == null) {
            // This dimension doesn't need a purifier
            return;
        }

        System.out.println("Cleaning up");
        BlockPos center = getPurifyingSpot();
        float radius = getPurifyingRadius() * .97f;
        if (radius < 0.001) {
            return;
        }
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
                        if (sqdist < sqinner) {
                            removeStrongAir(data, p);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (!setup) {
                return;
            }
            setup = false;

            CleanAirManager manager = CleanAirManager.getManager();
            DimensionData data = manager.getDimensionData(world.provider.getDimension());
            if (data == null) {
                // This dimension doesn't need a purifier
                return;
            }

            BlockPos center = getPurifyingSpot();
            float radius = getPurifyingRadius() * .97f;
            if (radius < 0.001) {
                return;
            }
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
                            if (sqdist < sqinner) {
                                data.fillCleanAirStrong(p);
                            } else if (sqdist < sqradius) {
                                //@todo
                                purifyAir(data, p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeStrongAir(DimensionData data, BlockPos pp) {
        data.removeStrongAir(pp);
    }

    private void purifyAir(DimensionData data, BlockPos pp) {
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
        LCSphere sphere = Config.CREATIVE_PURIFIER_LOSTCITIES ? LostCitySupport.isInSphere(world, pos) : null;
        if (sphere != null) {
            center = sphere.getCenter();
            radius = sphere.getRadius();

        } else {
            center = pos.up();
            radius = Config.CREATIVE_PURIFIER_RADIUS;
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
