package com.minecraftabnormals.upgrade_aquatic.client.render.overlay;

import com.minecraftabnormals.upgrade_aquatic.client.model.PikeModel;
import com.minecraftabnormals.upgrade_aquatic.common.entities.pike.PikeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PikeCarriedItemRenderLayer extends LayerRenderer<PikeEntity, PikeModel<PikeEntity>> {
	
	public PikeCarriedItemRenderLayer(IEntityRenderer<PikeEntity, PikeModel<PikeEntity>> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, PikeEntity pike, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = pike.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		if(!itemstack.isEmpty()) {
			if (!itemstack.isEmpty()) {
				matrixStack.push();
				
				matrixStack.translate((this.getEntityModel()).nose.rotationPointX / 16.0F, (this.getEntityModel()).nose.rotationPointY / 16.0F + 1.3F, (this.getEntityModel()).nose.rotationPointZ / 16.0F - 0.5F);
				
				matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
				
				Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(pike, itemstack, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
				matrixStack.pop();
			}
		}
	}

}