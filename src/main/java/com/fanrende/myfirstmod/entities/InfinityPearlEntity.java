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
	protected void onImpact(RayTraceResult result)
	{
		RayTraceResult.Type raytraceresult$type = result.getType();
		if (raytraceresult$type == RayTraceResult.Type.ENTITY)
			this.onEntityHit((EntityRayTraceResult) result);
		else if (raytraceresult$type == RayTraceResult.Type.BLOCK)
			this.func_230299_a_((BlockRayTraceResult) result);

		Entity entity = this.getShooter();

		for (int i = 0; i < 32; ++i)
		{
			this.world.addParticle(ParticleTypes.PORTAL,
					this.getPosX(),
					this.getPosY() + this.rand.nextDouble() * 2.0D,
					this.getPosZ(),
					this.rand.nextGaussian(),
					0.0D,
					this.rand.nextGaussian()
			);
		}

		if (!this.world.isRemote && !this.removed)
		{
			if (entity instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entity;
				if (serverplayerentity.connection.getNetworkManager()
						.isChannelOpen() && serverplayerentity.world == this.world && !serverplayerentity.isSleeping())
				{
					EntityTeleportEvent.EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverplayerentity,
							this.getPosX(),
							this.getPosY(),
							this.getPosZ(),
							this,
							5.0F
					);
					if (!event.isCanceled())
					{ // Don't indent to lower patch size
						if (this.rand.nextFloat() < 0.05F && this.world.getGameRules()
								.getBoolean(GameRules.DO_MOB_SPAWNING))
						{
							EndermiteEntity endermiteentity = EntityType.ENDERMITE.create(this.world);
							endermiteentity.setSpawnedByPlayer(true);
							endermiteentity.setLocationAndAngles(entity.getPosX(),
									entity.getPosY(),
									entity.getPosZ(),
									entity.rotationYaw,
									entity.rotationPitch
							);
							this.world.addEntity(endermiteentity);
						}

						if (entity.isPassenger())
						{
							entity.stopRiding();
						}

						entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						entity.fallDistance = 0.0F;
					} //Forge: End
				}
			}
			else if (entity != null)
			{
				entity.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
				entity.fallDistance = 0.0F;
			}

			this.remove();
		}
	}
}
