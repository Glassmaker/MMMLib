package net.minecraft.src;

import java.util.Map;

import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * ・ｽA・ｽ[・ｽ}・ｽ[・ｽﾌ難ｿｽd・ｽ`・ｽ・ｽp・ｽN・ｽ・ｽ・ｽX・ｽB
 * ・ｽK・ｽ・ｽInner・ｽ・ｽ・ｽﾉは・ｿｽ・ｽf・ｽ・ｽ・ｽ・ｽﾝ定す・ｽ驍ｱ・ｽﾆ。
 * ・ｽﾊ擾ｿｽ・ｽRenderer・ｽﾅ描・ｽ謔ｷ・ｽ驍ｽ・ｽﾟのク・ｽ・ｽ・ｽX・ｽﾈので、Render・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾆ記・ｽq・ｽ・ｽ・ｽ・ｽﾈらい・ｽ・ｽﾈゑｿｽ・ｽN・ｽ・ｽ・ｽX・ｽﾅゑｿｽ・ｽB
 */
public class MMM_ModelBaseDuo extends MMM_ModelBaseNihil implements MMM_IModelBaseMMM {

	public MMM_ModelMultiBase modelOuter;
	public MMM_ModelMultiBase modelInner;
	/**
	 * ・ｽ・ｽ・ｽﾊ厄ｿｽ・ｽﾌア・ｽ[・ｽ}・ｽ[・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ指・ｽ・ｽB
	 * ・ｽO・ｽ・ｽ・ｽB
	 */
	public ResourceLocation[] textureOuter;
	/**
	 * ・ｽ・ｽ・ｽﾊ厄ｿｽ・ｽﾌア・ｽ[・ｽ}・ｽ[・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ指・ｽ・ｽB
	 * ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public ResourceLocation[] textureInner;
	/**
	 * ・ｽ・ｽ・ｽﾊ厄ｿｽ・ｽﾌア・ｽ[・ｽ}・ｽ[・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ指・ｽ・ｽB
	 * ・ｽO・ｽ・ｽ・ｽE・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public ResourceLocation[] textureOuterLight;
	/**
	 * ・ｽ・ｽ・ｽﾊ厄ｿｽ・ｽﾌア・ｽ[・ｽ}・ｽ[・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ指・ｽ・ｽB
	 * ・ｽ・ｽ・ｽ・ｽ・ｽE・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public ResourceLocation[] textureInnerLight;
	/**
	 * ・ｽ`・ｽ謔ｳ・ｽ・ｽ・ｽA・ｽ[・ｽ}・ｽ[・ｽﾌ包ｿｽ・ｽﾊ。
	 * shouldRenderPass・ｽﾆゑｿｽ・ｽﾅ指・ｽ閧ｷ・ｽ・ｽB
	 */
	public int renderParts;

	public float[] textureLightColor;

	public MMM_ModelBaseDuo(RendererLivingEntity pRender) {
		rendererLivingEntity = pRender;
		renderParts = 0;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
		if (modelInner != null) {
			modelInner.setLivingAnimations(entityCaps, par2, par3, par4);
		}
		if (modelOuter != null) {
			modelOuter.setLivingAnimations(entityCaps, par2, par3, par4);
		}
		isAlphablend = true;
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		boolean lri = (renderCount & 0x0f) == 0;
		if (modelInner != null) {
			if (textureInner != null && lri) {
				if (textureInner[renderParts] != null) {
					// ・ｽﾊ擾ｿｽp・ｽ[・ｽc
					MMM_Client.setTexture(textureInner[renderParts]);
					modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
				}
			} else {
				// ・ｽﾙぼエ・ｽ・ｽ・ｽ`・ｽ・ｽ・ｽ・ｽ・ｽg・ｽG・ｽt・ｽF・ｽN・ｽg・ｽp
				modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureInnerLight != null && renderCount == 0) {
				// ・ｽ・ｽ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽ\・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
				if (textureInnerLight[renderParts] != null) {
					MMM_Client.setTexture(textureInnerLight[renderParts]);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
					
					MMM_Client.setLightmapTextureCoords(0x00f000f0);//61680
					if (textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//・ｽ・ｽ・ｽ・ｽ・ｽF・ｽｲ撰ｿｽ
						GL11.glColor4f(
								textureLightColor[0],
								textureLightColor[1],
								textureLightColor[2],
								textureLightColor[3]);
					}
					modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					MMM_Client.setLightmapTextureCoords(lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}
		}
		if (modelOuter != null) {
			if (textureOuter != null && lri) {
				// ・ｽﾊ擾ｿｽp・ｽ[・ｽc
				if (textureOuter[renderParts] != null) {
					MMM_Client.setTexture(textureOuter[renderParts]);
					modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
				}
			} else {
				// ・ｽﾙぼエ・ｽ・ｽ・ｽ`・ｽ・ｽ・ｽ・ｽ・ｽg・ｽG・ｽt・ｽF・ｽN・ｽg・ｽp
				modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureOuterLight != null && renderCount == 0) {
				// ・ｽ・ｽ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽ\・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
				if (textureOuterLight[renderParts] != null) {
					MMM_Client.setTexture(textureOuterLight[renderParts]);
					float var4 = 1.0F;
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
					
					MMM_Client.setLightmapTextureCoords(0x00f000f0);//61680
					if (textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//・ｽ・ｽ・ｽ・ｽ・ｽF・ｽｲ撰ｿｽ
						GL11.glColor4f(
								textureLightColor[0],
								textureLightColor[1],
								textureLightColor[2],
								textureLightColor[3]);
					}
					modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					MMM_Client.setLightmapTextureCoords(lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}
		}
//		isAlphablend = false;
		renderCount++;
	}

	@Override
	public TextureOffset getTextureOffset(String par1Str) {
		return modelInner == null ? null : modelInner.getTextureOffset(par1Str);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity par7Entity) {
		if (modelInner != null) {
			modelInner.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
		}
		if (modelOuter != null) {
			modelOuter.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
		}
	}


	// IModelMMM・ｽﾇ会ｿｽ・ｽ・ｽ

	@Override
	public void renderItems(EntityLivingBase pEntity, Render pRender) {
		if (modelInner != null) {
			modelInner.renderItems(entityCaps);
		}
	}

	@Override
	public void showArmorParts(int pParts) {
		if (modelInner != null) {
			modelInner.showArmorParts(pParts, 0);
		}
		if (modelOuter != null) {
			modelOuter.showArmorParts(pParts, 1);
		}
	}

	/**
	 * Renderer・ｽﾓでゑｿｽ・ｽﾌ変撰ｿｽ・ｽ・ｽﾝ定す・ｽ・ｽB
	 * ・ｽﾝ抵ｿｽl・ｽ・ｽMMM_IModelCaps・ｽ・ｽ・ｽp・ｽ・ｽ・ｽ・ｽ・ｽ・ｽEntitiy・ｽﾆゑｿｽ・ｽ・ｽz・ｽ・ｽB
	 */
	@Override
	public void setEntityCaps(MMM_IModelCaps pEntityCaps) {
		entityCaps = pEntityCaps;
		if (capsLink != null) {
			capsLink.setEntityCaps(pEntityCaps);
		}
	}

	@Override
	public void setRender(Render pRender) {
		if (modelInner != null) {
			modelInner.render = pRender;
		}
		if (modelOuter != null) {
			modelOuter.render = pRender;
		}
	}

	@Override
	public void setArmorRendering(boolean pFlag) {
		isRendering = pFlag;
	}


	// IModelCaps・ｽﾇ会ｿｽ・ｽ・ｽ

	@Override
	public Map<String, Integer> getModelCaps() {
		return modelInner == null ? null : modelInner.getModelCaps();
	}

	@Override
	public Object getCapsValue(int pIndex, Object ... pArg) {
		return modelInner == null ? null : modelInner.getCapsValue(pIndex, pArg);
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		if (capsLink != null) {
			capsLink.setCapsValue(pIndex, pArg);
		}
		if (modelOuter != null) {
			modelOuter.setCapsValue(pIndex, pArg);
		}
		if (modelInner != null) {
			return modelInner.setCapsValue(pIndex, pArg);
		}
		return false;
	}

	@Override
	public void showAllParts() {
		if (modelInner != null) {
			modelInner.showAllParts();
		}
		if (modelOuter != null) {
			modelOuter.showAllParts();
		}
	}

}
