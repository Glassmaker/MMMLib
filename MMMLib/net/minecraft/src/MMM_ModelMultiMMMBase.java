package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

/**
 * MMMの実験コードを含む部分。
 * ModelMultiBaseに追加するに足りるかをここで実験。
 * このクラスにある機能は予告なく削除される恐れが有るためご留意下さい。
 */
public abstract class MMM_ModelMultiMMMBase extends MMM_ModelMultiBase {

	public Map<String, MMM_EquippedStabilizer> stabiliser;

	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float onGround;
	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float heldItemLeft;
	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float heldItemRight;


	public MMM_ModelMultiMMMBase() {
		super();
	}
	public MMM_ModelMultiMMMBase(float pSizeAdjust) {
		super(pSizeAdjust);
	}
	public MMM_ModelMultiMMMBase(float pSizeAdjust, float pYOffset, int pTextureWidth, int pTextureHeight) {
		super(pSizeAdjust, pYOffset, pTextureWidth, pTextureHeight);
	}

	/**
	 * mainFrameに全てぶら下がっているならば標準で描画する。
	 */
	@Override
	public void render(MMM_IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
		setRotationAngles(par2, par3, ticksExisted, pheadYaw, pheadPitch, par7, pEntityCaps);
		mainFrame.render(par7, pIsRender);
		renderStabilizer(pEntityCaps, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
	}

	/**
	 * 通常のレンダリング前に呼ばれる。
	 * @return falseを返すと通常のレンダリングをスキップする。
	 */
	public boolean preRender(float par2, float par3,
			float par4, float par5, float par6, float par7) {
		return true;
	}

	/**
	 * 通常のレンダリング後に呼ぶ。 基本的に装飾品などの自律運動しないパーツの描画用。
	 */
	public void renderExtention(float par2, float par3,
			float par4, float par5, float par6, float par7) {
	}

	/**
	 * スタビライザーの描画。 自動では呼ばれないのでrender内で呼ぶ必要があります。
	 */
	protected void renderStabilizer(MMM_IModelCaps pEntityCaps, float par2, float par3,
			float ticksExisted, float pheadYaw, float pheadPitch, float par7) {
		// スタビライザーの描画、doRenderの方がいいか？
		if (stabiliser == null || stabiliser.isEmpty() || render == null)
			return;

		GL11.glPushMatrix();
		for (Entry<String, MMM_EquippedStabilizer> le : stabiliser.entrySet()) {
			MMM_EquippedStabilizer les = le.getValue();
			if (les != null && les.equipPoint != null) {
				MMM_ModelStabilizerBase lsb = les.stabilizer;
				if (lsb.isLoadAnotherTexture()) {
					MMM_Client.setTexture(lsb.getTexture());
				}
				les.equipPoint.loadMatrix();
				lsb.render(this, null, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
			}
		}
		GL11.glPopMatrix();
	}

	/**
	 * モデル切替時に実行されるコード
	 * @param pEntityCaps
	 * Entityの値を操作するためのModelCaps。
	 */
	public void changeModel(MMM_IModelCaps pEntityCaps) {
		// カウンタ系の加算値、リミット値の設定など行う予定。
	}

	/**
	 * 初期ロード時に実行
	 */
	public void buildTexture() {
		
	}

	public void setDefaultPause() {
	}

	public void setDefaultPause(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, MMM_IModelCaps pEntityCaps) {
		setDefaultPause();
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_changeModel:
			changeModel((MMM_IModelCaps)pArg[0]);
			return true;
		case caps_renderFace:
			renderFace((MMM_IModelCaps)pArg[0], (Float)pArg[1], (Float)pArg[2], (Float)pArg[3],
				(Float)pArg[4], (Float)pArg[5], (Float)pArg[6], (Boolean)pArg[7]);
			return true;
		case caps_renderBody:
			renderBody((MMM_IModelCaps)pArg[0], (Float)pArg[1], (Float)pArg[2], (Float)pArg[3],
				(Float)pArg[4], (Float)pArg[5], (Float)pArg[6], (Boolean)pArg[7]);
			return true;
		}
		return super.setCapsValue(pIndex, pArg);
	}

	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_setFaceTexture:
			return setFaceTexture((Integer)pArg[0]);
		case caps_textureLightColor:
			return getTextureLightColor((MMM_IModelCaps)pArg[0]);
		}
		return super.getCapsValue(pIndex, pArg);
	}

	// Actors実験区画
	// このへん未だ未整理
	public void renderFace(MMM_IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}
	public void renderBody(MMM_IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}
	/**
	 * 表情をテクスチャのUVマップを変えることで表現
	 * @param pIndex
	 */
	public int setFaceTexture(int pIndex) {
		// u = (int)(pIndex % 2) * 32 / 64
		// v = (int)(pIndex / 2) * 32 / 32
		GL11.glTranslatef(((pIndex & 0x01) * 32) / textureWidth, (((pIndex >>> 1) & 0x01) * 16) / textureHeight , 0F);
		return pIndex / 4;
	}

	public float[] getTextureLightColor(MMM_IModelCaps pEntityCaps) {
		return null;
	}

}
