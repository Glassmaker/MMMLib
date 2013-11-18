package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

/**
 * MMMの実験コードを含む部分。
 * ModelMultiBaseに追加するに足りるかをここで実験。
 * このクラスにある機能は予告なく削除される恐れが有るためご留意下さい。
 * Part including the experimental code of MMM.
 * The experiment here or sufficient to add to ModelMultiBase.
 * Please note that because there is a possibility that the function in this class will be removed without notice.
 */
public abstract class MMM_ModelMultiMMMBase extends MMM_ModelMultiBase {

	public Map<String, MMM_EquippedStabilizer> stabiliser;

	/**
	 * 削除予定変数使わないで下さい。
	 * Please do not use variable deletion.
	 */
	@Deprecated
	public float onGround;
	/**
	 * 削除予定変数使わないで下さい。
	 * Please do not use variable deletion.
	 */
	@Deprecated
	public float heldItemLeft;
	/**
	 * 削除予定変数使わないで下さい。
	 * Please do not use variable deletion.
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
	 * to be rendered in standard if you are hanging all mainFrame.
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
	 * It is called before rendering normal.
	 * @return falseを返すと通常のレンダリングをスキップする。
	 * Skip the normal rendering If you return false.
	 */
	public boolean preRender(float par2, float par3,
			float par4, float par5, float par6, float par7) {
		return true;
	}

	/**
	 * 通常のレンダリング後に呼ぶ。 基本的に装飾品などの自律運動しないパーツの描画用。
	 * I call after rendering normal. 
	 * Drawing of parts that do not autonomous movement, such as ornaments basically.
	 */
	public void renderExtention(float par2, float par3,
			float par4, float par5, float par6, float par7) {
	}

	/**
	 * スタビライザーの描画。 自動では呼ばれないのでrender内で呼ぶ必要があります。
	 * Drawing of the stabilizer. There is a need to call in the render inside is not called automatically.
	 */
	protected void renderStabilizer(MMM_IModelCaps pEntityCaps, float par2, float par3,
			float ticksExisted, float pheadYaw, float pheadPitch, float par7) {
		// スタビライザーの描画、doRenderの方がいいか？, Drawing of the stabilizer, who doRender or good?
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
	 * ode to be executed when switching model
	 * @param pEntityCaps
	 * Entityの値を操作するためのModelCaps。
	 * ModelCaps to manipulate the value of the Entity.
	 */
	public void changeModel(MMM_IModelCaps pEntityCaps) {
		// カウンタ系の加算値、リミット値の設定など行う予定。
		// Plans to add the value of the counter system, such as setting limits.
	}

	/**
	 * 初期ロード時に実行
	 * Run the initial load time
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

	// Actors実験区画, Actors experiment compartment
	// このへん未だ未整理, Unorganized still around here
	public void renderFace(MMM_IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}
	public void renderBody(MMM_IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
	}
	/**
	 * 表情をテクスチャのUVマップを変えることで表現
	 * Expression by changing the UV map the texture look
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
