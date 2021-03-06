package com.fanrende.myfirstmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderLivingEvent;

import static com.fanrende.myfirstmod.setup.Registration.FIRSTITEM;

public class AfterLivingRenderer
{
	public static void render(RenderLivingEvent.Post event)
	{
		ClientPlayerEntity player = Minecraft.getInstance().player;

		if (player.getMainHandItem().getItem() == FIRSTITEM.get())
		{
			showMobs(event.getMatrixStack(), event.getBuffers(), event.getEntity());
		}
	}

	private static void drawLine(
			IVertexBuilder builder, Matrix4f positionMatrix, Vector3f line, Vector3f color, float alpha
	)
	{
		builder.vertex(positionMatrix, 0.0f, 0.0f, 0.0f)
				.color(color.x(), color.y(), color.z(), alpha)
				.endVertex();
		builder.vertex(positionMatrix, line.x(), line.y(), line.z())
				.color(color.x(), color.y(), color.z(), alpha)
				.endVertex();
	}

	private static void showMobs(MatrixStack matrixStack, IRenderTypeBuffer buffer, LivingEntity entity)
	{
		IVertexBuilder builder = buffer.getBuffer(MyRenderType.OVERLAY_LINES);

		Matrix4f positionMatrix = matrixStack.last().pose();

		Vector3f line = new Vector3f(0.0f, 6.0f, 0.0f);

		float alpha = 1.0f;

		if (entity instanceof IMob)
			drawLine(builder, positionMatrix, line, new Vector3f(1.0f, 0.0f, 0.0f), alpha);
		else
			drawLine(builder, positionMatrix, line, new Vector3f(0.0f, 1.0f, 0.0f), alpha);
	}
}
