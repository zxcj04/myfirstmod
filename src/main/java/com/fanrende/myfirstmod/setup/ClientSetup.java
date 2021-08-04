package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.blocks.BakedModelLoader;
import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.blocks.MagicTileRenderer;
import com.fanrende.myfirstmod.client.AfterLivingRenderer;
import com.fanrende.myfirstmod.client.InWorldRenderer;
import com.fanrende.myfirstmod.entities.WeirdMobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.fanrende.myfirstmod.blocks.MagicTileRenderer.MAGICBLOCK_TEXTURE;
import static com.fanrende.myfirstmod.setup.Registration.*;

@Mod.EventBusSubscriber(modid = MyFirstMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(FIRSTBLOCK_CONTAINER.get(), FirstBlockScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(WEIRDMOB.get(), WeirdMobRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(INFINITYEYE_ENTITY.get(),
				manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer())
		);
		RenderingRegistry.registerEntityRenderingHandler(INFINITYPEARL_ENTITY.get(),
				manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer())
		);
		ModelLoaderRegistry.registerLoader(new ResourceLocation(MyFirstMod.MODID, "bakedloader"),
				new BakedModelLoader()
		);
		MagicTileRenderer.register();
		MinecraftForge.EVENT_BUS.addListener(AfterLivingRenderer::render);
		MinecraftForge.EVENT_BUS.addListener(InWorldRenderer::render);
		RenderTypeLookup.setRenderLayer(COMPLEX_MULTIPART_BLOCK.get(), RenderType.getTranslucent());
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, i) -> 0xff0000, WEIRDMOB_EGG.get());
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
			return;

		event.addSprite(MAGICBLOCK_TEXTURE);
	}
}
