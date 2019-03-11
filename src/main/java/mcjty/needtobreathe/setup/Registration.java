package mcjty.needtobreathe.setup;


import mcjty.lib.McJtyRegister;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Registration {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(NeedToBreathe.instance, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(NeedToBreathe.instance, event.getRegistry());
        event.getRegistry().register(ModItems.protectiveHelmet);
        event.getRegistry().register(ModItems.informationGlasses);
        event.getRegistry().register(ModItems.hazmatSuitBoots);
        event.getRegistry().register(ModItems.hazmatSuitChest);
        event.getRegistry().register(ModItems.hazmatSuitHelmet);
        event.getRegistry().register(ModItems.hazmatSuitLegs);
        event.getRegistry().register(ModItems.insulatedLeather);

        if (ModSetup.baubles) {
            event.getRegistry().register(ModItems.protectiveBauble);
        }
    }

}
