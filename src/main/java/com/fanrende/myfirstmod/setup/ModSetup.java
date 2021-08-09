package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.commands.ModCommands;
import com.fanrende.myfirstmod.data.CapabilityEntityCharge;
import com.fanrende.myfirstmod.data.ChargeEventHandler;
import com.fanrende.myfirstmod.entities.WeirdMobEntity;
import com.fanrende.myfirstmod.network.Networking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MyFirstMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup
{
	public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("myfirstmod")
	{
		@Override
		public ItemStack makeIcon()
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
	}

	public static void onAttributeCreate(EntityAttributeCreationEvent event)
	{
		event.put(Registration.WEIRDMOB.get(), WeirdMobEntity.prepareAttributes().build());
	}

	@SubscribeEvent
	public static void serverLoad(RegisterCommandsEvent event)
	{
		ModCommands.register(event.getDispatcher());
	}
}
