package net.minecraft.src;

import net.minecraft.util.ResourceLocation;

/**
 * MMM_Texture�d�l�̃e�N�X�`���p�b�N�ݒ�ɑΉ����Ă���Entity�֌p��������B
 */
public interface MMM_ITextureEntity {

	/**
	 * Server�p�B
	 * TextureManager���T�[�o�[����Entity�փe�N�X�`���ύX�̒ʒm���s���B
	 * @param pIndex
	 * �ݒ肳���e�N�X�`���p�b�N�̃C���f�b�N�X�iTextureBoxServer�j
	 */
	public void setTexturePackIndex(int pColor, int[] pIndex);

	/**
	 * Client�p�B
	 * TextureManager���N���C�A���g����Entity�փe�N�X�`���ύX�̒ʒm���s���B
	 * @param pPackName
	 * �ݒ肳���e�N�X�`���p�b�N�̖��́iTextureBoxClient�j
	 */
	public void setTexturePackName(MMM_TextureBox[] pTextureBox);

	/**
	 * ���݂�Entity�ɐF��ݒ肷��B
	 * @param pColor
	 */
	public void setColor(int pColor);

	/**
	 * ���݂�Entity�ɐݒ肳��Ă���F��Ԃ��B
	 * @return
	 */
	public int getColor();

	public void setContract(boolean pContract);
	public boolean isContract();

	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox);
	public MMM_TextureBoxBase[] getTextureBox();

	public void setTextureIndex(int[] pTextureIndex);
	public int[] getTextureIndex();

	public void setTextures(int pIndex, ResourceLocation[] pNames);
	public ResourceLocation[] getTextures(int pIndex);
	
	/**
	 * �d�l�ύX�ɂ��A����ȊO�͕K�v�����Ȃ�\��B
	 * @return
	 */
	public MMM_TextureData getTextureData();


}
