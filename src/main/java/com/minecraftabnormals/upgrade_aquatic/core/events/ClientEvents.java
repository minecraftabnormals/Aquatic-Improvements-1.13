package com.minecraftabnormals.upgrade_aquatic.core.events;

import com.minecraftabnormals.upgrade_aquatic.common.entities.thrasher.ThrasherEntity;
import com.minecraftabnormals.upgrade_aquatic.core.UpgradeAquatic;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid = UpgradeAquatic.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void onEntityRenderPre(RenderLivingEvent.Pre<?, ?> event) {
		if(event.getEntity() instanceof ClientPlayerEntity) {
			ClientPlayerEntity clientPlayer = (ClientPlayerEntity) event.getEntity();
			if(clientPlayer.getRidingEntity() instanceof ThrasherEntity) {
				ThrasherEntity thrasher = (ThrasherEntity) clientPlayer.getRidingEntity();
				ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, clientPlayer, 1.0F, "field_205017_bL");
				ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, clientPlayer, 1.0F, "field_205018_bM");
				clientPlayer.rotationPitch = 0.0F;
				clientPlayer.rotationYaw = thrasher.rotationYaw + (90.0F % 360);
				clientPlayer.renderYawOffset = thrasher.renderYawOffset + (90.0F % 360);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
		MatrixStack stack = event.getMatrixStack();
		stack.push();
		if(event.getEntityLiving().getRidingEntity() instanceof ThrasherEntity) {
			ThrasherEntity thrasher = (ThrasherEntity) event.getEntityLiving().getRidingEntity();
			double dx = Math.cos((MathHelper.lerp(event.getPartialRenderTick(), thrasher.prevRotationYaw, thrasher.rotationYaw)) * Math.PI / 180.0D);
			double dz = Math.sin((MathHelper.lerp(event.getPartialRenderTick(), thrasher.prevRotationYaw, thrasher.rotationYaw)) * Math.PI / 180.0D);
		
			stack.translate((float) dx, 0.0F, (float) dz);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
		event.getMatrixStack().pop();
	}
	
}