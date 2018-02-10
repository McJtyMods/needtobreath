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

public class ProtectiveHelmetModel2 extends ModelBiped {

    public static ProtectiveHelmetModel2 modelHelm2;

    // Helmet
    public ModelRenderer helmet_g;
    public ModelRenderer controller_g;
    public ModelRenderer respirator_g;
    public ModelRenderer connector_g;
    public ModelRenderer gas_g;
    public ModelRenderer cannister_g;

    public ProtectiveHelmetModel2(float scale) {
    	super(scale);

    	this.textureWidth = 64;
        this.textureHeight = 32;
        float s = 0.01F;
        
        this.bipedHead.cubeList.clear();

        this.helmet_g = new ModelRenderer(this, 0, 0);
        this.helmet_g.addBox(-4.5F, -9.0F, -4.5F, 9, 10, 9, s);
        this.helmet_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.respirator_g = new ModelRenderer(this, 46, 12);
        this.respirator_g.addBox(-1.5F, -4.0F, -5.5F, 3, 7, 4, s);
        this.respirator_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.controller_g = new ModelRenderer(this, 36, 12);
        this.controller_g.addBox(-1.5F, -8.0F, 4.5F, 3, 5, 2, s);
        this.controller_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.connector_g = new ModelRenderer(this, 48, 0);
        this.connector_g.addBox(-0.5F, -3.0F, 4.5F, 1, 1, 1, s);
        this.connector_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.cannister_g = new ModelRenderer(this, 36, 0);
        this.cannister_g.addBox(-1.5F, -2.0F, 4.5F, 3, 7, 3, s);
        this.cannister_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.gas_g = new ModelRenderer(this, 52, 0);
        this.gas_g.addBox(-1.0F, -1.5F, 5.0F, 2, 6, 2, s);
        this.gas_g.setRotationPoint(0.0F, 0.0F, 0.0F);
        
    
        this.bipedHead.addChild(this.helmet_g);
        this.bipedHead.addChild(this.gas_g);
        this.bipedHead.addChild(this.connector_g);
        this.bipedHead.addChild(this.cannister_g);
        this.bipedHead.addChild(this.controller_g);
        this.bipedHead.addChild(this.respirator_g);
        
        
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

        ProtectiveHelmetModel2 armor;
        if (slot == EntityEquipmentSlot.HEAD && modelHelm2 != null) {
            return modelHelm2;
        }

        armor = new ProtectiveHelmetModel2(0.0625f);
        armor.bipedBody.isHidden = true;
        armor.bipedLeftArm.isHidden = true;
        armor.bipedRightArm.isHidden = true;

        armor.bipedHead.isHidden = true;

        armor.bipedLeftLeg.isHidden = true;
        armor.bipedRightLeg.isHidden = true;

        switch (slot) {
            case HEAD:
                armor.bipedHead.isHidden = false;
                modelHelm2 = armor;
                break;
        }
        return modelHelm2;
    }
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		if(entity instanceof EntityLivingBase)	{
			this.isSneak = entity.isSneaking();
			this.isRiding = entity.isRiding();
			this.isChild = ((EntityLivingBase)entity).isChild();
			this.setLivingAnimations((EntityLivingBase)entity, limbSwing, limbSwingAmount, ageInTicks);
		}
		
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
