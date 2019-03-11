package mcjty.needtobreathe.proxy;

import mcjty.lib.setup.DefaultCommonSetup;
import mcjty.needtobreathe.CommandHandler;
import mcjty.needtobreathe.ForgeEventHandlers;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.blocks.ModBlocks;
import mcjty.needtobreathe.compat.LostCitySupport;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.items.ModItems;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

import java.io.File;

public class CommonSetup extends DefaultCommonSetup {

    public static boolean baubles = false;
    public static boolean lostcities = false;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        baubles = Loader.isModLoaded("Baubles") || Loader.isModLoaded("baubles");
        if (baubles) {
            getLogger().log(Level.INFO, "NeedToBreathe Detected Baubles: enabling support");
        }

        lostcities = Loader.isModLoaded("lostcities");
        if (lostcities) {
            getLogger().log(Level.INFO, "NeedToBreathe Detected Lost Cities: enabling support");
            LostCitySupport.register();
        }

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        CommandHandler.registerCommands();

        Config.readConfig();

        NTBMessages.registerMessages("needtobreathe");

        // Initialization of blocks and items typically goes here:
//        ModEntities.init();
        ModItems.init();
        ModBlocks.init();
    }

    @Override
    public void createTabs() {
        createTab("needtobreathe", new ItemStack(Blocks.DIAMOND_BLOCK));
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        NetworkRegistry.INSTANCE.registerGuiHandler(NeedToBreathe.instance, new GuiProxy());
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        if (Config.mainConfig.hasChanged()) {
            Config.mainConfig.save();
        }
        Config.mainConfig = null;
    }
}