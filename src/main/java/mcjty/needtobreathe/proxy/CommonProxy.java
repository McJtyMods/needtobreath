package mcjty.needtobreathe.proxy;

import mcjty.lib.network.PacketHandler;
import mcjty.lib.proxy.AbstractCommonProxy;
import mcjty.needtobreathe.CommandHandler;
import mcjty.needtobreathe.ForgeEventHandlers;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.blocks.ModBlocks;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.items.ModItems;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.io.File;

public class CommonProxy extends AbstractCommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        CommandHandler.registerCommands();

        mainConfig = new Configuration(new File(modConfigDir.getPath(), "needtobreathe.cfg"));
        Config.readConfig(mainConfig);

//        PacketHandler.registerMessages("meecreeps");
        SimpleNetworkWrapper network = PacketHandler.registerMessages(NeedToBreathe.MODID, "needtobreathe");
        NTBMessages.registerNetworkMessages(network);

        // Initialization of blocks and items typically goes here:
//        ModEntities.init();
        ModItems.init();
        ModBlocks.init();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        NetworkRegistry.INSTANCE.registerGuiHandler(NeedToBreathe.instance, new GuiProxy());
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
        mainConfig = null;
    }
}
