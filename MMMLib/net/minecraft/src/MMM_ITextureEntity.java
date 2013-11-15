package net.minecraft.src;

import net.minecraft.util.ResourceLocation;

/**
 * MMM_Texture・ｽd・ｽl・ｽﾌテ・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾝ抵ｿｽﾉ対会ｿｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽEntity・ｽﾖ継・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
 */
public interface MMM_ITextureEntity {

	/**
	 * Server・ｽp・ｽB
	 * TextureManager・ｽ・ｽ・ｽT・ｽ[・ｽo・ｽ[・ｽ・ｽ・ｽ・ｽEntity・ｽﾖテ・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾏ更・ｽﾌ通知・ｽ・ｽ・ｽs・ｽ・ｽ・ｽB
	 * @param pIndex
	 * ・ｽﾝ定さ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌイ・ｽ・ｽ・ｽf・ｽb・ｽN・ｽX・ｽiTextureBoxServer・ｽj
	 */
	public void setTexturePackIndex(int pColor, int[] pIndex);

	/**
	 * Client・ｽp・ｽB
	 * TextureManager・ｽ・ｽ・ｽN・ｽ・ｽ・ｽC・ｽA・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽEntity・ｽﾖテ・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾏ更・ｽﾌ通知・ｽ・ｽ・ｽs・ｽ・ｽ・ｽB
	 * @param pPackName
	 * ・ｽﾝ定さ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌ厄ｿｽ・ｽﾌ（TextureBoxClient・ｽj
	 */
	public void setTexturePackName(MMM_TextureBox[] pTextureBox);

	/**
	 * ・ｽ・ｽ・ｽﾝゑｿｽEntity・ｽﾉ色・ｽ・ｽﾝ定す・ｽ・ｽB
	 * @param pColor
	 */
	public void setColor(int pColor);

	/**
	 * ・ｽ・ｽ・ｽﾝゑｿｽEntity・ｽﾉ設定さ・ｽ・ｽﾄゑｿｽ・ｽ・ｽF・ｽ・ｽﾔゑｿｽ・ｽB
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
	 * ・ｽd・ｽl・ｽﾏ更・ｽﾉゑｿｽ・ｽA・ｽ・ｽ・ｽ・ｽﾈ外・ｽﾍ必・ｽv・ｽ・ｽ・ｽ・ｽ・ｽﾈゑｿｽ\・ｽ・ｽB
	 * @return
	 */
	public MMM_TextureData getTextureData();


}
