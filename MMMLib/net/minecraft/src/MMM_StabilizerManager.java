package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 追加パーツたるスタビライザーを管理する
 * I manage a barrel stabilizer added parts
 */
public class MMM_StabilizerManager extends MMM_ManagerBase {

	public static final String preFix = "ModelStabilizer";
	public static Map<String, MMM_ModelStabilizerBase> stabilizerList = new TreeMap<String, MMM_ModelStabilizerBase>();
	
	
	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
		// Earn the mod files with the prefix a specific name
		MMM_FileManager.getModFile("Stabilizer", preFix);
	}

	public static void loadStabilizer() {
		(new MMM_StabilizerManager()).load();
	}

	@Override
	protected String getPreFix() {
		return preFix;
	}

	@Override
	protected boolean append(Class pclass) {
		if (!(MMM_ModelStabilizerBase.class).isAssignableFrom(pclass)) {
			return false;
		}
		
		try {
			MMM_ModelStabilizerBase lms = (MMM_ModelStabilizerBase)pclass.newInstance();
			stabilizerList.put(lms.getName(), lms);
			return true;
		} catch (Exception e) {
		}
		
		return false;
	}

	/**
	 * 指定された名称のスタビライザーモデルを返す。
	 * I return the stabilizer model of the specified name.
	 */
	public static MMM_EquippedStabilizer getStabilizer(String pname, String pequippoint) {
		if (!stabilizerList.containsKey(pname)) {
			return null;
		}
		
		MMM_EquippedStabilizer lequip = new MMM_EquippedStabilizer();
		lequip.stabilizer = stabilizerList.get(pname);
		lequip.stabilizer.init(lequip);
		lequip.equipPointName = pequippoint;
		
		return lequip;
	}

	/**
	 * 実装場所のアップデート
	 * Update of installation location
	 */
	public static void updateEquippedPoint(Map<String, MMM_EquippedStabilizer> pMap, MMM_ModelBase pModel) {
		for (Entry<String, MMM_EquippedStabilizer> le : pMap.entrySet()) {
			le.getValue().updateEquippedPoint(pModel);
		}
	}

}
