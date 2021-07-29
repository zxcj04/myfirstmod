package com.fanrende.myfirstmod.datagen;

import com.fanrende.myfirstmod.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

	public LootTables(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables() {
		lootTables.put(ModBlocks.FIRSTBLOCK, createStandardTable("firstblock", ModBlocks.FIRSTBLOCK));
	}
}