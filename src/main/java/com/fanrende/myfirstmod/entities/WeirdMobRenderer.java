package com.fanrende.myfirstmod.entities;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class WeirdMobRenderer extends MobRenderer<WeirdMobEntity, WeirdMobModel>
{

	private static final ResourceLocation TEXTURE = new ResourceLocation(MyFirstMod.MODID,
			"textures/entity/weirdmob.png"
	);

	public WeirdMobRenderer(EntityRendererProvider.Context context)
	{
		super(context, new WeirdMobModel(context.getModelSet().bakeLayer(WeirdMobModel.CUBE_LAYER)), 0.5f);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(WeirdMobEntity entity)
	{
		return TEXTURE;
	}
}
