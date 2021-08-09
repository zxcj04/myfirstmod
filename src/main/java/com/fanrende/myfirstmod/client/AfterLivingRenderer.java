package com.fanrende.myfirstmod.client;

import com.fanrende.myfirstmod.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.client.event.RenderLivingEvent;

public class AfterLivingRenderer
{
	public static void render(RenderLivingEvent.Post event)
	{
		LocalPlayer player = Minecraft.getInstance().player;

		if (player.getMainHandItem().getItem() == Registration.FIRSTITEM.get())
		{
			showMobs(event.getMatrixStack(), event.getBuffers(), event.getEntity());
		}
	}

	private static void drawLine(
			VertexConsumer builder, Matrix4f positionMatrix, Vector3f line, Vector3f color, float alpha
	)
	{
		builder.vertex(positionMatrix, 0.0f, 0.0f, 0.0f).color(color.x(), color.y(), color.z(), alpha).endVertex();
		builder.vertex(positionMatrix, line.x(), line.y(), line.z())
				.color(color.x(), color.y(), color.z(), alpha)
				.endVertex();
	}

	private static void showMobs(PoseStack matrixStack, MultiBufferSource buffer, LivingEntity entity)
	{
		VertexConsumer builder = buffer.getBuffer(MyRenderType.OVERLAY_LINES);

		Matrix4f positionMatrix = matrixStack.last().pose();

		Vector3f line = new Vector3f(0.0f, 6.0f, 0.0f);

		float alpha = 1.0f;

		if (entity instanceof Enemy)
			drawLine(builder, positionMatrix, line, new Vector3f(1.0f, 0.0f, 0.0f), alpha);
		else
			drawLine(builder, positionMatrix, line, new Vector3f(0.0f, 1.0f, 0.0f), alpha);
	}
}
