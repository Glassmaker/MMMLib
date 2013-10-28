package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class MMM_ModelStabilizerBase extends MMM_ModelBase {

	public MMM_ModelStabilizerBase() {
	}

	/**
	 * �g�p�����e�N�X�`����Ԃ��B
	 */
	public ResourceLocation getTexture() {
		return null;
	}

	/**
	 * ���̃n�[�h�|�C���g�ɑ����\���ǂ�����Ԃ��B
	 * pName:�n�[�h�|�C���g�̎��ʖ��́B
	 */
	public boolean checkEquipment(String pName) {
		return true;
	}

	/**
	 * �p�[�c�̖��́B
	 */
	public abstract String getName();

	/**
	 * �����n�[�h�|�C���g�ɑ����ł��邩�ǂ����B
	 */
	public int getExclusive() {
		return 0;
	}

	/**
	 * ���C�h����̃e�N�X�`�������̂܂܎g�킸�ɁA�Ⴄ�e�N�X�`�����g�����H
	 */
	public boolean isLoadAnotherTexture() {
		return false;
	}

	/**
	 * �����Ɏ��s�����
	 */
	public void init(MMM_EquippedStabilizer pequipped) {
		// �ϐ��Ȃǂ��`����
	}
/*	
	@Deprecated
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

	/**
	 * �����_�����O�͊�{��������ĂԂ���
	 */
	public void render(MMM_ModelMultiBase pModel, Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
//		render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

}
