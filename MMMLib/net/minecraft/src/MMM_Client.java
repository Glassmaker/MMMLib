package net.minecraft.src;

import static net.minecraft.src.mod_MMM_MMMLib.Debug;

public class MMM_Client {

	public static MMM_ItemRenderer itemRenderer;


	public static void setItemRenderer() {
		if (itemRenderer == null) {
			itemRenderer = new MMM_ItemRenderer(MMM_Helper.mc);
		}
		if (!(MMM_Helper.mc.entityRenderer.itemRenderer instanceof MMM_ItemRenderer)) {
			mod_MMM_MMMLib.Debug("replace entityRenderer.itemRenderer.");
			MMM_Helper.mc.entityRenderer.itemRenderer = itemRenderer;
		}
		if (!(RenderManager.instance.itemRenderer instanceof MMM_ItemRenderer)) {
			mod_MMM_MMMLib.Debug("replace RenderManager.itemRenderer.");
			RenderManager.instance.itemRenderer = itemRenderer;
		}
		// GUI�̕\����ς���ɂ͏펞�Ď����K�v�H
	}

	public static void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// �N���C�A���g���̓���p�P�b�g��M����
		byte lmode = var2.data[0];
		int leid = 0;
		Entity lentity = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lentity = MMM_Helper.getEntity(var2.data, 1, MMM_Helper.mc.theWorld);
			if (lentity == null) return;
		}
		Debug("MMM|Upd Clt Call[%2x:%d].", lmode, leid);
		
		switch (lmode) {
		case MMM_Statics.Client_SetTextureIndex:
			// �₢���킹���e�N�X�`���p�b�N�̊Ǘ��ԍ����󂯎��
			MMM_TextureManager.reciveFormServerSetTexturePackIndex(var2.data);
			break;
		case MMM_Statics.Client_SetTexturePackName:
			// �Ǘ��ԍ��ɓo�^����Ă���e�N�X�`���p�b�N�̏����󂯎��
			MMM_TextureManager.reciveFromServerSetTexturePackName(var2.data);
			break;
		}
	}

	public static void clientConnect(NetClientHandler var1) {
		if (MMM_Helper.mc.isIntegratedServerRunning()) {
//			Debug("Localmode: InitTextureList.");
//			MMM_TextureManager.initTextureList(true);
		} else {
			Debug("Remortmode: ClearTextureList.");
			MMM_TextureManager.initTextureList(false);
		}
	}

	public static void clientDisconnect(NetClientHandler var1) {
//		super.clientDisconnect(var1);
		Debug("Localmode: InitTextureList.");
		MMM_TextureManager.initTextureList(true);
	}

	public static void sendToServer(byte[] pData) {
		ModLoader.clientSendPacket(new Packet250CustomPayload("MMM|Upd", pData));
		Debug("MMM|Upd:%2x:NOEntity", pData[0]);
	}

	public static boolean isIntegratedServerRunning() {
		return MMM_Helper.mc.isIntegratedServerRunning();
	}

	public static void setArmorPrefix() {
		// �A�[�}�[�v���t�B�b�N�X��ݒ�
		try {
			ModLoader.setPrivateValue(RenderBiped.class, null, 4, ModLoader.getPrivateValue(RenderPlayer.class, null, 3));
		} catch (Exception e) {
		}
	}

}
