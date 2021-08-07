package com.fanrende.myfirstmod.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

import net.minecraft.client.renderer.RenderState.LineState;

public class MyRenderType extends RenderType
{
	public MyRenderType(
			String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn,
			boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn
	)
	{
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	private static final LineState THICK_LINES = new LineState(OptionalDouble.of(3.0D));

	public static final RenderType OVERLAY_LINES = create(
			"overlay_lines",
			DefaultVertexFormats.POSITION_COLOR,
			GL11.GL_LINES,
			256,
			RenderType.State.builder()
					.setLineState(THICK_LINES)
					.setLayeringState(VIEW_OFFSET_Z_LAYERING)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setTextureState(NO_TEXTURE)
					.setDepthTestState(NO_DEPTH_TEST)
					.setCullState(NO_CULL)
					.setLightmapState(NO_LIGHTMAP)
					.setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false)
	);
}
