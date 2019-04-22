package mcjty.needtobreathe.setup;

import mcjty.lib.setup.DefaultModSetup;
import mcjty.needtobreathe.CommandHandler;
import mcjty.needtobreathe.ForgeEventHandlers;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.blocks.ModBlocks;
import mcjty.needtobreathe.compat.LostCitySupport;
import mcjty.needtobreathe.config.ConfigSetup;
import mcjty.needtobreathe.items.ModItems;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

public class ModSetup extends DefaultModSetup {

    public static boolean baubles = false;
    public static boolean lostcities = false;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        NetworkRegistry.INSTANCE.registerGuiHandler(NeedToBreathe.instance, new GuiProxy());

        CommandHandler.registerCommands();

        NTBMessages.registerMessages("needtobreathe");

        ModItems.init();
        ModBlocks.init();
    }

    @Override
    protected void setupModCompat() {
        baubles = Loader.isModLoaded("Baubles") || Loader.isModLoaded("baubles");
        if (baubles) {
            getLogger().log(Level.INFO, "NeedToBreathe Detected Baubles: enabling support");
        }

        lostcities = Loader.isModLoaded("lostcities");
        if (lostcities) {
            getLogger().log(Level.INFO, "NeedToBreathe Detected Lost Cities: enabling support");
            LostCitySupport.register();
        }
    }

    @Override
    protected void setupConfig() {
        ConfigSetup.init();
    }

    @Override
    public void createTabs() {
        createTab("needtobreathe", () -> new ItemStack(ModItems.hazmatSuitHelmet));
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        ConfigSetup.postInit();
    }
}
