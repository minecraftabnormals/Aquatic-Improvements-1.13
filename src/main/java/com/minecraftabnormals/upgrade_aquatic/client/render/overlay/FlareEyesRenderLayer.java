package com.minecraftabnormals.upgrade_aquatic.client.render.overlay;

import com.minecraftabnormals.abnormals_core.client.ACRenderTypes;
import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.upgrade_aquatic.client.model.FlareModel;
import com.minecraftabnormals.upgrade_aquatic.common.entities.FlareEntity;
import com.minecraftabnormals.upgrade_aquatic.core.UpgradeAquatic;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlareEyesRenderLayer<T extends FlareEntity, M extends FlareModel<T>> extends LayerRenderer<T, M> {	
	private static final ResourceLocation EYES_LAYER = new ResourceLocation(UpgradeAquatic.MOD_ID, "textures/entity/flare/flare_eyes.png");
	
	public FlareEyesRenderLayer(IEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T flare, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ClientInfo.MINECRAFT.getTextureManager().bindTexture(EYES_LAYER);

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(EYES_LAYER));
		
		this.getEntityModel().setRotationAngles(flare, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}