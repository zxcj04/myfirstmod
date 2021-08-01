package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.setup.ModSetup;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class EnergyPickaxe extends Item
{
	public EnergyPickaxe()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(
			ItemStack stack, @Nullable CompoundNBT nbt
	)
	{
		return new CustomEnergyStorage.Item.Provider(stack, 10000, 1000);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		return 12.0f;
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
		if (blockIn.getHarvestTool() == ToolType.PICKAXE)
			return true;

		Material material = blockIn.getMaterial();
		if(material == Material.ROCK || material == Material.IRON || material == Material.ANVIL)
			return true;

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		AtomicInteger energyStored = new AtomicInteger(0);

		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> energyStored.set(h.getEnergyStored()));

		return (double) 1 - ( (double) energyStored.get() / (double) 10000 );
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return MathHelper.hsvToRGB(0.72F, 0.66F, 1.0F);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(
			World worldIn, PlayerEntity playerIn, Hand handIn
	)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
		{
			( (CustomEnergyStorage.Item) h ).setEnergy(10000);

			System.out.println(h.getEnergyStored());
		});

		worldIn.playSound(
				(PlayerEntity) null,
				playerIn.getPosition(),
				SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE,
				SoundCategory.NEUTRAL,
				0.65F,
				0.4F / ( random.nextFloat() * 0.4F + 0.8F )
		);

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player)
	{
		AtomicBoolean stopBreaking = new AtomicBoolean(false);

		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> stopBreaking.set(h.getEnergyStored() < 1000));

		if (stopBreaking.get())
			return true;

		return super.onBlockStartBreak(stack, pos, player);
	}

	@Override
	public boolean onBlockDestroyed(
			ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving
	)
	{
		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
		{
			( (CustomEnergyStorage.Item) h ).extractEnergy(1000, false);

			System.out.println(h.getEnergyStored());
		});
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
}
