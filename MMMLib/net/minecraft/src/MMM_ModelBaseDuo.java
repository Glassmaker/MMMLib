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
 * �A�[�}�[�̓�d�`��p�N���X�B
 * �K��Inner���ɂ̓��f����ݒ肷�邱�ƁB
 * �ʏ��Renderer�ŕ`�悷�邽�߂̃N���X�Ȃ̂ŁARender�������ƋL�q����Ȃ炢��Ȃ��N���X�ł��B
 */
public class MMM_ModelBaseDuo extends MMM_ModelBaseNihil implements MMM_IModelBaseMMM {

	public MMM_ModelMultiBase modelOuter;
	public MMM_ModelMultiBase modelInner;
	/**
	 * ���ʖ��̃A�[�}�[�e�N�X�`���̎w��B
	 * �O���B
	 */
	public ResourceLocation[] textureOuter;
	/**
	 * ���ʖ��̃A�[�}�[�e�N�X�`���̎w��B
	 * �����B
	 */
	public ResourceLocation[] textureInner;
	/**
	 * ���ʖ��̃A�[�}�[�e�N�X�`���̎w��B
	 * �O���E�����B
	 */
	public ResourceLocation[] textureOuterLight;
	/**
	 * ���ʖ��̃A�[�}�[�e�N�X�`���̎w��B
	 * �����E�����B
	 */
	public ResourceLocation[] textureInnerLight;
	/**
	 * �`�悳���A�[�}�[�̕��ʁB
	 * shouldRenderPass�Ƃ��Ŏw�肷��B
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
					// �ʏ�p�[�c
					MMM_Client.setTexture(textureInner[renderParts]);
					modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
				}
			} else {
				// �قڃG���`�����g�G�t�F�N�g�p
				modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureInnerLight != null && renderCount == 0) {
				// �����e�N�X�`���\������
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
						//�����F�𒲐�
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
				// �ʏ�p�[�c
				if (textureOuter[renderParts] != null) {
					MMM_Client.setTexture(textureOuter[renderParts]);
					modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
				}
			} else {
				// �قڃG���`�����g�G�t�F�N�g�p
				modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureOuterLight != null && renderCount == 0) {
				// �����e�N�X�`���\������
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
						//�����F�𒲐�
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


	// IModelMMM�ǉ���

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
	 * Renderer�ӂł��̕ϐ���ݒ肷��B
	 * �ݒ�l��MMM_IModelCaps���p������Entitiy�Ƃ���z��B
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


	// IModelCaps�ǉ���

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
