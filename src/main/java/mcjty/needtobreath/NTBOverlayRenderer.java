package mcjty.needtobreath;

import mcjty.lib.gui.RenderGlowEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Set;

public class NTBOverlayRenderer {

    private static Map<Long, Byte> cleanAir;

    public static void setCleanAir(Map<Long, Byte> cleanAir) {
        NTBOverlayRenderer.cleanAir = cleanAir;
    }

    public static void onRender(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return;
        }

//        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != ModItems.radiationMonitorItem) {
//            return;
//        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

    }

    private static void renderProtectedBlocks(RenderWorldLastEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP p = mc.player;
        ItemStack heldItem = p.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            return;
        }
//        renderHighlightedBlocks(evt, p, current.getCoordinate(), coordinates);
    }

    public static final ResourceLocation YELLOWGLOW = new ResourceLocation(NeedToBreath.MODID, "textures/blocks/yellowglow.png");

    private static void renderHighlightedBlocks(RenderWorldLastEvent evt, EntityPlayerSP p, BlockPos base, Set<BlockPos> coordinates) {
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

        for (BlockPos coordinate : coordinates) {
            float x = base.getX() + coordinate.getX();
            float y = base.getY() + coordinate.getY();
            float z = base.getZ() + coordinate.getZ();
            buffer.setTranslation(buffer.xOffset + x, buffer.yOffset + y, buffer.zOffset + z);

            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.UP.ordinal(), 1.1f, -0.05f);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.DOWN.ordinal(), 1.1f, -0.05f);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.NORTH.ordinal(), 1.1f, -0.05f);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.SOUTH.ordinal(), 1.1f, -0.05f);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.WEST.ordinal(), 1.1f, -0.05f);
            RenderGlowEffect.addSideFullTexture(buffer, EnumFacing.EAST.ordinal(), 1.1f, -0.05f);
            buffer.setTranslation(buffer.xOffset - x, buffer.yOffset - y, buffer.zOffset - z);
        }
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.color(.5f, .3f, 0);
        GlStateManager.glLineWidth(2);

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (BlockPos coordinate : coordinates) {
            mcjty.lib.gui.RenderHelper.renderHighLightedBlocksOutline(buffer,
                    base.getX() + coordinate.getX(), base.getY() + coordinate.getY(), base.getZ() + coordinate.getZ(),
                    .5f, .3f, 0f, 1.0f);
        }
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }


}
