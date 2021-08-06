package com.fanrende.myfirstmod.datagen;

import net.minecraft.data.DataGenerator;

import static com.fanrende.myfirstmod.setup.Registration.*;

public class LootTables extends BaseLootTableProvider
{

	public LootTables(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables()
	{
		lootTables.put(FIRSTBLOCK.get(), createTagsTable("firstblock", FIRSTBLOCK.get(), "inv", "energy"));
		lootTables.put(
				COMPLEX_MULTIPART_BLOCK.get(),
				createTagsTable("complexmultipartblock", COMPLEX_MULTIPART_BLOCK.get(), "mode")
		);
		lootTables.put(MAGICBLOCK.get(), createSelfDropTable("magicblock", MAGICBLOCK.get()));
		lootTables.put(BAKEDBLOCK.get(), createSelfDropTable("bakedblock", BAKEDBLOCK.get()));
	}
}