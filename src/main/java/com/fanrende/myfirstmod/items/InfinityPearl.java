package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.entities.InfinityPearlEntity;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class InfinityPearl extends Item
{
	public InfinityPearl()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		worldIn.playSound(
				(PlayerEntity) null,
				playerIn.getPosX(),
				playerIn.getPosY(),
				playerIn.getPosZ(),
				SoundEvents.ENTITY_ENDER_PEARL_THROW,
				SoundCategory.NEUTRAL,
				0.5F,
				0.4F / ( random.nextFloat() * 0.4F + 0.8F )
		);
		playerIn.getCooldownTracker().setCooldown(this, 5);
		if (!worldIn.isRemote)
		{
			InfinityPearlEntity infinityPearlEntity = new InfinityPearlEntity(worldIn, playerIn);
			infinityPearlEntity.setItem(itemstack);
			infinityPearlEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.5F, 1.0F);
			worldIn.addEntity(infinityPearlEntity);
		}

		playerIn.addStat(Stats.ITEM_USED.get(this));

		return ActionResult.resultSuccess(itemstack);
	}
}
