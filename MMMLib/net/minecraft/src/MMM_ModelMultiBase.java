package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

/**
 * マルチモデル用の基本クラス、これを継承していればマルチモデルとして使用できる。
 * Mincraftネイティブなクラスや継承関数などを排除して、難読化対策を行う。
 * 継承クラスではなくなったため、直接的な互換性はない。
 * Can be used as a multi-model base class of multi-models, if you have inherited this.
 * By eliminating the inheritance and function classes and Mincraft native, to perform the obfuscation measures.
 * Since it is no longer the inheriting class, there is no direct compatibility.
 */
public abstract class MMM_ModelMultiBase extends MMM_ModelBase implements MMM_IModelCaps {

	public float heldItem[] = new float[] {0.0F, 0.0F};
	public boolean aimedBow;
	public boolean isSneak;
	public boolean isWait;
	
	public MMM_ModelRenderer mainFrame;
	public MMM_ModelRenderer HeadMount;
	public MMM_ModelRenderer HeadTop;
	public MMM_ModelRenderer Arms[];
	public MMM_ModelRenderer HardPoint[];
	
	public float entityIdFactor;
	public int entityTicksExisted;
	// 変数である意味ない？, No meaning is a variable?
	public float scaleFactor = 0.9375F;
	/**
	 * モデルが持っている機能群, Function group model has
	 */
	private final Map<String, Integer> fcapsmap = new HashMap<String, Integer>() {{
		put("onGround",			caps_onGround);
		put("isRiding",			caps_isRiding);
		put("isSneak",			caps_isSneak);
		put("isWait",			caps_isWait);
		put("isChild",			caps_isChild);
		put("heldItemLeft",		caps_heldItemLeft);
		put("heldItemRight",	caps_heldItemRight);
		put("aimedBow",			caps_aimedBow);
		put("ScaleFactor", 		caps_ScaleFactor);
		put("entityIdFactor",	caps_entityIdFactor);
		put("dominantArm",	caps_dominantArm);
	}};



	public MMM_ModelMultiBase() {
		this(0.0F);
	}

	public MMM_ModelMultiBase(float pSizeAdjust) {
		this(pSizeAdjust, 0.0F, 64, 32);
	}

	public MMM_ModelMultiBase(float pSizeAdjust, float pYOffset, int pTextureWidth, int pTextureHeight) {
		isSneak = false;
		aimedBow = false;
		textureWidth = pTextureWidth;
		textureHeight = pTextureHeight;
		
		if (MMM_Helper.isClient) {
			// ハードポイント, Hardpoint
			Arms = new MMM_ModelRenderer[2];
			HeadMount = new MMM_ModelRenderer(this, "HeadMount");
			HeadTop = new MMM_ModelRenderer(this, "HeadTop");
			
			initModel(pSizeAdjust, pYOffset);
		}
	}

	// 独自定義関数群, Own defined function group

	/**
	 * モデルの初期化コード
	 * Initialization code of the model
	 */
	public abstract void initModel(float psize, float pyoffset);

	/**
	 * アーマーモデルのサイズを返す。
	 * サイズは内側のものから。
	 * I return the size of the armor model.
	 * Size from the inner one.
	 */
	public abstract float[] getArmorModelsSize();

	/**
	 * モデル指定詞に依らずに使用するテクスチャパック名。
	 * 一つのテクスチャに複数のモデルを割り当てる時に使う。
	 * Texture pack name to be used regardless of the model specify that.
	 * Used when assigning multiple models in one texture.
	 * @return
	 */
	public String getUsingTexture() {
		return null;
	}

	/**
	 *  身長
	 *  Height
	 */
	@Deprecated
	public abstract float getHeight();
	/**
	 *  身長
	 *  Height
	 */
	public float getHeight(MMM_IModelCaps pEntityCaps) {
		return getHeight();
	}
	/**
	 * 横幅
	 * Width
	 */
	@Deprecated
	public abstract float getWidth();
	/**
	 * 横幅
	 * Width
	 */
	public float getWidth(MMM_IModelCaps pEntityCaps) {
		return getWidth();
	}
	/**
	 * モデルのYオフセット
	 * Y offset of the model
	 */
	@Deprecated
	public abstract float getyOffset();
	/**
	 * モデルのYオフセット
	 * Y offset of the model
	 */
	public float getyOffset(MMM_IModelCaps pEntityCaps) {
		return getyOffset();
	}
	/**
	 * 上に乗せる時のオフセット高
	 * Offset high time to put on
	 */
	@Deprecated
	public abstract float getMountedYOffset();
	/**
	 * 上に乗せる時のオフセット高
	 * Offset high time to put on
	 */
	public float getMountedYOffset(MMM_IModelCaps pEntityCaps) {
		return getMountedYOffset();
	}

