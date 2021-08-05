package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.Config;
import com.fanrende.myfirstmod.setup.ModSetup;
import com.fanrende.myfirstmod.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
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
		return new CustomEnergyStorage.Item.Provider(stack,
				Config.ENERGYPICKAXE_MAXPOWER.get(),
				Config.ENERGYPICKAXE_MINECOST.get()
		);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		return 12.0f;
	}

	@Override
	public int getItemEnchantability()
	{
		return 30;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(
			ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		tooltip.add(new StringTextComponent("\u00A75" + "energy: \u00A77" + getEnergyStored(stack) + "\u00A75/\u00A77" + Config.ENERGYPICKAXE_MAXPOWER.get()));
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.energypickaxe",
					Config.ENERGYPICKAXE_MINECOST.get()
			));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn)
	{
		if (blockIn.getHarvestTool() == ToolType.PICKAXE)
			return true;

		Material material = blockIn.getMaterial();
		if (material == Material.ROCK || material == Material.IRON || material == Material.ANVIL)
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
		return (double) 1 - ( (double) getEnergyStored(stack) / (double) Config.ENERGYPICKAXE_MAXPOWER.get() );
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return MathHelper.hsvToRGB(0.72F, 0.66F, 1.0F);
	}

	private int getEnergyStored(ItemStack stack)
	{
		AtomicInteger energyStored = new AtomicInteger(0);

		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> energyStored.set(h.getEnergyStored()));

		return energyStored.get();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(
			World worldIn, PlayerEntity playerIn, Hand handIn
	)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(h ->
		{
			( (CustomEnergyStorage.Item) h ).setEnergy(Config.ENERGYPICKAXE_MAXPOWER.get());

			System.out.println(h.getEnergyStored());
		});

		worldIn.playSound((PlayerEntity) null,
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
		boolean stopBreaking = getEnergyStored(stack) < Config.ENERGYPICKAXE_MINECOST.get();

		if (stopBreaking)
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
			( (CustomEnergyStorage.Item) h ).extractEnergy(Config.ENERGYPICKAXE_MINECOST.get(), false);

			System.out.println(h.getEnergyStored());
		});
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
}
