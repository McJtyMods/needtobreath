package mcjty.needtobreathe.data;

import net.minecraft.util.EnumFacing;

/**
 * All clean air in a subchunk (16x16x16)
 */
public class ChunkData {

    private byte data[];        // 0 = no clean air, 255 = 100% clean
    private boolean strong;     // Full clean, no ticking

    public ChunkData() {
        data = new byte[4096];
        strong = false;
    }

    public ChunkData(byte[] data) {
        this.data = data;
        strong = false;
    }

    public ChunkData(boolean strong) {
        this.data = null;
        this.strong = strong;
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

    public int getPoison(int x, int y, int z) {
        int idx = ((x & 0xf) << 8) + ((y & 0xf) << 4) + (z & 0xf);
        return 255-(data[idx] & 0xff);
    }

    public int getAir(int x, int y, int z) {
        int idx = ((x & 0xf) << 8) + ((y & 0xf) << 4) + (z & 0xf);
        return data[idx] & 0xff;
    }

    public void putAir(int x, int y, int z, int air) {
        int idx = ((x & 0xf) << 8) + ((y & 0xf) << 4) + (z & 0xf);
        data[idx] = (byte) air;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isStrong() {
        return strong;
    }
}
