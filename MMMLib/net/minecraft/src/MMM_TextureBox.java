package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MMM_TextureBox extends MMM_TextureBoxBase {

	/**
	 * �e�N�X�`���p�b�N�̖��́A���f���w�莌�̑O�܂ł̕�����B
	 */
	public String packegeName;
	/**
	 * �e�N�X�`���t�@�C���̃t�@�C�������X�g�B
	 */
	public Map<Integer, ResourceLocation> textures;
	/**
	 * �A�[�}�[�t�@�C���̃t�@�C�������X�g�B
	 */
	public Map<String, Map<Integer, ResourceLocation>> armors;
	/**
	 * ���f���w�莌
	 */
	public String modelName;
	/**
	 * �}���`���f���N���X
	 */
	public MMM_ModelMultiBase[] models;
	/**
	 * pName, pTextureDir, pClassPrefix
	 */
	public String[] textureDir;
	/**
	 * �e�N�X�`���̊i�[����Ă���p�b�N�̖��O�i���f���Ɋ֌W�Ȃ��j
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
	 * �e�N�X�`���̃t���p�X��Ԃ��B
	 * �o�^�C���f�b�N�X�������ꍇ��NULL��Ԃ��B
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
		// index��0x40,0x50�ԑ�
		// light���ǉ�
		if (armors.isEmpty() || itemstack == null) return null;
		if (!(itemstack.getItem() instanceof ItemArmor)) return null;
		
		int l = 0;
		if (itemstack.getMaxDamage() > 0) {
			l = (10 * itemstack.getItemDamage() / itemstack.getMaxDamage());
		}
		return getArmorTextureName(pIndex, MMM_TextureManager.armorFilenamePrefix[((ItemArmor)itemstack.getItem()).renderIndex], l);
	}
	public ResourceLocation getArmorTextureName(int pIndex, String pArmorPrefix, int pDamage) {
		// index��0x40,0x50�ԑ�
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
	 * �_��F�̗L�����r�b�g�z��ɂ��ĕԂ�
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
	 * �쐶�F�̗L�����r�b�g�z��ɂ��ĕԂ�
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
