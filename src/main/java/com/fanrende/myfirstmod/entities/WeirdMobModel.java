package com.fanrende.myfirstmod.entities;

import com.fanrende.myfirstmod.MyFirstMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class WeirdMobModel extends EntityModel<WeirdMobEntity>
{
	public final ModelPart body;

	private static final String BODY = "body";
	public static ModelLayerLocation CUBE_LAYER = new ModelLayerLocation(new ResourceLocation(MyFirstMod.MODID,
			"weirdmob"
	), BODY);

	public WeirdMobModel(ModelPart body)
	{
		this.body = body;
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild(BODY,
				CubeListBuilder.create().texOffs(0, 0).addBox(-3, 16, -3, 6, 6, 6),
				PartPose.ZERO
		);

		return LayerDefinition.create(meshDefinition, 64, 32);
	}

	@Override
	public void setupAnim(
			WeirdMobEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch
	)
	{

	}

	@Override
	public void renderToBuffer(
			PoseStack matrixStack, VertexConsumer iVertexBuilder, int packedLightIn, int packedOverlayIn, float red,
			float green, float blue, float alpha
	)
	{
		body.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}