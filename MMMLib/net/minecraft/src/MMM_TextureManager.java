package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.DebugGraphics;

import org.bouncycastle.asn1.pkcs.Pfx;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class MMM_TextureManager {

	/**
	 * �p���N���X�Œu�������邱�Ƃ��l���B
	 */
	public static MMM_TextureManager instance = new MMM_TextureManager();
	
	public static String nameTextureIndex = "config/mod_MMM_textureList.cfg";
	public static String defaultModelName = "Orign";
	
	public static final int tx_oldwild		= 0x10; //16;
	public static final int tx_oldarmor1	= 0x11; //17;
	public static final int tx_oldarmor2	= 0x12; //18;
	public static final int tx_oldeye		= 0x13; //19;
	public static final int tx_gui			= 0x20; //32;
	public static final int tx_wild			= 0x30; //48;
	public static final int tx_armor1		= 0x40; //64;
	public static final int tx_armor2		= 0x50; //80;
	public static final int tx_eye			= 0x60; //96;
	public static final int tx_eyecontract	= 0x60; //96;
	public static final int tx_eyewild		= 0x70; //112;
	public static final int tx_armor1light	= 0x80; //128;
	public static final int tx_armor2light	= 0x90; //144;
	public static String[] armorFilenamePrefix;
	/**
	 * ���^�C�v�̃t�@�C����
	 */
	protected static String defNames[] = {
		"mob_littlemaid0.png", "mob_littlemaid1.png",
		"mob_littlemaid2.png", "mob_littlemaid3.png",
		"mob_littlemaid4.png", "mob_littlemaid5.png",
		"mob_littlemaid6.png", "mob_littlemaid7.png",
		"mob_littlemaid8.png", "mob_littlemaid9.png",
		"mob_littlemaida.png", "mob_littlemaidb.png",
		"mob_littlemaidc.png", "mob_littlemaidd.png",
		"mob_littlemaide.png", "mob_littlemaidf.png",
		"mob_littlemaidw.png",
		"mob_littlemaid_a00.png", "mob_littlemaid_a01.png"
	};
	
	/**
	 * ���[�J���ŕێ����Ă��郂�f���̃��X�g
	 */
	protected Map<String, MMM_ModelMultiBase[]> modelMap = new TreeMap<String, MMM_ModelMultiBase[]>();
	/**
	 * ���[�J���ŕێ����Ă���e�N�X�`���p�b�N
	 */
	protected List<MMM_TextureBox> textures = new ArrayList<MMM_TextureBox>();
	/**
	 * �T�[�o�[���ł̊Ǘ��ԍ������ʂ���̂Ɏg���A�N���C�A���g�p�B
	 */
	protected Map<MMM_TextureBox, Integer> textureServerIndex = new HashMap<MMM_TextureBox, Integer>();
	/**
	 * �T�[�o�[�E�N���C�A���g�ԂŃe�N�X�`���p�b�N�̖��̃��X�g�̓�������̂Ɏg���A�T�[�o�[�p�B
	 */
	protected List<MMM_TextureBoxServer> textureServer = new ArrayList<MMM_TextureBoxServer>();
	/**
	 * Entity���Ƀf�t�H���g�e�N�X�`�����Q�ƁB
	 * �\�z��@��EntityList���Q�Ƃ̂��ƁB
	 */
	protected Map<Class, MMM_TextureBox> defaultTextures = new HashMap<Class, MMM_TextureBox>();
	
	/**
	 * �N���C�A���g���Ŏg��
	 */
	protected String[] requestString = new String[] {
		null, null, null, null, null, null, null, null,
		null, null, null, null, null, null, null, null
	};
	protected int[] requestStringCounter = new int[] {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	protected int[] requestIndex = new int[] {
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	};
	protected int[] requestIndexCounter = new int[] {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	protected Map<MMM_ITextureEntity, int[]> stackGetTexturePack = new HashMap<MMM_ITextureEntity, int[]>();
	protected Map<MMM_ITextureEntity, Object[]> stackSetTexturePack = new HashMap<MMM_ITextureEntity, Object[]>();
	
	protected List<String[]> searchPrefix = new ArrayList<String[]>();



	protected void init() {
		// �����Ώۃt�@�C������o�^���܂��B
		// �p�^�[����o�^���Ȃ��ꍇ�A�Ǝ����̂�MOD�t�@�C���A�e�N�X�`���f�B���N�g���A�N���X���ǂݍ��܂�܂���B
		MMM_FileManager.getModFile("MMMLib", "MMMLib");
		MMM_FileManager.getModFile("MMMLib", "ModelMulti");
		addSearch("MMMLib", "/mob/ModelMulti/", "ModelMulti_");
		addSearch("MMMLib", "/mob/littleMaid/", "ModelLittleMaid_");
		addSearch("MMMLib", "/assets/minecraft/textures/entity/ModelMulti/", "ModelMulti_");
		addSearch("MMMLib", "/assets/minecraft/textures/entity/littleMaid/", "ModelLittleMaid_");

		MMM_FileManager.getModFile("littleMaidMob", "littleMaidMob");
		addSearch("littleMaidMob", "/mob/littleMaid/", "ModelLittleMaid_");
		addSearch("littleMaidMob", "/assets/minecraft/textures/entity/littleMaid/", "ModelLittleMaid_");
	}

	protected String[] getSearch(String pName) {
		for (String[] lss : searchPrefix) {
			if (lss[0].equals(pName)) {
				return lss;
			}
		}
		return null;
	}

	/**
	 * �ǉ��ΏۂƂȂ錟���Ώۃt�@�C���Q�Ƃ��ꂼ��̌����������ݒ肷��B
	 */
	public void addSearch(String pName, String pTextureDir, String pClassPrefix) {
		searchPrefix.add(new String[] {pName, pTextureDir, pClassPrefix});
	}

	/**
	 * �e�N�X�`�����̂̈�v���镨��Ԃ��B
	 */
	public MMM_TextureBox getTextureBox(String pName) {
		for (MMM_TextureBox ltb : textures) {
			if (ltb.textureName.equals(pName)) {
				return ltb;
			}
		}
		return null;
	}

	/**
	 * �n���ꂽTextureBoxBase�𔻒肵��TextureBox��Ԃ��B
	 * @param pBoxBase
	 * @return
	 */
	public MMM_TextureBox getTextureBox(MMM_TextureBoxBase pBoxBase) {
		if (pBoxBase instanceof MMM_TextureBox) {
			return (MMM_TextureBox)pBoxBase;
		} else if (pBoxBase instanceof MMM_TextureBoxServer) {
			return getTextureBox(pBoxBase.textureName);
		}
		return null;
	}

	public MMM_TextureBoxServer getTextureBoxServer(String pName) {
		for (MMM_TextureBoxServer lbox : textureServer) {
			if (lbox.textureName.equals(pName)) {
				return lbox;
			}
		}
		return null;
	}

	public MMM_TextureBoxServer getTextureBoxServer(int pIndex) {
//		mod_MMM_MMMLib.Debug("getTextureBoxServer: %d / %d", pIndex, textureServer.size());
		if (textureServer.size() > pIndex) {
			return textureServer.get(pIndex);
		}
		return null;
	}

	protected void getArmorPrefix() {
		// �A�[�}�[�t�@�C���̃v���t�B�b�N�X���l��
		try {
			armorFilenamePrefix = (String[])ModLoader.getPrivateValue(RenderBiped.class, null, 5);
			return;
		} catch (Exception e) {
		} catch (Error e) {
			e.printStackTrace();
		}
		armorFilenamePrefix = null;
	}


	public boolean loadTextures() {
		mod_MMM_MMMLib.Debug("loadTexturePacks.");
		// �A�[�}�[�̃t�@�C���������ʂ��邽�߂̕�������l������
		if (MMM_Helper.isClient) {
			getArmorPrefix();
		}
		
		// �t�@�C������͂��ăe�N�X�`����ǉ�
		// jar���̃e�N�X�`����ǉ�
		if (MMM_FileManager.minecraftJar == null) {
			mod_MMM_MMMLib.Debug("getTexture-append-jar-file not founded.");
		} else {
			for (String[] lss : searchPrefix) {
				mod_MMM_MMMLib.Debug("getTexture[%s:%s].", lss[0], lss[1]);
				addTexturesJar(MMM_FileManager.minecraftJar, lss);
			}
		}
		
		for (String[] lss : searchPrefix) {
			mod_MMM_MMMLib.Debug("getTexture[%s:%s].", lss[0], lss[1]);
			// mods
			for (File lf : MMM_FileManager.getFileList(lss[0])) {
				for (String[] lst : searchPrefix) {
					boolean lflag;
					if (lf.isDirectory()) {
						// �f�B���N�g��
						lflag = addTexturesDir(lf, lst);
					} else {
						// zip
						lflag = addTexturesZip(lf, lst);
					}
					mod_MMM_MMMLib.Debug("getTexture-append-%s-%s.", lf.getName(), lflag ? "done" : "fail");
				}
			}
		}
		
		// TODO:�����R�[�h
		buildCrafterTexture();
		
		// �e�N�X�`���p�b�P�[�W�Ƀ��f���N���X��R�t��
		MMM_ModelMultiBase[] ldm = modelMap.get(defaultModelName);
		if (ldm == null && !modelMap.isEmpty()) {
			ldm = (MMM_ModelMultiBase[])modelMap.values().toArray()[0];
		}
		for (MMM_TextureBox ltb : textures) {
			if (ltb.modelName.isEmpty()) {
				ltb.setModels(defaultModelName, null, ldm);
			} else {
				if (modelMap.containsKey(ltb.modelName)) {
					ltb.setModels(ltb.modelName, modelMap.get(ltb.modelName), ldm);
				}
			}
		}
		for (Entry<String, MMM_ModelMultiBase[]> le : modelMap.entrySet()) {
			String ls = le.getValue()[0].getUsingTexture();
			if (ls != null) {
				if (getTextureBox(ls + "_" + le.getKey()) == null) {
					MMM_TextureBox lbox = null;
					for (MMM_TextureBox ltb : textures) {
						if (ltb.packegeName.equals(ls)) {
							lbox = ltb;
							break;
						}
					}
					if (lbox != null) {
						lbox = lbox.duplicate();
						lbox.setModels(le.getKey(), null, le.getValue());
						textures.add(lbox);
					}
				}
			}
		}
		mod_MMM_MMMLib.Debug("Loaded Texture Lists.(%d)", textures.size());
		for (MMM_TextureBox lbox : textures) {
			mod_MMM_MMMLib.Debug("texture: %s(%s) - hasModel:%b", lbox.textureName, lbox.fileName, lbox.models != null);
		}
		for (int li = textures.size() - 1; li >= 0; li--) {
			if (textures.get(li).models == null) {
				textures.remove(li);
			}
		}
		mod_MMM_MMMLib.Debug("Rebuild Texture Lists.(%d)", textures.size());
		for (MMM_TextureBox lbox : textures) {
			mod_MMM_MMMLib.Debug("texture: %s(%s) - hasModel:%b", lbox.textureName, lbox.fileName, lbox.models != null);
		}
		
		
		setDefaultTexture(EntityLivingBase.class, getTextureBox("default_" + defaultModelName));
		
		return false;
	}

	public void buildCrafterTexture() {
		// TODO:�����R�[�h�W�����f���e�N�X�`���ō\�z
		MMM_TextureBox lbox = new MMM_TextureBox("Crafter_Steve", new String[] {"", "", ""});
		lbox.fileName = "";
		
		lbox.addTexture(0x0c, "/assets/minecraft/textures/entity/steve.png");
		if (armorFilenamePrefix != null && armorFilenamePrefix.length > 0) {
			for (String ls : armorFilenamePrefix) {
				Map<Integer, ResourceLocation> lmap = new HashMap<Integer, ResourceLocation>();
				lmap.put(tx_armor1, new ResourceLocation((new StringBuilder()).append("textures/models/armor/").append(ls).append("_layer_2.png").toString()));
				lmap.put(tx_armor2, new ResourceLocation((new StringBuilder()).append("textures/models/armor/").append(ls).append("_layer_1.png").toString()));
				lbox.armors.put(ls, lmap);
			}
		}
		
		textures.add(lbox);
	}


	public boolean loadTextureServer() {
		// �T�[�o�[�p�e�N�X�`�����̂̃C���f�N�b�X���[�_�[
		// �悸�͎莝���̃e�N�X�`���p�b�N��ǉ�����B
		textureServer.clear();
		for (MMM_TextureBox lbox : textures) {
			textureServer.add(new MMM_TextureBoxServer(lbox));
		}
		// �t�@�C�����烍�[�h
		File lfile = MinecraftServer.getServer().getFile(nameTextureIndex);
		if (lfile.exists() && lfile.isFile()) {
			try {
				FileReader fr = new FileReader(lfile);
				BufferedReader br = new BufferedReader(fr);
				String ls;
				
				while ((ls = br.readLine()) != null) {
					String lt[] = ls.split(",");
					if (lt.length >= 7) {
						// �t�@�C���̂ق����D��
						MMM_TextureBoxServer lbox = getTextureBoxServer(lt[6]);
						if (lbox == null) {
							lbox = new MMM_TextureBoxServer();
							textureServer.add(lbox);
						}
						lbox.contractColor	= MMM_Helper.getHexToInt(lt[0]);
						lbox.wildColor		= MMM_Helper.getHexToInt(lt[1]);
						lbox.setModelSize(
								Float.valueOf(lt[2]),
								Float.valueOf(lt[3]),
								Float.valueOf(lt[4]),
								Float.valueOf(lt[5]));
						lbox.textureName	= lt[6];
					}
				}
				
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mod_MMM_MMMLib.Debug("Loaded ServerBoxList.(%d)", textureServer.size());
			for (int li = 0; li < textureServer.size(); li++) {
				MMM_TextureBoxServer lbox = textureServer.get(li);
				mod_MMM_MMMLib.Debug("%04d=%s:%04x:%04x", li, lbox.textureName, lbox.contractColor, lbox.wildColor);
			}
			return true;
		} else {
		}
		
		return false;
	}

	public void saveTextureServer() {
		// �T�[�o�[�p�e�N�X�`�����̂̃C���f�N�b�X�Z�[�o�[
		File lfile = MinecraftServer.getServer().getFile(nameTextureIndex);
		try {
			FileWriter fw = new FileWriter(lfile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (MMM_TextureBoxServer lbox : textureServer) {
				bw.write(String.format(
						"%04x,%04x,%f,%f,%f,%f,%s",
						lbox.getContractColorBits(),
						lbox.getWildColorBits(),
						lbox.getHeight(null),
						lbox.getWidth(null),
						lbox.getYOffset(null),
						lbox.getMountedYOffset(null),
						lbox.textureName));
				bw.newLine();
			}
			
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �e�N�X�`���C���f�b�N�X���\�z�B
	 */
	protected void initTextureList(boolean pFlag) {
		mod_MMM_MMMLib.Debug("Clear TextureBoxServer.");
		textureServerIndex.clear();
		textureServer.clear();
		if (pFlag) {
			int li = 0;
			for (MMM_TextureBox lbc : textures) {
				MMM_TextureBoxServer lbs = new MMM_TextureBoxServer(lbc);
				textureServer.add(lbs);
				textureServerIndex.put(lbc, li++);
			}
			mod_MMM_MMMLib.Debug("Rebuild TextureBoxServer(%d).", textureServer.size());
		}
	}

	/**
	 * �n���ꂽ���̂���͂���LMM�p�̃��f���N���X���ǂ����𔻒肷��B
	 * �uModelLittleMaid_�v�Ƃ��������񂪊܂܂�Ă��āA
	 * �uMMM_ModelBiped�v���p�����Ă���΃}���`���f���Ƃ��ăN���X��o�^����B
	 * @param fname
	 */
	protected void addModelClass(String fname, String[] pSearch) {
		// ���f����ǉ�
		int lfindprefix = fname.indexOf(pSearch[2]);
		if (lfindprefix > -1 && fname.endsWith(".class")) {
			String cn = fname.replace(".class", "");
			String pn = cn.substring(pSearch[2].length() + lfindprefix);
			
			if (modelMap.containsKey(pn)) return;
			
			ClassLoader lclassloader = mod_MMM_MMMLib.class.getClassLoader();
			Package lpackage = mod_MMM_MMMLib.class.getPackage();
			Class lclass;
			try {
				if (lpackage != null) {
					cn = (new StringBuilder(String.valueOf(lpackage.getName()))).append(".").append(cn).toString();
					lclass = lclassloader.loadClass(cn);
				} else {
					lclass = Class.forName(cn);
				}
				if (!(MMM_ModelMultiBase.class).isAssignableFrom(lclass) || Modifier.isAbstract(lclass.getModifiers())) {
					mod_MMM_MMMLib.Debug("getModelClass-fail.");
					return;
				}
				MMM_ModelMultiBase mlm[] = new MMM_ModelMultiBase[3];
				Constructor<MMM_ModelMultiBase> cm = lclass.getConstructor(float.class);
				mlm[0] = cm.newInstance(0.0F);
				float[] lsize = mlm[0].getArmorModelsSize();
				mlm[1] = cm.newInstance(lsize[0]);
				mlm[2] = cm.newInstance(lsize[1]);
				modelMap.put(pn, mlm);
				mod_MMM_MMMLib.Debug("getModelClass-%s:%s", pn, cn);
			}
			catch (Exception exception) {
				mod_MMM_MMMLib.Debug("getModelClass-Exception: %s", fname);
				exception.printStackTrace();
			}
			catch (Error error) {
				mod_MMM_MMMLib.Debug("getModelClass-Error: %s", fname);
			}
		}
	}
	
	protected void addTextureName(String fname, String[] pSearch) {
		// �p�b�P�[�W�Ƀe�N�X�`����o�^
		if (!fname.startsWith("/")) {
			fname = (new StringBuilder()).append("/").append(fname).toString();
		} else {
			
		}
		
		if (fname.startsWith(pSearch[1])) {
			int i = fname.lastIndexOf("/");
			if (pSearch[1].length() < i) {
				String pn = fname.substring(pSearch[1].length(), i);
				pn = pn.replace('/', '.');
				String fn = fname.substring(i);
				int lindex = getIndex(fn);
				if (lindex > -1) {
					String an = null;
					if (lindex == tx_oldarmor1) {
						lindex = tx_armor1;
						an = "default";
					}
					if (lindex == tx_oldarmor2) {
						lindex = tx_armor2;
						an = "default";
					}
					if (lindex == tx_oldwild) {
						lindex = tx_wild + 12;
					}
					MMM_TextureBox lts = getTextureBox(pn);
					if (lts == null) {
						lts = new MMM_TextureBox(pn, pSearch);
						textures.add(lts);
						mod_MMM_MMMLib.Debug("getTextureName-append-texturePack-%s", pn);
					}
					lts.addTexture(lindex, fname);
				}
			}
		}
	}

	protected boolean addTexturesZip(File file, String[] pSearch) {
		//
		if (file == null || file.isDirectory()) {
			return false;
		}
		try {
			FileInputStream fileinputstream = new FileInputStream(file);
			ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
			ZipEntry zipentry;
			do {
				zipentry = zipinputstream.getNextEntry();
				if(zipentry == null)
				{
					break;
				}
				if (!zipentry.isDirectory()) {
					if (zipentry.getName().endsWith(".class")) {
						addModelClass(zipentry.getName(), pSearch);
					} else {
						addTextureName(zipentry.getName(), pSearch);
					}
				}
			} while(true);
			
			zipinputstream.close();
			fileinputstream.close();
			
			return true;
		} catch (Exception exception) {
			mod_MMM_MMMLib.Debug("addTextureZip-Exception.");
			return false;
		}
	}

	protected void addTexturesJar(File file, String[] pSearch) {
		// 
		if (file.isFile()) {
			mod_MMM_MMMLib.Debug("addTextureJar-zip.");
			if (addTexturesZip(file, pSearch)) {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-done.");
			} else {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-fail.");
			}
		}
		
		// �Ӗ��Ȃ��H
		if (file.isDirectory()) {
			mod_MMM_MMMLib.Debug("addTextureJar-file.");
			boolean lflag = false;
			
			for (File t : file.listFiles()) {
				if (t.isDirectory()) {
					lflag |= addTexturesDir(t, pSearch);
				}
			}
			if (lflag) {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-done.");
			} else {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-fail.");
			}
			
			Package package1 = (net.minecraft.src.ModLoader.class).getPackage();
			if(package1 != null)
			{
				String s = package1.getName().replace('.', File.separatorChar);
				file = new File(file, s);
				mod_MMM_MMMLib.Debug("addTextureJar-file-Packege:%s", s);
			} else {
				mod_MMM_MMMLib.Debug("addTextureJar-file-null.");
			}
			if (addTexturesDir(file, pSearch)) {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-done.");
			} else {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-fail.");
			}
			
		}
	}

	protected boolean addTexturesDir(File file, String[] pSearch) {
		// mods�t�H���_�ɓ˂�����ł�����̂������A�ċA�ŁB
		if (file == null) {
			return false;
		}
		
		try {
			for (File t : file.listFiles()) {
				if(t.isDirectory()) {
					addTexturesDir(t, pSearch);
				} else {
					if (t.getName().endsWith(".class")) {
						addModelClass(t.getName(), pSearch);
					} else {
						String s = t.getPath().replace('\\', '/');
						int i = s.indexOf(pSearch[1]);
						if (i > -1) {
							// �Ώۂ̓e�N�X�`���f�B���N�g��
							addTextureName(s.substring(i), pSearch);
//							addTextureName(s.substring(i).replace('\\', '/'));
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			mod_MMM_MMMLib.Debug("addTextureDebug-Exception.");
			return false;
		}
	}

	protected int getIndex(String name) {
		// ���O����C���f�b�N�X�����o��
		for (int i = 0; i < defNames.length; i++) {
			if (name.endsWith(defNames[i])) {
				return i;
			}
		}
		
		Pattern p = Pattern.compile("_([0-9a-f]+).png");
		Matcher m = p.matcher(name);
		if (m.find()) {
			return Integer.decode("0x" + m.group(1));
		}
		
		return -1;
	}

	public MMM_TextureBox getNextPackege(MMM_TextureBox pNowBox, int pColor) {
		// ���̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		boolean f = false;
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.hasColor(pColor)) {
				if (f) {
					return ltb;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
			}
			if (ltb == pNowBox) {
				f = true;
			}
		}
		return lreturn == null ? null : lreturn;
	}

	public MMM_TextureBox getPrevPackege(MMM_TextureBox pNowBox, int pColor) {
		// �O�̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb == pNowBox) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasColor(pColor)) {
				lreturn = ltb;
			}
		}
		return lreturn == null ? null : lreturn;
	}

	/**
	 * ���[�J���œǂݍ��܂�Ă���e�N�X�`���p�b�N�̐��B
	 */
	public int getTextureCount() {
		return textures.size();
	}

	public MMM_TextureBox getNextArmorPackege(MMM_TextureBox pNowBox) {
		// ���̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		boolean f = false;
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.hasArmor()) {
				if (f) {
					return ltb;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
			}
			if (ltb == pNowBox) {
				f = true;
			}
		}
		return lreturn;
	}

	public MMM_TextureBox getPrevArmorPackege(MMM_TextureBox pNowBox) {
		// �O�̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb == pNowBox) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasArmor()) {
				lreturn = ltb;
			}
		}
		return lreturn;
	}

	public String getRandomTextureString(Random pRand) {
		return getRandomTexture(pRand).textureName;
	}

	public MMM_TextureBoxServer getRandomTexture(Random pRand) {
		if (textureServer.isEmpty()) {
			return null;
		} else {
			// �쐶�F��������̂����X�g�A�b�v
			List<MMM_TextureBoxServer> llist = new ArrayList<MMM_TextureBoxServer>();
			for (MMM_TextureBoxServer lbox : textureServer) {
				if (lbox.getWildColorBits() > 0) {
					llist.add(lbox);
				}
			}
			return llist.get(pRand.nextInt(llist.size()));
		}
	}

	/**
	 * �e�N�X�`���p�b�N���ɑΉ�����C���f�b�N�X��Ԃ��B
	 * ��{�T�[�o�[�p�B
	 * @param pEntity
	 * @param pPackName
	 * @return
	 */
	public int getIndexTextureBoxServer(MMM_ITextureEntity pEntity, String pPackName) {
		for (int li = 0; li < textureServer.size(); li++) {
			if (textureServer.get(li).textureName.equals(pPackName)) {
				return li;
			}
		}
		// ��������Ȃ������̂�Entity�ɑΉ�����f�t�H���g��Ԃ�
//		int li = textureServerIndex.get(getDefaultTexture(pEntity));
		MMM_TextureBox lbox = getDefaultTexture(pEntity);
		if (lbox != null) {
			pPackName = lbox.textureName;
			for (int li = 0; li < textureServer.size(); li++) {
				if (textureServer.get(li).textureName.equals(pPackName)) {
					return li;
				}
			}
		}
		return 0;
	}

	/**
	 * �w�肳�ꂽ�e�N�X�`���p�b�N�̃T�[�o�[���̊Ǘ��ԍ���Ԃ��B
	 * @param pBox
	 * @return
	 */
	public int getIndexTextureBoxServerIndex(MMM_TextureBox pBox) {
		return textureServerIndex.get(pBox);
	}

	/**
	 * Entity�ɑΉ�����f�t�H���g�̃e�N�X�`����ݒ肷��B
	 */
	public void setDefaultTexture(MMM_ITextureEntity pEntity, MMM_TextureBox pBox) {
		setDefaultTexture(pEntity.getClass(), pBox);
	}
	public void setDefaultTexture(Class pEntityClass, MMM_TextureBox pBox) {
		defaultTextures.put(pEntityClass, pBox);
		mod_MMM_MMMLib.Debug("appendDefaultTexture:%s(%s)",
				pEntityClass.getSimpleName(), pBox == null ? "NULL" : pBox.textureName);
	}

	/**
	 * Entity�ɑΉ�����f�t�H���g���f����Ԃ��B
	 */
	public MMM_TextureBox getDefaultTexture(MMM_ITextureEntity pEntity) {
		return getDefaultTexture(pEntity.getClass());
	}
	public MMM_TextureBox getDefaultTexture(Class pEntityClass) {
		if (defaultTextures.containsKey(pEntityClass)) {
			return defaultTextures.get(pEntityClass);
		} else {
			Class lsuper = pEntityClass.getSuperclass();
			if (lsuper != null) {
				MMM_TextureBox lbox = getDefaultTexture(lsuper);
				if (lbox != null) {
					setDefaultTexture(pEntityClass, lbox);
				}
				return lbox;
			}
			return null;
		}
	}



	/*
	 * �T�[�o�[�N���C�A���g�Ԃł̃e�N�X�`���Ǘ��֐��Q
	 */

	// �l�b�g���[�N�z���Ƀe�N�X�`���C���f�N�X�𓾂�ۂɎg��
	protected int getRequestStringIndex(String pVal) {
		int lblank = -1;
		for (int li = 0; li < requestString.length; li++) {
			if (requestString[li] == null) {
				lblank = li;
				requestStringCounter[li] = 0;
			} else if (requestString[li].equals(pVal)) {
				// ��ɗv����
				return -2;
			}
		}
		if (lblank >= 0) {
			requestString[lblank] = pVal;
		} else {
			mod_MMM_MMMLib.Debug("requestString Overflow!");
		}
		return lblank;
	}

	protected String getRequestString(int pIndex) {
		String ls = requestString[pIndex];
		requestString[pIndex] = null;
		return ls;
	}

	protected int getRequestIndex(int pTextureServerBoxIndex) {
		int lblank = -1;
		for (int li = 0; li < requestIndex.length; li++) {
			if (requestIndex[li] == -1) {
				lblank = li;
				requestIndexCounter[li] = 0;
			} else if (requestIndex[li] == pTextureServerBoxIndex) {
				// ��ɗv����
				return -2;
			}
		}
		if (lblank >= 0) {
			requestIndex[lblank] = pTextureServerBoxIndex;
		} else {
			mod_MMM_MMMLib.Debug("requestIndex Overflow!");
		}
		return lblank;
	}

	protected boolean clearRequestIndex(int pTextureServerBoxIndex) {
		for (int li = 0; li < requestIndex.length; li++) {
			if (requestIndex[li] == pTextureServerBoxIndex) {
				// �v�����������̂ŏ����B
				requestIndex[li] = -1;
				return true;
			}
		}
		return false;
	}


	public MMM_TextureBox getTextureBoxServerIndex(int pIndex) {
		for (Entry<MMM_TextureBox, Integer> le : textureServerIndex.entrySet()) {
			if (le.getValue() == pIndex) {
				return le.getKey();
			}
		}
		return null;
	}


	/**
	 * �e�N�X�`���p�b�N��ݒ肷�邽�߁A�T�[�o�[�֏��𑗂�B
	 * @param pEntity
	 * @param pBox
	 */
	public void postSetTexturePack(MMM_ITextureEntity pEntity, int pColor, MMM_TextureBoxBase[] pBox) {
		// Client
		if (!(pEntity instanceof Entity)) return;
		// �e�N�X�`���p�b�N��ݒ肷�邽�߁A�T�[�o�[�֏��𑗂�B
		int lindex[] = new int[pBox.length];
		boolean lflag = true;
		
		// PackeName����T�[�o�[���̃e�N�X�`���C���f�b�N�X���l������B
		for (int li = 0; li < pBox.length; li++) {
			lindex[li] = checkTextureBoxServer((MMM_TextureBox)pBox[li]);
			if (lindex[li] < 0) {
				lflag = false;
			}
		}
		
		if (lflag) {
			// ���ׂĂ̖��̂���C���f�b�N�X�����o�����ꍇ�A�T�[�o�[�փ|�X�g����B
			sendToServerSetTexturePackIndex(pEntity, pColor, lindex);
		} else {
			// ���[�J���ɐݒ�l���Ȃ��ꍇ�A�o�b�t�@�ɃW���u���X�^�b�N���I���B
			Object lo[] = new Object[1 + pBox.length];
			lo[0] = pColor;
			for (int li = 0; li < pBox.length; li++) {
				lo[li + 1] = pBox[li];
			}
			stackSetTexturePack.put(pEntity, lo);
		}
	}

	/**
	 * TextureBox�ɃT�[�o�[���ʔԍ����t�^����Ă��邩���m�F���A�Ȃ���Ζ₢���킹���s���B
	 * @param pBox
	 * @return
	 */
	public int checkTextureBoxServer(MMM_TextureBox pBox) {
		// Client
		if (textureServerIndex.containsKey(pBox)) {
			return textureServerIndex.get(pBox);
		} else {
			int ll = getRequestStringIndex(pBox.textureName);
			if (ll > -1) {
				sendToServerGetTextureIndex(ll, pBox);
				return -1;
			} else {
				return ll;
			}
		}
	}

	protected void sendToServerSetTexturePackIndex(MMM_ITextureEntity pEntity, int pColor, int[] pIndex) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̃C���f�b�N�X���ύX���ꂽ���Ƃ�ʒm����B
		if (pEntity instanceof Entity) {
			byte ldata[] = new byte[6 + pIndex.length * 2];
			ldata[0] = MMM_Statics.Server_SetTexturePackIndex;
			MMM_Helper.setInt(ldata, 1, ((Entity)pEntity).entityId);
			ldata[5] = (byte)pColor;
			int li = 6;
			for (int ll  : pIndex) {
				MMM_Helper.setShort(ldata, li, ll);
				li += 2;
			}
			MMM_Client.sendToServer(ldata);
		}
	}

	protected void reciveFromClientSetTexturePackIndex(Entity pEntity, byte[] pData) {
		// Server
		if (pEntity instanceof MMM_ITextureEntity) {
			// �N���C�A���g������e�N�X�`���p�b�N�̃C���f�b�N�X���ύX���ꂽ�ʒm���󂯎�����̂ŏ������s���B
			int lcount = (pData.length - 6) / 2;
			if (lcount < 1) return;
			int lindex[] = new int[lcount];
			
			for (int li = 0; li < lcount; li++) {
				lindex[li] = MMM_Helper.getShort(pData, 6 + li * 2);
			}
			mod_MMM_MMMLib.Debug("reciveFromClientSetTexturePackIndex: %d, %4x", pData[5], lindex[0]);
			((MMM_ITextureEntity)pEntity).setTexturePackIndex(pData[5], lindex);
		}
	}

	protected void sendToServerGetTextureIndex(int pBufIndex, MMM_TextureBox pBox) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̊Ǘ��ԍ���₢���킹��B
		// �Ăяo�����̃N���C�A���g�ւ̂ݕԂ��B
		// �Ԃ��Ƃ���Name�͕s�v�ABufIndex�݂̂Ŏ��ʂ�����
		byte ldata[] = new byte[22 + pBox.textureName.length()];
		ldata[0] = MMM_Statics.Server_GetTextureIndex;
		ldata[1] = (byte)pBufIndex;
		MMM_Helper.setShort(ldata, 2, pBox.getContractColorBits());
		MMM_Helper.setShort(ldata, 4, pBox.getWildColorBits());
		MMM_Helper.setFloat(ldata, 6, pBox.getHeight(null));
		MMM_Helper.setFloat(ldata, 10, pBox.getWidth(null));
		MMM_Helper.setFloat(ldata, 14, pBox.getYOffset(null));
		MMM_Helper.setFloat(ldata, 18, pBox.getMountedYOffset(null));
		MMM_Helper.setStr(ldata, 22, pBox.textureName);
		MMM_Client.sendToServer(ldata);
		mod_MMM_MMMLib.Debug("Server_GetTextureIndex: %s", pBox.textureName);
	}

	protected void reciveFromClientGetTexturePackIndex(NetServerHandler pHandler, byte[] pData) {
		// Server
		// �N���C�A���g���փe�N�X�`���p�b�N�̊Ǘ��ԍ���Ԃ��B
		String lpackname = MMM_Helper.getStr(pData, 22);
		MMM_TextureBoxServer lboxsrv = getTextureBoxServer(lpackname);
		int li;
		if (lboxsrv == null) {
			li = textureServer.size();
			lboxsrv = new MMM_TextureBoxServer();
			textureServer.add(lboxsrv);
		} else {
			li = textureServer.indexOf(lboxsrv);
		}
		lboxsrv.setValue(pData);
		
		byte ldata[] = new byte[4];
		ldata[0] = MMM_Statics.Client_SetTextureIndex;
		ldata[1] = pData[1];
		MMM_Helper.setShort(ldata, 2, li);
		mod_MMM_MMMLib.Debug("reciveFromClientGetTexturePackIndex: %s, %04x", lpackname, li);
		mod_MMM_MMMLib.sendToClient(pHandler, ldata);
	}

	protected void reciveFormServerSetTexturePackIndex(byte[] pData) {
		// Client
		// �T�[�o�[������e�N�X�`���p�b�N�̃C���f�b�N�X���󂯎�����̂Œl��o�^����B
		MMM_TextureBox lbox = getTextureBox(getRequestString(pData[1]));
		textureServerIndex.put(lbox, (int)MMM_Helper.getShort(pData, 2));
		mod_MMM_MMMLib.Debug("reciveFormServerSetTexturePackIndex: %s, %04x", lbox.textureName, (int)MMM_Helper.getShort(pData, 2));
		
		// �X�^�b�N���ꂽ�W���u���珈���\�ȕ�������Ύ��s����B
		Map<MMM_ITextureEntity, Object[]> lmap = new HashMap<MMM_ITextureEntity, Object[]>(stackSetTexturePack);
		stackSetTexturePack.clear();
		for (Entry<MMM_ITextureEntity, Object[]> le : lmap.entrySet()) {
			Object lo[] = le.getValue();
			MMM_TextureBox ls[] = new MMM_TextureBox[le.getValue().length - 1];
			int lc = (Integer)lo[0];
			for (int li = 1; li < lo.length; li++) {
				ls[li - 1] = (MMM_TextureBox)lo[li];
			}
			postSetTexturePack(le.getKey(), lc, ls);
		}
	}



	/**
	 * �T�[�o�[����ݒ肳�ꂽ�e�N�X�`���C���f�b�N�X����e�N�X�`���p�b�N���擾����B
	 * @param pEntity
	 * @param pIndex
	 */
	public void postGetTexturePack(MMM_ITextureEntity pEntity, int[] pIndex) {
		// Client
		// �N���C�A���g���Ŏw�肳�ꂽ�C���f�b�N�X�ɑ΂��ăe�N�X�`���p�b�N�̖��̂�Ԃ��ݒ肳����
		MMM_TextureBox lbox[] = new MMM_TextureBox[pIndex.length];
		boolean lflag = true;
		
		// ���[�J���C���f�b�N�X�ɖ��̂��o�^����Ă��Ȃ���΃T�[�o�[�֖₢���킹��B
		for (int li = 0; li < pIndex.length; li++) {
			lbox[li] = getTextureBoxServerIndex(pIndex[li]);
			if (lbox[li] == null) {
				if (getRequestIndex(pIndex[li]) > -1) {
					sendToServerGetTexturePackName(pIndex[li]);
				}
				lflag = false;
			}
		}
		
		if (lflag) {
			// �S�Ă̒l������ꍇ��Entity�֒l��ݒ肷��B
			pEntity.setTexturePackName(lbox);
		} else {
			// �s���l������ꍇ�͏������X�^�b�N����B
			stackGetTexturePack.put(pEntity, pIndex);
		}
	}

	protected void sendToServerGetTexturePackName(int pIndex) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̖��̂�₢���킹��
		if (pIndex < 0) {
			mod_MMM_MMMLib.Debug("request range out.");
			return;
		}
		byte ldata[] = new byte[3];
		ldata[0] = MMM_Statics.Server_GetTexturePackName;
		MMM_Helper.setShort(ldata, 1, pIndex);
		MMM_Client.sendToServer(ldata);
	}

	protected void reciveFromClientGetTexturePackName(NetServerHandler pHandler, byte[] pData) {
		// Server
		// �N���C�A���g����e�N�X�`���p�b�N�̖��̂��₢���킹��ꂽ�B
		int lindex = MMM_Helper.getShort(pData, 1);
		MMM_TextureBoxServer lboxserver = getTextureBoxServer(lindex);
		
		// Client�֊Ǘ��ԍ��ɓo�^����Ă���e�N�X�`�����̂��|�X�g����
		byte ldata[] = new byte[23 + lboxserver.textureName.length()];
		ldata[0] = MMM_Statics.Client_SetTexturePackName;
		MMM_Helper.setShort(ldata, 1, lindex);
		MMM_Helper.setShort(ldata, 3, lboxserver.getContractColorBits());
		MMM_Helper.setShort(ldata, 5, lboxserver.getWildColorBits());
		MMM_Helper.setFloat(ldata, 7, lboxserver.getHeight(null));
		MMM_Helper.setFloat(ldata, 11, lboxserver.getWidth(null));
		MMM_Helper.setFloat(ldata, 15, lboxserver.getYOffset(null));
		MMM_Helper.setFloat(ldata, 19, lboxserver.getMountedYOffset(null));
		MMM_Helper.setStr(ldata, 23, lboxserver.textureName);
		mod_MMM_MMMLib.sendToClient(pHandler, ldata);
		mod_MMM_MMMLib.Debug("SetTexturePackName:%04x - %s", lindex, lboxserver.textureName);
	}

	protected void reciveFromServerSetTexturePackName(byte[] pData) {
		// Client
		// �T�[�o�[����C���f�b�N�X�ɑ΂��閼�̂̐ݒ肪�������B
		String lpackname = MMM_Helper.getStr(pData, 23);
		MMM_TextureBox lbox = getTextureBox(lpackname);
		if (lbox == null) {
			// ���[�J���ɂ͑��݂��Ȃ��e�N�X�`���p�b�N
			// TODO:���̕ӗv�C��
			lbox = getTextureBox("default_Orign").duplicate();
			lbox.textureName = lpackname;
//			lbox = new MMM_TextureBox(lpackname, null);
			lbox.setModelSize(
					MMM_Helper.getFloat(pData, 7),
					MMM_Helper.getFloat(pData, 11),
					MMM_Helper.getFloat(pData, 15),
					MMM_Helper.getFloat(pData, 19));
			textures.add(lbox);
		}
		int lindex = MMM_Helper.getShort(pData, 1);
		textureServerIndex.put(lbox, lindex);
		clearRequestIndex(lindex);
		
		// �����\�ȕ����X�^�b�N����Ă���ꍇ�͏������s���B
		Map<MMM_ITextureEntity, int[]> lmap = new HashMap<MMM_ITextureEntity, int[]>(stackGetTexturePack);
		stackGetTexturePack.clear();
		for (Entry<MMM_ITextureEntity, int[]> le : lmap.entrySet()) {
			postGetTexturePack(le.getKey(), le.getValue());
		}
	}

	/**
	 * Request�n�̒l�����J�E���g�ŏ���
	 */
	protected void onUpdate() {
		for (int li = 0; li < requestString.length; li++) {
			// ��30�b�ŉ��
			if (requestString[li] != null && requestStringCounter[li]++ > 600) {
				requestString[li] = null;
				requestStringCounter[li] = 0;
			}
			if (requestIndex[li] != -1 && requestIndexCounter[li]++ > 600) {
				requestIndex[li] = -1;
				requestIndexCounter[li] = 0;
			}
		}
	}

}
