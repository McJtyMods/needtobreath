package mcjty.needtobreathe.data;

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
