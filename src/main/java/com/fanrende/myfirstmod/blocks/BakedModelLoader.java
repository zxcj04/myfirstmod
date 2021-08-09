package com.fanrende.myfirstmod.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class BakedModelLoader implements IModelLoader<BakedModelGeometry>
{
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
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
