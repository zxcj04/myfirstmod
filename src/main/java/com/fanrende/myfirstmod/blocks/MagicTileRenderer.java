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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

public class MagicTileRenderer extends TileEntityRenderer<MagicBlockTile>
{
	public static final ResourceLocation MAGICBLOCK_TEXTURE = new ResourceLocation(MyFirstMod.MODID,
			"block/magicblock"
	);

	public MagicTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	private void addVertex(IVertexBuilder builder, MatrixStack matrixStack, float x, float y, float z, float u, float v)
	{
		builder.pos(matrixStack.getLast().getMatrix(), x, y, z)
				.color(1.0f, 1.0f, 1.0f, 1.0f)
				.tex(u, v)
				.lightmap(0, 240)
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
		TextureAtlasSprite sprite = Minecraft.getInstance()
				.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
				.apply(MAGICBLOCK_TEXTURE);

		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());

		long time = System.currentTimeMillis();

		float size = transferFunction(time, 2000, 0.00025f);

		float angle = ( time / 20 ) % 360;
		Quaternion rotation = Vector3f.YP.rotationDegrees(angle);

		float itemMove = transferFunction(time, 2000, 0.00005f);

		matrixStack.push();

		matrixStack.translate(.5, .5, .5);
		matrixStack.scale(size + 0.5f, size + 0.5f, size + 0.5f);
		matrixStack.translate(-.5, -.5, -.5);

		addVertex(builder, matrixStack, 0.0f, 0.75f, 0.0f, sprite.getMinU(), sprite.getMinV());
		addVertex(builder, matrixStack, 1.0f, 0.75f, 0.0f, sprite.getMaxU(), sprite.getMinV());
		addVertex(builder, matrixStack, 1.0f, 0.75f, 1.0f, sprite.getMaxU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 0.0f, 0.75f, 1.0f, sprite.getMinU(), sprite.getMaxV());

		addVertex(builder, matrixStack, 0.0f, 0.75f, 1.0f, sprite.getMinU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 1.0f, 0.75f, 1.0f, sprite.getMaxU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 1.0f, 0.75f, 0.0f, sprite.getMaxU(), sprite.getMinV());
		addVertex(builder, matrixStack, 0.0f, 0.75f, 0.0f, sprite.getMinU(), sprite.getMinV());

		matrixStack.pop();

		matrixStack.push();

		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
		{
			ItemStack stackInSlot = h.getStackInSlot(0);

			if (stackInSlot.getCount() > 0)
			{
				matrixStack.translate(.5, .5, .5);
				matrixStack.rotate(rotation);
				matrixStack.translate(-.5, -.5, -.5);

				matrixStack.translate(0.5, 1.5 + itemMove, 0.5);
				ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
				IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(
						stackInSlot,
						tileEntity.getWorld(),
						null
				);
				itemRenderer.renderItem(stackInSlot,
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

		matrixStack.pop();
	}

	public static void register()
	{
		ClientRegistry.bindTileEntityRenderer(Registration.MAGICBLOCK_TILE.get(), MagicTileRenderer::new);
	}
}
