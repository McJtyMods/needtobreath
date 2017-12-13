package mcjty.needtobreath;

import mcjty.lib.base.ModBase;
import mcjty.needtobreath.data.CleanAirManager;
import mcjty.needtobreath.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NeedToBreath.MODID, name = "NeedToBreath",
        dependencies = "after:forge@[" + NeedToBreath.MIN_FORGE_VER + ",)",
        version = NeedToBreath.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)")
public class NeedToBreath implements ModBase {
    public static final String MODID = "needtobreath";
    public static final String VERSION = "1.0.0";
    public static final String MIN_FORGE_VER = "14.22.0.2464";

    @SidedProxy(clientSide = "mcjty.needtobreath.proxy.ClientProxy", serverSide = "mcjty.needtobreath.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static NeedToBreath instance;

    public static Logger logger;

    public static CreativeTabs creativeTab = new CreativeTabs("needtobreath") {
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

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {

    }
}
