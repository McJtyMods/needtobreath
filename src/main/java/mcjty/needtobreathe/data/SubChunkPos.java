package mcjty.needtobreathe.data;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static mcjty.needtobreathe.data.ChunkData.*;

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

    public SubChunkPos offset(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return new SubChunkPos(cx, cy-1, cz);
            case UP:
                return new SubChunkPos(cx, cy+1, cz);
            case NORTH:
                return new SubChunkPos(cx, cy, cz-1);
            case SOUTH:
                return new SubChunkPos(cx, cy, cz+1);
            case WEST:
                return new SubChunkPos(cx-1, cy, cz);
            case EAST:
                return new SubChunkPos(cx+1, cy, cz);
        }
        return this;
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
        return new SubChunkPos(pos.getX()>>CHUNK_SHIFT, pos.getY()>>CHUNK_SHIFT, pos.getZ()>>CHUNK_SHIFT);
    }

    public static SubChunkPos fromPos(int x, int y, int z) {
        return new SubChunkPos(x>>CHUNK_SHIFT, y>>CHUNK_SHIFT, z>>CHUNK_SHIFT);
    }

    public BlockPos toPos(int dx, int dy, int dz) {
        return new BlockPos((cx<<CHUNK_SHIFT)+dx, (cy<<CHUNK_SHIFT)+dy, (cz<<CHUNK_SHIFT)+dz);
    }

    public BlockPos toPos(int idx) {
        return new BlockPos((cx<<CHUNK_SHIFT)+((idx>>CHUNK_2SHIFT) & CHUNK_MASK), (cy<<CHUNK_SHIFT)+((idx>>CHUNK_SHIFT) & CHUNK_MASK), (cz<<CHUNK_SHIFT)+(idx & CHUNK_MASK));
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

    @Override
    public String toString() {
        return "SubChunkPos{" + cx + "," + cy + "," + cz + '}';
    }
}
