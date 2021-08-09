package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.*;
import com.fanrende.myfirstmod.entities.InfinityEyeEntity;
import com.fanrende.myfirstmod.entities.InfinityPearlEntity;
import com.fanrende.myfirstmod.entities.WeirdMobEntity;
import com.fanrende.myfirstmod.items.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.fanrende.myfirstmod.MyFirstMod.MODID;

public class Registration
{
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,
			MODID
	);
	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
			MODID
	);
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			MODID
	);

	public static void init()
	{
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<FirstBlock> FIRSTBLOCK = BLOCKS.register("firstblock", FirstBlock::new);
	public static final RegistryObject<Item> FIRSTBLOCK_ITEM = ITEMS.register("firstblock",
			() -> new FirstBlockItem(FIRSTBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP))
	);
	public static final RegistryObject<BlockEntityType<FirstBlockTile>> FIRSTBLOCK_TILE = TILES.register("firstblock",
			() -> BlockEntityType.Builder.of(FirstBlockTile::new, FIRSTBLOCK.get()).build(null)
	);
	public static final RegistryObject<MenuType<FirstBlockContainer>> FIRSTBLOCK_CONTAINER = CONTAINERS.register("firstblock",
			() -> IForgeContainerType.create((windowId, inv, data) ->
			{
				BlockPos pos = data.readBlockPos();
				return new FirstBlockContainer(windowId, inv.player.getCommandSenderWorld(), pos, inv);
			})
	);

	public static final RegistryObject<BakedBlock> BAKEDBLOCK = BLOCKS.register("bakedblock", BakedBlock::new);
	public static final RegistryObject<Item> BAKEDBLOCK_ITEM = ITEMS.register("bakedblock",
			() -> new BlockItem(BAKEDBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP))
	);
	public static final RegistryObject<BlockEntityType<BakedBlockTile>> BAKEDBLOCK_TILE = TILES.register("bakedblock",
			() -> BlockEntityType.Builder.of(BakedBlockTile::new, BAKEDBLOCK.get()).build(null)
	);

	public static final RegistryObject<MagicBlock> MAGICBLOCK = BLOCKS.register("magicblock", MagicBlock::new);
	public static final RegistryObject<Item> MAGICBLOCK_ITEM = ITEMS.register("magicblock",
			() -> new BlockItem(MAGICBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP))
	);
	public static final RegistryObject<BlockEntityType<MagicBlockTile>> MAGICBLOCK_TILE = TILES.register("magicblock",
			() -> BlockEntityType.Builder.of(MagicBlockTile::new, MAGICBLOCK.get()).build(null)
	);

	public static final RegistryObject<ComplexMultipartBlock> COMPLEX_MULTIPART_BLOCK = BLOCKS.register("complexmultipartblock",
			ComplexMultipartBlock::new
	);
	public static final RegistryObject<Item> COMPLEX_MULTIPART_ITEM = ITEMS.register("complexmultipartblock",
			() -> new BlockItem(COMPLEX_MULTIPART_BLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP))
	);
	public static final RegistryObject<BlockEntityType<ComplexMultipartTile>> COMPLEX_MULTIPART_TILE = TILES.register(
			"complexmultipartblock",
			() -> BlockEntityType.Builder.of(ComplexMultipartTile::new, COMPLEX_MULTIPART_BLOCK.get()).build(null)
	);

	public static final RegistryObject<FirstItem> FIRSTITEM = ITEMS.register("firstitem", FirstItem::new);
	public static final RegistryObject<EnergyPickaxe> ENERGYPICKAXE = ITEMS.register("energypickaxe",
			EnergyPickaxe::new
	);
	public static final RegistryObject<SpawnTool> SPAWNTOOL = ITEMS.register("spawntool", SpawnTool::new);
	public static final RegistryObject<WeirdMobEggItem> WEIRDMOB_EGG = ITEMS.register("weirdmob_egg",
			WeirdMobEggItem::new
	);
	public static final RegistryObject<InfinityPearl> INFINITY_PEARL = ITEMS.register("infinitypearl",
			InfinityPearl::new
	);
	public static final RegistryObject<InfinityEye> INFINITY_EYE = ITEMS.register("infinityeye", InfinityEye::new);

	public static final RegistryObject<EntityType<InfinityPearlEntity>> INFINITYPEARL_ENTITY = ENTITIES.register(
			"infinitypearl_entity",
			() -> EntityType.Builder.<InfinityPearlEntity>of(InfinityPearlEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.build("infinitypearl_entity")
	);

	public static final RegistryObject<EntityType<InfinityEyeEntity>> INFINITYEYE_ENTITY = ENTITIES.register(
			"infinityeye_entity",
			() -> EntityType.Builder.<InfinityEyeEntity>of(InfinityEyeEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.build("infinityeye_entity")
	);

	public static final RegistryObject<EntityType<WeirdMobEntity>> WEIRDMOB = ENTITIES.register("weirdmob",
			() -> EntityType.Builder.of(WeirdMobEntity::new, MobCategory.CREATURE)
					.sized(.5f, .5f)
					.setShouldReceiveVelocityUpdates(false)
					.build("weirdmob")
	);
}