	/**
	 * ロープの取り付け位置調整用
	 * Mounting position adjustment of the rope
	 * @return
	 */
	public float getLeashOffset(MMM_IModelCaps pEntityCaps) {
		return 0.4F;
	}

	/**
	 * アイテムを持っているときに手を前に出すかどうか。
	 * Whether out before hand when you have the item.
	 */
	@Deprecated
	public boolean isItemHolder() {
		return false;
	}
	/**
	 * アイテムを持っているときに手を前に出すかどうか。
	 * Whether out before hand when you have the item.
	 */
	public boolean isItemHolder(MMM_IModelCaps pEntityCaps) {
		return isItemHolder();
	}

	/**
	 * 表示すべきすべての部品
	 * All the parts to be displayed
	 */
	@Deprecated
	public void showAllParts() {
	}
	/**
	 * 表示すべきすべての部品
	 * All the parts to be displayed
	 */
	public void showAllParts(MMM_IModelCaps pEntityCaps) {
		showAllParts();
	}

	/**
	 * 部位ごとの装甲表示。
	 * Armor display of each part.
	 * @param parts
	 * 3:頭部。
	 * 2:胴部。
	 * 1:脚部
	 * 0:足部
	 * 3: head.
	 * 2: the body portion.
	 * 1: leg
	 * 0: foot
	 * @param index
	 * 0:inner
	 * 1:outer
	 * @return
	 * 戻り値は基本 -1
	 * The return value is -1 basic
	 */
	public int showArmorParts(int parts, int index) {
		return -1;
	}

	/**
	 * ハードポイントに接続されたアイテムを表示する
	 * I want to display the items that have been connected to the hard point
	 */
	public abstract void renderItems(MMM_IModelCaps pEntityCaps);

	public abstract void renderFirstPersonHand(MMM_IModelCaps pEntityCaps);



	// IModelCaps

	@Override
	public Map<String, Integer> getModelCaps() {
		return fcapsmap;
	}

	@Override
	public Object getCapsValue(int pIndex, Object ...pArg) {
		switch (pIndex) {
		case caps_onGround:
			return onGrounds;
		case caps_isRiding:
			return isRiding;
		case caps_isSneak:
			return isSneak;
		case caps_isWait:
			return isWait;
		case caps_isChild:
			return isChild;
		case caps_heldItemLeft:
			return heldItem[1];
		case caps_heldItemRight:
			return heldItem[0];
		case caps_aimedBow:
			return aimedBow;
		case caps_entityIdFactor:
			return entityIdFactor;
		case caps_ticksExisted:
			return entityTicksExisted;
		case caps_ScaleFactor:
			return scaleFactor;
		case caps_dominantArm:
			return dominantArm;
		}
		return null;
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_onGround:
			for (int li = 0; li < onGrounds.length && li < pArg.length; li++) {
				onGrounds[li] = (Float)pArg[li];
			}
			return true;
		case caps_isRiding:
			isRiding = (Boolean)pArg[0];
			return true;
		case caps_isSneak:
			isSneak = (Boolean)pArg[0];
			return true;
		case caps_isWait:
			isWait = (Boolean)pArg[0];
			return true;
		case caps_isChild:
			isChild = (Boolean)pArg[0];
			return true;
		case caps_heldItemLeft:
			heldItem[1] = (Integer)pArg[0];
			return true;
		case caps_heldItemRight:
			heldItem[0] = (Integer)pArg[0];
			return true;
		case caps_aimedBow:
			aimedBow = (Boolean)pArg[0];
			return true;
		case caps_entityIdFactor:
			entityIdFactor = (Float)pArg[0];
			return true;
		case caps_ticksExisted:
			entityTicksExisted = (Integer)pArg[0];
			return true;
		case caps_ScaleFactor:
			scaleFactor = (Float)pArg[0];
			return true;
		case caps_dominantArm:
			dominantArm = (Integer)pArg[0];
			return true;
		}
		
		return false;
	}

}
