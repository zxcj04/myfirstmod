package com.fanrende.myfirstmod;

import com.fanrende.myfirstmod.setup.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("myfirstmod")
public class MyFirstMod
{
	public static final String MODID = "myfirstmod";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static ModSetup setup = new ModSetup();

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public MyFirstMod()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

		Registration.init();

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(ModSetup::init);
		modBus.addListener(ModSetup::onAttributeCreate);
		modBus.addListener(ClientSetup::init);
	}
}
