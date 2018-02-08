package mcjty.needtobreathe.data;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static mcjty.needtobreathe.data.ChunkData.*;

/**
 * A coordinate for a subchunk
 */
public class SubChunkPosIndexed {

    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public static long toLong(int cx, int cy, int cz) {
        return (cx & X_MASK) << X_SHIFT | (cy & Y_MASK) << Y_SHIFT | (cz & Z_MASK);
    }

    public static int getX(long sidx) {
        return (int) ((sidx >> X_SHIFT) & X_MASK);
    }

    public static int getY(long sidx) {
        return (int) ((sidx >> Y_SHIFT) & Y_MASK);
    }

    public static int getZ(long sidx) {
        return (int) (sidx & Z_MASK);
    }

    public static long offset(EnumFacing facing, long sidx) {
        switch (facing) {
            case DOWN:
                if (getY(sidx) == 0) {
                    return sidx;
                }
                return sidx - (1L << Y_SHIFT);
            case UP:
                if (getY(sidx) >= (256/CHUNK_DIM)-1) {
                    return sidx;
                }
                return sidx + (1L << Y_SHIFT);
            case NORTH:
                return sidx-1;
            case SOUTH:
                return sidx+1;
            case WEST:
                return sidx - (1L << X_SHIFT);
            case EAST:
                return sidx + (1L << X_SHIFT);
        }
        return sidx;
    }

    public static long fromPos(BlockPos pos) {
        return toLong(pos.getX()>>CHUNK_SHIFT, pos.getY()>>CHUNK_SHIFT, pos.getZ()>>CHUNK_SHIFT);
    }

    public static long fromPos(int x, int y, int z) {
        return toLong(x>>CHUNK_SHIFT, y>>CHUNK_SHIFT, z>>CHUNK_SHIFT);
    }

    public static BlockPos toPos(long sidx, int dx, int dy, int dz) {
        return new BlockPos((getX(sidx)<<CHUNK_SHIFT)+dx, (getY(sidx)<<CHUNK_SHIFT)+dy, (getZ(sidx)<<CHUNK_SHIFT)+dz);
    }

    public static BlockPos toPos(long sidx, int idx) {
        return new BlockPos((getX(sidx)<<CHUNK_SHIFT)+((idx>>CHUNK_2SHIFT) & CHUNK_MASK), (getY(sidx)<<CHUNK_SHIFT)+((idx>>CHUNK_SHIFT) & CHUNK_MASK), (getZ(sidx)<<CHUNK_SHIFT)+(idx & CHUNK_MASK));
    }
}
