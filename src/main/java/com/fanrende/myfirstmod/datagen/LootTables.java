package com.fanrende.myfirstmod.datagen;

import net.minecraft.data.DataGenerator;

import static com.fanrende.myfirstmod.setup.Registration.BAKEDBLOCK;
import static com.fanrende.myfirstmod.setup.Registration.FIRSTBLOCK;

public class LootTables extends BaseLootTableProvider
{

	public LootTables(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables()
	{
		lootTables.put(FIRSTBLOCK.get(), createStandardTable("firstblock", FIRSTBLOCK.get()));
		lootTables.put(BAKEDBLOCK.get(), createStandardTable("bakedblock", BAKEDBLOCK.get()));
	}
}