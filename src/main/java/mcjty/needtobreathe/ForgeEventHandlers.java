package mcjty.needtobreathe;

import mcjty.lib.McJtyRegister;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.network.PacketSendCleanAirToClient;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(NeedToBreathe.instance, event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(NeedToBreathe.instance, event.getRegistry());
    }

    public static final int MAXTICKS = 10;
    private int counter = MAXTICKS;

    private static Potion witherEffect;
    private static Potion weaknessEffect;
    private static Potion poisonEffect;

    private static void getPotions() {
        if (witherEffect == null) {
            witherEffect = Potion.REGISTRY.getObject(new ResourceLocation("wither"));
            weaknessEffect = Potion.REGISTRY.getObject(new ResourceLocation("weakness"));
            poisonEffect = Potion.REGISTRY.getObject(new ResourceLocation("poison"));
        }
    }

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
            CleanAirManager manager = CleanAirManager.getManager();
            manager.tick(evt.world);

            // @todo Set timeout for this
            for (Entity entity : evt.world.loadedEntityList) {
                int poison = manager.getPoison(entity.getPosition().up());
                if (entity instanceof EntityPlayer) {
                    if (poison > 250) {
                        entity.attackEntityFrom(DamageSource.GENERIC, 1000);
                    }

                    if (poison > 220) {
                        getPotions();
                        ((EntityPlayer) entity).addPotionEffect(new PotionEffect(witherEffect, 10));
                    }

                    if (poison > 180) {
                        getPotions();
                        ((EntityPlayer) entity).addPotionEffect(new PotionEffect(poisonEffect, 10));
                    }

                    if (poison > 100) {
                        getPotions();
                        ((EntityPlayer) entity).addPotionEffect(new PotionEffect(weaknessEffect, 10));
                    }
                }
            }

            // @todo temporary debug code!
            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(manager.getCleanAir());
            NTBMessages.INSTANCE.sendToAll(message);
        }
    }

}
