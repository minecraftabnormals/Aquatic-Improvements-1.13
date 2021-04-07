package com.minecraftabnormals.upgrade_aquatic.client.render.jellyfish;

import com.minecraftabnormals.upgrade_aquatic.client.model.jellyfish.ImmortalJellyfishModel;
import com.minecraftabnormals.upgrade_aquatic.client.render.jellyfish.layer.JellyfishEmissiveLayer;
import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.ImmortalJellyfishEntity;
import com.minecraftabnormals.upgrade_aquatic.core.UpgradeAquatic;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class ImmortalJellyfishRenderer<I extends ImmortalJellyfishEntity> extends AbstractJellyfishRenderer<I> {

	public ImmortalJellyfishRenderer(EntityRendererManager renderManager) {
		super(renderManager, new ImmortalJellyfishModel<>(), 0.25F);
		this.addLayer(new JellyfishEmissiveLayer<>(this, this));
	}
	
	@Override
	public ResourceLocation getEntityTexture(I jellyfish) {
		return new ResourceLocation(UpgradeAquatic.MOD_ID, "textures/entity/jellyfish/immortal/" + jellyfish.getBucketName() + "_jellyfish.png");
	}
	
	@Override
	public ResourceLocation getOverlayTexture(I jellyfish) {
		return new ResourceLocation(UpgradeAquatic.MOD_ID, "textures/entity/jellyfish/immortal/" + jellyfish.getBucketName() + "_jellyfish_overlay.png");
	}
	
	@Override
	protected RenderType func_230496_a_(I jellyfish, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.getEntityTranslucent(this.getEntityTexture(jellyfish));
	}

}