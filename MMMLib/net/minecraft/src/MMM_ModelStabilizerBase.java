package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class MMM_ModelStabilizerBase extends MMM_ModelBase {

	public MMM_ModelStabilizerBase() {
	}

	/**
	 * 使用されるテクスチャを返す。
	 * I return the texture to be used.
	 */
	public ResourceLocation getTexture() {
		return null;
	}

	/**
	 * そのハードポイントに装備可能かどうかを返す。
	 * return whether or not the equipment available on the hard point.
	 * pName:ハードポイントの識別名称。
	 * Identify the name of the hard point.
	 */
	public boolean checkEquipment(String pName) {
		return true;
	}

	/**
	 * パーツの名称。
	 * The name of the part.
	 */
	public abstract String getName();

	/**
	 * 同じハードポイントに装備できるかどうか。
	 * Whether the same can be equipped with hard point.
	 */
	public int getExclusive() {
		return 0;
	}

	/**
	 * メイドさんのテクスチャをそのまま使わずに、違うテクスチャを使うか？
	 * Instead of accepting the texture of the maid, you can either use a different texture?
	 */
	public boolean isLoadAnotherTexture() {
		return false;
	}

	/**
	 * 初期化時に実行される
	 * It is executed at initialization
	 */
	public void init(MMM_EquippedStabilizer pequipped) {
		// 変数などを定義する, I define variables and the like
	}
/*	
	@Deprecated
	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

	/**
	 * レンダリングは基本こちらを呼ぶこと, Rendering will be referred to as the basic here
	 */
	public void render(MMM_ModelMultiBase pModel, Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
//		render(par1Entity, par2, par3, par4, par5, par6, par7);
	}

}
