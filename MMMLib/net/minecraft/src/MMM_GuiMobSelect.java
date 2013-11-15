package net.minecraft.src;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class MMM_GuiMobSelect extends GuiScreen {

	public Map<String, Entity> entityMap;
	public static Map<Class, String> entityMapClass = new HashMap<Class, String>();
	public static List<String> exclusionList = new ArrayList<String>();
	
	protected String screenTitle;
	protected GuiSlot selectPanel;



	public MMM_GuiMobSelect(World pWorld) {
		entityMap = new TreeMap<String, Entity>();
		initEntitys(pWorld, true);
	}

	public MMM_GuiMobSelect(World pWorld, Map<String, Entity> pMap) {
		entityMap = pMap;
		initEntitys(pWorld, false);
	}


	public void initEntitys(World world, boolean pForce) {
		// ・ｽ\・ｽ・ｽ・ｽpEntityList・ｽﾌ擾ｿｽ・ｽ・ｽ
		if (entityMapClass.isEmpty()) {
			try {
				Map lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 1);
				entityMapClass.putAll(lmap);
			}
			catch (Exception e) {
				mod_MMM_MMMLib.Debug("EntityClassMap copy failed.");
			}
		}
		
		if (entityMap == null) return;
		if (!pForce && !entityMap.isEmpty()) return;
		
		for (Map.Entry<Class, String> le : entityMapClass.entrySet()) {
			if (Modifier.isAbstract(le.getKey().getModifiers())) continue;
			int li = 0;
			Entity lentity = null;
			try {
				// ・ｽ\・ｽ・ｽ・ｽp・ｽ・ｽEntity・ｽ・ｽ・ｽ・ｽ・ｽ
				do {
					lentity = (EntityLivingBase)le.getKey().getConstructor(World.class).newInstance(world);
//					lentity = (EntityLivingBase)EntityList.createEntityByName(le.getValue(), world);
				} while (lentity != null && checkEntity(le.getValue(), lentity, li++));
			} catch (Exception e) {
				mod_MMM_MMMLib.Debug("Entity [" + le.getValue() + "] can't created.");
			}
		}
	}

	/**
	 * ・ｽn・ｽ・ｽ・ｽ黷ｽEntity・ｽﾌチ・ｽF・ｽb・ｽN・ｽy・ｽﾑ会ｿｽ・ｽH・ｽB
	 * true・ｽ・ｽﾔゑｿｽ・ｽﾆ難ｿｽ・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽﾌエ・ｽ・ｽ・ｽe・ｽB・ｽe・ｽB・ｽ・ｽ・ｽﾄ度・ｽn・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽA・ｽ・ｽ・ｽﾌとゑｿｽpIndex・ｽﾍカ・ｽE・ｽ・ｽ・ｽg・ｽA・ｽb・ｽv・ｽ・ｽ・ｽ・ｽ・ｽ
	 */
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		entityMap.put(pName, pEntity);
		return false;
	}

	@Override
	public void initGui() {
		selectPanel = new MMM_GuiSlotMobSelect(mc, this);
		selectPanel.registerScrollButtons(3, 4);
	}

	@Override
	public void drawScreen(int px, int py, float pf) {
		float lhealthScale = BossStatus.healthScale;
		int lstatusBarLength = BossStatus.statusBarLength;
		String lbossName = BossStatus.bossName;
		boolean lfield_82825_d = BossStatus.field_82825_d;
		
		drawDefaultBackground();
		selectPanel.drawScreen(px, py, pf);
		drawCenteredString(fontRenderer, StatCollector.translateToLocal(screenTitle), width / 2, 20, 0xffffff);
		super.drawScreen(px, py, pf);
		
		// GUI・ｽﾅ表・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌボ・ｽX・ｽﾌス・ｽe・ｽ[・ｽ^・ｽX・ｽ・ｽ\・ｽ・ｽ・ｽ・ｽ・ｽﾈゑｿｽ
		BossStatus.healthScale = lhealthScale;
		BossStatus.statusBarLength = lstatusBarLength;
		BossStatus.bossName = lbossName;
		BossStatus.field_82825_d = lfield_82825_d;
	}

	/**
	 *  ・ｽX・ｽ・ｽ・ｽb・ｽg・ｽ・ｽ・ｽN・ｽ・ｽ・ｽb・ｽN・ｽ・ｽ・ｽ黷ｽ
	 */
	public abstract void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity);

	/**
	 *  ・ｽX・ｽ・ｽ・ｽb・ｽg・ｽﾌ描・ｽ・ｽ
	 */
	public abstract void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, Tessellator pTessellator, String pName, Entity pEntity);
	
}
