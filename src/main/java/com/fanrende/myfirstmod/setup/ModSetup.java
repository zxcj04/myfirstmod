package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.commands.ModCommands;
import com.fanrende.myfirstmod.network.Networking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = MyFirstMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup
{
	public static final ItemGroup ITEM_GROUP = new ItemGroup("myfirstmod")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Registration.FIRSTBLOCK.get());
		}
	};

	public static void init(final FMLCommonSetupEvent event)
	{
		Networking.registerMessages();
	}

	@SubscribeEvent
	public static void serverLoad(FMLServerStartingEvent event)
	{
		ModCommands.register(event.getCommandDispatcher());
	}

	@SubscribeEvent
	public static void onDimensionRegistry(RegisterDimensionsEvent event)
	{

	}
}
