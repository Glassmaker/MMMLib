package net.minecraft.src;

import net.minecraft.util.ResourceLocation;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 * I to inherit to the Entity that corresponds to the texture pack set of MMM_Texture specification.
 */
public interface MMM_ITextureEntity {

	/**
	 * Server用。
	 * TextureManagerがサーバー側のEntityへテクスチャ変更の通知を行う。
	 * Server.
	 * TextureManager makes a notification of the texture changes to Entity on the server side.
	 * @param pIndex
	 * 設定されるテクスチャパックのインデックス（TextureBoxServer）
	 * The index of the texture pack that is set (TextureBoxServer)
	 */
	public void setTexturePackIndex(int pColor, int[] pIndex);

	/**
	 * Client用。
	 * TextureManagerがクライアント側のEntityへテクスチャ変更の通知を行う。
	 * Client.
	 * TextureManager performs notification of changes to the texture Entity on the client side.
	 * @param pPackName
	 * 設定されるテクスチャパックの名称（TextureBoxClient）
	 * The name of the texture pack that is set (TextureBoxClient)
	 */
	public void setTexturePackName(MMM_TextureBox[] pTextureBox);

	/**
	 * 現在のEntityに色を設定する。
	 * I set the color to the current Entity.
	 * @param pColor
	 */
	public void setColor(int pColor);

	/**
	 * 現在のEntityに設定されている色を返す。
	 * I returns a color that is set in the current Entity.
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
	 * 仕様変更により、これ以外は必要無くなる予定。
	 * Plans according to specifications changes, no longer need the other.
	 * @return
	 */
	public MMM_TextureData getTextureData();


}
