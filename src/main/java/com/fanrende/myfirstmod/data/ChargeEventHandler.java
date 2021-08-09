package com.fanrende.myfirstmod.data;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ChargeEventHandler
{
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof PathfinderMob)
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
			if (charge > 0)
			{
				event.setCanceled(true);
				entity.setInvulnerable(true);
				( (LivingEntity) entity ).setHealth(1);

				charge--;

				h.setCharge(charge);

				if (damageSource.getEntity() instanceof Player)
				{
					Player player = (Player) damageSource.getEntity();
					player.displayClientMessage(new TranslatableComponent("message.prevent_kill",
							Integer.toString(charge)
					), true);
				}
			}
		});
	}

	public static void onAttackEvent(AttackEntityEvent event)
	{
		Entity attacker = event.getEntity();
		if (attacker instanceof Player)
		{
			Player player = (Player) attacker;
			ItemStack stack = player.getMainHandItem();

			if (stack.getItem() == Items.BEETROOT)
			{
				Entity target = event.getTarget();

				target.getCapability(CapabilityEntityCharge.ENTITY_CHARGE_CAPABILITY).ifPresent(h ->
				{
					int charge = h.getCharge() + 1;
					h.setCharge(charge);

					player.displayClientMessage(new TranslatableComponent("message.increase_charge",
							Integer.toString(charge)
					), true);
					stack.shrink(1);
					player.setItemInHand(InteractionHand.MAIN_HAND, stack);

					event.setCanceled(true);
					target.getCommandSenderWorld()
							.addParticle(ParticleTypes.HEART,
									target.getX(),
									target.getY() + 1,
									target.getZ(),
									0.0,
									0.0,
									0.0
							);
				});
			}
		}
	}
}
