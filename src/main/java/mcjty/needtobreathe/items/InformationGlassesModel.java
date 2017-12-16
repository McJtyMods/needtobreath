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

public class InformationGlassesModel extends ModelBiped {

    public static InformationGlassesModel modelHelm;

    // Helmet
    ModelRenderer helmettop;
    ModelRenderer helmetside1;
    ModelRenderer helmetside2;
    ModelRenderer helmetbottom;
    ModelRenderer helmetfront1;
    ModelRenderer helmetfront2;
    ModelRenderer helmetfront3;
    ModelRenderer helmetfront4;
    ModelRenderer helmetback;
    ModelRenderer helmetvisor;

    public InformationGlassesModel() {
        textureWidth = 64;
        textureHeight = 32;

        setupHelmet();

        bipedHead.addChild(helmetback);
        bipedHead.addChild(helmetbottom);
        bipedHead.addChild(helmetfront1);
        bipedHead.addChild(helmetfront2);
        bipedHead.addChild(helmetfront3);
        bipedHead.addChild(helmetfront4);
        bipedHead.addChild(helmetside1);
        bipedHead.addChild(helmetside2);
        bipedHead.addChild(helmettop);
        bipedHead.addChild(helmetvisor);
    }

    private void setupHelmet() {
        helmettop = new ModelRenderer(this, 18, 0);
        helmettop.addBox(0F, 0F, 0F, 8, 1, 8);
        helmettop.setRotationPoint(-4F, -9F, -4F);
        helmettop.setTextureSize(64, 32);
        helmettop.mirror = true;
        setRotation(helmettop, 0F, 0F, 0F);
        helmetside1 = new ModelRenderer(this, 0, 9);
        helmetside1.addBox(0F, 0F, 0F, 1, 8, 8);
        helmetside1.setRotationPoint(4F, -8F, -4F);
        helmetside1.setTextureSize(64, 32);
        helmetside1.mirror = true;
        setRotation(helmetside1, 0F, 0F, 0F);
        helmetside2 = new ModelRenderer(this, 0, 9);
        helmetside2.addBox(0F, 0F, 0F, 1, 8, 8);
        helmetside2.setRotationPoint(-5F, -8F, -4F);
        helmetside2.setTextureSize(64, 32);
        helmetside2.mirror = true;
        setRotation(helmetside2, 0F, 0F, 0F);
        helmetbottom = new ModelRenderer(this, 18, 17);
        helmetbottom.addBox(0F, 0F, 0F, 8, 0, 8);
        helmetbottom.setRotationPoint(-4F, 0F, -4F);
        helmetbottom.setTextureSize(64, 32);
        helmetbottom.mirror = true;
        setRotation(helmetbottom, 0F, 0F, 0F);
        helmetfront1 = new ModelRenderer(this, 18, 9);
        helmetfront1.addBox(0F, 0F, 0F, 8, 2, 1);
        helmetfront1.setRotationPoint(-4F, -8F, -5F);
        helmetfront1.setTextureSize(64, 32);
        helmetfront1.mirror = true;
        setRotation(helmetfront1, 0F, 0F, 0F);
        helmetfront2 = new ModelRenderer(this, 18, 12);
        helmetfront2.addBox(0F, 0F, 0F, 3, 4, 1);
        helmetfront2.setRotationPoint(2F, -6F, -5F);
        helmetfront2.setTextureSize(64, 32);
        helmetfront2.mirror = true;
        setRotation(helmetfront2, 0F, 0F, 0F);
        helmetfront3 = new ModelRenderer(this, 18, 12);
        helmetfront3.addBox(0F, 0F, 0F, 3, 4, 1);
        helmetfront3.setRotationPoint(-5F, -6F, -5F);
        helmetfront3.setTextureSize(64, 32);
        helmetfront3.mirror = true;
        setRotation(helmetfront3, 0F, 0F, 0F);
        helmetfront4 = new ModelRenderer(this, 18, 9);
        helmetfront4.addBox(0F, 0F, 0F, 8, 2, 1);
        helmetfront4.setRotationPoint(-4F, -2F, -5F);
        helmetfront4.setTextureSize(64, 32);
        helmetfront4.mirror = true;
        setRotation(helmetfront4, 0F, 0F, 0F);
        helmetback = new ModelRenderer(this, 0, 0);
        helmetback.addBox(0F, 0F, 0F, 8, 8, 1);
        helmetback.setRotationPoint(-4F, -8F, 4F);
        helmetback.setTextureSize(64, 32);
        helmetback.mirror = true;
        setRotation(helmetback, 0F, 0F, 0F);
        helmetvisor = new ModelRenderer(this, 26, 12);
        helmetvisor.addBox(0F, 0F, 0F, 4, 4, 0);
        helmetvisor.setRotationPoint(-2F, -6F, -4F);
        helmetvisor.setTextureSize(64, 32);
        helmetvisor.mirror = true;
        setRotation(helmetvisor, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack) {

        if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor)) {
            return null;
        }
        EntityEquipmentSlot slot = ((ItemArmor) stack.getItem()).armorType;

        InformationGlassesModel armor;
        if (slot == EntityEquipmentSlot.HEAD && modelHelm != null) {
            return modelHelm;
        }

        armor = new InformationGlassesModel();
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
