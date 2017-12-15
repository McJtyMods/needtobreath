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
    }

    public static final int MAXTICKS = 10;
    public static final int MAXEFFECTSTICKS = 5;
    public static final int EFFECT_DURATION = MAXTICKS * MAXEFFECTSTICKS * 2;

    private int counter = MAXTICKS;
    private int effectCounter = MAXEFFECTSTICKS;

    private static Potion witherEffect;
    private static Potion weaknessEffect;
    private static Potion poisonEffect;
    private static Potion slownessEffect;

    private static void getPotions() {
        if (witherEffect == null) {
            witherEffect = Potion.REGISTRY.getObject(new ResourceLocation("wither"));
            weaknessEffect = Potion.REGISTRY.getObject(new ResourceLocation("weakness"));
            poisonEffect = Potion.REGISTRY.getObject(new ResourceLocation("poison"));
            slownessEffect = Potion.REGISTRY.getObject(new ResourceLocation("slowness"));
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

            effectCounter--;
            if (effectCounter <= 0) {
                effectCounter = MAXEFFECTSTICKS;
                handleEffects(evt, manager);
            }

            // @todo temporary debug code!
            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(manager.getCleanAir());
            NTBMessages.INSTANCE.sendToAll(message);
        }
    }

    private void handleEffects(TickEvent.WorldTickEvent evt, CleanAirManager manager) {
        List<Pair<Integer, Entity>> affectedEntities = new ArrayList<>();
        for (Entity entity : evt.world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                int poison = manager.getPoison(entity.getPosition().up());
                if (poison > 30) {
                    affectedEntities.add(Pair.of(poison, entity));
                }
            }
        }

        for (Pair<Integer, Entity> pair : affectedEntities) {
            Entity entity = pair.getRight();
            Integer poison = pair.getLeft();

            if (poison > 250) {
                entity.attackEntityFrom(DamageSource.GENERIC, 1000);
            } else {
                if (poison > 220) {
                    getPotions();
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(witherEffect, EFFECT_DURATION));
                }

                if (poison > 150) {
                    getPotions();
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(poisonEffect, EFFECT_DURATION));
                }

                if (poison > 60) {
                    getPotions();
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(slownessEffect, EFFECT_DURATION));
                }

                if (poison > 30) {
                    getPotions();
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(weaknessEffect, EFFECT_DURATION));
                }
            }
        }
    }

}
