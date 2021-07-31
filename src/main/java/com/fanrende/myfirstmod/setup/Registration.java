package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.BakedBlock;
import com.fanrende.myfirstmod.blocks.FirstBlock;
import com.fanrende.myfirstmod.blocks.FirstBlockContainer;
import com.fanrende.myfirstmod.blocks.FirstBlockTile;
import com.fanrende.myfirstmod.entities.InfinityEyeEntity;
import com.fanrende.myfirstmod.entities.InfinityPearlEntity;
import com.fanrende.myfirstmod.entities.WeirdMobEntity;
import com.fanrende.myfirstmod.items.FirstItem;
import com.fanrende.myfirstmod.items.InfinityEye;
import com.fanrende.myfirstmod.items.InfinityPearl;
import com.fanrende.myfirstmod.items.WeirdMobEggItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.fanrende.myfirstmod.MyFirstMod.MODID;

public class Registration
{
	private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES,
			MODID
	);
	private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS,
			MODID
	);
	private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES,
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
			() -> new BlockItem(FIRSTBLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP))
	);
	public static final RegistryObject<TileEntityType<FirstBlockTile>> FIRSTBLOCK_TILE = TILES.register("firstblock",
			() -> TileEntityType.Builder.create(FirstBlockTile::new, FIRSTBLOCK.get()).build(null)
	);

	public static final RegistryObject<ContainerType<FirstBlockContainer>> FIRSTBLOCK_CONTAINER = CONTAINERS.register("firstblock",
			() -> IForgeContainerType.create((windowId, inv, data) ->
			{
				BlockPos pos = data.readBlockPos();
				return new FirstBlockContainer(windowId, inv.player.getEntityWorld(), pos, inv);
			})
	);

	public static final RegistryObject<BakedBlock> BAKEDBLOCK = BLOCKS.register("bakedblock", BakedBlock::new);
	public static final RegistryObject<Item> BAKEDBLOCK_ITEM = ITEMS.register("bakedblock",
			() -> new BlockItem(BAKEDBLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP))
	);

	public static final RegistryObject<FirstItem> FIRSTITEM = ITEMS.register("firstitem", FirstItem::new);
	public static final RegistryObject<WeirdMobEggItem> WEIRDMOB_EGG = ITEMS.register("weirdmob_egg",
			WeirdMobEggItem::new
	);
	public static final RegistryObject<InfinityPearl> INFINITY_PEARL = ITEMS.register("infinitypearl",
			InfinityPearl::new
	);
	public static final RegistryObject<InfinityEye> INFINITY_EYE = ITEMS.register("infinityeye", InfinityEye::new);

	public static final RegistryObject<EntityType<InfinityPearlEntity>> INFINITYPEARL_ENTITY = ENTITIES.register(
			"infinitypearl_entity",
			() -> EntityType.Builder.<InfinityPearlEntity>create(InfinityPearlEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.build("infinitypearl_entity")
	);

	public static final RegistryObject<EntityType<InfinityEyeEntity>> INFINITYEYE_ENTITY = ENTITIES.register(
			"infinityeye_entity",
			() -> EntityType.Builder.<InfinityEyeEntity>create(InfinityEyeEntity::new, EntityClassification.MISC)
					.size(0.25F, 0.25F)
					.build("infinityeye_entity")
	);

	public static final RegistryObject<EntityType<WeirdMobEntity>> WEIRDMOB = ENTITIES.register("weirdmob",
			() -> EntityType.Builder.create(WeirdMobEntity::new, EntityClassification.CREATURE)
					.size(1, 1)
					.setShouldReceiveVelocityUpdates(false)
					.build("weirdmob")
	);
}
