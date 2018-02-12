package mcjty.needtobreathe.data;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IntersectionHelper {

    public static boolean rayTraceBlocks(World world, Vec3d start, Vec3d end) {

        int sx = MathHelper.floor(start.x);
        int sy = MathHelper.floor(start.y);
        int sz = MathHelper.floor(start.z);

        BlockPos blockpos = new BlockPos(sx, sy, sz);
        IBlockState state = world.getBlockState(blockpos);
        if (!DimensionData.isValid(world, state, blockpos)) {
//            RayTraceResult rc = state.collisionRayTrace(world, blockpos, start, end);
//            if (rc != null) {
//                return true;
//            }
            return true;
        }

        int ex = MathHelper.floor(end.x);
        int ey = MathHelper.floor(end.y);
        int ez = MathHelper.floor(end.z);

        int k1 = 200;

        while (k1-- >= 0) {
            if (sx == ex && sy == ey && sz == ez) {
                return false;
            }

            double dx = 999.0D;
            double dy = 999.0D;
            double dz = 999.0D;
            double ddx = 999.0D;
            double ddy = 999.0D;
            double ddz = 999.0D;
            double diffx = end.x - start.x;
            double diffy = end.y - start.y;
            double diffz = end.z - start.z;

            if (ex > sx) {
                dx = sx + 1.0D;
                ddx = (dx - start.x) / diffx;
            } else if (ex < sx) {
                dx = sx + 0.0D;
                ddx = (dx - start.x) / diffx;
            }
            if (ddx == -0.0D) {
                ddx = -1.0E-4D;
            }

            if (ey > sy) {
                dy = sy + 1.0D;
                ddy = (dy - start.y) / diffy;
            } else if (ey < sy) {
                dy = sy + 0.0D;
                ddy = (dy - start.y) / diffy;
            }
            if (ddy == -0.0D) {
                ddy = -1.0E-4D;
            }

            if (ez > sz) {
                dz = sz + 1.0D;
                ddz = (dz - start.z) / diffz;
            } else if (ez < sz) {
                dz = sz + 0.0D;
                ddz = (dz - start.z) / diffz;
            }
            if (ddz == -0.0D) {
                ddz = -1.0E-4D;
            }

            EnumFacing enumfacing;

            if (ddx < ddy && ddx < ddz) {
                enumfacing = ex > sx ? EnumFacing.WEST : EnumFacing.EAST;
                start = new Vec3d(dx, start.y + diffy * ddx, start.z + diffz * ddx);
            } else if (ddy < ddz) {
                enumfacing = ey > sy ? EnumFacing.DOWN : EnumFacing.UP;
                start = new Vec3d(start.x + diffx * ddy, dy, start.z + diffz * ddy);
            } else {
                enumfacing = ez > sz ? EnumFacing.NORTH : EnumFacing.SOUTH;
                start = new Vec3d(start.x + diffx * ddz, start.y + diffy * ddz, dz);
            }

            sx = MathHelper.floor(start.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            sy = MathHelper.floor(start.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
            sz = MathHelper.floor(start.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(sx, sy, sz);
            state = world.getBlockState(blockpos);
            if (!DimensionData.isValid(world, state, blockpos)) {
//                RayTraceResult rc = state.collisionRayTrace(world, blockpos, start, end);
//                if (rc != null) {
//                    return true;
//                }
                return true;
            }
        }

        return false;
    }

}
