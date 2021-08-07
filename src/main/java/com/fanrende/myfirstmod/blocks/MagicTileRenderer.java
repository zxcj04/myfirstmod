package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.setup.Registration;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

public class MagicTileRenderer extends TileEntityRenderer<MagicBlockTile>
{
	public static final ResourceLocation MAGICBLOCK_TEXTURE = new ResourceLocation(MyFirstMod.MODID,
			"block/magicblock"
	);
	public static final ResourceLocation MAGICBLOCK_TOP_TEXTURE = new ResourceLocation(MyFirstMod.MODID,
			"block/magicblock_top"
	);

	public MagicTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	private void addVertex(IVertexBuilder builder, MatrixStack matrixStack, float x, float y, float z, float u, float v)
	{
		builder.vertex(matrixStack.last().pose(), x, y, z)
				.color(1.0f, 1.0f, 1.0f, 1.0f)
				.uv(u, v)
				.uv2(0, 240)
				.normal(1, 0, 0)
				.endVertex();
	}

	private float transferFunction(long time, long delta, float scale)
	{
		float result = time % ( delta * 2 );

		if (result > delta)
			result = delta * 2 - result;

		return result * scale;
	}

	@Override
	public void render(
			MagicBlockTile tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay
	)
	{
		TextureAtlasSprite spriteBase = Minecraft.getInstance()
				.getTextureAtlas(AtlasTexture.LOCATION_BLOCKS)
				.apply(MAGICBLOCK_TEXTURE);
		TextureAtlasSprite spriteTop = Minecraft.getInstance()
				.getTextureAtlas(AtlasTexture.LOCATION_BLOCKS)
				.apply(MAGICBLOCK_TOP_TEXTURE);

		IVertexBuilder builder = buffer.getBuffer(RenderType.translucent());

		long time = System.currentTimeMillis();

		float size = transferFunction(time, 2000, 0.00025f);

		float angleItem = ( time / 20 ) % 360;
		float angleBase = ( time / 120 ) % 360;
		float angleTop = ( time / 4 ) % 360;
		Quaternion rotationItem = Vector3f.YP.rotationDegrees(angleItem);
		Quaternion rotationBase = Vector3f.YP.rotationDegrees(angleBase);
		Quaternion rotationTop = Vector3f.YP.rotationDegrees(-angleTop);

		float topMove = transferFunction(time, 3500, 0.000025f);
		float itemMove = transferFunction(time, 2000, 0.00005f);

		renderBase(matrixStack, builder, rotationBase, spriteBase);
		renderTop(matrixStack, builder, rotationTop, spriteTop, topMove);
		renderItem(matrixStack, tileEntity, rotationItem, itemMove, buffer, combinedLight, combinedOverlay);
	}

	private void renderBase(
			MatrixStack matrixStack, IVertexBuilder builder, Quaternion rotationBase, TextureAtlasSprite spriteBase
	)
	{
		matrixStack.pushPose();

		matrixStack.translate(.5, .5, .5);
		//		matrixStack.scale(size + 0.5f, size + 0.5f, size + 0.5f);
		matrixStack.translate(-.5, -.5, -.5);

		addVertex(builder, matrixStack, 0.0f, 0.1f, 0.0f, spriteBase.getU0(), spriteBase.getV0());
		addVertex(builder, matrixStack, 1.0f, 0.1f, 0.0f, spriteBase.getU1(), spriteBase.getV0());
		addVertex(builder, matrixStack, 1.0f, 0.1f, 1.0f, spriteBase.getU1(), spriteBase.getV1());
		addVertex(builder, matrixStack, 0.0f, 0.1f, 1.0f, spriteBase.getU0(), spriteBase.getV1());

		addVertex(builder, matrixStack, 0.0f, 0.1f, 1.0f, spriteBase.getU0(), spriteBase.getV1());
		addVertex(builder, matrixStack, 1.0f, 0.1f, 1.0f, spriteBase.getU1(), spriteBase.getV1());
		addVertex(builder, matrixStack, 1.0f, 0.1f, 0.0f, spriteBase.getU1(), spriteBase.getV0());
		addVertex(builder, matrixStack, 0.0f, 0.1f, 0.0f, spriteBase.getU0(), spriteBase.getV0());

		matrixStack.popPose();
	}

	private void renderTop(
			MatrixStack matrixStack, IVertexBuilder builder, Quaternion rotationTop, TextureAtlasSprite spriteTop,
			float topMove
	)
	{
		matrixStack.pushPose();

		matrixStack.translate(0.0f, 0.15f + topMove, 0.0f);

		matrixStack.translate(.5, .5, .5);
		matrixStack.mulPose(rotationTop);
		matrixStack.scale(0.5f, 1.0f, 0.5f);
		matrixStack.translate(-.5, -.5, -.5);

		addVertex(builder, matrixStack, 0.0f, 0.0f, 0.0f, spriteTop.getU0(), spriteTop.getV0());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 0.0f, spriteTop.getU1(), spriteTop.getV0());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 1.0f, spriteTop.getU1(), spriteTop.getV1());
		addVertex(builder, matrixStack, 0.0f, 0.0f, 1.0f, spriteTop.getU0(), spriteTop.getV1());

		addVertex(builder, matrixStack, 0.0f, 0.0f, 1.0f, spriteTop.getU0(), spriteTop.getV1());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 1.0f, spriteTop.getU1(), spriteTop.getV1());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 0.0f, spriteTop.getU1(), spriteTop.getV0());
		addVertex(builder, matrixStack, 0.0f, 0.0f, 0.0f, spriteTop.getU0(), spriteTop.getV0());

		matrixStack.popPose();
	}

	private void renderItem(
			MatrixStack matrixStack, TileEntity tileEntity, Quaternion rotationItem, float itemMove,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay
	)
	{
		matrixStack.pushPose();

		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
		{
			ItemStack stackInSlot = h.getStackInSlot(0);

			if (stackInSlot.getCount() > 0)
			{
				matrixStack.translate(.5, .5, .5);
				matrixStack.mulPose(rotationItem);
				matrixStack.translate(-.5, -.5, -.5);

				matrixStack.translate(0.5, 0.75 + itemMove, 0.5);
				ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
				IBakedModel ibakedmodel = itemRenderer.getModel(stackInSlot,
						tileEntity.getLevel(),
						null
				);
				itemRenderer.render(stackInSlot,
						ItemCameraTransforms.TransformType.FIXED,
						true,
						matrixStack,
						buffer,
						combinedLight,
						combinedOverlay,
						ibakedmodel
				);
			}
		});

		matrixStack.popPose();
	}

	public static void register()
	{
		ClientRegistry.bindTileEntityRenderer(Registration.MAGICBLOCK_TILE.get(), MagicTileRenderer::new);
	}
}
