package com.fanrende.myfirstmod.datagen;

import com.fanrende.myfirstmod.blocks.ModBlocks;
import com.fanrende.myfirstmod.items.ModItem;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.FIRSTBLOCK)
				.patternLine("x#x")
				.patternLine("# #")
				.patternLine("x#x")
				.key('x', Blocks.COBBLESTONE)
				.key('#', Tags.Items.DYES)
				.setGroup("myfirstmod")
				.addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItem.FIRSTITEM)
				.patternLine("o##")
				.patternLine(" x ")
				.patternLine(" x ")
				.key('x', Blocks.COBBLESTONE)
				.key('#', ModBlocks.FIRSTBLOCK)
				.key('o', Items.DIAMOND)
				.setGroup("myfirstmod")
				.addCriterion("firstblock", InventoryChangeTrigger.Instance.forItems(ModBlocks.FIRSTBLOCK))
				.build(consumer);
	}
}