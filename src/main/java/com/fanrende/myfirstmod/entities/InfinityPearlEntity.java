package com.fanrende.myfirstmod.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;

import javax.annotation.Nullable;

public class InfinityPearlEntity extends EnderPearlEntity
{
	public InfinityPearlEntity(EntityType<? extends EnderPearlEntity> p_i50153_1_, World world)
	{
		super(p_i50153_1_, world);
	}

	public InfinityPearlEntity(World worldIn, LivingEntity throwerIn)
	{
		super(EntityType.ENDER_PEARL, worldIn);
	}

	@OnlyIn(Dist.CLIENT)
	public InfinityPearlEntity(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	@Override
	protected void onHit(RayTraceResult result)
	{
		RayTraceResult.Type raytraceresult$type = result.getType();
		if (raytraceresult$type == RayTraceResult.Type.ENTITY)
			this.onHitEntity((EntityRayTraceResult) result);
		else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
			this.onHitBlock((BlockRayTraceResult) result);

		Entity entity = this.getOwner();

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

		if (!this.level.isClientSide && !this.removed)
		{
			if (entity instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entity;
				if (serverplayerentity.connection.getConnection()
						.isConnected() && serverplayerentity.level == this.level && !serverplayerentity.isSleeping())
				{
					EntityTeleportEvent.EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverplayerentity,
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
							EndermiteEntity endermiteentity = EntityType.ENDERMITE.create(this.level);
							endermiteentity.setPlayerSpawned(true);
							endermiteentity.moveTo(entity.getX(),
									entity.getY(),
									entity.getZ(),
									entity.yRot,
									entity.xRot
							);
							this.level.addFreshEntity(endermiteentity);
						}

						if (entity.isPassenger())
						{
							entity.stopRiding();
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

			this.remove();
		}
	}
}
