package com.fanrende.myfirstmod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InfinityEyeEntity extends EyeOfEnderEntity
{
	public InfinityEyeEntity(EntityType<? extends EyeOfEnderEntity> t, World w)
	{
		super(t, w);
	}

	public InfinityEyeEntity(World worldIn, double posX, double d, double posZ)
	{
		super(worldIn, posX, d, posZ);
	}

	@Override
	public ItemStack getItem()
	{
		return ItemStack.EMPTY;
	}
}
