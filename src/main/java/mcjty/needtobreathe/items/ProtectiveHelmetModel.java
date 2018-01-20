package mcjty.needtobreathe.items;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ProtectiveHelmetModel extends ModelBiped {

    public static ProtectiveHelmetModel modelHelm;

    // Helmet
    public ModelRenderer helmet_p;
    public ModelRenderer controller_p;

    public ProtectiveHelmetModel() {

    	this.textureWidth = 64;
        this.textureHeight = 32;
        float s = 0.01F;
        
        this.helmet_p = new ModelRenderer(this, 0, 0);
        this.helmet_p.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet_p.addBox(-4.5F, -9.0F, -4.5F, 9, 10, 9, s);
        this.controller_p = new ModelRenderer(this, 36, 12);
        this.controller_p.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.controller_p.addBox(-1.5F, -8.0F, 4.5F, 3, 5, 2, s);

        this.helmet_p.addChild(this.controller_p);
    }

    public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack) {

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor)) {
            return null;
        }
        EntityEquipmentSlot slot = ((ItemArmor) stack.getItem()).armorType;

        ProtectiveHelmetModel armor;
        if (slot == EntityEquipmentSlot.HEAD && modelHelm != null) {
            return modelHelm;
        }

        armor = new ProtectiveHelmetModel();
        armor.bipedBody.isHidden = true;
        armor.bipedLeftArm.isHidden = true;
        armor.bipedRightArm.isHidden = true;

        armor.bipedHead.isHidden = true;

        armor.bipedLeftLeg.isHidden = true;
        armor.bipedRightLeg.isHidden = true;

        switch (slot) {
            case HEAD:
                armor.bipedHead.isHidden = false;
                modelHelm = armor;
                break;
        }
        return armor;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.isSneak = entity.isSneaking();
        this.isRiding = entity.isRiding();
//        if (entity instanceof EntityLivingBase) {
//            this.isChild = ((EntityLivingBase) entity).isChild();
//            this.rightArmPose = (((EntityLivingBase) entity).getHeldItem(EnumHand.MAIN_HAND) != null ? ArmPose.BOW_AND_ARROW : ArmPose.EMPTY);
//            // TODO possibly check if it can be completely removed? 1.9 thing
//        }

        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        if (this.isChild) {
            float f6 = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            this.bipedHead.render(scale);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            this.bipedHead.render(scale);
            GlStateManager.disableBlend();
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
        }
    }

}
