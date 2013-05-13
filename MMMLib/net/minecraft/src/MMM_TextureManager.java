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

import net.minecraft.server.MinecraftServer;

public class MMM_TextureManager {

//	private static String defDirName = "/mob/littleMaid/";
	private static String nameTextureIndex = "config/mod_MMM_textureList.cfg";
	/**
	 * ���^�C�v�̃t�@�C����
	 */
	private static String defNames[] = {
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
	
	public static final int tx_oldwild		= 0x10; //16;
	public static final int tx_oldarmor1	= 0x11; //17;
	public static final int tx_oldarmor2	= 0x12; //18;
	public static final int tx_gui			= 0x20; //32;
	public static final int tx_wild			= 0x30; //48;
	public static final int tx_armor1		= 0x40; //64;
	public static final int tx_armor2		= 0x50; //80;
	public static final int tx_light		= 0x60; //96;
	private static Map<String, MMM_ModelMultiBase[]> modelMap = new TreeMap<String, MMM_ModelMultiBase[]>();
	public static String[] armorFilenamePrefix;
	public static MMM_ModelMultiBase[] defaultModel;

	/**
	 * ���[�J���ŕێ����Ă���e�N�X�`���p�b�N
	 */
	public static List<MMM_TextureBox> textures = new ArrayList<MMM_TextureBox>();
	/**
	 * �T�[�o�[���ł̊Ǘ��ԍ������ʂ���̂Ɏg���B
	 */
	public static Map<MMM_TextureBox, Integer> textureServerIndex = new HashMap<MMM_TextureBox, Integer>();
	/**
	 * �T�[�o�[�E�N���C�A���g�ԂŃe�N�X�`���p�b�N�̖��̃��X�g�̓��������̂Ɏg���B
	 */
	public static Map<Integer, MMM_TextureBoxServer> textureServer = new HashMap<Integer, MMM_TextureBoxServer>();
//	public static List<MMM_TextureBoxServer> textureServer = new ArrayList<MMM_TextureBoxServer>();
	/**
	 * Entity���Ƀf�t�H���g�e�N�X�`�����Q�ƁB
	 * �\�z���@��EntityList���Q�Ƃ̂��ƁB
	 */
	public static Map<MMM_ITextureEntity, MMM_TextureBox> defaultTextures = new HashMap<MMM_ITextureEntity, MMM_TextureBox>();
	
	/**
	 * �N���C�A���g���Ŏg��
	 */
	private static String[] requestString = new String[] {
		null, null, null, null, null, null, null, null,
		null, null, null, null, null, null, null, null
	};
	private static Map<MMM_ITextureEntity, int[]> stackGetTexturePack = new HashMap<MMM_ITextureEntity, int[]>();
	private static Map<MMM_ITextureEntity, Object[]> stackSetTexturePack = new HashMap<MMM_ITextureEntity, Object[]>();
	
	protected static List<String[]> searchPrefix = new ArrayList<String[]>();



	public static void init() {
		MMM_FileManager.getModFile("MMMLib", "MMMLib");
		MMM_FileManager.getModFile("littleMaidMob", "littleMaidMob");
		addSearch("littleMaidMob", "/mob/littleMaid/", "ModelLittleMaid_");
//		addSearch("littleMaidMob", "/mob/littleMaid/", "ModelLittleMaid_");
	}

	public static String[] getSearch(String pName) {
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
	public static void addSearch(String pName, String pTextureDir, String pClassPrefix) {
		searchPrefix.add(new String[] {pName, pTextureDir, pClassPrefix});
	}

	/**
	 * �p�b�P�[�W���̂̈�v���镨��Ԃ��B
	 */
	public static MMM_TextureBox getTextureBox(String pName) {
		for (MMM_TextureBox ltb : textures) {
			if (ltb.packegeName.equals(pName)) {
				return ltb;
			}
		}
		return null;
	}

	public static Entry<Integer, MMM_TextureBoxServer> getTextureBoxServer(String pName) {
		for (Entry<Integer, MMM_TextureBoxServer> le : textureServer.entrySet()) {
			if (le.getValue().textureName.equals(pName)) {
				return le;
			}
		}
		return null;
	}

	public static MMM_TextureBoxServer getTextureBoxServer(int pIndex) {
		if (textureServer.containsKey(pIndex)) {
			return textureServer.get(pIndex);
		}
		return null;
	}

	private static void getArmorPrefix() {
		// �A�[�}�[�t�@�C���̃v���t�B�b�N�X���l��
		try {
			Field f = RenderPlayer.class.getDeclaredFields()[3];
			f.setAccessible(true);
			String[] s = (String[])f.get(null);
			List<String> list = Arrays.asList(s);
			armorFilenamePrefix = list.toArray(new String[0]);
//			for (String t : armorFilenamePrefix) {
//				mod_littleMaidMob.Debug("armor:".concat(t));
//			}
		}
		catch (Exception e) {
		}
	}


	public static boolean loadTextures() {
		// �A�[�}�[�̃t�@�C���������ʂ��邽�߂̕�������l������
		getArmorPrefix();
		
		// �f�t�H���g�e�N�X�`�����̍쐬
		if (defaultModel != null) {
			String[] lss = getSearch("littleMaidMob");
			for (int i = 0; i < defNames.length; i++) {
				addTextureName((new StringBuilder()).append(lss[1]).append("default/").append(defNames[i]).toString(), lss);
			}
			modelMap.put("default", defaultModel);
//			getStringToIndex("default");
			mod_MMM_MMMLib.Debug("getTexture-append-default-done.");
		}
		
		for (String[] lss : searchPrefix) {
			mod_MMM_MMMLib.Debug("getTexture[%s].", lss[0]);
			// jar���̃e�N�X�`����ǉ�
			if (MMM_FileManager.minecraftJar == null) {
				mod_MMM_MMMLib.Debug("getTexture-append-jar-file not founded.");
			} else {
				addTexturesJar(MMM_FileManager.minecraftJar, lss);
			}
			
			// mods
			for (File lf : MMM_FileManager.getFileList(lss[0])) {
				boolean lflag;
				if (lf.isDirectory()) {
					// �f�B���N�g��
					lflag = addTexturesDir(lf, lss);
				} else {
					// zip
					lflag = addTexturesZip(lf, lss);
				}
				mod_MMM_MMMLib.Debug("getTexture-append-%s-%s.", lf.getName(), lflag ? "done" : "fail");
			}
		}
/*		
		// ���[�h�����e�N�X�`���p�b�N����N���X���������[�h
		for (Entry<String, Map<Integer, String>> tt: textures.entrySet()) {
			String st = tt.getKey();
			int index = st.lastIndexOf("_");
			if (index > -1) {
				st = st.substring(index + 1);
				if (!st.isEmpty()) {
					addModelClass("ModelLittleMaid_".concat(st).concat(".class"));
				}
			}
		}
		mod_MMM_MMMLib.Debug("getTexture-append-Models-append-done.");
*/
		// �e�N�X�`���p�b�P�[�W�Ƀ��f���N���X��R�t��
		for (MMM_TextureBox ltb : textures) {
			int li = ltb.packegeName.lastIndexOf("_");
			if (li > -1) {
				String ls = ltb.packegeName.substring(li + 1);
				ltb.setModels(ls, modelMap.get(ls));
				if (ltb.models == null) {
					ltb.setModels("default", defaultModel);
				}
			} else {
				ltb.setModels("default", defaultModel);
			}
		}
		
		return false;
	}

	public static boolean loadTextureIndex() {
		// �T�[�o�[�p�e�N�X�`�����̂̃C���f�N�b�X���[�_�[
		File lfile = MinecraftServer.getServer().getFile(nameTextureIndex);
		if (lfile.exists() && lfile.isFile()) {
			try {
				FileReader fr = new FileReader(lfile);
				BufferedReader br = new BufferedReader(fr);
				String ls;
				int li = 0;
				textureServer.clear();
				
				while ((ls = br.readLine()) != null) {
					String lt[] = ls.split(",");
					if (lt.length > 1) {
						MMM_TextureBoxServer lbox = new MMM_TextureBoxServer();
						lbox.contractColor	= Integer.valueOf(lt[0], 16);
						lbox.wildColor		= Integer.valueOf(lt[1], 16);
						lbox.modelHeight	= Float.valueOf(lt[2]);
						lbox.modelWidth		= Float.valueOf(lt[3]);
						lbox.modelYOffset	= Float.valueOf(lt[4]);
						lbox.textureName	= lt[5];
						textureServer.put(li++, lbox);
					}
				}
				
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {
			MMM_TextureBoxServer lbox = new MMM_TextureBoxServer();
			lbox.contractColor	= 0xffff;
			lbox.wildColor		= 0x1000;
			lbox.modelHeight	= 1.35F;
			lbox.modelWidth		= 0.5F;
			lbox.modelYOffset	= 1.35F;
			lbox.textureName	= "default";
			textureServer.put(0, lbox);
		}
		
		return false;
	}

	public static void saveTextureIndex() {
		// �T�[�o�[�p�e�N�X�`�����̂̃C���f�N�b�X�Z�[�o�[
		File lfile = MinecraftServer.getServer().getFile(nameTextureIndex);
		try {
			FileWriter fw = new FileWriter(lfile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (MMM_TextureBoxServer lbox : textureServer.values()) {
				bw.write(String.format(
						"%04x,%04x,%f,%f,%f,%s",
						lbox.contractColor,
						lbox.wildColor,
						lbox.modelHeight,
						lbox.modelWidth,
						lbox.modelYOffset,
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
	public static void initTextureList(boolean pFlag) {
		textureServerIndex.clear();
		
		
		textureServer.clear();
		if (pFlag) {
			// Internal
			int li = 0;
			for (MMM_TextureBox lte : textures) {
				MMM_TextureBoxServer lbox = new MMM_TextureBoxServer();
				lbox.contractColor	= lte.getContractColorBits();
				lbox.wildColor		= lte.getWildColorBits();
				if (lte.models != null && lte.models[0] != null) {
					lbox.modelHeight	= lte.models[0].getHeight();
					lbox.modelWidth		= lte.models[0].getWidth();
					lbox.modelYOffset	= lte.models[0].getyOffset();
				} else {
					// TODO:����ꎞ�I�ɂO����Ă邯�ǁA�Ȃ񂩃f�t�H���g�̃��f����ݒ肵���ق����ǂ�������H
					lbox.modelHeight	= 0F;
					lbox.modelWidth		= 0F;
					lbox.modelYOffset	= 0F;
				}
				lbox.textureName	= lte.packegeName;
				textureServer.put(li++, lbox);
			}
		}
	}

	/**
	 * �n���ꂽ���̂���͂���LMM�p�̃��f���N���X���ǂ����𔻒肷��B
	 * �uModelLittleMaid_�v�Ƃ��������񂪊܂܂�Ă��āA
	 * �uMMM_ModelBiped�v���p�����Ă���΃}���`���f���Ƃ��ăN���X��o�^����B
	 * @param fname
	 */
	private static void addModelClass(String fname, String[] pSearch) {
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
//				mod_littleMaidMob.Debug(String.format("getModelClass-%s", mlm[0].getClass().getName()));
				mod_MMM_MMMLib.Debug("getModelClass-%s:%s", pn, cn);
				
			}
			catch (Exception exception) {
				mod_MMM_MMMLib.Debug("getModelClass-Exception: %s", fname);
			}
			catch (Error error) {
				mod_MMM_MMMLib.Debug("getModelClass-Error: %s", fname);
			}
		}
	}
	
	private static void addTextureName(String fname, String[] pSearch) {
		// �p�b�P�[�W�Ƀe�N�X�`����o�^
		if (!fname.startsWith("/")) {
			fname = (new StringBuilder()).append("/").append(fname).toString();
		}
		
		if (fname.startsWith(pSearch[1])) {
			int i = fname.lastIndexOf("/");
			if (pSearch[1].length() < i) {
				String pn = fname.substring(pSearch[1].length(), i);
				pn = pn.replace('/', '.');
				String fn = fname.substring(i);
				int j = getIndex(fn);
				if (j > -1) {
					String an = null;
					if (j == tx_oldarmor1) {
						j = tx_armor1;
						an = "default";
					}
					if (j == tx_oldarmor2) {
						j = tx_armor2;
						an = "default";
					}
					if (j == tx_oldwild) {
						j = tx_wild + 12;
					}
					MMM_TextureBox lts = getTextureBox(pn);
					if (lts == null) {
						lts = new MMM_TextureBox(pn, pSearch);
						textures.add(lts);
						mod_MMM_MMMLib.Debug("getTextureName-append-texturePack-%s", pn);
//						mod_MMM_MMMLib.Debug(String.format("getTextureName-append-armorPack-%s", pn));
					}
					if (j >= 0x40 && j <= 0x5f) {
						// �_���[�W�h�A�[�}�[
						Map<String, Map<Integer, String>> s = lts.armors;
						if (an == null) an = fn.substring(1, fn.lastIndexOf('_'));
						Map<Integer, String> ss = s.get(an);
						if (ss == null) {
							ss = new HashMap<Integer, String>();
							s.put(an, ss);
						}
						ss.put(j, fn);
//						mod_littleMaidMob.Debug(String.format("getTextureName-append-armor-%s:%d:%s", pn, j, fn));
					} else {
						// �ʏ�̃e�N�X�`��
						Map<Integer, String> s = lts.textures;
						s.put(j, fn);
//						mod_littleMaidMob.Debug(String.format("getTextureName-append-%s:%d:%s", pn, j, fn));
					}
				}
			}
		}
	}

	public static boolean addTexturesZip(File file, String[] pSearch) {
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

	protected static void addTexturesJar(File file, String[] pSearch) {
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
			
			for (File t : file.listFiles()) {
				if (t.isDirectory() && t.getName().equalsIgnoreCase("mob")) {
//					if (addTexturesDir(file, pSearch)) {
					if (addTexturesDir(t, pSearch)) {
						mod_MMM_MMMLib.Debug("getTexture-append-jar-done.");
					} else {
						mod_MMM_MMMLib.Debug("getTexture-append-jar-fail.");
					}
				}
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

	public static boolean addTexturesDir(File file, String[] pSearch) {
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

	private static int getIndex(String name) {
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

	public static String getNextPackege(String nowname, int index) {
		// ���̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		boolean f = false;
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.hasColor(index)) {
				if (f) {
					return ltb.packegeName;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
			}
			if (ltb.packegeName.equalsIgnoreCase(nowname)) {
				f = true;
			}
		}
		return lreturn == null ? null : lreturn.packegeName;
	}

	public static String getPrevPackege(String nowname, int index) {
		// �O�̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.packegeName.equalsIgnoreCase(nowname)) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasColor(index)) {
				lreturn = ltb;
			}
		}
		return lreturn == null ? null  :lreturn.packegeName;
	}

	public static String getTextureName(String name, int index) {
		MMM_TextureBox ltb = getTextureBox(name);
		if (ltb == null) {
			return null;
		} else if (!ltb.hasColor(index)) {
			// ����p�^�[��
			if (index >= 0x60 && index <= 0x6f) {
				// �ڂ̃e�N�X�`��
				return getTextureName(name, 0x13);
			}
			return null;
		} else {
			return ltb.getTextureName(index);
		}
	}
	
	public static int getTextureCount() {
		return textures.size();
	}
	
	public static String getNextArmorPackege(String nowname) {
		// ���̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		boolean f = false;
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.hasArmor()) {
				if (f) {
					return ltb.packegeName;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
			}
			if (ltb.packegeName.equalsIgnoreCase(nowname)) {
				f = true;
			}
		}
		return lreturn.packegeName;
	}

	public static String getPrevArmorPackege(String nowname) {
		// �O�̃e�N�X�`���p�b�P�[�W�̖��O��Ԃ�
		MMM_TextureBox lreturn = null;
		for (MMM_TextureBox ltb : textures) {
			if (ltb.packegeName.equalsIgnoreCase(nowname)) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasArmor()) {
				lreturn = ltb;
			}
		}
		return lreturn.packegeName;
	}

	/**
	 * �A�[�}�[�̃e�N�X�`���t�@�C������Ԃ�
	 */
	public static String getArmorTextureName(String name, int index, ItemStack itemstack) {
		// index��0x40,0x50�ԑ�
		MMM_TextureBox ltb = getTextureBox(name);
		if (ltb == null) {
			return null;
		}
		return ltb.getArmorTextureName(index, itemstack);
	}

	public static String getRandomTexture(Random pRand) {
		if (textureServer.isEmpty()) {
			return "default";
		} else {
			// �쐶�F��������̂����X�g�A�b�v
			List<MMM_TextureBoxServer> llist = new ArrayList<MMM_TextureBoxServer>();
			for (Entry<Integer, MMM_TextureBoxServer> le : textureServer.entrySet()) {
				if (le.getValue().wildColor > 0) {
					llist.add(le.getValue());
				}
			}
			return llist.get(pRand.nextInt(llist.size())).textureName;
		}
	}

	/**
	 * �쐶�̃��C�h�̐F�������_���ŕԂ�
	 */
	public static int getRandomWildColor(int pIndex, Random rand) {
		if (textureServer.isEmpty() || pIndex < 0) return -1;
		
		List<Integer> llist = new ArrayList<Integer>();
		int lcolor = textureServer.get(pIndex).wildColor;
		for (int li = 0; li < 16; li++) {
			if ((lcolor & 0x01) > 0) {
				llist.add(li);
			}
			lcolor = lcolor >>> 1;
		}
		
		if (llist.size() > 0) {
			return llist.get(rand.nextInt(llist.size()));
		} else {
			return -1;
		}
	}

	/**
	 * �_��̃��C�h�̐F�������_���ŕԂ�
	 */
	public static int getRandomContractColor(int pIndex, Random rand) {
		if (textureServer.isEmpty() || pIndex < 0) return -1;
		
		List<Integer> llist = new ArrayList<Integer>();
		int lcolor = textureServer.get(pIndex).contractColor;
		for (int li = 0; li < 16; li++) {
			if ((lcolor & 0x01) > 0) {
				llist.add(li);
			}
			lcolor = lcolor >>> 1;
		}
		
		if (llist.size() > 0) {
			return llist.get(rand.nextInt(llist.size()));
		} else {
			return -1;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * �T�[�o�[�N���C�A���g�Ԃł̃e�N�X�`���Ǘ��֐��Q
	 */

	// �l�b�g���[�N�z���Ƀe�N�X�`���C���f�N�X�𓾂�ۂɎg��
	public static int getRequestIndex(String pVal) {
		int lblank = -1;
		for (int li = 0; li < requestString.length; li++) {
			if (requestString[li] == null) {
				lblank = li;
			} else if (requestString[li].equals(pVal)) {
				// ���ɗv����
				return -2;
			}
		}
		if (lblank >= 0) {
			requestString[lblank] = pVal;
		}
		return lblank;
	}

	public static String getRequestString(int pIndex) {
		String ls = requestString[pIndex];
		requestString[pIndex] = null;
		return ls;
	}



	public static MMM_TextureBox getTextureBoxServerIndex(int pIndex) {
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
	 * @param pPackName
	 */
	public static void postSetTexturePack(MMM_ITextureEntity pEntity, int pColor, String[] pPackName) {
		// Client
		if (pEntity instanceof Entity) return;
		// �e�N�X�`���p�b�N��ݒ肷�邽�߁A�T�[�o�[�֏��𑗂�B
		int lindex[] = new int[pPackName.length];
		boolean lflag = true;
		
		// PackeName����T�[�o�[���̃e�N�X�`���C���f�b�N�X���l������B
		for (int li = 0; li < pPackName.length; li++) {
			MMM_TextureBox lbox = getTextureBox(pPackName[li]);
			if (!textureServerIndex.containsKey(lbox)) {
				lflag = false;
				lindex[li] = -1;
				int ll = getRequestIndex(pPackName[li]);
				if (ll == -2) continue;
				if (ll > -1) {
					sendToServerGetTextureIndex(ll, lbox);
				}
			} else {
				lindex[li] = textureServerIndex.get(lbox);
			}
		}
		
		if (lflag) {
			// ���ׂĂ̖��̂���C���f�b�N�X�����o�����ꍇ�A�T�[�o�[�փ|�X�g����B
			sendToServerSetTexturePackIndex(pEntity, pColor, lindex);
		} else {
			// ���[�J���ɐݒ�l���Ȃ��ꍇ�A�o�b�t�@�ɃW���u���X�^�b�N���I���B
			Object lo[] = new Object[1 + pPackName.length];
			lo[0] = pColor;
			for (int li = 0; li < pPackName.length; li++) {
				lo[li + 1] = pPackName[li];
			}
			stackSetTexturePack.put(pEntity, lo);
		}
	}

	protected static void sendToServerSetTexturePackIndex(MMM_ITextureEntity pEntity, int pColor, int[] pIndex) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̃C���f�b�N�X���ύX���ꂽ���Ƃ�ʒm����B
		if (pEntity instanceof Entity) {
			byte ldata[] = new byte[5 + pIndex.length * 2];
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

	protected static void reciveFromClientSetTexturePackIndex(Entity pEntity, byte[] pData) {
		// Server
		if (pEntity instanceof MMM_ITextureEntity) {
			// �N���C�A���g������e�N�X�`���p�b�N�̃C���f�b�N�X���ύX���ꂽ�ʒm���󂯎�����̂ŏ������s���B
			int lcount = (pData.length - 8) / 2;
			if (lcount < 1) return;
			int lindex[] = new int[lcount];
			
			for (int li = 0; li < lcount; li++) {
				lindex[li] = MMM_Helper.getShort(pData, 8 + li * 2);
			}
			((MMM_ITextureEntity)pEntity).setTexturePackIndex(pData[5], lindex);
		}
	}

	protected static void sendToServerGetTextureIndex(int pBufIndex, MMM_TextureBox pBox) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̊Ǘ��ԍ���₢���킹��B
		// �Ăяo�����̃N���C�A���g�ւ̂ݕԂ��B
		// �Ԃ��Ƃ���Name�͕s�v�ABufIndex�݂̂Ŏ��ʂ�����
		byte ldata[] = new byte[18 + pBox.packegeName.length()];
		ldata[0] = MMM_Statics.Server_GetTextureIndex;
		ldata[1] = (byte)pBufIndex;
		MMM_Helper.setShort(ldata, 2, pBox.getContractColorBits());
		MMM_Helper.setShort(ldata, 4, pBox.getWildColorBits());
		MMM_Helper.setFloat(ldata, 6, pBox.getHeight());
		MMM_Helper.setFloat(ldata, 10, pBox.getWidth());
		MMM_Helper.setFloat(ldata, 14, pBox.getYOffset());
		MMM_Helper.setStr(ldata, 18, pBox.packegeName);
		MMM_Client.sendToServer(ldata);
	}

	protected static void reciveFromClientGetTexturePackIndex(byte[] pData) {
		// Server
		// �N���C�A���g���փe�N�X�`���p�b�N�̊Ǘ��ԍ���Ԃ��B
		String lpackname = MMM_Helper.getStr(pData, 18);
		Entry<Integer, MMM_TextureBoxServer> le = getTextureBoxServer(lpackname);
		MMM_TextureBoxServer lboxsrv;
		int li;
		if (le == null) {
			li = textureServer.size() + 1;
			lboxsrv = new MMM_TextureBoxServer();
		} else {
			li = le.getKey();
			lboxsrv = le.getValue();
		}
		lboxsrv.contractColor = MMM_Helper.getShort(pData, 2);
		lboxsrv.wildColor = MMM_Helper.getShort(pData, 4);
		lboxsrv.modelHeight = MMM_Helper.getFloat(pData, 6);
		lboxsrv.modelWidth = MMM_Helper.getFloat(pData, 10);
		lboxsrv.modelYOffset = MMM_Helper.getFloat(pData, 14);
		lboxsrv.textureName = lpackname;
		textureServer.put(li, lboxsrv);
		
		byte ldata[] = new byte[4];
		ldata[0] = MMM_Statics.Client_SetTextureIndex;
		ldata[1] = pData[1];
		MMM_Helper.setShort(ldata, 2, li);
	}

	protected static void reciveFormServerSetTexturePackIndex(byte[] pData) {
		// Client
		// �T�[�o�[������e�N�X�`���p�b�N�̃C���f�b�N�X���󂯎�����̂Œl��o�^����B
		MMM_TextureBox lbox = getTextureBox(getRequestString(pData[1]));
		textureServerIndex.put(lbox, (int)MMM_Helper.getShort(pData, 2));
		
		// �X�^�b�N���ꂽ�W���u���珈���\�ȕ�������Ύ��s����B
		Map<MMM_ITextureEntity, Object[]> lmap = new HashMap<MMM_ITextureEntity, Object[]>(stackSetTexturePack);
		stackSetTexturePack.clear();
		for (Entry<MMM_ITextureEntity, Object[]> le : lmap.entrySet()) {
			Object lo[] = le.getValue();
			String ls[] = new String[le.getValue().length - 1];
			int lc = (Integer)lo[0];
			for (int li = 1; li < lo.length; li++) {
				ls[li - 1] = (String)lo[li];
			}
			postSetTexturePack(le.getKey(), lc, ls);
		}
	}



	/**
	 * �T�[�o�[����ݒ肳�ꂽ�e�N�X�`���C���f�b�N�X����e�N�X�`���p�b�N���擾����B
	 * @param pEntity
	 * @param pIndex
	 */
	public static void postGetTexturePack(MMM_ITextureEntity pEntity, int[] pIndex) {
		// Client
		// �N���C�A���g���Ŏw�肳�ꂽ�C���f�b�N�X�ɑ΂��ăe�N�X�`���p�b�N�̖��̂�Ԃ��ݒ肳����
		MMM_TextureBox lbox[] = new MMM_TextureBox[pIndex.length];
		boolean lflag = true;
		
		// ���[�J���C���f�b�N�X�ɖ��̂��o�^����Ă��Ȃ���΃T�[�o�[�֖₢���킹��B
		for (int li = 0; li < pIndex.length; li++) {
			lbox[li] = getTextureBoxServerIndex(pIndex[li]);
			if (lbox[li] == null) {
				sendToServerGetTexturePackName(pIndex[li]);
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

	protected static void sendToServerGetTexturePackName(int pIndex) {
		// Client
		// �T�[�o�[���փe�N�X�`���p�b�N�̖��̂�₢���킹��
		byte ldata[] = new byte[3];
		ldata[0] = MMM_Statics.Server_GetTexturePackName;
		MMM_Helper.setShort(ldata, 1, pIndex);
		MMM_Client.sendToServer(ldata);
	}

	protected static void reciveFromClientGetTexturePackName(NetServerHandler pHandler, byte[] pData) {
		// Server
		// �N���C�A���g����e�N�X�`���p�b�N�̖��̂��₢���킹��ꂽ�B
		int lindex = MMM_Helper.getShort(pData, 1);
		MMM_TextureBoxServer lboxserver = getTextureBoxServer(lindex);
		
		// Client�֊Ǘ��ԍ��ɓo�^����Ă���e�N�X�`�����̂��|�X�g����
		byte ldata[] = new byte[19 + lboxserver.textureName.length()];
		ldata[0] = MMM_Statics.Client_SetTexturePackName;
		MMM_Helper.setShort(ldata, 1, lindex);
		MMM_Helper.setShort(ldata, 3, lboxserver.contractColor);
		MMM_Helper.setShort(ldata, 5, lboxserver.wildColor);
		MMM_Helper.setFloat(ldata, 7, lboxserver.modelHeight);
		MMM_Helper.setFloat(ldata, 11, lboxserver.modelWidth);
		MMM_Helper.setFloat(ldata, 15, lboxserver.modelYOffset);
		MMM_Helper.setStr(ldata, 19, lboxserver.textureName);
		mod_MMM_MMMLib.sendToClient(pHandler, pData);
	}

	protected static void reciveFromServerSetTexturePackName(byte[] pData) {
		// Client
		// �T�[�o�[����C���f�b�N�X�ɑ΂��閼�̂̐ݒ肪�������B
		String lpackname = MMM_Helper.getStr(pData, 19);
		MMM_TextureBox lbox = getTextureBox(lpackname);
		if (lbox == null) {
			// ���[�J���ɂ͑��݂��Ȃ��e�N�X�`���p�b�N
			lbox = new MMM_TextureBox(lpackname, null);
			lbox.setModelSize(
					MMM_Helper.getFloat(pData, 7),
					MMM_Helper.getFloat(pData, 11),
					MMM_Helper.getFloat(pData, 15));
			textures.add(lbox);
		}
		textureServerIndex.put(lbox, (int)MMM_Helper.getShort(pData, 1));
		
		// �����\�ȕ����X�^�b�N����Ă���ꍇ�͏������s���B
		Map<MMM_ITextureEntity, int[]> lmap = new HashMap<MMM_ITextureEntity, int[]>(stackGetTexturePack);
		stackGetTexturePack.clear();
		for (Entry<MMM_ITextureEntity, int[]> le : lmap.entrySet()) {
			postGetTexturePack(le.getKey(), le.getValue());
		}
	}

}
