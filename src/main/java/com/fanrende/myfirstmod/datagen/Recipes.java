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
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(FIRSTBLOCK.get())
				.pattern("x#x")
				.pattern("# #")
				.pattern("x#x")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', Tags.Items.DYES)
				.group("myfirstmod")
				.unlockedBy("cobblestone", InventoryChangeTrigger.Instance.hasItems(Blocks.COBBLESTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(FIRSTITEM.get())
				.pattern(" # ")
				.pattern("ox#")
				.pattern(" x ")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', FIRSTBLOCK.get())
				.define('o', Items.DIAMOND)
				.group("myfirstmod")
				.unlockedBy("firstblock", InventoryChangeTrigger.Instance.hasItems(FIRSTBLOCK.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(MAGICBLOCK.get())
				.pattern("xxx")
				.define('x', Tags.Items.DYES_PURPLE)
				.unlockedBy("purpledye", InventoryChangeTrigger.Instance.hasItems(Items.PURPLE_DYE))
				.group("myfirstmod")
				.save(consumer);

		ShapedRecipeBuilder.shaped(COMPLEX_MULTIPART_BLOCK.get())
				.pattern("x#x")
				.pattern("#d#")
				.pattern("x#x")
				.define('x', Tags.Items.OBSIDIAN)
				.define('#', Tags.Items.GEMS_EMERALD)
				.define('d', Items.DIAMOND)
				.group("myfirstmod")
				.unlockedBy("diamond", InventoryChangeTrigger.Instance.hasItems(Items.DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ENERGYPICKAXE.get())
				.pattern("ooo")
				.pattern(" x ")
				.pattern(" x ")
				.define('x', Items.BLAZE_ROD)
				.define('o', Tags.Items.OBSIDIAN)
				.group("myfirstmod")
				.unlockedBy("obsidian", InventoryChangeTrigger.Instance.hasItems(Blocks.OBSIDIAN))
				.save(consumer);

		ShapedRecipeBuilder.shaped(BAKEDBLOCK.get())
				.pattern("## ")
				.pattern("xx ")
				.pattern("xx ")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', Tags.Items.DYES_BLUE)
				.group("myfirstmod")
				.unlockedBy("cobblestone", InventoryChangeTrigger.Instance.hasItems(Blocks.COBBLESTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(INFINITY_PEARL.get())
				.pattern("ooo")
				.pattern("oxo")
				.pattern("ooo")
				.define('x', Items.EGG)
				.define('o', Items.ENDER_PEARL)
				.group("myfirstmod")
				.unlockedBy("enderpearl", InventoryChangeTrigger.Instance.hasItems(Items.ENDER_PEARL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(INFINITY_EYE.get())
				.pattern("ooo")
				.pattern("oxo")
				.pattern("ooo")
				.define('x', Items.BLAZE_ROD)
				.define('o', INFINITY_PEARL.get())
				.group("myfirstmod")
				.unlockedBy("infinitypearl", InventoryChangeTrigger.Instance.hasItems(INFINITY_PEARL.get()))
				.save(consumer);
	}
}