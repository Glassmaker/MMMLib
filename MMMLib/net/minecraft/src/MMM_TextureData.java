package net.minecraft.src;

import static net.minecraft.src.LMM_Statics.dataWatch_Texture;

/**
 * �e�N�X�`���Ǘ��p�̕ϐ��Q���܂Ƃ߂����́B
 */
public class MMM_TextureData implements MMM_ITextureEntity {

	public EntityLivingBase owner;
	public MMM_IModelCaps entityCaps;
	
	/**
	 * �g�p�����e�N�X�`�����\�[�X�̃R���e�i
	 */
	public ResourceLocation textures[][];
	/**
	 * �I��F
	 */
	public int color;
	/**
	 * �_��e�N�X�`����I�����邩�ǂ���
	 */
	public boolean contract;
	
	public MMM_TextureBoxBase textureBox[];
	public int textureIndex[];


	public MMM_TextureData(EntityLivingBase pEntity, MMM_IModelCaps pCaps) {
		owner = pEntity;
		entityCaps = pCaps;
		textures = new ResourceLocation[][] {
				/**
				 * ��{�A����
				 */
				{ null, null },
				/**
				 * �A�[�}�[���F���A���A���A��
				 */
				{ null, null, null, null },
				/**
				 * �A�[�}�[�O�F���A���A���A��
				 */
				{ null, null, null, null },
				/**
				 * �A�[�}�[�������F���A���A���A��
				 */
				{ null, null, null, null },
				/**
				 * �A�[�}�[�������F���A���A���A��
				 */
				{ null, null, null, null }
		};
		color = 12;
		contract = false;
		textureBox = new MMM_TextureBox[2];
		textureIndex = new int[2];
	}

	/**
	 * �e�N�X�`�����\�[�X�����ݒl�ɍ��킹�Đݒ肷��B
	 */
	public void setTextureNames() {
		// Client
		if (textureBox[0] instanceof MMM_TextureBox) {
			int lc = (color & 0x00ff) + (contract ? 0 : MMM_TextureManager.tx_wild);
			if (((MMM_TextureBox)textureBox[0]).hasColor(lc)) {
				textures[0][0] = ((MMM_TextureBox)textureBox[0]).getTextureName(lc);
				lc = (color & 0x00ff) + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild);
				textures[0][1] = ((MMM_TextureBox)textureBox[0]).getTextureName(lc);
			}
		}
		if (textureBox[1] instanceof MMM_TextureBox && owner != null) {
			for (int i = 1; i < 5; i++) {
				ItemStack is = owner.getCurrentItemOrArmor(i);
				textures[1][i] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, is);
				textures[2][i] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, is);
				textures[3][i] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, is);
				textures[4][i] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, is);
			}
		}
	}



	@Override
	public void setTexturePackIndex(int pColor, int[] pIndex) {
		// Server
		textureIndex[0] = pIndex[0];
		textureIndex[1] = pIndex[1];
		textureBox[0] = MMM_TextureManager.instance.getTextureBoxServer(textureIndex[0]);
		textureBox[1] = MMM_TextureManager.instance.getTextureBoxServer(textureIndex[1]);
		color = pColor;
		// �T�C�Y�̕ύX
		owner.setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
		if (owner instanceof EntityAgeable) {
			// EntityAgeable�͂�������Ȃ��Ƒ傫���ύX���Ȃ��悤�ɂȂ��Ă�A�������B
			((EntityAgeable)owner).func_98054_a(owner.isChild());
		}
	}

	@Override
	public void setTexturePackName(MMM_TextureBox[] pTextureBox) {
		// Client
		textureBox[0] = pTextureBox[0];
		textureBox[1] = pTextureBox[1];
		// �T�C�Y�̕ύX
		owner.setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
		if (owner instanceof EntityAgeable) {
			// EntityAgeable�͂�������Ȃ��Ƒ傫���ύX���Ȃ��悤�ɂȂ��Ă�A�������B
			((EntityAgeable)owner).func_98054_a(owner.isChild());
		}
//		setTextureNames();
	}

	@Override
	public void setColor(int pColor) {
		color = pColor;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public void setContract(boolean pContract) {
		contract = pContract;
	}

	@Override
	public boolean isContract() {
		return contract;
	}

	@Override
	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox) {
		textureBox = pTextureBox;
	}

	@Override
	public MMM_TextureBoxBase[] getTextureBox() {
		return textureBox;
	}

	@Override
	public void setTextureIndex(int[] pTextureIndex) {
		textureIndex = pTextureIndex;
	}

	@Override
	public int[] getTextureIndex() {
		return textureIndex;
	}

	@Override
	public void setTextures(int pIndex, ResourceLocation[] pNames) {
		textures[pIndex] = pNames;
	}

	@Override
	public ResourceLocation[] getTextures(int pIndex) {
		return textures[pIndex];
	}

}
