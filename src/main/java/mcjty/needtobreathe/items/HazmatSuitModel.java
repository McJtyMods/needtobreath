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

public class HazmatSuitModel extends ModelBiped {

    public static HazmatSuitModel modelHelm;

    // Hazmat Suit

    public ModelRenderer mask;
    public ModelRenderer respirator;
    public ModelRenderer resp_l;
    public ModelRenderer resp_r;
    
    public ModelRenderer body;
    public ModelRenderer arm_l;
    public ModelRenderer arm_r;
    public ModelRenderer glove_l;
    public ModelRenderer glove_r;
    
    public ModelRenderer belt;
    public ModelRenderer leg_l;
    public ModelRenderer leg_r;
    public ModelRenderer boot_l;
    public ModelRenderer boot_r;

    public HazmatSuitModel() {

    	this.textureWidth = 64;
        this.textureHeight = 128;
        float s = 0.01F;
        
        //helmet
        this.mask = new ModelRenderer(this, 0, 0);
        this.mask.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mask.addBox(-4.5F, -9.0F, -4.6F, 9, 13, 9, s);
        this.respirator = new ModelRenderer(this, 36, 0);
        this.respirator.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.respirator.addBox(-2.5F, -5.0F, -5.5F, 5, 4, 4, 0.0F);
        this.setRotation(respirator, 0.5235987755982988F, 0.0F, s);
        this.resp_l = new ModelRenderer(this, 36, 8);
        this.resp_l.mirror = true;
        this.resp_l.setRotationPoint(0.5F, -4.0F, -5.5F);
        this.resp_l.addBox(0.0F, 0.0F, -2.5F, 3, 3, 3, 0.0F);
        this.setRotation(resp_l, 0.0F, -0.7853981633974483F, s);
        this.resp_r = new ModelRenderer(this, 36, 8);
        this.resp_r.setRotationPoint(-0.5F, -4.0F, -5.5F);
        this.resp_r.addBox(-3.0F, 0.0F, -2.5F, 3, 3, 3, 0.0F);
        this.setRotation(resp_r, 0.0F, 0.7853981633974483F, s);
        
        //chestplate
        this.body = new ModelRenderer(this, 0, 22);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -3.0F, 9, 9, 6, s);
        this.arm_l = new ModelRenderer(this, 0, 37);
        this.arm_l.mirror = true;
        this.arm_l.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.arm_l.addBox(-1.5F, -2.0F, -2.9F, 5, 8, 6, s);
        this.glove_l = new ModelRenderer(this, 22, 37);
        this.glove_l.mirror = true;
        this.glove_l.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glove_l.addBox(-1.5F, 6.0F, -2.0F, 5, 4, 5, s);
        this.arm_r = new ModelRenderer(this, 0, 37);
        this.arm_r.setRotationPoint(-5.0F, 2.0F, -0.0F);
        this.arm_r.addBox(-3.5F, -2.0F, -3.0F, 5, 8, 6, s);
        this.setRotation(arm_r, 0.0F, 0.0F, 0.17453292519943295F);
        this.glove_r = new ModelRenderer(this, 22, 37);
        this.glove_r.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.glove_r.addBox(-3.5F, 6.0F, -2.5F, 5, 4, 5, s);

        //legs
        this.belt = new ModelRenderer(this, 0, 52);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.5F, 9.0F, -3.0F, 9, 5, 6, s);
        this.leg_l = new ModelRenderer(this, 0, 63);
        this.leg_l.mirror = true;
        this.leg_l.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.leg_l.addBox(-2.0F, -3.0F, -2.55F, 5, 9, 5, s);
        this.setRotation(leg_l, 0.0F, 0.0F, -0.08726646259971647F);
        this.leg_r = new ModelRenderer(this, 0, 63);
        this.leg_r.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.leg_r.addBox(-3.0F, -3.0F, -2.55F, 5, 9, 5, s);
        this.setRotation(leg_r, 0.0F, 0.0F, 0.08726646259971647F);
        
        //boots
        this.boot_l = new ModelRenderer(this, 0, 77);
        this.boot_l.mirror = true;
        this.boot_l.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.boot_l.addBox(-2.0F, 6.0F, -2.5F, 5, 6, 5, s);
        this.setRotation(boot_l, 0.0F, 0.0F, -0.08726646259971647F);
        this.boot_r = new ModelRenderer(this, 0, 77);
        this.boot_r.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.boot_r.addBox(-2.95F, 6.0F, -2.5F, 5, 6, 5, s);

        this.mask.addChild(this.respirator);
        this.respirator.addChild(this.resp_l);
        this.respirator.addChild(this.resp_r);
        
        this.arm_l.addChild(this.glove_l);
        this.arm_r.addChild(this.glove_r);
        
        this.belt.addChild(this.leg_l);
        this.belt.addChild(this.leg_r);
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

        HazmatSuitModel armor;
        if (slot == EntityEquipmentSlot.HEAD && modelHelm != null) {
            return modelHelm;
        }

        armor = new HazmatSuitModel();
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
