package com.fanrende.myfirstmod.entities;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class InfinityPearlEntity extends ThrownEnderpearl
{
	public InfinityPearlEntity(EntityType<? extends ThrownEnderpearl> entityType, Level level)
	{
		super(entityType, level);
	}

	public InfinityPearlEntity(Level level, LivingEntity entity)
	{
		super(EntityType.ENDER_PEARL, level);
	}

	protected Item getDefaultItem()
	{
		return Registration.INFINITY_PEARL.get();
	}

	protected void onHitEntity(EntityHitResult result)
	{
		super.onHitEntity(result);
		result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
	}

	@Override
	protected void onHit(HitResult result)
	{
		super.onHit(result);

		for (int i = 0; i < 32; ++i)
		{
			this.level.addParticle(ParticleTypes.PORTAL,
					this.getX(),
					this.getY() + this.random.nextDouble() * 2.0D,
					this.getZ(),
					this.random.nextGaussian(),
					0.0D,
					this.random.nextGaussian()
			);
		}

		if (!this.level.isClientSide && !this.isRemoved())
		{
			Entity entity = this.getOwner();
			if (entity instanceof ServerPlayer)
			{
				ServerPlayer serverplayer = (ServerPlayer) entity;
				if (serverplayer.connection.getConnection()
						.isConnected() && serverplayer.level == this.level && !serverplayer.isSleeping())
				{
					net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl event = net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand(serverplayer,
							this.getX(),
							this.getY(),
							this.getZ(),
							this,
							5.0F
					);
					if (!event.isCanceled())
					{ // Don't indent to lower patch size
						if (this.random.nextFloat() < 0.05F && this.level.getGameRules()
								.getBoolean(GameRules.RULE_DOMOBSPAWNING))
						{
							Endermite endermite = EntityType.ENDERMITE.create(this.level);
							endermite.moveTo(entity.getX(),
									entity.getY(),
									entity.getZ(),
									entity.getYRot(),
									entity.getXRot()
							);
							this.level.addFreshEntity(endermite);
						}

						if (entity.isPassenger())
						{
							serverplayer.dismountTo(this.getX(), this.getY(), this.getZ());
						}
						else
						{
							entity.teleportTo(this.getX(), this.getY(), this.getZ());
						}

						entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						entity.fallDistance = 0.0F;
					} //Forge: End
				}
			}
			else if (entity != null)
			{
				entity.teleportTo(this.getX(), this.getY(), this.getZ());
				entity.fallDistance = 0.0F;
			}

			this.discard();
		}
	}
}
