package com.minecraftabnormals.upgrade_aquatic.client.render.jellyfish.layer;

import com.minecraftabnormals.abnormals_core.client.ACRenderTypes;
import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.upgrade_aquatic.client.render.jellyfish.AbstractJellyfishRenderer;
import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.AbstractJellyfishEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;

public class JellyfishEmissiveLayer<T extends AbstractJellyfishEntity, M extends EndimatorEntityModel<T>> extends LayerRenderer<T, M> {
	private final AbstractJellyfishRenderer<T> jellyfishRenderer;
	
	public JellyfishEmissiveLayer(IEntityRenderer<T, M> renderer, AbstractJellyfishRenderer<T> jellyfishRenderer) {
		super(renderer);
		this.jellyfishRenderer = jellyfishRenderer;
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T jellyfish, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ClientInfo.MINECRAFT.getTextureManager().bindTexture(this.jellyfishRenderer.getOverlayTexture(jellyfish));
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEmissiveTransluscentEntityWithDiffusedLight(this.jellyfishRenderer.getOverlayTexture(jellyfish), true));
		
		this.getEntityModel().setRotationAngles(jellyfish, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, MathHelper.clamp((packedLightIn + MathUtil.getBrightLightForLight(packedLightIn)) - 20, 50, 240), LivingRenderer.getPackedOverlay(jellyfish, 0.0F), 1.0F, 1.0F, 1.0F, 0.7F);
	}
}