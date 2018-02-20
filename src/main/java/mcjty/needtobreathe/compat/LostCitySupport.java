package mcjty.needtobreathe.compat;

import mcjty.lostcities.api.ILostChunkGenerator;
import mcjty.lostcities.api.ILostChunkInfo;
import mcjty.lostcities.api.ILostCities;
import mcjty.lostcities.api.ILostSphere;
import mcjty.needtobreathe.blocks.ModBlocks;
import mcjty.needtobreathe.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class LostCitySupport {

    private static ILostCities lostCities;

    public static LCSphere isInSphere(World world, BlockPos pos) {
        ILostChunkGenerator generator = lostCities.getLostGenerator(world.provider.getDimension());
        if (generator != null) {
            ILostChunkInfo info = generator.getChunkInfo(pos.getX() >> 4, pos.getZ() >> 4);
            ILostSphere sphere = info.getSphere();
            if (sphere != null) {
                int realHeight = generator.getRealHeight(0);
                // @todo temporary until LC is fixed with correct center
                return new LCSphere(new BlockPos(sphere.getCenterPos().getX(), realHeight, sphere.getCenterPos().getZ()), sphere.getRadius());
//                return new LCSphere(sphere.getCenterPos(), sphere.getRadius());
            }
        }
        return null;
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new LostCitySupport());
        FMLInterModComms.sendFunctionMessage("lostcities", "getLostCities", "mcjty.needtobreathe.compat.LostCitySupport$GetLostCities");
    }

    private static Random random = new Random();

    @SubscribeEvent
    public void onLostCityChunkPopulate(PopulateChunkEvent.Post event) {
        ILostChunkGenerator generator = lostCities.getLostGenerator(event.getWorld().provider.getDimension());
        if (generator != null) {
            int chunkX = event.getChunkX();
            int chunkZ = event.getChunkZ();
            ILostChunkInfo info = generator.getChunkInfo(chunkX, chunkZ);
            ILostSphere sphere = info.getSphere();
            if (sphere != null && sphere.isEnabled() && sphere.getCenter().getChunkX() == chunkX && sphere.getCenter().getChunkZ() == chunkZ) {
                int y;
                if (Config.CREATIVE_PURIFIER_GENERATE_HEIGHT == -1) {
                    y = generator.getRealHeight(0);
                } else {
                    y = Config.CREATIVE_PURIFIER_GENERATE_HEIGHT;
                }
                event.getWorld().setBlockState(new BlockPos(sphere.getCenterPos().getX(), y, sphere.getCenterPos().getZ()), ModBlocks.largePurifierBlock.getDefaultState());
            }
        }
    }

    public static class GetLostCities implements Function<ILostCities, Void> {
        @Nullable
        @Override
        public Void apply(ILostCities lc) {
            lostCities = lc;
            return null;
        }
    }

}
