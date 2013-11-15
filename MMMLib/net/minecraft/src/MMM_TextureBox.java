package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MMM_TextureBox extends MMM_TextureBoxBase {

	/**
	 * ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌ厄ｿｽ・ｽﾌ、・ｽ・ｽ・ｽf・ｽ・ｽ・ｽw・ｽ闔鯉ｿｽﾌ前・ｽﾜでの包ｿｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public String packegeName;
	/**
	 * ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽﾌフ・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽX・ｽg・ｽB
	 */
	public Map<Integer, ResourceLocation> textures;
	/**
	 * ・ｽA・ｽ[・ｽ}・ｽ[・ｽt・ｽ@・ｽC・ｽ・ｽ・ｽﾌフ・ｽ@・ｽC・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽX・ｽg・ｽB
	 */
	public Map<String, Map<Integer, ResourceLocation>> armors;
	/**
	 * ・ｽ・ｽ・ｽf・ｽ・ｽ・ｽw・ｽ闔・
	 */
	public String modelName;
	/**
	 * ・ｽ}・ｽ・ｽ・ｽ`・ｽ・ｽ・ｽf・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX
	 */
	public MMM_ModelMultiBase[] models;
	/**
	 * pName, pTextureDir, pClassPrefix
	 */
	public String[] textureDir;
	/**
	 * ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ格・ｽ[・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽp・ｽb・ｽN・ｽﾌ厄ｿｽ・ｽO・ｽi・ｽ・ｽ・ｽf・ｽ・ｽ・ｽﾉ関係・ｽﾈゑｿｽ・ｽj
	 */
	public String fileName;



	public MMM_TextureBox() {
		textures = new HashMap<Integer, ResourceLocation>();
		armors = new TreeMap<String, Map<Integer, ResourceLocation>>();
		modelHeight = modelWidth = modelYOffset = modelMountedYOffset = 0.0F;
		contractColor = -1;
		wildColor = -1;
	}

	public MMM_TextureBox(String pTextureName, String[] pSearch) {
		this();
		textureName = pTextureName;
		fileName = pTextureName;
		int li = pTextureName.lastIndexOf("_");
		if (li > -1) {
			packegeName = pTextureName.substring(0, li);
			modelName = pTextureName.substring(li + 1);
		} else {
			packegeName = pTextureName;
			modelName = "";
		}
		textureDir = pSearch;
	}

	public void setModels(String pModelName, MMM_ModelMultiBase[] pModels, MMM_ModelMultiBase[] pDefModels) {
		modelName = pModelName;
		models = pModels == null ? pDefModels : pModels;
		textureName = (new StringBuilder()).append(packegeName).append("_").append(modelName).toString();
		isUpdateSize = (models != null && models[0] != null) ? MMM_ModelCapsHelper.getCapsValueBoolean(models[0], MMM_IModelCaps.caps_isUpdateSize) : false;
	}

	/**
	 * ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌフ・ｽ・ｽ・ｽp・ｽX・ｽ・ｽﾔゑｿｽ・ｽB
	 * ・ｽo・ｽ^・ｽC・ｽ・ｽ・ｽf・ｽb・ｽN・ｽX・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ鼾・ｿｽ・ｽNULL・ｽ・ｽﾔゑｿｽ・ｽB
	 */
	public ResourceLocation getTextureName(int pIndex) {
		if (textures.containsKey(pIndex)) {
			return textures.get(pIndex);
		} else if (pIndex >= MMM_TextureManager.tx_eyecontract && pIndex < (16 + MMM_TextureManager.tx_eyecontract)) {
			return getTextureName(MMM_TextureManager.tx_oldeye);
		} else if (pIndex >= MMM_TextureManager.tx_eyewild && pIndex < (16 + MMM_TextureManager.tx_eyewild)) {
			return getTextureName(MMM_TextureManager.tx_oldeye);
		}
		return null;
	}

	public ResourceLocation getArmorTextureName(int pIndex, ItemStack itemstack) {
		// index・ｽ・ｽ0x40,0x50・ｽﾔ托ｿｽ
		// light・ｽ・ｽ・ｽﾇ会ｿｽ
		if (armors.isEmpty() || itemstack == null) return null;
		if (!(itemstack.getItem() instanceof ItemArmor)) return null;
		
		int l = 0;
		if (itemstack.getMaxDamage() > 0) {
			l = (10 * itemstack.getItemDamage() / itemstack.getMaxDamage());
		}
		return getArmorTextureName(pIndex, MMM_TextureManager.armorFilenamePrefix[((ItemArmor)itemstack.getItem()).renderIndex], l);
	}
	public ResourceLocation getArmorTextureName(int pIndex, String pArmorPrefix, int pDamage) {
		// index・ｽ・ｽ0x40,0x50・ｽﾔ托ｿｽ
		if (armors.isEmpty() || pArmorPrefix == null) return null;
		
		Map<Integer, ResourceLocation> m = armors.get(pArmorPrefix);
		if (m == null) {
			m = armors.get("default");
			if (m == null) {
//				return null;
				m = (Map)armors.values().toArray()[0];
			}
		}
		ResourceLocation ls = null;
//		int lindex = pInner ? MMM_TextureManager.tx_armor1 : MMM_TextureManager.tx_armor2;
		for (int i = pIndex + pDamage; i >= pIndex; i--) {
			ls = m.get(i);
			if (ls != null) break;
		}
		return ls;
	}

	/**
	 * ・ｽ_・ｽ・ｽF・ｽﾌ有・ｽ・ｽ・ｽ・ｽ・ｽr・ｽb・ｽg・ｽz・ｽ・ｽﾉゑｿｽ・ｽﾄ返ゑｿｽ
	 */
	@Override
	public int getContractColorBits() {
		if (contractColor == -1) {
			int li = 0;
			for (Integer i : textures.keySet()) {
				if (i >= 0x00 && i <= 0x0f) {
					li |= 1 << (i & 0x0f);
				}
			}
			contractColor = li;
		}
		return contractColor;
	}
	/**
	 * ・ｽ・ｶ・ｽF・ｽﾌ有・ｽ・ｽ・ｽ・ｽ・ｽr・ｽb・ｽg・ｽz・ｽ・ｽﾉゑｿｽ・ｽﾄ返ゑｿｽ
	 */
	@Override
	public int getWildColorBits() {
		if (wildColor == -1) {
			int li = 0;
			for (Integer i : textures.keySet()) {
				if (i >= MMM_TextureManager.tx_wild && i <= (MMM_TextureManager.tx_wild + 0x0f)) {
					li |= 1 << (i & 0x0f);
				}
			}
			wildColor = li;
		}
		return wildColor;
	}

	public boolean hasColor(int pIndex) {
		return textures.containsKey(pIndex);
	}

	public boolean hasColor(int pIndex, boolean pContract) {
		return textures.containsKey(pIndex + (pContract ? 0 : MMM_TextureManager.tx_wild));
	}

	public boolean hasArmor() {
		return !armors.isEmpty();
	}

	@Override
	public float getHeight(MMM_IModelCaps pEntityCaps) {
		return models != null ? models[0].getHeight(pEntityCaps) : modelHeight;
	}

	@Override
	public float getWidth(MMM_IModelCaps pEntityCaps) {
		return models != null ? models[0].getWidth(pEntityCaps) : modelWidth;
	}

	@Override
	public float getYOffset(MMM_IModelCaps pEntityCaps) {
		return models != null ? models[0].getyOffset(pEntityCaps) : modelYOffset;
	}

	@Override
	public float getMountedYOffset(MMM_IModelCaps pEntityCaps) {
		return models != null ? models[0].getMountedYOffset(pEntityCaps) : modelMountedYOffset;
	}

	public MMM_TextureBox duplicate() {
		MMM_TextureBox lbox = new MMM_TextureBox();
		lbox.textureName = textureName;
		lbox.packegeName = packegeName;
		lbox.fileName = fileName;
		lbox.modelName = modelName;
		lbox.textureDir = textureDir;
		lbox.textures = textures;
		lbox.armors = armors;
		lbox.models = models;
		lbox.isUpdateSize = lbox.isUpdateSize;
		
		return lbox;
	}

	public boolean addTexture(int pIndex, String pLocation) {
		String ls;
		ls = "/assets/minecraft/";
		if (pLocation.startsWith(ls)) {
			pLocation = pLocation.substring(ls.length());
		} else {
//			pLocation = "../.." + pLocation;
		}
		boolean lflag = false;
		switch ((pIndex & 0xfff0)) {
		case MMM_TextureManager.tx_armor1:
		case MMM_TextureManager.tx_armor2:
		case MMM_TextureManager.tx_armor1light:
		case MMM_TextureManager.tx_armor2light:
		case MMM_TextureManager.tx_oldarmor1:
		case MMM_TextureManager.tx_oldarmor2:
			ls = pLocation.substring(pLocation.lastIndexOf("/") + 1, pLocation.lastIndexOf("_"));
			Map<Integer, ResourceLocation> lmap;
			if (armors.containsKey(ls)) {
				lmap = armors.get(ls);
			} else {
				lmap = new HashMap<Integer, ResourceLocation>();
				armors.put(ls, lmap);
			}
			lmap.put(pIndex, new ResourceLocation(pLocation));
			break;
			
		default:
			textures.put(pIndex, new ResourceLocation(pLocation));
			return true;
		}
		return lflag;
	}

}
