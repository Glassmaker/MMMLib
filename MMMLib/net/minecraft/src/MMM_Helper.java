package net.minecraft.src;

import static net.minecraft.src.mod_MMM_MMMLib.Debug;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.inventory.Slot;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;

public class MMM_Helper {

	public static final boolean isClient;
	public static final Package fpackage;
	public static final String packegeBase;
	public static final boolean isForge = ModLoader.isModLoaded("Forge");
	public static final Minecraft mc;
	public static Method methGetSmeltingResultForge = null;
	public static Class entityRegistry = null;
	public static Method registerModEntity = null;
	protected static final Map<Class, Class>replaceEntitys = new HashMap<Class, Class>();
	protected static Map<String, Integer> entityIDList = new HashMap<String, Integer>();
	
	static {
		fpackage = ModLoader.class.getPackage();
		packegeBase = fpackage == null ? "" : fpackage.getName().concat(".");

		Minecraft lm = null;
		try {
			lm = ModLoader.getMinecraftInstance();
		} catch (Exception e) {
//			e.printStackTrace();
		} catch (Error e) {
//			e.printStackTrace();
		}
		mc = lm;
		isClient = mc != null;
		if (isForge) {
			try {
				methGetSmeltingResultForge = FurnaceRecipes.class.getMethod("getExperience", ItemStack.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				entityRegistry = getNameOfClass("cpw.mods.fml.common.registry.EntityRegistry");
				registerModEntity = entityRegistry.getMethod("registerModEntity",
						Class.class, String.class, int.class, Object.class, int.class, int.class, boolean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Œ»�Ý‚ÌŽÀ�sŠÂ‹«‚ªƒ��[ƒJƒ‹‚©‚Ç‚¤‚©‚ð”»’è‚·‚é�B
	 */
	public static boolean isLocalPlay() {
		return isClient && mc.isIntegratedServerRunning();
	}

	/**
	 * ƒ}ƒ‹ƒ`‘Î‰ž—p�B
	 * ItemStack‚É�î•ñ�X�V‚ð�s‚¤‚Æ�AƒT�[ƒo�[‘¤‚Æ‚Ì�·ˆÙ‚©‚çSlot‚ÌƒAƒbƒvƒf�[ƒg‚ª�s‚í‚ê‚é�B
	 * ‚»‚Ì�Û�AUsingItem‚Ì�X�V�ˆ—�‚ª�s‚í‚ê‚È‚¢‚½‚ßˆá‚¤ƒAƒCƒeƒ€‚ÉŽ�‘Ö‚¦‚ç‚ê‚½‚Æ”»’è‚³‚ê‚é�B
	 * ‚±‚±‚Å‚Í”äŠr—p‚ÉŽg‚í‚ê‚éƒXƒ^ƒbƒNƒŠƒXƒg‚ð‹­�§“I‚É�‘Š·‚¦‚éŽ–‚É‚æ‚è‘Î‰ž‚µ‚½�B
	 */
	public static void updateCheckinghSlot(Entity pEntity, ItemStack pItemstack) {
		if (pEntity instanceof EntityPlayerMP) {
			// ƒT�[ƒo�[‘¤‚Å‚Ì‚Ý�ˆ—�
			EntityPlayerMP lep = (EntityPlayerMP)pEntity;
			Container lctr = lep.openContainer;
			for (int li = 0; li < lctr.inventorySlots.size(); li++) {
				ItemStack lis = ((Slot)lctr.getSlot(li)).getStack(); 
				if (lis == pItemstack) {
					lctr.inventoryItemStacks.set(li, pItemstack.copy());
					break;
				}
			}
		}
	}
	
	/**
	 * Forge—pƒNƒ‰ƒXŠl“¾�B
	 */
	public static Class getForgeClass(BaseMod pMod, String pName) {
		if (isForge) {
			pName = pName.concat("_Forge");
		}
		return getNameOfClass(pName);
	}

	/**
	 * –¼‘O‚©‚çƒNƒ‰ƒX‚ðŠl“¾‚·‚é
	 */
	public static Class getNameOfClass(String pName) {
		if (fpackage != null) {
			pName = fpackage.getName() + "." + pName;
		}
		Class lclass = null;
		try {
			lclass = Class.forName(pName);
		} catch (Exception e) {
			mod_MMM_MMMLib.Debug("Class:%s is not found.", pName);
		}
		
		return lclass;
	}

	/**
	 * ‘—�M—pƒf�[ƒ^‚ÌƒZƒbƒg
	 */
	public static void setValue(byte[] pData, int pIndex, int pVal, int pSize) {
		for (int li = 0; li < pSize; li++) {
			pData[pIndex++] = (byte)(pVal & 0xff);
			pVal = pVal >>> 8;
		}
	}
	
	public static void setInt(byte[] pData, int pIndex, int pVal) {
		pData[pIndex + 3]	= (byte)(pVal & 0xff);
		pData[pIndex + 2]	= (byte)((pVal >>> 8) & 0xff);
		pData[pIndex + 1]	= (byte)((pVal >>> 16) & 0xff);
		pData[pIndex + 0]	= (byte)((pVal >>> 24) & 0xff);
	}
	
	public static int getInt(byte[] pData, int pIndex) {
		return (pData[pIndex + 3] & 0xff) | ((pData[pIndex + 2] & 0xff) << 8) | ((pData[pIndex + 1] & 0xff) << 16) | ((pData[pIndex + 0] & 0xff) << 24);
	}

	public static void setFloat(byte[] pData, int pIndex, float pVal) {
		setInt(pData, pIndex, Float.floatToIntBits(pVal));
	}

	public static float getFloat(byte[] pData, int pIndex) {
		return Float.intBitsToFloat(getInt(pData, pIndex));
	}

	public static void setShort(byte[] pData, int pIndex, int pVal) {
		pData[pIndex++]	= (byte)(pVal & 0xff);
		pData[pIndex]	= (byte)((pVal >>> 8) & 0xff);
	}

	public static short getShort(byte[] pData, int pIndex) {
		return (short)((pData[pIndex] & 0xff) | ((pData[pIndex + 1] & 0xff) << 8));
	}

	public static String getStr(byte[] pData, int pIndex, int pLen) {
		String ls = new String(pData, pIndex, pLen);
		return ls;
	}
	public static String getStr(byte[] pData, int pIndex) {
		return getStr(pData, pIndex, pData.length - pIndex);
	}

	public static void setStr(byte[] pData, int pIndex, String pVal) {
		byte[] lb = pVal.getBytes();
		for (int li = pIndex; li < pData.length; li++) {
			pData[li] = lb[li - pIndex];
		}
	}

	// �ó‹µ”»’f—vŠÖ�”ŒQ
	protected static boolean canBlockBeSeen(Entity pEntity, int x, int y, int z, boolean toTop, boolean do1, boolean do2) {
		// ƒuƒ�ƒbƒN‚Ì‰ÂŽ‹”»’è
		Vec3 vec3d = Vec3.createVectorHelper(pEntity.posX, pEntity.posY + pEntity.getEyeHeight(), pEntity.posZ);
		Vec3 vec3d1 = Vec3.createVectorHelper((double)x + 0.5D, (double)y + (toTop ? 0.9D : 0.5D), (double)z + 0.5D);
		
		MovingObjectPosition movingobjectposition = pEntity.worldObj.rayTraceBlocks_do_do(vec3d, vec3d1, do1, do2);
		if (movingobjectposition == null) {
			return false;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			if (movingobjectposition.blockX == MathHelper.floor_double(vec3d1.xCoord) && 
				movingobjectposition.blockY == MathHelper.floor_double(vec3d1.yCoord) &&
				movingobjectposition.blockZ == MathHelper.floor_double(vec3d1.zCoord)) {
				return true;
			}
		}
		return false;
	}

	public static boolean setPathToTile(EntityLiving pEntity, TileEntity pTarget, boolean flag) {
		// Tile‚Ü‚Å‚ÌƒpƒX‚ð�ì‚é
		PathNavigate lpn = pEntity.getNavigator();
		float lspeed = 1.0F;
		// Œü‚«‚É�‡‚í‚¹‚Ä‹——£‚ð’²�®
		int i = (pTarget.yCoord == MathHelper.floor_double(pEntity.posY) && flag) ? 2 : 1;
		switch (pEntity.worldObj.getBlockMetadata(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord)) {
		case 3:
			return lpn.tryMoveToXYZ(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord + i, lspeed);
		case 2:
			return lpn.tryMoveToXYZ(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord - i, lspeed);
		case 5:
			return lpn.tryMoveToXYZ(pTarget.xCoord + 1, pTarget.yCoord, pTarget.zCoord, lspeed);
		case 4:
			return lpn.tryMoveToXYZ(pTarget.xCoord - i, pTarget.yCoord, pTarget.zCoord, lspeed);
		default:
			return lpn.tryMoveToXYZ(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord, lspeed);
		}
	}

	/**
	 * ModloaderŠÂ‹«‰º‚Å‹ó‚¢‚Ä‚¢‚éEntityID‚ð•Ô‚·�B
	 * —LŒø‚È’l‚ðŠl“¾‚Å‚«‚È‚¯‚ê‚Î-1‚ð•Ô‚·�B
	 */
	private static int getNextEntityID(boolean isLiving) {
		if (isLiving) {
			// �¶•¨—p
			for (int li = 1; li < 256; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		} else {
			// •¨—p
			for (int li = mod_MMM_MMMLib.cfg_startVehicleEntityID; li < mod_MMM_MMMLib.cfg_startVehicleEntityID + 2048; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		}
		return -1;
	}

	/**
	 * Entity‚ð“o˜^‚·‚é�B
	 * RML�AForge—¼‘Î‰ž�B
	 * @param entityclass
	 * @param entityName
	 * @param defaultId
	 * 0 : ƒI�[ƒgƒAƒTƒCƒ“
	 * @param mod
	 * @param uniqueModeName
	 * @param trackingRange
	 * @param updateFrequency
	 * @param sendVelocityUpdate
	 */
	public static int registerEntity(
			Class<? extends Entity> entityclass, String entityName, int defaultId,
			BaseMod mod, int trackingRange, int updateFrequency, boolean sendVelocityUpdate,
			int pEggColor1, int pEggColor2) {
		int lid = 0;
		lid = getModEntityID(mod.getName());
		if (isForge) {
			try {
				Method lmethod;
				// EntityID‚ÌŠl“¾
				lmethod = entityRegistry.getMethod("findGlobalUniqueEntityId");
				defaultId = (Integer)lmethod.invoke(null);
				
				if (pEggColor1 == 0 && pEggColor2 == 0) {
					lmethod = entityRegistry.getMethod("registerGlobalEntityID",
							Class.class, String.class, int.class);
					lmethod.invoke(null, entityclass, entityName, defaultId);
				} else {
					lmethod = entityRegistry.getMethod("registerGlobalEntityID",
							Class.class, String.class, int.class, int.class, int.class);
					lmethod.invoke(null, entityclass, entityName, defaultId, pEggColor1, pEggColor2);
				}
				// EntityList‚Ö‚Ì“o˜^‚Í“K“–‚È�”Žš‚Å‚æ‚¢�B
				registerModEntity.invoke(
						null, entityclass, entityName, lid,
						mod, trackingRange, updateFrequency, sendVelocityUpdate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// EntityList‚Ö‚Ì“o˜^‚Í
			if (defaultId == 0) {
				defaultId = getNextEntityID(entityclass.isAssignableFrom(EntityLivingBase.class));
			}
			if (pEggColor1 == 0 && pEggColor2 == 0) {
				ModLoader.registerEntityID(entityclass, entityName, defaultId);
			} else {
				ModLoader.registerEntityID(entityclass, entityName, defaultId, pEggColor1, pEggColor2);
			}
			ModLoader.addEntityTracker(mod, entityclass, defaultId, trackingRange, updateFrequency, sendVelocityUpdate);
		}
		Debug("RegisterEntity ID:%d / %s-%d : %s", defaultId, mod.getName(), lid, entityName);
		return defaultId;
	}
	public static int registerEntity(
			Class<? extends Entity> entityclass, String entityName, int defaultId,
			BaseMod mod, int trackingRange, int updateFrequency, boolean sendVelocityUpdate) {
		return registerEntity(entityclass, entityName, defaultId, mod, trackingRange, updateFrequency, sendVelocityUpdate, 0, 0);
	}

	private static int getModEntityID(String uniqueModeName) {
		int li = 0;
		if (entityIDList.containsKey(uniqueModeName)) {
			li = entityIDList.get(uniqueModeName);
		}
		entityIDList.put(uniqueModeName, li + 1);
		return li;
	}

	/**
	 * Entity‚ð•Ô‚·�B
	 */
	public static Entity getEntity(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(MMM_Helper.getInt(pData, pIndex));
	}

	/**
	 * •Ï�”�uavatar�v‚©‚ç’l‚ðŽæ‚è�o‚µ–ß‚è’l‚Æ‚µ‚Ä•Ô‚·�B
	 * avatar‚ª‘¶�Ý‚µ‚È‚¢�ê�‡‚ÍŒ³‚Ì’l‚ð•Ô‚·�B
	 * avatar‚ÍEntityLivingŒÝŠ·�B
	 */
	public static Entity getAvatarEntity(Entity pEntity){
		// littleMaid—pƒR�[ƒh‚±‚±‚©‚ç
		if (pEntity == null) return null;
		try {
			// ŽËŽè‚Ì�î•ñ‚ðEntityLittleMaidAvatar‚©‚çEntityLittleMaid‚Ö’u‚«Š·‚¦‚é
			Field field = pEntity.getClass().getField("avatar");
			pEntity = (EntityLivingBase)field.get(pEntity);
		} catch (NoSuchFieldException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		// ‚±‚±‚Ü‚Å
		return pEntity;
	}

	/**
	 * •Ï�”�umaidAvatar�v‚©‚ç’l‚ðŽæ‚è�o‚µ–ß‚è’l‚Æ‚µ‚Ä•Ô‚·�B
	 * maidAvatar‚ª‘¶�Ý‚µ‚È‚¢�ê�‡‚ÍŒ³‚Ì’l‚ð•Ô‚·�B
	 * maidAvatar‚ÍEntityPlayerŒÝŠ·�B
	 */
	public static Entity getAvatarPlayer(Entity entity) {
		// ƒ�ƒCƒh‚³‚ñƒ`ƒFƒbƒN
		try {
			Field field = entity.getClass().getField("maidAvatar");
			entity = (Entity)field.get(entity);
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
		}
		return entity;
	}

	/**
	 * ƒvƒŒ�[ƒ„‚ÌƒCƒ“ƒxƒ“ƒgƒŠ‚©‚çƒAƒCƒeƒ€‚ðŒ¸‚ç‚·
	 */
	protected static ItemStack decPlayerInventory(EntityPlayer par1EntityPlayer, int par2Index, int par3DecCount) {
		if (par1EntityPlayer == null) {
			return null;
		}
		
		if (par2Index == -1) {
			par2Index = par1EntityPlayer.inventory.currentItem;
		}
		ItemStack itemstack1 = par1EntityPlayer.inventory.getStackInSlot(par2Index);
		if (itemstack1 == null) {
			return null;
		}
		
		if (!par1EntityPlayer.capabilities.isCreativeMode) {
			// ƒNƒŠƒGƒCƒeƒBƒu‚¾‚ÆŒ¸‚ç‚È‚¢
			itemstack1.stackSize -= par3DecCount;
		}
		
		if (itemstack1.getItem() instanceof ItemPotion) {
			if(itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Item.glassBottle, par3DecCount));
				return null;
			} else {
				par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle, par3DecCount));
			}
		} else {
			if (itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par2Index, null);
				return null;
			}
		}
		
		return itemstack1;
	}

	protected static float convRevision(String pRev) {
		Pattern lp = Pattern.compile("(\\d+)(\\w*)");
		Matcher lm = lp.matcher(pRev);
		float lf = 0;
		if (lm.find()) {
			lf = Integer.valueOf(lm.group(1));
			if (!lm.group(2).isEmpty()) {
				lf += (float)(lm.group(2).charAt(0) - 96) * 0.01;
			}
		}
		return lf;
	}
	protected static float convRevision() {
		return convRevision(mod_MMM_MMMLib.Revision);
	}

	/**
	 * Žw’è‚³‚ê‚½ƒŠƒrƒWƒ‡ƒ“‚æ‚è‚àŒÃ‚¯‚ê‚Î—áŠO‚ð“Š‚°‚ÄƒXƒgƒbƒv
	 */
	public static void checkRevision(String pRev) {
		if (convRevision() < convRevision(pRev)) {
			// “K�‡ƒo�[ƒWƒ‡ƒ“‚Å‚Í‚È‚¢‚Ì‚ÅƒXƒgƒbƒv
			ModLoader.getLogger().warning("you must check MMMLib revision.");
			throw new RuntimeException("The revision of MMMLib is old.");
		}
	}

	/**
	 * EntityList‚É“o˜^‚³‚ê‚Ä‚¢‚¢‚éEntity‚ð’u‚«Š·‚¦‚é�B
	 */
	public static void replaceEntityList(Class pSrcClass, Class pDestClass) {
		// EntityList“o˜^�î•ñ‚ð’u‚«Š·‚¦
		// ŒÃ‚¢Entity‚Å‚àƒXƒ|�[ƒ“‚Å‚«‚é‚æ‚¤‚Éˆê•”‚Ì•¨‚Í“ñ�d“o˜^
		try {
			// stringToClassMapping
			Map lmap;
			int lint = 0;
			String ls = "";
			lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 0);
			for (Entry<String, Class> le : ((Map<String, Class>)lmap).entrySet()) {
				if (le.getValue() == pSrcClass) {
					le.setValue(pDestClass);
				}
			}
			// classToStringMapping
			lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 1);
			if (lmap.containsKey(pSrcClass)) {
				ls = (String)lmap.get(pSrcClass);
//				lmap.remove(pSrcClass);
				lmap.put(pDestClass, ls);
			}
			// IDtoClassMapping
			lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 2);
			for (Entry<Integer, Class> le : ((Map<Integer, Class>)lmap).entrySet()) {
				if (le.getValue() == pSrcClass) {
					le.setValue(pDestClass);
				}
			}
			// classToIDMapping
			lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 3);
			if (lmap.containsKey(pSrcClass)) {
				lint = (Integer)lmap.get(pSrcClass);
//				lmap.remove(pSrcClass);
				lmap.put(pDestClass, lint);
			}
			replaceEntitys.put(pSrcClass, pDestClass);
			Debug("Replace %s -> %s(EntityListID: %d, EntityListString: %s)", pSrcClass.getSimpleName(), pDestClass.getSimpleName(), lint, ls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void replaceCreatureList(List<SpawnListEntry> pMobs) {
		if (pMobs == null) return;
		for (Entry<Class, Class> le : replaceEntitys.entrySet()) {
			for (int j = 0; j < pMobs.size(); j++) {
				if (pMobs.get(j).entityClass == le.getKey()) {
					pMobs.get(j).entityClass = le.getValue();
					Debug("ReplaceCreatureList: %s -> %s", le.getKey().getSimpleName(), le.getValue().getSimpleName());
				}
			}
		}
	}

	/**
	 * ƒoƒCƒI�[ƒ€‚Ì�Ý’èEntity‚ð’u‚«Š·‚¦‚ç‚ê‚½Entity‚Ö’u‚«Š·‚¦‚é�B
	 * Šî–{“I‚ÉMMMLibˆÈŠO‚©‚ç‚ÍŒÄ‚Î‚ê‚È‚¢�B
	 */
	protected static void replaceBaiomeSpawn() {
		// ƒoƒCƒI�[ƒ€‚Ì”­�¶�ˆ—�‚ð‚Ì‚Á‚Æ‚é
		// TODO: fix the commented
		if (replaceEntitys.isEmpty()) return;
		for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
			if (BiomeGenBase.biomeList[i] == null) continue;
			List<SpawnListEntry> mobs;
			Debug("ReplaceBaiomeSpawn:%s", BiomeGenBase.biomeList[i].biomeName);
			/*Debug("[Creature]");
			replaceCreatureList(BiomeGenBase.biomeList[i].spawnableCreatureList);
			Debug("[WaterCreature]");
			replaceCreatureList(BiomeGenBase.biomeList[i].spawnableWaterCreatureList);
			Debug("[CaveCreature]");
			replaceCreatureList(BiomeGenBase.biomeList[i].spawnableCaveCreatureList);
			Debug("[Monster]");
			replaceCreatureList(BiomeGenBase.biomeList[i].spawnableMonsterList);*/
		}
	}

	/**
	 * Ž‹�ü‚Ì�æ‚É‚¢‚é�Å�‰‚ÌEntity‚ð•Ô‚·
	 * @param pEntity
	 * Ž‹“_
	 * @param pRange
	 * Ž‹�ü‚Ì—LŒø‹——£
	 * @param pDelta
	 * Žž��•â�³
	 * @param pExpand
	 * ŒŸ’m—Ìˆæ‚ÌŠg‘å”ÍˆÍ
	 * @return
	 */
	public static Entity getRayTraceEntity(EntityLivingBase pEntity, double pRange, float pDelta, float pExpand) {
		Vec3 lvpos = pEntity.worldObj.getWorldVec3Pool().getVecFromPool(
				pEntity.posX, pEntity.posY + pEntity.getEyeHeight(), pEntity.posZ);
//		Vec3 lvpos = pEntity.getPosition(pDelta).addVector(0D, pEntity.getEyeHeight(), 0D);
		Vec3 lvlook = pEntity.getLook(pDelta);
		Vec3 lvview = lvpos.addVector(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange);
		Entity ltarget = null;
		List llist = pEntity.worldObj.getEntitiesWithinAABBExcludingEntity(pEntity, pEntity.boundingBox.addCoord(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange).expand((double)pExpand, (double)pExpand, (double)pExpand));
		double ltdistance = pRange * pRange;
		
		for (int var13 = 0; var13 < llist.size(); ++var13) {
			Entity lentity = (Entity)llist.get(var13);
			
			if (lentity.canBeCollidedWith()) {
				float lexpand = lentity.getCollisionBorderSize() + 0.3F;
				AxisAlignedBB laabb = lentity.boundingBox.expand((double)lexpand, (double)lexpand, (double)lexpand);
				MovingObjectPosition lmop = laabb.calculateIntercept(lvpos, lvview);
				
				if (laabb.isVecInside(lvpos)) {
					if (0.0D < ltdistance || ltdistance == 0.0D) {
						ltarget = lentity;
						ltdistance = 0.0D;
					}
				} else if (lmop != null) {
					double ldis = lvpos.squareDistanceTo(lmop.hitVec);
					
					if (ldis < ltdistance || ltdistance == 0.0D) {
						ltarget = lentity;
						ltdistance = ldis;
					}
				}
			}
		}
		return ltarget;
	}


	// Forge‘Î�ô

	/**
	 * Forge‘Î�ô—p‚Ìƒ�ƒ\ƒbƒh
	 */
	public static ItemStack getSmeltingResult(ItemStack pItemstack) {
		if (methGetSmeltingResultForge != null) {
			try {
				return (ItemStack)methGetSmeltingResultForge.invoke(FurnaceRecipes.smelting(), pItemstack);
			}catch (Exception e) {
			}
		}
		return FurnaceRecipes.smelting().getSmeltingResult(pItemstack.itemID);
	}

	/**
	 * ƒAƒCƒeƒ€‚É’Ç‰ÁŒø‰Ê‚ª�Ý‚é‚©‚ð”»’è‚·‚é�B
	 * Forge‘Î�ô�B
	 * @param pItemStack
	 * @return
	 */
	public static boolean hasEffect(ItemStack pItemStack) {
		// ƒ}ƒWClientSIDE‚Æ‚©Ž«‚ß‚Ä‚Ù‚µ‚¢�B
		if (pItemStack != null) {
			Item litem = pItemStack.getItem();
			if (litem instanceof ItemPotion) {
				List llist = ((ItemPotion)litem).getEffects(pItemStack);
				return llist != null && !llist.isEmpty();
			}
		}
		return false;
	}

	/**
	 * Block‚ÌƒCƒ“ƒXƒ^ƒ“ƒX‚ð’u‚«Š·‚¦‚é�B
	 * static final‚Ì•Ï�”‚É‘Î‚µ‚Ä�s‚¤‚Ì‚ÅForge‚Å‚Í–³Œø�B
	 * @param pOriginal
	 * @param pReplace
	 * @return
	 */
	public static boolean replaceBlock(Block pOriginal, Block pReplace) {
		if (isForge) {
			return false;
		}
		try {
			// Block‚Ìstatic final•ª‚Ì’uŠ·‚¦
			Field[] lfield = Block.class.getDeclaredFields();
			for (int li = 0; li < lfield.length; li++) {
				if (!Modifier.isStatic(lfield[li].getModifiers())) {
					// staticˆÈŠO‚Í‘Î�ÛŠO
					continue;
				}
				
				Object lobject = lfield[li].get(null);
				if (lobject == pOriginal) {
					ModLoader.setPrivateValue(Block.class, null, li, pReplace);
					return true;
				}
			}
		}
		catch(Exception exception) {
		}
		return false;
	}

	/**
	 * 16�i�”‚Ì•¶Žš—ñ‚ðInt‚Ö•ÏŠ·‚·‚é�B
	 * 0xffffffff‘Î�ô�B
	 * @param pValue
	 * @return
	 */
	public static int getHexToInt(String pValue) {
		String ls = "00000000".concat(pValue);
		int llen = ls.length();
		int li = Integer.parseInt(ls.substring(llen - 4, llen), 16);
		int lj = Integer.parseInt(ls.substring(llen - 8, llen - 4), 16);
		return (lj << 16) | li;
	}

	/**
	 *  ƒAƒCƒeƒ€‚É�Ý’è‚³‚ê‚½�UŒ‚—Í‚ðŒ©‚é
	 * @param pItemStack
	 * @return
	 */
	public static double getAttackVSEntity(ItemStack pItemStack) {
		AttributeModifier lam = (AttributeModifier)pItemStack.getAttributeModifiers().get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		return lam == null ? 0 : lam.getAmount(); 
	}

}
