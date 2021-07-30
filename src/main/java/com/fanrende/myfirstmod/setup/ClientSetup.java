package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.blocks.BakedModelLoader;
import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.entities.WeirdMobRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.fanrende.myfirstmod.setup.Registration.*;

@Mod.EventBusSubscriber(modid = MyFirstMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(FIRSTBLOCK_CONTAINER.get(), FirstBlockScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(WEIRDMOB.get(), WeirdMobRenderer::new);
		ModelLoaderRegistry.registerLoader(new ResourceLocation(MyFirstMod.MODID, "bakedloader"), new BakedModelLoader());
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, i) -> 0xff0000, WEIRDMOB_EGG.get());
	}
}
