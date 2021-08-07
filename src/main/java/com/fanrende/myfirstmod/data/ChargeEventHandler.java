package com.fanrende.myfirstmod.data;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ChargeEventHandler
{
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof CreatureEntity)
		{
			EntityChargeProvider provider = new EntityChargeProvider();
			event.addCapability(new ResourceLocation(MyFirstMod.MODID, "charge"), provider);
			event.addListener(provider::invalidate);
		}
	}

	public static void onDeathEvent(LivingDeathEvent event)
	{
		Entity entity = event.getEntity();
		DamageSource damageSource = event.getSource();

		entity.getCapability(CapabilityEntityCharge.ENTITY_CHARGE_CAPABILITY).ifPresent(h ->
		{
			int charge = h.getCharge();
			if(charge > 0)
			{
				event.setCanceled(true);
				entity.setInvulnerable(true);
				((LivingEntity) entity).setHealth(1);

				charge--;

				h.setCharge(charge);

				if(damageSource.getTrueSource() instanceof PlayerEntity)
				{
					PlayerEntity player = (PlayerEntity) damageSource.getTrueSource();
					player.sendStatusMessage(new TranslationTextComponent("message.prevent_kill", Integer.toString(charge)), true);
				}
			}
		});
	}

	public static void onAttackEvent(AttackEntityEvent event)
	{
		Entity attacker = event.getEntity();
		if(attacker instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) attacker;
			ItemStack stack = player.getHeldItemMainhand();

			if(stack.getItem() == Items.BEETROOT)
			{
				Entity target = event.getTarget();

				target.getCapability(CapabilityEntityCharge.ENTITY_CHARGE_CAPABILITY).ifPresent(h ->
				{
					int charge = h.getCharge() + 1;
					h.setCharge(charge);

					player.sendStatusMessage(new TranslationTextComponent("message.increase_charge", Integer.toString(charge)), true);
					stack.shrink(1);
					player.setHeldItem(Hand.MAIN_HAND, stack);

					event.setCanceled(true);
					target.getEntityWorld().addParticle(ParticleTypes.HEART, target.getPosX(), target.getPosY()+1, target.getPosZ(), 0.0, 0.0, 0.0);
				});
			}
		}
	}
}
