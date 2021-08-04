package com.fanrende.myfirstmod.datagen;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.blocks.ComplexMultipartBlock;
import com.fanrende.myfirstmod.blocks.ComplexMultipartTile;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import static com.fanrende.myfirstmod.setup.Registration.COMPLEX_MULTIPART_BLOCK;

public class BlockStates extends BlockStateProvider
{
	public BlockStates(DataGenerator generator, ExistingFileHelper exFileHelper)
	{
		super(generator, MyFirstMod.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		BlockModelBuilder cellFrame = models().getBuilder("block/complex/main");

		floatingCube(cellFrame,  0f,  0f,  0f,  1f, 16f,  1f);
		floatingCube(cellFrame, 15f,  0f,  0f, 16f, 16f,  1f);
		floatingCube(cellFrame,  0f,  0f, 15f,  1f, 16f, 16f);
		floatingCube(cellFrame, 15f,  0f, 15f, 16f, 16f, 16f);

		floatingCube(cellFrame,  1f,  0f,  0f, 15f,  1f,  1f);
		floatingCube(cellFrame,  1f, 15f,  0f, 15f, 16f,  1f);
		floatingCube(cellFrame,  1f,  0f, 15f, 15f,  1f, 16f);
		floatingCube(cellFrame,  1f, 15f, 15f, 15f, 16f, 16f);

		floatingCube(cellFrame,  0f,  0f,  1f,  1f,  1f, 15f);
		floatingCube(cellFrame, 15f,  0f,  1f, 16f,  1f, 15f);
		floatingCube(cellFrame,  0f, 15f,  1f,  1f, 16f, 15f);
		floatingCube(cellFrame, 15f, 15f,  1f, 16f, 16f, 15f);

		floatingCube(cellFrame,  1f,  1f,  1f, 15f, 15f, 15f);

		cellFrame.texture("window", modLoc("block/complex_window"));
		cellFrame.texture("particle", modLoc("block/complex"));

		createDimensionalCellModel(COMPLEX_MULTIPART_BLOCK.get(), cellFrame);
	}

	private void floatingCube(BlockModelBuilder builder, float fx, float fy, float fz, float tx, float ty, float tz)
	{
		builder.element().from(fx, fy, fz).to(tx, ty, tz).allFaces((direction, faceBuilder) -> faceBuilder.texture("#window")).end();
	}

	private void createDimensionalCellModel(Block block, BlockModelBuilder cellFrame)
	{
		BlockModelBuilder singleNone = models().getBuilder("block/complex/singlenone")
				.element().from(3, 3, 3).to(13, 13, 13).face(Direction.DOWN).texture("#single").end().end()
				.texture("single", modLoc("block/complex"));
		BlockModelBuilder singleIn = models().getBuilder("block/complex/singlein")
				.element().from(3, 3, 3).to(13, 13, 13).face(Direction.DOWN).texture("#single").end().end()
				.texture("single", modLoc("block/complex_in"));
		BlockModelBuilder singleOut = models().getBuilder("block/complex/singleout")
				.element().from(3, 3, 3).to(13, 13, 13).face(Direction.DOWN).texture("#single").end().end()
				.texture("single", modLoc("block/complex_out"));

		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

		builder.part().modelFile(cellFrame).addModel();

		BlockModelBuilder[] models = new BlockModelBuilder[] { singleNone, singleIn, singleOut };
		for (ComplexMultipartTile.Mode mode : ComplexMultipartTile.Mode.values())
		{
			builder.part().modelFile(models[mode.ordinal()]).addModel().condition(ComplexMultipartBlock.DOWN, mode);
			builder.part().modelFile(models[mode.ordinal()]).rotationX(180).addModel().condition(ComplexMultipartBlock.UP, mode);
			builder.part().modelFile(models[mode.ordinal()]).rotationX(90).addModel().condition(ComplexMultipartBlock.SOUTH, mode);
			builder.part().modelFile(models[mode.ordinal()]).rotationX(270).addModel().condition(ComplexMultipartBlock.NORTH, mode);
			builder.part().modelFile(models[mode.ordinal()]).rotationY(90).rotationX(90).addModel().condition(ComplexMultipartBlock.WEST, mode);
			builder.part().modelFile(models[mode.ordinal()]).rotationY(270).rotationX(90).addModel().condition(ComplexMultipartBlock.EAST, mode);
		}
	}
}
