package com.fanrende.myfirstmod.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class BakedModelLoader implements IModelLoader<BakedModelGeometry>
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{

	}

	@Override
	public BakedModelGeometry read(
			JsonDeserializationContext deserializationContext, JsonObject modelContents
	)
	{
		return new BakedModelGeometry();
	}
}
