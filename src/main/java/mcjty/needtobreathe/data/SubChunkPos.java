package mcjty.needtobreathe.data;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * A coordinate for a subchunk
 */
public class SubChunkPos {
    private final int cx;
    private final int cy;
    private final int cz;

    public SubChunkPos(int cx, int cy, int cz) {
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
    }

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
    }

    public int getCz() {
        return cz;
    }

    public static SubChunkPos fromPos(BlockPos pos) {
        return new SubChunkPos(pos.getX()>>4, pos.getY()>>4, pos.getZ()>>4);
    }

    public static SubChunkPos fromPos(int x, int y, int z) {
        return new SubChunkPos(x>>4, y>>4, z>>4);
    }

    public BlockPos toPos(int dx, int dy, int dz) {
        return new BlockPos(cx*16+dx, cy*16+dy, cz*16+dz);
    }

    public BlockPos toPos(int idx) {
        return new BlockPos(cx*16+((idx>>8) & 0xf), cy*16+((idx>>4) & 0xf), cz*16+(idx & 0xf));
    }

    public static int index(int dx, int dy, int dz) {
        return (dx << 8) + (dy << 4) + dz;
    }

    // Given an index in the inner part of the data (not including the outer boundary of the 16x16x16 subchunk) calculate a new index with the given offset
    public static int offset(int idx, EnumFacing offset) {
        switch (offset) {
            case DOWN:
                return idx - 16;
            case UP:
                return idx + 16;
            case NORTH:
                return idx - 1;
            case SOUTH:
                return idx + 1;
            case WEST:
                return idx - 256;
            case EAST:
                return idx + 256;
        }
        return idx;
    }

    // This version will take the border into account by returning a negative index in that case (negating that index makes a valid index in the adjacent subchunk)
    public static int offsetWithCheck(int idx, EnumFacing offset) {
        switch (offset) {
            case DOWN:
                if ((idx & 0xf0) == 0) {
                    return - (idx | 0xf0);
                }
                return idx - 16;
            case UP:
                if ((idx & 0xf0) == 0xf0) {
                    return - (idx & 0xf0f);
                }
                return idx + 16;
            case NORTH:
                if ((idx & 0xf) == 0) {
                    return - (idx | 0xf);
                }
                return idx - 1;
            case SOUTH:
                if ((idx & 0xf) == 0xf) {
                    return - (idx & 0xff0);
                }
                return idx + 1;
            case WEST:
                if ((idx & 0xf00) == 0) {
                    return - (idx | 0xf00);
                }
                return idx - 256;
            case EAST:
                if ((idx & 0xf00) == 0xf00) {
                    return - (idx & 0x0ff);
                }
                return idx + 256;
        }
        return idx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubChunkPos that = (SubChunkPos) o;

        if (cx != that.cx) return false;
        if (cy != that.cy) return false;
        return cz == that.cz;

    }

    @Override
    public int hashCode() {
        int result = cx;
        result = 31 * result + cy;
        result = 31 * result + cz;
        return result;
    }
}
