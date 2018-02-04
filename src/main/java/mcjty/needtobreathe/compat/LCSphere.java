package mcjty.needtobreathe.compat;

import net.minecraft.util.math.BlockPos;

public class LCSphere {
    private final BlockPos center;
    private final float radius;

    public LCSphere(BlockPos center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public BlockPos getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }
}
