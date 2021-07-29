package com.fanrende.myfirstmod;

import com.fanrende.myfirstmod.blocks.FirstBlock;
import com.fanrende.myfirstmod.blocks.FirstBlockContainer;
import com.fanrende.myfirstmod.blocks.FirstBlockTile;
import com.fanrende.myfirstmod.blocks.ModBlocks;
import com.fanrende.myfirstmod.items.FirstItem;
import com.fanrende.myfirstmod.setup.ClientProxy;
import com.fanrende.myfirstmod.setup.IProxy;
import com.fanrende.myfirstmod.setup.ModSetup;
import com.fanrende.myfirstmod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("myfirstmod")
public class MyFirstMod
{
	public static String MODID = "myfirstmod";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static ModSetup setup = new ModSetup();

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public MyFirstMod()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("myfirstmod-client.toml"));
		Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("myfirstmod-common.toml"));
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
			Item.Properties properties = new Item.Properties().group(setup.itemGroup);

			event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK, properties).setRegistryName("firstblock"));
			event.getRegistry().register(new FirstItem());
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
		{
			event.getRegistry()
					.register(TileEntityType.Builder.create(FirstBlockTile::new, ModBlocks.FIRSTBLOCK)
							.build(null)
							.setRegistryName("firstblock"));
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event)
		{
			event.getRegistry().register(IForgeContainerType.create(( (windowId, inv, data) ->
			{
				BlockPos pos = data.readBlockPos();
				return new FirstBlockContainer(windowId, proxy.getClientWorld(), pos, inv);
			} )).setRegistryName("firstblock"));
		}
	}
}
