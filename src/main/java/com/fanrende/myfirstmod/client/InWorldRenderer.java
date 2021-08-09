package com.fanrende.myfirstmod.client;

import com.fanrende.myfirstmod.setup.Registration;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.Tags;

public class InWorldRenderer
{
	public static void render(RenderWorldLastEvent event)
	{
		LocalPlayer player = Minecraft.getInstance().player;

		if (player.getMainHandItem().getItem() == Registration.ENERGYPICKAXE.get())
		{
			if (Screen.hasShiftDown())
				showTileEntities(player, event.getMatrixStack());
			else
				showOres(player, event.getMatrixStack());
		}
	}

	private static void drawLine(
			VertexConsumer builder, Matrix4f positionMatrix, BlockPos pos, int dx1, int dy1, int dz1, int dx2, int dy2,
			int dz2, Vector4f color
	)
	{
		BlockPos start = pos.offset(dx1, dy1, dz1);
		BlockPos end = pos.offset(dx2, dy2, dz2);

		builder.vertex(positionMatrix, start.getX(), start.getY(), start.getZ())
				.color(color.x(), color.y(), color.z(), color.w())
				.endVertex();
		builder.vertex(positionMatrix, end.getX(), end.getY(), end.getZ())
				.color(color.x(), color.y(), color.z(), color.w())
				.endVertex();
	}

	private static void drawCube(VertexConsumer builder, Matrix4f positionMatrix, BlockPos pos, Vector4f color)
	{
		drawLine(builder, positionMatrix, pos, 0, 0, 0, 1, 0, 0, color);
		drawLine(builder, positionMatrix, pos, 1, 0, 0, 1, 1, 0, color);
		drawLine(builder, positionMatrix, pos, 1, 1, 0, 0, 1, 0, color);
		drawLine(builder, positionMatrix, pos, 0, 1, 0, 0, 0, 0, color);

		drawLine(builder, positionMatrix, pos, 0, 0, 1, 1, 0, 1, color);
		drawLine(builder, positionMatrix, pos, 1, 0, 1, 1, 1, 1, color);
		drawLine(builder, positionMatrix, pos, 1, 1, 1, 0, 1, 1, color);
		drawLine(builder, positionMatrix, pos, 0, 1, 1, 0, 0, 1, color);

		drawLine(builder, positionMatrix, pos, 0, 0, 0, 0, 0, 1, color);
		drawLine(builder, positionMatrix, pos, 1, 0, 0, 1, 0, 1, color);
		drawLine(builder, positionMatrix, pos, 1, 1, 0, 1, 1, 1, color);
		drawLine(builder, positionMatrix, pos, 0, 1, 0, 0, 1, 1, color);
	}

	private static void showTileEntities(LocalPlayer player, PoseStack matrixStack)
	{
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(MyRenderType.OVERLAY_LINES);

		BlockPos playerPos = player.blockPosition();
		Level world = player.getCommandSenderWorld();

		matrixStack.pushPose();

		Vec3 projectrdView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		matrixStack.translate(
				playerPos.getX() - projectrdView.x,
				playerPos.getY() - projectrdView.y,
				playerPos.getZ() - projectrdView.z
		);

		Matrix4f positionMatrix = matrixStack.last().pose();

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		Vector4f colorBlue = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);

		for (int x = -10; x <= 10; x++)
			for (int y = -10; y <= 10; y++)
				for (int z = -10; z <= 10; z++)
				{
					pos.set(x, y, z);

					if (world.getBlockEntity(playerPos.offset(pos)) != null)
					{
						drawCube(builder, positionMatrix, pos, colorBlue);
					}
				}

		matrixStack.popPose();

		RenderSystem.disableDepthTest();
		buffer.endBatch(MyRenderType.OVERLAY_LINES);
	}

	private static void showOres(LocalPlayer player, PoseStack matrixStack)
	{
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(MyRenderType.OVERLAY_LINES);

		BlockPos playerPos = player.blockPosition();
		Level world = player.getCommandSenderWorld();

		matrixStack.pushPose();

		Vec3 projectrdView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		matrixStack.translate(
				playerPos.getX() - projectrdView.x,
				playerPos.getY() - projectrdView.y,
				playerPos.getZ() - projectrdView.z
		);

		Matrix4f positionMatrix = matrixStack.last().pose();

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		Vector4f colorBlack = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		Vector4f colorWhite = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		Vector4f colorRed = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		Vector4f colorGreen = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
		Vector4f colorBlue = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
		Vector4f colorGold = new Vector4f(1.0f, 0.75f, 0.13f, 1.0f);
		Vector4f colorCyan = new Vector4f(0.0f, 1.0f, 1.0f, 1.0f);
		Vector4f colorGray = new Vector4f(0.5f, 0.5f, 0.5f, 1.0f);

		for (int x = -10; x <= 10; x++)
			for (int y = -10; y <= 10; y++)
				for (int z = -10; z <= 10; z++)
				{
					pos.set(x, y, z);

					Block targetBlock = world.getBlockState(playerPos.offset(pos)).getBlock();

					if (Tags.Blocks.ORES_COAL.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorBlack);
					else if (Tags.Blocks.ORES_IRON.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorWhite);
					else if (Tags.Blocks.ORES_REDSTONE.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorRed);
					else if (Tags.Blocks.ORES_EMERALD.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorGreen);
					else if (Tags.Blocks.ORES_LAPIS.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorBlue);
					else if (Tags.Blocks.ORES_GOLD.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorGold);
					else if (Tags.Blocks.ORES_DIAMOND.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorCyan);
					else if (Tags.Blocks.ORES.contains(targetBlock))
						drawCube(builder, positionMatrix, pos, colorGray);
				}

		matrixStack.popPose();

		RenderSystem.disableDepthTest();
		buffer.endBatch(MyRenderType.OVERLAY_LINES);
	}
}
