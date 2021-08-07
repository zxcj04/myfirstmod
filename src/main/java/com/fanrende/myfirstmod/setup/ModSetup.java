package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.commands.ModCommands;
import com.fanrende.myfirstmod.data.CapabilityEntityCharge;
import com.fanrende.myfirstmod.data.ChargeEventHandler;
import com.fanrende.myfirstmod.entities.WeirdMobEntity;
import com.fanrende.myfirstmod.network.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.fanrende.myfirstmod.setup.Registration.WEIRDMOB;

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

		CapabilityEntityCharge.register();

		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ChargeEventHandler::onAttachCapabilitiesEvent);
		MinecraftForge.EVENT_BUS.addListener(ChargeEventHandler::onDeathEvent);
		MinecraftForge.EVENT_BUS.addListener(ChargeEventHandler::onAttackEvent);

		event.enqueueWork(() ->
		{
			GlobalEntityTypeAttributes.put(WEIRDMOB.get(), WeirdMobEntity.prepareAttributes().create());
		});
	}

	@SubscribeEvent
	public static void serverLoad(RegisterCommandsEvent event)
	{
		ModCommands.register(event.getDispatcher());
	}
}
