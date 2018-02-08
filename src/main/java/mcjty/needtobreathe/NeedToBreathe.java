package mcjty.needtobreathe;

import mcjty.lib.base.ModBase;
import mcjty.needtobreathe.commands.CommandTest;
import mcjty.needtobreathe.compat.LostCitySupport;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = NeedToBreathe.MODID, name = "NeedToBreathe",
        dependencies =
                "required-after:mcjtylib_ng@[" + NeedToBreathe.MIN_MCJTYLIB_VER + ",);" +
                "after:forge@[" + NeedToBreathe.MIN_FORGE_VER + ",)",
        version = NeedToBreathe.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)")
public class NeedToBreathe implements ModBase {
    public static final String MODID = "needtobreathe";
    public static final String MIN_MCJTYLIB_VER = "2.6.2";
    public static final String VERSION = "0.1.0beta";
    public static final String MIN_FORGE_VER = "14.22.0.2464";

    @SidedProxy(clientSide = "mcjty.needtobreathe.proxy.ClientProxy", serverSide = "mcjty.needtobreathe.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static NeedToBreathe instance;

    public static Logger logger;

    public static boolean lostcities = false;

    public static final String SHIFT_MESSAGE = "<Press Shift>";


    public static CreativeTabs creativeTab = new CreativeTabs("needtobreathe") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.DIAMOND_BLOCK);
        }
    };


    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        this.proxy.preInit(e);
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        this.proxy.init(e);
        lostcities = Loader.isModLoaded("lostcities");
        if (lostcities) {
            LostCitySupport.registerListener();
        }
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        this.proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        CleanAirManager.clearInstance();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTest());
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {

    }
}
