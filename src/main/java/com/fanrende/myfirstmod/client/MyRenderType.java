package com.fanrende.myfirstmod.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class MyRenderType extends RenderType
{
	public MyRenderType(
			String name, VertexFormat format, VertexFormat.Mode drawMode, int bufferSize, boolean useDelegate,
			boolean needSorting, Runnable setupTask, Runnable clearTask
	)
	{
		super(name, format, drawMode, bufferSize, useDelegate, needSorting, setupTask, clearTask);
	}

	private static final LineStateShard THICK_LINES = new LineStateShard(OptionalDouble.of(3.0D));

	public static final RenderType OVERLAY_LINES = create(
			"overlay_lines",
			DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.LINES,
			256,
			false,
			false,
			RenderType.CompositeState.builder()
					.setLineState(THICK_LINES)
					.setLayeringState(VIEW_OFFSET_Z_LAYERING)
					.setShaderState(RENDERTYPE_LINES_SHADER)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setTextureState(NO_TEXTURE)
					.setDepthTestState(NO_DEPTH_TEST)
					.setCullState(NO_CULL)
					.setLightmapState(NO_LIGHTMAP)
					.setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false)
	);
}
