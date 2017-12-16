package mcjty.needtobreathe;

import mcjty.lib.McJtyRegister;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.config.PotionEffectConfig;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.items.InformationGlasses;
import mcjty.needtobreathe.items.ModItems;
import mcjty.needtobreathe.items.ProtectiveHelmet;
import mcjty.needtobreathe.network.NTBMessages;
import mcjty.needtobreathe.network.PacketSendCleanAirToClient;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(NeedToBreathe.instance, event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(NeedToBreathe.instance, event.getRegistry());
        event.getRegistry().register(ModItems.protectiveHelmet);
        event.getRegistry().register(ModItems.informationGlasses);
    }

    public static final int MAXTICKS = 10;
    public static final int MAXEFFECTSTICKS = 5;
    public static final int EFFECT_DURATION = MAXTICKS * MAXEFFECTSTICKS * 2;

    private int counter = MAXTICKS;
    private int effectCounter = MAXEFFECTSTICKS;

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

            effectCounter--;
            if (effectCounter <= 0) {
                effectCounter = MAXEFFECTSTICKS;
                handleEffects(evt, manager);
            }

            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(manager.getCleanAir());
            for (EntityPlayer player : evt.world.playerEntities) {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof InformationGlasses) {
                    NTBMessages.INSTANCE.sendTo(message, (EntityPlayerMP) player);
                }
            }
        }
    }

    private void handleEffects(TickEvent.WorldTickEvent evt, CleanAirManager manager) {
        List<Pair<Integer, Entity>> affectedEntities = new ArrayList<>();
        for (Entity entity : evt.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                int poison = manager.getPoison(entity.getPosition().up());
                if (poison > 20) {
                    affectedEntities.add(Pair.of(poison, entity));
                }
            }
        }

        for (Pair<Integer, Entity> pair : affectedEntities) {
            Entity entity = pair.getRight();
            Integer poison = pair.getLeft();

            PotionEffectConfig[] potionConfigs;

            if (entity instanceof EntityPlayer) {
                potionConfigs = Config.getPlayerEffects();
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof ProtectiveHelmet) {
                    poison = (int) (poison * Config.PROTECTIVE_HELMET_FACTOR);
                    System.out.println("poison = " + poison);
                }
            } else if (entity instanceof IMob) {
                potionConfigs = Config.getHostileEffects();
            } else {
                potionConfigs = Config.getPassiveEffects();
            }

            if (potionConfigs.length > 0) {
                for (PotionEffectConfig config : potionConfigs) {
                    if (poison >= config.getPoisonThresshold()) {
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(config.getPotion(), EFFECT_DURATION, config.getAmplitude()));
                    }
                }
            }
        }
    }

}
