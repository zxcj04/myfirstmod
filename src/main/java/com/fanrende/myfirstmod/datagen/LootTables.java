package com.fanrende.myfirstmod.datagen;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider
{

	public LootTables(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables()
	{
		lootTables.put(
				Registration.FIRSTBLOCK.get(),
				createTagsTable("firstblock", Registration.FIRSTBLOCK.get(), "inv", "energy")
		);
		lootTables.put(
				Registration.COMPLEX_MULTIPART_BLOCK.get(),
				createTagsTable("complexmultipartblock", Registration.COMPLEX_MULTIPART_BLOCK.get(), "mode")
		);
		lootTables.put(Registration.MAGICBLOCK.get(), createSelfDropTable("magicblock", Registration.MAGICBLOCK.get()));
		lootTables.put(Registration.BAKEDBLOCK.get(), createSelfDropTable("bakedblock", Registration.BAKEDBLOCK.get()));
	}
}