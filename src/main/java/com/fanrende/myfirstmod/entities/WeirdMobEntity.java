package com.fanrende.myfirstmod.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class WeirdMobEntity extends AnimalEntity
{

	public WeirdMobEntity(EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Nullable
	@Override
	public AgeableEntity createChild(ServerWorld world, AgeableEntity mate)
	{
		return null;
	}

	public static AttributeModifierMap.MutableAttribute prepareAttributes()
	{
		return LivingEntity.registerAttributes()
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 40.D);
	}
}
