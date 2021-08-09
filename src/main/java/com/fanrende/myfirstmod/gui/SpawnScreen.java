package com.fanrende.myfirstmod.gui;

import com.fanrende.myfirstmod.MyFirstMod;
import com.fanrende.myfirstmod.network.Networking;
import com.fanrende.myfirstmod.network.PacketSpawn;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class SpawnScreen extends Screen
{
	private static final int WIDTH = 179;
	private static final int HEIGHT = 151;

	private ResourceLocation GUI = new ResourceLocation(MyFirstMod.MODID, "textures/gui/spawn_gui.png");

	protected SpawnScreen()
	{
		super(new TextComponent("Spawn Entities"));
	}

	private void spawn(String id)
	{
		Networking.sendToServer(new PacketSpawn(new ResourceLocation(id),
				minecraft.player.getCommandSenderWorld().dimension(),
				minecraft.player.blockPosition()
		));
		minecraft.setScreen(null);
	}

	@Override
	protected void init()
	{
		int relX = ( this.width - WIDTH ) / 2;
		int relY = ( this.height - HEIGHT ) / 2;

		addRenderableWidget(new Button(relX + 10,
				relY + 10,
				160,
				20,
				new TextComponent("Skeleton"),
				button -> spawn("minecraft:skeleton")
		));
		addRenderableWidget(new Button(relX + 10,
				relY + 37,
				160,
				20,
				new TextComponent("Zombie"),
				button -> spawn("minecraft:zombie")
		));
		addRenderableWidget(new Button(relX + 10,
				relY + 64,
				160,
				20,
				new TextComponent("Cow"),
				button -> spawn("minecraft:cow")
		));
		addRenderableWidget(new Button(relX + 10,
				relY + 91,
				160,
				20,
				new TextComponent("Sheep"),
				button -> spawn("minecraft:sheep")
		));
		addRenderableWidget(new Button(relX + 10,
				relY + 118,
				160,
				20,
				new TextComponent("Chicken"),
				button -> spawn("minecraft:chicken")
		));
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.setShaderTexture(0, GUI);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		int relX = ( this.width - WIDTH ) / 2;
		int relY = ( this.height - HEIGHT ) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public static void open()
	{
		Minecraft.getInstance().setScreen(new SpawnScreen());
	}
}
