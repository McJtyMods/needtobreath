package mcjty.needtobreathe;

import mcjty.lib.McJtyRegister;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(NeedToBreathe.instance, event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(NeedToBreathe.instance, event.getRegistry());
        event.getRegistry().register(ModItems.protectiveHelmet);
        event.getRegistry().register(ModItems.informationGlasses);
        event.getRegistry().register(ModItems.hazmatSuitBoots);
        event.getRegistry().register(ModItems.hazmatSuitChest);
        event.getRegistry().register(ModItems.hazmatSuitHelmet);
        event.getRegistry().register(ModItems.hazmatSuitLegs);
    }

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
        DimensionData data = getDimensionData(world);
        if (data != null) {
            if (preventPlantGrowth(world, data, evt.getPos())) {
                evt.setResult(Event.Result.ALLOW);
                if (world.isRemote) {
                    evt.getEntityPlayer().sendStatusMessage(new TextComponentString(TextFormatting.RED + "The bonemeal did not work!"), false);
                }
            }
        }
    }

    private boolean preventPlantGrowth(World world, DimensionData data, BlockPos pos) {
        int poison = data.getPoison(world, pos);
        if (poison > Config.PLANT_GROWTH_POISON_DENY) {
            return true;
        } else if (poison > Config.PLANT_GROWTH_POISON_SLOW) {
            if (world.rand.nextFloat() < Config.PLANT_GROWTH_SLOWDOWN_FACTOR) {
                return true;
            }
        }
        return false;
    }

    private DimensionData getDimensionData(World world) {
        if (!Config.hasPoison(world.provider.getDimension())) {
            return null;
        }
        CleanAirManager manager = CleanAirManager.getManager();
        return manager.getDimensionData(world.provider.getDimension());
    }
}
