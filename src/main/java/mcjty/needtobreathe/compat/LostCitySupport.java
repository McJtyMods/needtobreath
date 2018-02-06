package mcjty.needtobreathe.compat;

import mcjty.lostcities.api.ILostChunkGenerator;
import mcjty.lostcities.api.ILostChunkInfo;
import mcjty.lostcities.api.ILostSphere;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LostCitySupport {



    public static LCSphere isInSphere(World world, BlockPos pos) {
        WorldServer ws = (WorldServer) world;
        if (ws.getChunkProvider().chunkGenerator instanceof ILostChunkGenerator) {
            ILostChunkGenerator gen = (ILostChunkGenerator) ws.getChunkProvider().chunkGenerator;
            ILostChunkInfo chunkInfo = gen.getChunkInfo(pos.getX() >> 4, pos.getZ() >> 4);
            int realHeight = gen.getRealHeight(0);
            ILostSphere sphere = chunkInfo.getSphere();
            if (sphere != null) {
                // @todo temporary until LC is fixed with correct center
                return new LCSphere(new BlockPos(sphere.getCenterPos().getX(), realHeight, sphere.getCenterPos().getZ()), sphere.getRadius());
//                return new LCSphere(sphere.getCenterPos(), sphere.getRadius());
            }
        }
        return null;
    }

}