package com.fanrende.myfirstmod.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.fanrende.myfirstmod.setup.Registration.*;

public class Recipes extends RecipeProvider
{

	public Recipes(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shapedRecipe(FIRSTBLOCK.get())
				.patternLine("x#x")
				.patternLine("# #")
				.patternLine("x#x")
				.key('x', Blocks.COBBLESTONE)
				.key('#', Tags.Items.DYES)
				.setGroup("myfirstmod")
				.addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(FIRSTITEM.get())
				.patternLine("o##")
				.patternLine(" x ")
				.patternLine(" x ")
				.key('x', Blocks.COBBLESTONE)
				.key('#', FIRSTBLOCK.get())
				.key('o', Items.DIAMOND)
				.setGroup("myfirstmod")
				.addCriterion("firstblock", InventoryChangeTrigger.Instance.forItems(FIRSTBLOCK.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(BAKEDBLOCK.get())
				.patternLine("## ")
				.patternLine("xx ")
				.patternLine("xx ")
				.key('x', Blocks.COBBLESTONE)
				.key('#', Tags.Items.DYES_BLUE)
				.setGroup("myfirstmod")
				.addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(INFINITY_PEARL.get())
				.patternLine("ooo")
				.patternLine("oxo")
				.patternLine("ooo")
				.key('x', Items.EGG)
				.key('o', Items.ENDER_PEARL)
				.setGroup("myfirstmod")
				.addCriterion("enderpearl", InventoryChangeTrigger.Instance.forItems(Items.ENDER_PEARL))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(INFINITY_EYE.get())
				.patternLine("ooo")
				.patternLine("oxo")
				.patternLine("ooo")
				.key('x', Items.BLAZE_ROD)
				.key('o', INFINITY_PEARL.get())
				.setGroup("myfirstmod")
				.addCriterion("infinitypearl", InventoryChangeTrigger.Instance.forItems(INFINITY_PEARL.get()))
				.build(consumer);
	}
}