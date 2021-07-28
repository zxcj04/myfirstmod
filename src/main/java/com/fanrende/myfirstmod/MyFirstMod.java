package com.fanrende.myfirstmod;

import com.fanrende.myfirstmod.blocks.FirstBlock;
import com.fanrende.myfirstmod.blocks.ModBlocks;
import com.fanrende.myfirstmod.items.FirstItem;
import com.fanrende.myfirstmod.setup.ClientProxy;
import com.fanrende.myfirstmod.setup.IProxy;
import com.fanrende.myfirstmod.setup.ModSetup;
import com.fanrende.myfirstmod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("myfirstmod")
public class MyFirstMod
{
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static ModSetup setup = new ModSetup();

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public MyFirstMod()
	{
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		setup.init();
		proxy.init();
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
		{
			event.getRegistry().register(new FirstBlock());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event)
		{
			Item.Properties properties = new Item.Properties()
					.group(setup.itemGroup);

			event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK, properties).setRegistryName("firstblock"));
			event.getRegistry().register(new FirstItem());
		}
	}
}
