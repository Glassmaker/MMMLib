package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;

public class mod_MMM_MMMLib extends BaseMod {

	public static final String Revision = "6";
	
	public static String[] cfg_comment = {
		"cfg_renderHacking = Override RenderItem.",
		"cfg_startVehicleEntityID = starting auto assigned ID.",
		"cfg_isModelAlphaBlend = true: AlphaBlend(request power), false: AlphaTest(more fast)"
	};
//	@MLProp()
	public static boolean cfg_isDebugView = false;
//	@MLProp()
	public static boolean cfg_isDebugMessage = true;
//	@MLProp(info = "Override RenderItem.")
	public static boolean cfg_renderHacking = true;
//	@MLProp(info = "starting auto assigned ID.")
	public static int cfg_startVehicleEntityID = 2176;
//	@MLProp(info="true: AlphaBlend(request power), false: AlphaTest(more fast)")
	public static boolean cfg_isModelAlphaBlend = true;



	public static void Debug(String pText, Object... pVals) {
		// ・ｽf・ｽo・ｽb・ｽO・ｽ・ｽ・ｽb・ｽZ・ｽ[・ｽW
		if (cfg_isDebugMessage) {
			System.out.println(String.format("MMMLib-" + pText, pVals));
		}
	}

	@Override
	public String getName() {
		return "MMMLib";
	}

	@Override
	public String getVersion() {
		return "1.6.2-" + Revision;
	}
	
	@Override
	public String getPriorities() {
		return MMM_Helper.isForge ? "befor-all" : "before:*";
	}

	@Override
	public void load() {
		// ・ｽ・ｽ・ｽ・ｽ
		Debug(MMM_Helper.isClient ? "Client" : "Server");
		Debug(MMM_Helper.isForge ? "Forge" : "Modloader");
		MMM_FileManager.init();
		MMM_Config.init();
		MMM_Config.checkConfig(mod_MMM_MMMLib.class);
		MMM_TextureManager.instance.init();
		MMM_StabilizerManager.init();
		if (MMM_Helper.isClient) {
			MMM_Client.init();
		}
		ModLoader.setInGameHook(this, true, true);
		if (cfg_isDebugView) {
			MMM_EntityDummy.isEnable = true;
		}
		
		// ・ｽﾆ趣ｿｽ・ｽp・ｽP・ｽb・ｽg・ｽp・ｽ`・ｽ・ｽ・ｽ・ｽ・ｽl・ｽ・ｽ
		ModLoader.registerPacketChannel(this, "MMM|Upd");
		
		// Forge・ｽg・ｽp・ｽ・ｽ・ｽﾍ厄ｿｽ・ｽ・ｽ
		cfg_renderHacking &= !MMM_Helper.isForge;
	}

	@Override
	public void modsLoaded() {
		// ・ｽo・ｽC・ｽI・ｽ[・ｽ・ｽ・ｽﾉ設定さ・ｽ黷ｽ・ｽX・ｽ|・ｽ[・ｽ・ｽ・ｽ・ｽ・ｽ・ｽu・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
		MMM_Helper.replaceBaiomeSpawn();
		
		// ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌ構・ｽz
		MMM_TextureManager.instance.loadTextures();
		// ・ｽ・ｽ・ｽ[・ｽh
		if (MMM_Helper.isClient) {
			// ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌ構・ｽz
//			MMM_TextureManager.loadTextures();
			MMM_StabilizerManager.loadStabilizer();
			// ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽC・ｽ・ｽ・ｽf・ｽb・ｽN・ｽX・ｽﾌ構・ｽz
			Debug("Localmode: InitTextureList.");
			MMM_TextureManager.instance.initTextureList(true);
		} else {
			MMM_TextureManager.instance.loadTextureServer();
		}
		
	}

	@Override
	public void addRenderer(Map var1) {
		if (cfg_isDebugView) {
			var1.put(net.minecraft.src.MMM_EntityDummy.class, new MMM_RenderDummy());
		}
		// RenderItem
		if (cfg_renderHacking && MMM_Helper.isClient) {
			var1.put(EntityItem.class, new MMM_RenderItem());
		}
		// RenderSelect
		var1.put(MMM_EntitySelect.class, new MMM_RenderModelMulti(0.0F));
	}

	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		if (cfg_isDebugView && MMM_Helper.isClient) {
			// ・ｽ_・ｽ~・ｽ[・ｽ}・ｽ[・ｽJ・ｽ[・ｽﾌ表・ｽ・ｽ・ｽp・ｽ・ｽ・ｽ・ｽ
			if (var2.theWorld != null && var2.thePlayer != null) {
				try {
					for (Iterator<MMM_EntityDummy> li = MMM_EntityDummy.appendList.iterator(); li.hasNext();) {
						var2.theWorld.spawnEntityInWorld(li.next());
						li.remove();
					}
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		}
		
		// ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ_・ｽ[・ｽ・ｽ・ｽI・ｽ[・ｽo・ｽ[・ｽ・ｽ・ｽC・ｽh
		if (cfg_renderHacking && MMM_Helper.isClient) {
			MMM_Client.setItemRenderer();
		}
		
		// ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾇ暦ｿｽ・ｽp
		MMM_TextureManager.instance.onUpdate();
		
		return true;
	}

	@Override
	public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// ・ｽT・ｽ[・ｽo・ｽ・ｽ・ｽﾌ難ｿｽ・ｽ・ｽ
		byte lmode = var2.data[0];
		int leid = 0;
		Entity lentity = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lentity = MMM_Helper.getEntity(var2.data, 1, var1.playerEntity.worldObj);
			if (lentity == null) return;
		}
		Debug("MMM|Upd Srv Call[%2x:%d].", lmode, leid);
		byte[] ldata;
		
		switch (lmode) {
		case MMM_Statics.Server_SetTexturePackIndex:
			// ・ｽT・ｽ[・ｽo・ｽ[・ｽ・ｽ・ｽ・ｽEntity・ｽﾉ対ゑｿｽ・ｽﾄテ・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽC・ｽ・ｽ・ｽf・ｽb・ｽN・ｽX・ｽ・ｽﾝ定す・ｽ・ｽ
			MMM_TextureManager.instance.reciveFromClientSetTexturePackIndex(lentity, var2.data);
			break;
		case MMM_Statics.Server_GetTextureIndex:
			// ・ｽT・ｽ[・ｽo・ｽ[・ｽ・ｽ・ｽﾅの管暦ｿｽ・ｽﾔ搾ｿｽ・ｽﾌ問い・ｽ・ｽ・ｽ墲ｹ・ｽﾉ対ゑｿｽ・ｽﾄ会ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
			MMM_TextureManager.instance.reciveFromClientGetTexturePackIndex(var1, var2.data);
			break;
		case MMM_Statics.Server_GetTexturePackName:
			// ・ｽﾇ暦ｿｽ・ｽﾔ搾ｿｽ・ｽﾉ対会ｿｽ・ｽ・ｽ・ｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽ・ｽ・ｽ・ｽﾔゑｿｽ・ｽB
			MMM_TextureManager.instance.reciveFromClientGetTexturePackName(var1, var2.data);
			break;
		}
	}

	public static void sendToClient(NetServerHandler pHandler, byte[] pData) {
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("MMM|Upd", pData));
	}

	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		MMM_Client.clientCustomPayload(var1, var2);
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		MMM_Client.clientConnect(var1);
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		MMM_Client.clientDisconnect(var1);
	}

	// Forge
	public void serverDisconnect() {
		MMM_TextureManager.instance.saveTextureServer();
	}

}
