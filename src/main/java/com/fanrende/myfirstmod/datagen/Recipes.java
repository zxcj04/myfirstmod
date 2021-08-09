package com.fanrende.myfirstmod.datagen;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{

	public Recipes(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		super.buildCraftingRecipes(consumer);
		ShapedRecipeBuilder.shaped(Registration.FIRSTBLOCK.get())
				.pattern("x#x")
				.pattern("# #")
				.pattern("x#x")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', Tags.Items.DYES)
				.group("myfirstmod")
				.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.FIRSTITEM.get())
				.pattern(" # ")
				.pattern("ox#")
				.pattern(" x ")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', Registration.FIRSTBLOCK.get())
				.define('o', Items.DIAMOND)
				.group("myfirstmod")
				.unlockedBy("firstblock",
						InventoryChangeTrigger.TriggerInstance.hasItems(Registration.FIRSTBLOCK.get())
				)
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.MAGICBLOCK.get())
				.pattern("xxx")
				.define('x', Tags.Items.DYES_PURPLE)
				.unlockedBy("purpledye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PURPLE_DYE))
				.group("myfirstmod")
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.COMPLEX_MULTIPART_BLOCK.get())
				.pattern("x#x")
				.pattern("#d#")
				.pattern("x#x")
				.define('x', Tags.Items.OBSIDIAN)
				.define('#', Tags.Items.GEMS_EMERALD)
				.define('d', Items.DIAMOND)
				.group("myfirstmod")
				.unlockedBy("diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.ENERGYPICKAXE.get())
				.pattern("ooo")
				.pattern(" x ")
				.pattern(" x ")
				.define('x', Items.BLAZE_ROD)
				.define('o', Tags.Items.OBSIDIAN)
				.group("myfirstmod")
				.unlockedBy("obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.OBSIDIAN))
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.BAKEDBLOCK.get())
				.pattern("## ")
				.pattern("xx ")
				.pattern("xx ")
				.define('x', Tags.Items.COBBLESTONE)
				.define('#', Tags.Items.DYES_BLUE)
				.group("myfirstmod")
				.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.INFINITY_PEARL.get())
				.pattern("ooo")
				.pattern("oxo")
				.pattern("ooo")
				.define('x', Items.EGG)
				.define('o', Items.ENDER_PEARL)
				.group("myfirstmod")
				.unlockedBy("enderpearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(Registration.INFINITY_EYE.get())
				.pattern("ooo")
				.pattern("oxo")
				.pattern("ooo")
				.define('x', Items.BLAZE_ROD)
				.define('o', Registration.INFINITY_PEARL.get())
				.group("myfirstmod")
				.unlockedBy("infinitypearl",
						InventoryChangeTrigger.TriggerInstance.hasItems(Registration.INFINITY_PEARL.get())
				)
				.save(consumer);
	}
}