package mcjty.needtobreathe;

import mcjty.needtobreathe.config.ConfigSetup;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            return;
        }
        World world = evt.world;
        DimensionData data = getDimensionData(world);
        if (data != null) {
            data.worldTick(world);
        }
    }

    public void onBlockPlace(BlockEvent.PlaceEvent evt) {
        World world = evt.getWorld();
        if (!world.isRemote) {
            DimensionData data = getDimensionData(world);
            if (data != null) {
                data.placeBlock(evt.getPos());
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent evt) {
        World world = evt.getWorld();
        if (!world.isRemote) {
            DimensionData data = getDimensionData(world);
            if (data != null) {
                data.breakBlock(evt.getPos());
            }
        }
    }

    @SubscribeEvent
    public void onCropGrowth(BlockEvent.CropGrowEvent.Pre evt) {
        World world = evt.getWorld();
        if (world.isRemote) {
            return;
        }
        DimensionData data = getDimensionData(world);
        if (data != null) {
            if (preventPlantGrowth(world, data, evt.getPos())) {
                evt.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void onBonemeal(BonemealEvent evt) {
        World world = evt.getWorld();
        if (world.isRemote) {
            return;
        }
        DimensionData data = getDimensionData(world);
        if (data != null) {
            if (preventPlantGrowth(world, data, evt.getPos())) {
                evt.setResult(Event.Result.ALLOW);
            }
        }
    }

    private boolean preventPlantGrowth(World world, DimensionData data, BlockPos pos) {
        int poison = data.getPoison(world, pos);
        if (poison > ConfigSetup.PLANT_GROWTH_POISON_DENY) {
            return true;
        } else if (poison > ConfigSetup.PLANT_GROWTH_POISON_SLOW) {
            if (world.rand.nextFloat() < ConfigSetup.PLANT_GROWTH_SLOWDOWN_FACTOR) {
                return true;
            }
        }
        return false;
    }

    private DimensionData getDimensionData(World world) {
        if (!ConfigSetup.hasPoison(world.provider.getDimension())) {
            return null;
        }
        CleanAirManager manager = CleanAirManager.getManager();
        return manager.getDimensionData(world.provider.getDimension());
    }
}
