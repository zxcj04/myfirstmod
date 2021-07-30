package com.fanrende.myfirstmod;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config
{
	public static final String CATEGORY_GENERAL = "general";
	public static final String CATEGORY_POWER = "power";
	public static final String SUBCATEGORY_FIRSTBLOCK = "firstblock";

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	public static ForgeConfigSpec.IntValue FIRSTBLOCK_MAXPOWER;
	public static ForgeConfigSpec.IntValue FIRSTBLOCK_GENERATE;
	public static ForgeConfigSpec.IntValue FIRSTBLOCK_SEND;
	public static ForgeConfigSpec.IntValue FIRSTBLOCK_TICKS;

	static
	{
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

		COMMON_BUILDER.comment("General Setting").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Power Setting").push(CATEGORY_POWER);

		setupFirstBlockConfig(COMMON_BUILDER, CLIENT_BUILDER);

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	private static void setupFirstBlockConfig(
			ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER
	)
	{
		COMMON_BUILDER.comment("First Block Setting").push(SUBCATEGORY_FIRSTBLOCK);

		FIRSTBLOCK_MAXPOWER = COMMON_BUILDER.comment("Maximum Power")
				.defineInRange("firstBlockMaxPower", 100000, 0, Integer.MAX_VALUE);
		FIRSTBLOCK_GENERATE = COMMON_BUILDER.comment("Power Generation per tick")
				.defineInRange("generate", 50, 0, Integer.MAX_VALUE);
		FIRSTBLOCK_SEND = COMMON_BUILDER.comment("Power Send Limit per tick")
				.defineInRange("send", 100, 0, Integer.MAX_VALUE);
		FIRSTBLOCK_TICKS = COMMON_BUILDER.comment("Ticks per fuel").defineInRange("ticks", 20, 0, Integer.MAX_VALUE);

		COMMON_BUILDER.pop();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent)
	{

	}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent)
	{

	}
}
