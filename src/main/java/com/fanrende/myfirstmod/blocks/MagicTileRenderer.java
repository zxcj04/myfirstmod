package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.setup.Registration;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class MagicTileRenderer extends TileEntityRenderer<MagicBlockTile>
{
	public static final ResourceLocation MAGICBLOCK_TEXTURE = new ResourceLocation(
			MyFirstMod.MODID,
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

		float size = 1.0f;

		matrixStack.push();

		matrixStack.translate(.5, .5, .5);
		matrixStack.scale(size, size, size);
		matrixStack.translate(-.5, -.5, -.5);

		addVertex(builder, matrixStack, 0.0f, 0.0f, 0.5f, sprite.getMinU(), sprite.getMinV());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 0.5f, sprite.getMaxU(), sprite.getMinV());
		addVertex(builder, matrixStack, 1.0f, 1.0f, 0.5f, sprite.getMaxU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 0.0f, 1.0f, 0.5f, sprite.getMinU(), sprite.getMaxV());

		addVertex(builder, matrixStack, 0.0f, 1.0f, 0.5f, sprite.getMinU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 1.0f, 1.0f, 0.5f, sprite.getMaxU(), sprite.getMaxV());
		addVertex(builder, matrixStack, 1.0f, 0.0f, 0.5f, sprite.getMaxU(), sprite.getMinV());
		addVertex(builder, matrixStack, 0.0f, 0.0f, 0.5f, sprite.getMinU(), sprite.getMinV());

		matrixStack.pop();

		matrixStack.push();

		matrixStack.translate(0.5, 1.5, 0.5);
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ItemStack stack = new ItemStack(Items.DIAMOND);
		IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntity.getWorld(), null);
		itemRenderer.renderItem(
				stack,
				ItemCameraTransforms.TransformType.FIXED,
				true,
				matrixStack,
				buffer,
				combinedLight,
				combinedOverlay,
				ibakedmodel
		);

		matrixStack.pop();
	}

	public static void register()
	{
		ClientRegistry.bindTileEntityRenderer(Registration.MAGICBLOCK_TILE.get(), MagicTileRenderer::new);
	}
}
