package mcjty.needtobreathe.data;

import net.minecraft.util.math.BlockPos;

/**
 * All clean air in a subchunk (16x16x16)
 */
public class ChunkData {

    private byte data[];       // 0 = no clean air, 255 = 100% clean

    public ChunkData() {
        data = new byte[4096];
    }

    public ChunkData(byte[] data) {
        this.data = data;
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
}
