package mcjty.needtobreathe;

import mcjty.lib.base.ModBase;
import mcjty.lib.proxy.IProxy;
import mcjty.needtobreathe.commands.CommandTest;
import mcjty.needtobreathe.proxy.CommonSetup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = NeedToBreathe.MODID, name = "NeedToBreathe",
        dependencies =
                "required-after:mcjtylib_ng@[" + NeedToBreathe.MIN_MCJTYLIB_VER + ",);" +
                "after:forge@[" + NeedToBreathe.MIN_FORGE_VER + ",)",
        version = NeedToBreathe.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)")
public class NeedToBreathe implements ModBase {
    public static final String MODID = "needtobreathe";
    public static final String MIN_MCJTYLIB_VER = "3.1.0";
    public static final String VERSION = "0.3.1";
    public static final String MIN_FORGE_VER = "14.22.0.2464";

    @SidedProxy(clientSide = "mcjty.needtobreathe.proxy.ClientProxy", serverSide = "mcjty.needtobreathe.proxy.ServerProxy")
    public static IProxy proxy;
    public static CommonSetup setup = new CommonSetup();

    @Mod.Instance(MODID)
    public static NeedToBreathe instance;

    public static final String SHIFT_MESSAGE = "<Press Shift>";


    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        setup.preInit(e);
        proxy.preInit(e);
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        setup.init(e);
        proxy.init(e);
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        setup.postInit(e);
        proxy.postInit(e);
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
