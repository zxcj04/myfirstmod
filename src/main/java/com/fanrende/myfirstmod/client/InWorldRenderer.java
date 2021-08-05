package com.fanrende.myfirstmod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static com.fanrende.myfirstmod.setup.Registration.ENERGYPICKAXE;

public class InWorldRenderer
{
	public static void render(RenderWorldLastEvent event)
	{
		ClientPlayerEntity player = Minecraft.getInstance().player;

		if (player.getHeldItemMainhand().getItem() == ENERGYPICKAXE.get() && Screen.hasShiftDown())
		{
			showTileEntities(player, event.getMatrixStack());
		}
	}

	private static void drawLine(
			IVertexBuilder builder, Matrix4f positionMatrix, BlockPos pos, int dx1, int dy1, int dz1, int dx2, int dy2,
			int dz2, Vector4f color
	)
	{
		BlockPos start = pos.add(dx1, dy1, dz1);
		BlockPos end = pos.add(dx2, dy2, dz2);

		builder.pos(positionMatrix, start.getX(), start.getY(), start.getZ())
				.color(color.getX(), color.getY(), color.getZ(), color.getW())
				.endVertex();
		builder.pos(positionMatrix, end.getX(), end.getY(), end.getZ())
				.color(color.getX(), color.getY(), color.getZ(), color.getW())
				.endVertex();
	}

	private static void showTileEntities(ClientPlayerEntity player, MatrixStack matrixStack)
	{
		IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder builder = buffer.getBuffer(MyRenderType.OVERLAY_LINES);

		BlockPos playerPos = player.getPosition();
		World world = player.getEntityWorld();

		matrixStack.push();

		Vec3d projectrdView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
		matrixStack.translate(
				playerPos.getX() - projectrdView.x,
				playerPos.getY() - projectrdView.y,
				playerPos.getZ() - projectrdView.z
		);

		Matrix4f positionMatrix = matrixStack.getLast().getMatrix();

		BlockPos.Mutable pos = new BlockPos.Mutable();

		Vector4f colorBlue = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);

		for (int x = -10; x <= 10; x++)
			for (int y = -10; y <= 10; y++)
				for (int z = -10; z <= 10; z++)
				{
					pos.setPos(x, y, z);

					if (world.getTileEntity(playerPos.add(pos)) != null)
					{
						drawLine(builder, positionMatrix, pos, 0, 0, 0, 1, 0, 0, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 0, 0, 1, 1, 0, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 1, 0, 0, 1, 0, colorBlue);
						drawLine(builder, positionMatrix, pos, 0, 1, 0, 0, 0, 0, colorBlue);

						drawLine(builder, positionMatrix, pos, 0, 0, 1, 1, 0, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 0, 1, 1, 1, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 1, 1, 0, 1, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 0, 1, 1, 0, 0, 1, colorBlue);

						drawLine(builder, positionMatrix, pos, 0, 0, 0, 0, 0, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 0, 0, 1, 0, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 1, 1, 0, 1, 1, 1, colorBlue);
						drawLine(builder, positionMatrix, pos, 0, 1, 0, 0, 1, 1, colorBlue);
					}
				}

		matrixStack.pop();

		RenderSystem.disableDepthTest();
		buffer.finish(MyRenderType.OVERLAY_LINES);
	}
}
