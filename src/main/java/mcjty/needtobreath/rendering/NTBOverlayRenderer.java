package mcjty.needtobreath.rendering;

import mcjty.needtobreath.NeedToBreath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class NTBOverlayRenderer {

    private static Map<Long, Byte> cleanAir;

    public static void setCleanAir(Map<Long, Byte> cleanAir) {
        NTBOverlayRenderer.cleanAir = cleanAir;
    }


    private static int prevCnt = -1;

    public static void onRender(RenderWorldLastEvent event) {
//        EntityPlayerSP player = Minecraft.getMinecraft().player;
//        if (player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
//            return;
//        }

//        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != ModItems.radiationMonitorItem) {
//            return;
//        }


        if (cleanAir != null) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP p = mc.player;
            int cnt = cleanAir.size();
            if (cnt != prevCnt) {
                System.out.println("cleanAir = " + cnt);
                prevCnt = cnt;
            }
            renderHighlightedBlocks(event, p, cleanAir);
        }
    }

//    private static void renderProtectedBlocks(RenderWorldLastEvent evt) {
//        Minecraft mc = Minecraft.getMinecraft();
//        EntityPlayerSP p = mc.player;
//        ItemStack heldItem = p.getHeldItem(EnumHand.MAIN_HAND);
//        if (heldItem.isEmpty()) {
//            return;
//        }
//        renderHighlightedBlocks(evt, p, cleanAir);
//    }

    public static final ResourceLocation YELLOWGLOW = new ResourceLocation(NeedToBreath.MODID, "textures/effects/blueglow.png");

    private static void renderHighlightedBlocks(RenderWorldLastEvent evt, EntityPlayerSP p, Map<Long, Byte> cleanAir) {
        double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * evt.getPartialTicks();
        double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * evt.getPartialTicks();
        double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * evt.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Minecraft.getMinecraft().getTextureManager().bindTexture(YELLOWGLOW);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
//        tessellator.setColorRGBA(255, 255, 255, 64);
//        tessellator.setBrightness(240);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for (Map.Entry<Long, Byte> entry : cleanAir.entrySet()) {
            int value = entry.getValue() & 0xff;
            BlockPos coordinate = BlockPos.fromLong(entry.getKey());
            float x = coordinate.getX();
            float y = coordinate.getY();
            float z = coordinate.getZ();
            buffer.setTranslation(buffer.xOffset + x, buffer.yOffset + y, buffer.zOffset + z);

            int alpha = value;
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.UP.ordinal(), 1.1f, -0.05f, alpha);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.DOWN.ordinal(), 1.1f, -0.05f, alpha);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.NORTH.ordinal(), 1.1f, -0.05f, alpha);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.SOUTH.ordinal(), 1.1f, -0.05f, alpha);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.WEST.ordinal(), 1.1f, -0.05f, alpha);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.EAST.ordinal(), 1.1f, -0.05f, alpha);
            buffer.setTranslation(buffer.xOffset - x, buffer.yOffset - y, buffer.zOffset - z);
        }
        tessellator.draw();

        GlStateManager.disableBlend();
//        GlStateManager.disableTexture2D();
//        GlStateManager.color(.5f, .3f, 0);
//        GlStateManager.glLineWidth(2);

//        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
//
//        for (BlockPos coordinate : coordinates) {
//            mcjty.lib.gui.RenderHelper.renderHighLightedBlocksOutline(buffer,
//                    base.getX() + coordinate.getX(), base.getY() + coordinate.getY(), base.getZ() + coordinate.getZ(),
//                    .5f, .3f, 0f, 1.0f);
//        }
//        tessellator.draw();

//        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }


}
