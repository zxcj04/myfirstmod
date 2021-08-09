package com.fanrende.myfirstmod.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InfinityEyeEntity extends EyeOfEnder
{
	public InfinityEyeEntity(EntityType<? extends EyeOfEnder> t, Level w)
	{
		super(t, w);
	}

	public InfinityEyeEntity(Level worldIn, double posX, double d, double posZ)
	{
		super(worldIn, posX, d, posZ);
	}

	@Override
	public ItemStack getItem()
	{
		return ItemStack.EMPTY;
	}
}
