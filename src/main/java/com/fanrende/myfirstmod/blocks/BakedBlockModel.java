package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.MyFirstMod;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BakedBlockModel implements IDynamicBakedModel
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(MyFirstMod.MODID, "block/bakedblock");

	private TextureAtlasSprite getTexture()
	{
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(TEXTURE);
	}

	private void putVertex(
			BakedQuadBuilder builder, Vec3 normal, double x, double y, double z, float u, float v,
			TextureAtlasSprite sprite, float r, float g, float b
	)
	{
		ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();

		for (int i = 0; i < elements.size(); i++)
		{
			VertexFormatElement element = elements.get(i);

			switch (element.getUsage())
			{
				case POSITION:
					builder.put(i, (float) x, (float) y, (float) z, 1.0f);
					break;
				case COLOR:
					builder.put(i, r, g, b, 1.0f);
					break;
				case UV:
					switch (element.getIndex())
					{
						case 0:
							float iu = sprite.getU(u);
							float iv = sprite.getV(v);

							builder.put(i, iu, iv);
							break;
						case 2:
							builder.put(i, (short) 0, (short) 0);
							break;
						default:
							builder.put(i);
							break;
					}
					break;
				case NORMAL:
					builder.put(i, (float) normal.x, (float) normal.y, (float) normal.z);
					break;
				default:
					builder.put(i);
					break;
			}
		}
	}

	private BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite)
	{
		Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

		int tw = sprite.getWidth();
		int th = sprite.getHeight();

		BakedQuadBuilder builder = new BakedQuadBuilder(sprite);

		builder.setQuadOrientation(Direction.getNearest(normal.x, normal.y, normal.z));
		putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v2.x, v2.y, v2.z, 0, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v3.x, v3.y, v3.z, tw, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v4.x, v4.y, v4.z, tw, 0, sprite, 1.0f, 1.0f, 1.0f);

		return builder.build();
	}

	private Vec3 v(double x, double y, double z)
	{
		return new Vec3(x, y, z);
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(
			@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData
	)
	{
		RenderType layer = MinecraftForgeClient.getRenderLayer();
		BlockState mimic = extraData.getData(BakedBlockTile.MIMIC);

		if (mimic != null && !( mimic.getBlock() instanceof BakedBlock ))
		{
			if (layer == null || ItemBlockRenderTypes.canRenderInLayer(mimic, layer))
			{
				BakedModel model = Minecraft.getInstance()
						.getBlockRenderer()
						.getBlockModelShaper()
						.getBlockModel(mimic);
				try
				{
					return model.getQuads(mimic, side, rand, EmptyModelData.INSTANCE);
				}
				catch (Exception e)
				{
					return Collections.emptyList();
				}
			}

			return Collections.emptyList();
		}

		if (side != null || ( layer != null && !layer.equals(RenderType.solid()) ))
			return Collections.emptyList();

		TextureAtlasSprite texture = getTexture();
		List<BakedQuad> quads = new ArrayList<>();
		double l = .2;
		double r = 1 - .2;
		quads.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), texture));
		quads.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), texture));
		quads.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), texture));
		quads.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), texture));
		quads.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), texture));
		quads.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), texture));

		return quads;
	}

	@Override
	public boolean useAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean usesBlockLight()
	{
		return false;
	}

	@Override
	public boolean isCustomRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return getTexture();
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}

	@Override
	public ItemTransforms getTransforms()
	{
		return ItemTransforms.NO_TRANSFORMS;
	}
}
