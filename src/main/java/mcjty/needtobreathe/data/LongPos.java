package mcjty.needtobreathe.data;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

/**
 * A local position in a chunk can be represented with a single long like done
 * in this class.
 */
public class LongPos {

    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public static long offset(long pos, EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return posDown(pos);
            case UP:
                return posUp(pos);
            case NORTH:
                return posNorth(pos);
            case SOUTH:
                return posSouth(pos);
            case WEST:
                return posWest(pos);
            case EAST:
                return posEast(pos);
        }
        return pos;
    }

    public static long getX(long pos) {
        return (int) ((pos << (64 - X_SHIFT - NUM_X_BITS)) >> (64 - NUM_X_BITS));
    }

    public static long getY(long pos) {
        return (int) ((pos << (64 - Y_SHIFT - NUM_Y_BITS)) >> (64 - NUM_Y_BITS));
    }

    public static long getZ(long pos) {
        return (int) ((pos << (64 - NUM_Z_BITS)) >> (64 - NUM_Z_BITS));
    }

    public static long posSouth(long pos) {
        return pos + 1;
    }

    public static long posNorth(long pos) {
        return pos - 1;
    }

    public static long posEast(long pos) {
        return pos + (1L << X_SHIFT);
    }

    public static long posWest(long pos) {
        return pos - (1L << X_SHIFT);
    }

    public static long posUp(long pos) {
        if (getY(pos) > 255) {
            return -1L;
        }
        return pos + (1L << Y_SHIFT);
    }

    public static long posDown(long pos) {
        if (getY(pos) <= 0) {
            return -1L;
        }
        return pos - (1L << Y_SHIFT);
    }
}
