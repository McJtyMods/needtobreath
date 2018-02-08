package mcjty.needtobreathe.compat;

import mcjty.lostcities.api.ILostChunkGenerator;
import mcjty.lostcities.api.ILostChunkInfo;
import mcjty.lostcities.api.ILostSphere;
import mcjty.needtobreathe.blocks.ModBlocks;
import mcjty.needtobreathe.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class LostCitySupport {



    public static LCSphere isInSphere(World world, BlockPos pos) {
        WorldServer ws = (WorldServer) world;
        if (ws.getChunkProvider().chunkGenerator instanceof ILostChunkGenerator) {
            ILostChunkGenerator gen = (ILostChunkGenerator) ws.getChunkProvider().chunkGenerator;
            ILostChunkInfo info = gen.getChunkInfo(pos.getX() >> 4, pos.getZ() >> 4);
            ILostSphere sphere = info.getSphere();
            if (sphere != null) {
                int realHeight = gen.getRealHeight(0);
                // @todo temporary until LC is fixed with correct center
                return new LCSphere(new BlockPos(sphere.getCenterPos().getX(), realHeight, sphere.getCenterPos().getZ()), sphere.getRadius());
//                return new LCSphere(sphere.getCenterPos(), sphere.getRadius());
            }
        }
        return null;
    }

    public static void registerListener() {
        MinecraftForge.EVENT_BUS.register(new LostCitySupport());
    }

    private static Random random = new Random();

    @SubscribeEvent
    public void onLostCityChunkPopulate(PopulateChunkEvent.Post event) {
        WorldServer ws = (WorldServer) event.getWorld();
        if (ws.getChunkProvider().chunkGenerator instanceof ILostChunkGenerator) {
            int chunkX = event.getChunkX();
            int chunkZ = event.getChunkZ();
            ILostChunkGenerator gen = (ILostChunkGenerator) ws.getChunkProvider().chunkGenerator;
            ILostChunkInfo info = gen.getChunkInfo(chunkX, chunkZ);
            ILostSphere sphere = info.getSphere();
            if (sphere != null && sphere.isEnabled() && sphere.getCenter().getChunkX() == chunkX && sphere.getCenter().getChunkZ() == chunkZ) {
                int y;
                if (Config.CREATIVE_PURIFIER_GENERATE_HEIGHT == -1) {
                    y = gen.getRealHeight(0);
                } else {
                    y = Config.CREATIVE_PURIFIER_GENERATE_HEIGHT;
                }
                ws.setBlockState(new BlockPos(sphere.getCenterPos().getX(), y, sphere.getCenterPos().getZ()), ModBlocks.largePurifierBlock.getDefaultState());
            }
        }
    }
}
