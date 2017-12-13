package mcjty.needtobreath;

import mcjty.lib.McJtyRegister;
import mcjty.needtobreath.data.CleanAirManager;
import mcjty.needtobreath.network.PacketSendCleanAirToClient;
import mcjty.needtobreath.network.NTBMessages;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(NeedToBreath.instance, event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(NeedToBreath.instance, event.getRegistry());
    }

    public static final int MAXTICKS = 10;
    private int counter = MAXTICKS;

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            return;
        }
        if (evt.world.provider.getDimension() != 0) {
            return;
        }
        counter--;
        if (counter <= 0) {
            counter = MAXTICKS;
            CleanAirManager manager = CleanAirManager.getManager(evt.world);
            manager.tick();

            // @todo temporary debug code!
            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(manager.getCleanAir());
            NTBMessages.INSTANCE.sendToAll(message);
        }
    }

}
