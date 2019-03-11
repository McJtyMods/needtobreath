package mcjty.needtobreathe.setup;

import mcjty.lib.setup.DefaultClientProxy;
import mcjty.needtobreathe.rendering.NTBOverlayRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends DefaultClientProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(this);
//        OBJLoader.INSTANCE.addDomain(MeeCreeps.MODID);
//        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

        // Typically initialization of models and such goes here:
//        ModEntities.initModels();
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
        NTBOverlayRenderer.onRenderWorld(evt);
    }

    @SubscribeEvent
    public void renderGameOverlayEvent(RenderGameOverlayEvent evt) {
        NTBOverlayRenderer.onRenderGame(evt);
    }
}
