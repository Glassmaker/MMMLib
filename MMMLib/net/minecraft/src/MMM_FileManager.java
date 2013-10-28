package net.minecraft.src;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarFile;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * mods�f�B���N�g���̊l����minecraft�{�̂�jar���l�����A
 * �����Ɋ܂܂��w�肳�ꂽ��������܂�zip�����邩�ǂ����𔻒肷��B
 *
 */
public class MMM_FileManager {

	public static File minecraftJar;
	public static Map<String, List<File>> fileList = new TreeMap<String, List<File>>();
	public static File minecraftDir;
	public static File versionDir;
	public static File modDir;
	public static File assetsDir;

	
	public static void init() {
		// ����
		if (MMM_Helper.isClient) {
			minecraftDir = MMM_Helper.mc.mcDataDir;
		} else {
			minecraftDir = MinecraftServer.getServer().getFile("");
		}
		
		// mincraft.jar���擾
		// �J�����p��Jar���Ɋ܂܂�Ă��邱�Ƃ̑΍�
		try {
			ProtectionDomain ls1 = BaseMod.class.getProtectionDomain();
			CodeSource ls2 = ls1.getCodeSource();
			URL ls3 = ls2.getLocation();
			URI ls4 = ls3.toURI();
			minecraftJar = new File(ls4);
//			minecraftJar = new File(BaseMod.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//			mod_MMM_MMMLib.Debug(String.format("getMincraftFile-file:%s", minecraftJar.getName()));
			mod_MMM_MMMLib.Debug("getMinecraftFile-file:%s", minecraftJar.getAbsolutePath());
		} catch (Exception exception) {
			mod_MMM_MMMLib.Debug("getMinecraftFile-Exception.");
		}
		if (minecraftJar == null) {
			try {
				ClassLoader lcl1 = BaseMod.class.getClassLoader();
				String lcls1 = BaseMod.class.getName().concat(".class");
				URL lclu1 = lcl1.getResource(lcls1);
				JarURLConnection lclc1 = (JarURLConnection)lclu1.openConnection();
				JarFile lclj1 = lclc1.getJarFile();
				minecraftJar = new File(lclj1.getName());
				mod_MMM_MMMLib.Debug("getMinecraftFile-file:%s", lclj1.getName());
			} catch (Exception exception) {
				mod_MMM_MMMLib.Debug("getMinecraftFile-Exception.");
			}
		}
		if (minecraftJar == null) {
			String ls = System.getProperty("java.class.path");
			int li = ls.indexOf(';');
			if (li > -1) {
				ls = ls.substring(0, li);
			}
			minecraftJar = new File(ls);
			mod_MMM_MMMLib.Debug("getMinecraftFile-file:%s", ls);
		}
		if (!MMM_Helper.isForge && MMM_Helper.isClient) {
			File lversions = new File(minecraftDir, "versions");
			versionDir = new File(lversions, MMM_Client.getVersionString());
			if (lversions.exists() && lversions.isDirectory() && versionDir.exists() && versionDir.isDirectory()) {
				modDir = new File(versionDir, "/mods/");
			} else {
				modDir = new File(minecraftDir, "mods");
			}
		} else {
			modDir = new File(minecraftDir, "mods");
		}
		mod_MMM_MMMLib.Debug("getMods-Directory:%s", modDir.getAbsolutePath());
		
		if (MMM_Helper.isClient) {
			try {
				assetsDir = (File)ModLoader.getPrivateValue(Minecraft.class, MMM_Helper.mc, 42);
			} catch (Exception e) {
				e.printStackTrace();
				assetsDir = new File(minecraftDir, "assets");
			}
			mod_MMM_MMMLib.Debug("getAssets-Directory:%s", assetsDir.getAbsolutePath());
		} else {
			// �T�[�o�[���ł͎g���Ȃ��͂��B
		}
		
	}

	/**
	 * MOD�f�B���N�g���Ɋ܂܂��Ώۃt�@�C���̃I�u�W�F�N�g���擾�B
	 * @param pname �������X�g���́AgetFileList()�Ŏg���B
	 * @param pprefix ���̕�����̊܂܂��t�@�C����񋓂���B
	 * @return �񋓂��ꂽ�t�@�C���̃��X�g�B
	 */
	public static List<File> getModFile(String pname, String pprefix) {
		// �����ς݂��ǂ����̔���
		List<File> llist;
		if (fileList.containsKey(pname)) {
			llist = fileList.get(pname);
		} else {
			llist = new ArrayList<File>();
			fileList.put(pname, llist);
		}
		
		mod_MMM_MMMLib.Debug("getModFile:[%s]:%s", pname, modDir.getAbsolutePath());
		// �t�@�C���E�f�B���N�g��������
		try {
			if (modDir.isDirectory()) {
				mod_MMM_MMMLib.Debug("getModFile-get:%d.", modDir.list().length);
				for (File t : modDir.listFiles()) {
					if (t.getName().indexOf(pprefix) != -1) {
						if (t.getName().endsWith(".zip")) {
							llist.add(t);
							mod_MMM_MMMLib.Debug("getModFile-file:%s", t.getName());
						} else if (t.isDirectory()) {
							llist.add(t);
							mod_MMM_MMMLib.Debug("getModFile-file:%s", t.getName());
						}
					}
				}
				mod_MMM_MMMLib.Debug("getModFile-files:%d", llist.size());
			} else {
				// �܂����肦�Ȃ�
				mod_MMM_MMMLib.Debug("getModFile-fail.");
			}
			return llist;
		}
		catch (Exception exception) {
			mod_MMM_MMMLib.Debug("getModFile-Exception.");
			return null;
		}
	}

	/**
	 * �����ς݂̃��X�g�Ɋ܂܂��񋓃t�@�C����Ԃ��B
	 * @param pname �������X�g���B
	 * @return �񋓂��ꂽ�t�@�C���̃��X�g�B
	 */
	public static List<File> getFileList(String pname) {
		return fileList.get(pname);
	}


}
