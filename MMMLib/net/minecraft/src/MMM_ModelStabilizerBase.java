package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class MMM_ModelStabilizerBase extends MMM_ModelBase {

	public MMM_ModelStabilizerBase() {
	}

	/**
	 * ・ｽg・ｽp・ｽ・ｽ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽ・ｽﾔゑｿｽ・ｽB
	 */
	public ResourceLocation getTexture() {
		return null;
	}

	/**
	 * ・ｽ・ｽ・ｽﾌハ・ｽ[・ｽh・ｽ|・ｽC・ｽ・ｽ・ｽg・ｽﾉ托ｿｽ・ｽ・ｽ・ｽﾂ能・ｽ・ｽ・ｽﾇゑｿｽ・ｽ・ｽ・ｽ・ｽﾔゑｿｽ・ｽB
	 * pName:・ｽn・ｽ[・ｽh・ｽ|・ｽC・ｽ・ｽ・ｽg・ｽﾌ趣ｿｽ・ｽﾊ厄ｿｽ・ｽﾌ。
	 */
	public boolean checkEquipment(String pName) {
		return true;
	}

	/**
	 * ・ｽp・ｽ[・ｽc・ｽﾌ厄ｿｽ・ｽﾌ。
	 */
	public abstract String getName();

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽn・ｽ[・ｽh・ｽ|・ｽC・ｽ・ｽ・ｽg・ｽﾉ托ｿｽ・ｽ・ｽ・ｽﾅゑｿｽ・ｽ驍ｩ・ｽﾇゑｿｽ・ｽ・ｽ・ｽB
	 */
	public int getExclusive() {
		return 0;
	}

	/**
	 * ・ｽ・ｽ・ｽC・ｽh・ｽ・ｽ・ｽ・ｽﾌテ・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌまま使・ｽ墲ｸ・ｽﾉ、・ｽ痰､・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽ・ｽH
	 */
	public boolean isLoadAnotherTexture() {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽﾉ趣ｿｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽ
	 */
	public void init(MMM_EquippedStabilizer pequipped) {
		// ・ｽﾏ撰ｿｽ・ｽﾈどゑｿｽ・ｽ`・ｽ・ｽ・ｽ・ｽ
	}
/*	
	@Deprecated
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽ_・ｽ・ｽ・ｽ・ｽ・ｽO・ｽﾍ奇ｿｽ{・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄぶゑｿｽ・ｽ・ｽ
	 */
	public void render(MMM_ModelMultiBase pModel, Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
//		render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

}
