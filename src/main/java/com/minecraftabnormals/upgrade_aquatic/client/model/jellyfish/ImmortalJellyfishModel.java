package com.minecraftabnormals.upgrade_aquatic.client.model.jellyfish;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.ImmortalJellyfishEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * ModelImmortalJellyfish - Undefined
 * Created using Tabula 7.0.0
 */
public class ImmortalJellyfishModel<E extends ImmortalJellyfishEntity> extends EndimatorEntityModel<E> {
	public EndimatorModelRenderer body;
    public EndimatorModelRenderer innerBody;
    public EndimatorModelRenderer tentacleEast;
    public EndimatorModelRenderer tentacleWest;
    public EndimatorModelRenderer bottomBody;
    public EndimatorModelRenderer tentacleSouth;
    public EndimatorModelRenderer tentacleNorth;
    public EndimatorModelRenderer tentacleSouthEast;
    public EndimatorModelRenderer tentacleSouthWest;
    public EndimatorModelRenderer tentacleNorthEast;
    public EndimatorModelRenderer tentacleNorthWest;

    public ImmortalJellyfishModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.tentacleNorthWest = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleNorthWest.setRotationPoint(3.4F, 3.8F, -3.4F);
        this.tentacleNorthWest.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleNorthWest, 0.4363323129985824F, 2.356194490192345F, 0.0F);
        this.tentacleWest = new EndimatorModelRenderer(this, 0, 0);
        this.tentacleWest.setRotationPoint(3.0F, 4.0F, 0.0F);
        this.tentacleWest.addBox(-8.0F, 0.0F, 0.0F, 8, 8, 0, 0.0F);
        this.setRotateAngle(tentacleWest, 0.0F, 3.141592653589793F, -0.4363323129985824F);
        this.innerBody = new EndimatorModelRenderer(this, 18, 0);
        this.innerBody.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.innerBody.addBox(-1.5F, -2.0F, -1.5F, 3, 5, 3, 0.0F);
        this.tentacleNorth = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleNorth.setRotationPoint(0.0F, 4.0F, -3.2F);
        this.tentacleNorth.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleNorth, 0.4363323129985824F, 3.141592653589793F, 0.0F);
        this.tentacleSouthWest = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleSouthWest.setRotationPoint(-3.4F, 3.8F, 3.4F);
        this.tentacleSouthWest.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleSouthWest, 0.4363323129985824F, -0.7853981633974483F, 0.0F);
        this.body = new EndimatorModelRenderer(this, 0, 28);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.addBox(-4.0F, -4.0F, -4.0F, 8, 7, 8, 0.0F);
        this.tentacleNorthEast = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleNorthEast.setRotationPoint(-3.4F, 3.8F, -3.4F);
        this.tentacleNorthEast.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleNorthEast, 0.4363323129985824F, -2.356194490192345F, 0.0F);
        this.tentacleSouth = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleSouth.setRotationPoint(0.0F, 4.0F, 3.2F);
        this.tentacleSouth.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleSouth, 0.4363323129985824F, 0.0F, 0.0F);
        this.bottomBody = new EndimatorModelRenderer(this, 0, 15);
        this.bottomBody.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.bottomBody.addBox(-5.0F, -1.0F, -5.0F, 10, 1, 10, 0.0F);
        this.tentacleSouthEast = new EndimatorModelRenderer(this, 0, -8);
        this.tentacleSouthEast.setRotationPoint(3.4F, 3.8F, 3.4F);
        this.tentacleSouthEast.addBox(0.0F, 0.0F, 0.0F, 0, 8, 8, 0.0F);
        this.setRotateAngle(tentacleSouthEast, 0.4363323129985824F, 0.7853981633974483F, 0.0F);
        this.tentacleEast = new EndimatorModelRenderer(this, 0, 0);
        this.tentacleEast.setRotationPoint(-3.0F, 4.0F, 0.0F);
        this.tentacleEast.addBox(-8.0F, 0.0F, 0.0F, 8, 8, 0, 0.0F);
        this.setRotateAngle(tentacleEast, 0.0F, 0.0F, 0.4363323129985824F);
        this.body.addChild(this.tentacleNorthWest);
        this.body.addChild(this.tentacleWest);
        this.body.addChild(this.tentacleNorth);
        this.body.addChild(this.tentacleSouthWest);
        this.body.addChild(this.tentacleNorthEast);
        this.body.addChild(this.tentacleSouth);
        this.body.addChild(this.bottomBody);
        this.body.addChild(this.tentacleSouthEast);
        this.body.addChild(this.tentacleEast);
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	this.animateModel(this.entity);
    	
    	this.innerBody.render(matrixStackIn, bufferIn, 240, packedOverlayIn, red, green, blue, alpha);
    	this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    
    @Override
    public void setRotationAngles(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	
    	float[] rotations = entity.getRotationController().getRotations(ClientInfo.getPartialTicks());
    	this.body.rotateAngleY = this.innerBody.rotateAngleY = (float) Math.toRadians(rotations[0]);
    	this.body.rotateAngleX = this.innerBody.rotateAngleX = (float) Math.toRadians(rotations[1]);
    	
    	if(entity.isInWater()) {
    		this.tentacleNorth.rotateAngleX += 0.1F * MathHelper.sin(0.2F * ageInTicks);
    		this.tentacleNorthEast.rotateAngleX -= 0.12F * MathHelper.sin(0.225F * ageInTicks);
    		this.tentacleNorthWest.rotateAngleX += 0.1F * MathHelper.sin(0.2F * ageInTicks);
    		
    		this.tentacleSouth.rotateAngleX -= 0.1F * MathHelper.sin(0.2F * ageInTicks);
    		this.tentacleSouthEast.rotateAngleX += 0.12F * MathHelper.sin(0.2F * ageInTicks);
    		this.tentacleSouthWest.rotateAngleX -= 0.1F * MathHelper.sin(0.225F * ageInTicks);
    		
    		this.tentacleEast.rotateAngleZ += 0.1F * MathHelper.sin(0.2F * ageInTicks);
    		this.tentacleWest.rotateAngleZ -= 0.1F * MathHelper.sin(0.225F * ageInTicks);
    	}
    }
    
    @Override
    public void animateModel(E jellyfish) {
    	super.animateModel(jellyfish);
    	
    	if(jellyfish.isEndimationPlaying(ImmortalJellyfishEntity.SWIM_ANIMATION)) {
    		this.setEndimationToPlay(ImmortalJellyfishEntity.SWIM_ANIMATION);
    		
    		this.startKeyframe(10);
    		this.rotate(this.tentacleNorth, 0.45F, 0.0F, 0.0F);
    		this.rotate(this.tentacleNorthEast, 0.45F, 0.0F, 0.0F);
    		this.rotate(this.tentacleNorthWest, 0.45F, 0.0F, 0.0F);
    		
    		this.rotate(this.tentacleEast, 0.0F, 0.0F, 0.45F);
    		this.rotate(this.tentacleWest, 0.0F, 0.0F, 0.45F);
    		
    		this.rotate(this.tentacleSouth, 0.45F, 0.0F, 0.0F);
    		this.rotate(this.tentacleSouthEast, 0.45F, 0.0F, 0.0F);
    		this.rotate(this.tentacleSouthWest, -0.45F, 0.0F, 0.0F);
    		
    		this.scale(this.body, 0.15F, -0.25F, 0.15F);
    		this.scale(this.innerBody, 0.15F, -0.25F, 0.15F);
    		this.move(this.innerBody, 0.0F, 0.25F, 0.0F);
    		this.offset(this.body, 0.0F, 0.25F / 6, 0.0F);
    		this.endKeyframe();
    		
    		this.resetKeyframe(10);
    	} else if(jellyfish.isEndimationPlaying(ImmortalJellyfishEntity.BOOST_ANIMATION)) {
    		this.setEndimationToPlay(ImmortalJellyfishEntity.BOOST_ANIMATION);
    		
    		this.startKeyframe(10);
    		this.rotate(this.tentacleNorth, 0.35F, 0.0F, 0.0F);
    		this.rotate(this.tentacleNorthEast, 0.35F, 0.0F, 0.0F);
    		this.rotate(this.tentacleNorthWest, 0.35F, 0.0F, 0.0F);
    		
    		this.rotate(this.tentacleEast, 0.0F, 0.0F, 0.35F);
    		this.rotate(this.tentacleWest, 0.0F, 0.0F, -0.35F);
    		
    		this.rotate(this.tentacleSouth, 0.35F, 0.0F, 0.0F);
    		this.rotate(this.tentacleSouthEast, 0.35F, 0.0F, 0.0F);
    		this.rotate(this.tentacleSouthWest, 0.35F, 0.0F, 0.0F);
    		
    		this.scale(this.body, 0.15F, -0.25F, 0.15F);
    		this.scale(this.innerBody, 0.15F, -0.25F, 0.15F);
    		this.move(this.innerBody, 0.0F, 0.25F, 0.0F);
    		this.offset(this.body, 0.0F, 0.25F / 6, 0.0F);
    		this.endKeyframe();
    		
    		this.resetKeyframe(10);
    	}
    	
    	this.body.setShouldScaleChildren(false);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(EndimatorModelRenderer EndimatorEndimatorModelRenderer, float x, float y, float z) {
        EndimatorEndimatorModelRenderer.rotateAngleX = x;
        EndimatorEndimatorModelRenderer.rotateAngleY = y;
        EndimatorEndimatorModelRenderer.rotateAngleZ = z;
    }
}
