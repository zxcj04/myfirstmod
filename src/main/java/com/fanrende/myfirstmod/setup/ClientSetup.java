package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.blocks.BakedBlockColor;
import com.fanrende.myfirstmod.blocks.BakedModelLoader;
import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.blocks.MagicTileRenderer;
import com.fanrende.myfirstmod.client.AfterLivingRenderer;
import com.fanrende.myfirstmod.client.InWorldRenderer;
import com.fanrende.myfirstmod.entities.WeirdMobModel;
import com.fanrende.myfirstmod.entities.WeirdMobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.fanrende.myfirstmod.blocks.MagicTileRenderer.MAGICBLOCK_TEXTURE;
import static com.fanrende.myfirstmod.blocks.MagicTileRenderer.MAGICBLOCK_TOP_TEXTURE;

@Mod.EventBusSubscriber(modid = MyFirstMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event)
	{
		MenuScreens.register(Registration.FIRSTBLOCK_CONTAINER.get(), FirstBlockScreen::new);

		MinecraftForge.EVENT_BUS.addListener(AfterLivingRenderer::render);
		MinecraftForge.EVENT_BUS.addListener(InWorldRenderer::render);

		event.enqueueWork(() ->
		{
			MagicTileRenderer.register();

			ItemBlockRenderTypes.setRenderLayer(Registration.COMPLEX_MULTIPART_BLOCK.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(Registration.BAKEDBLOCK.get(), (RenderType) -> true);

			Minecraft.getInstance().getBlockColors().register(new BakedBlockColor(), Registration.BAKEDBLOCK.get());
		});
	}

	@SubscribeEvent
	public static void onRegisterLayer(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(WeirdMobModel.CUBE_LAYER, WeirdMobModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(Registration.WEIRDMOB.get(), WeirdMobRenderer::new);
		event.registerEntityRenderer(Registration.INFINITYEYE_ENTITY.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(Registration.INFINITYPEARL_ENTITY.get(), ThrownItemRenderer::new);
	}

	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(new ResourceLocation(MyFirstMod.MODID, "bakedloader"),
				new BakedModelLoader()
		);
	}

	@SubscribeEvent
	public static void onItemColor(ColorHandlerEvent.Item event)
	{
		event.getItemColors().register((stack, i) -> 0xff0000, Registration.WEIRDMOB_EGG.get());
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		if (!event.getMap().location().equals(TextureAtlas.LOCATION_BLOCKS))
			return;

		event.addSprite(MAGICBLOCK_TEXTURE);
		event.addSprite(MAGICBLOCK_TOP_TEXTURE);
	}
}
