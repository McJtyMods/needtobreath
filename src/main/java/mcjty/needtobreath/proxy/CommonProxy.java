package mcjty.needtobreath.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.lib.McJtyLib;
import mcjty.lib.network.PacketHandler;
import mcjty.needtobreath.ForgeEventHandlers;
import mcjty.needtobreath.NeedToBreath;
import mcjty.needtobreath.blocks.ModBlocks;
import mcjty.needtobreath.items.ModItems;
import mcjty.needtobreath.network.NTBMessages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.concurrent.Callable;

public class CommonProxy {

    // Config instance
//    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        McJtyLib.preInit(e);

//        File directory = e.getModConfigurationDirectory();
//        config = new Configuration(new File(directory.getPath(), "meecreeps.cfg"));
//        Config.readConfig();

//        PacketHandler.registerMessages("meecreeps");
        SimpleNetworkWrapper network = PacketHandler.registerMessages(NeedToBreath.MODID, "needtobreath");
        NTBMessages.registerNetworkMessages(network);

        // Initialization of blocks and items typically goes here:
//        ModEntities.init();
        ModItems.init();
        ModBlocks.init();
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
//        if (config.hasChanged()) {
//            config.save();
//        }
    }

    public World getClientWorld() {
        throw new IllegalStateException("This should only be called from client side");
    }

    public EntityPlayer getClientPlayer() {
        throw new IllegalStateException("This should only be called from client side");
    }

    public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
        throw new IllegalStateException("This should only be called from client side");
    }

    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        throw new IllegalStateException("This should only be called from client side");
    }

}
