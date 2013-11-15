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
	 * ﾅ陳ｻ・ｽﾃ昶堙固ｽﾃ・ｽsﾅﾃやｹﾂｫ窶堋ｪﾆ抵ｿｽ・ｽ[ﾆ谷ﾆ停ｹ窶堋ｩ窶堙・堋､窶堋ｩ窶堙ｰ窶敖ｻ窶凖ｨ窶堋ｷ窶堙ｩ・ｽB
	 */
	public static boolean isLocalPlay() {
		return isClient && mc.isIntegratedServerRunning();
	}

	/**
	 * ﾆ筑ﾆ停ｹﾆ蛋窶佚寂ｰﾅｾ窶廃・ｽB
	 * ItemStack窶堙会ｿｽﾃｮ窶｢ﾃｱ・ｽX・ｽV窶堙ｰ・ｽs窶堋､窶堙・ｿｽAﾆ探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・堙鯉ｿｽﾂｷﾋ・吮堋ｩ窶堙ｧSlot窶堙姑但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖窶堋ｪ・ｽs窶堙ｭ窶堙ｪ窶堙ｩ・ｽB
	 * 窶堋ｻ窶堙鯉ｿｽﾃ幢ｿｽAUsingItem窶堙鯉ｿｽX・ｽV・ｽﾋ・費ｿｽ窶堋ｪ・ｽs窶堙ｭ窶堙ｪ窶堙遺堋｢窶堋ｽ窶堙淪・｡窶堋､ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙嫁ｽ・ｽ窶佚問堋ｦ窶堙ｧ窶堙ｪ窶堋ｽ窶堙・敖ｻ窶凖ｨ窶堋ｳ窶堙ｪ窶堙ｩ・ｽB
	 * 窶堋ｱ窶堋ｱ窶堙・堙坂敕､ﾅr窶廃窶堙嫁ｽg窶堙ｭ窶堙ｪ窶堙ｩﾆ湛ﾆ耽ﾆ鍛ﾆ誰ﾆ椎ﾆ湛ﾆ暖窶堙ｰ窶ｹﾂｭ・ｽﾂｧ窶廬窶堙会ｿｽ窶佛ﾂｷ窶堋ｦ窶堙ｩﾅｽ窶凪堙俄堙ｦ窶堙ｨ窶佚寂ｰﾅｾ窶堋ｵ窶堋ｽ・ｽB
	 */
	public static void updateCheckinghSlot(Entity pEntity, ItemStack pItemstack) {
		if (pEntity instanceof EntityPlayerMP) {
			// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・堙娯堙晢ｿｽﾋ・費ｿｽ
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
	 * Forge窶廃ﾆ誰ﾆ停ｰﾆ湛ﾅl窶慊ｾ・ｽB
	 */
	public static Class getForgeClass(BaseMod pMod, String pName) {
		if (isForge) {
			pName = pName.concat("_Forge");
		}
		return getNameOfClass(pName);
	}

	/**
	 * 窶督ｼ窶楼窶堋ｩ窶堙ｧﾆ誰ﾆ停ｰﾆ湛窶堙ｰﾅl窶慊ｾ窶堋ｷ窶堙ｩ
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
	 * 窶倪費ｿｽM窶廃ﾆ断・ｽ[ﾆ耽窶堙姑短ﾆ鍛ﾆ暖
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

	// ・ｽﾃｳ窶ｹﾂｵ窶敖ｻ窶冉窶牌ﾅﾃ厄ｿｽ窶敘嘆
	protected static boolean canBlockBeSeen(Entity pEntity, int x, int y, int z, boolean toTop, boolean do1, boolean do2) {
		// ﾆ置ﾆ抵ｿｽﾆ鍛ﾆ誰窶堙娯ｰﾃでｽ窶ｹ窶敖ｻ窶凖ｨ
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
		// Tile窶堙懌堙・堙姑恥ﾆ湛窶堙ｰ・ｽﾃｬ窶堙ｩ
		PathNavigate lpn = pEntity.getNavigator();
		float lspeed = 1.0F;
		// ﾅ津ｼ窶堋ｫ窶堙会ｿｽ窶｡窶堙ｭ窶堋ｹ窶堙・ｹ窶披板｣窶堙ｰ窶卍ｲ・ｽﾂｮ
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
	 * Modloaderﾅﾃやｹﾂｫ窶ｰﾂｺ窶堙・ｹﾃｳ窶堋｢窶堙・堋｢窶堙ｩEntityID窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
	 * 窶猫ﾅ津ｸ窶堙遺冤窶堙ｰﾅl窶慊ｾ窶堙・堋ｫ窶堙遺堋ｯ窶堙ｪ窶堙・1窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
	 */
	private static int getNextEntityID(boolean isLiving) {
		if (isLiving) {
			// ・ｽﾂｶ窶｢ﾂｨ窶廃
			for (int li = 1; li < 256; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		} else {
			// 窶｢ﾂｨ窶廃
			for (int li = mod_MMM_MMMLib.cfg_startVehicleEntityID; li < mod_MMM_MMMLib.cfg_startVehicleEntityID + 2048; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		}
		return -1;
	}

	/**
	 * Entity窶堙ｰ窶徙ﾋ弯窶堋ｷ窶堙ｩ・ｽB
	 * RML・ｽAForge窶板ｼ窶佚寂ｰﾅｾ・ｽB
	 * @param entityclass
	 * @param entityName
	 * @param defaultId
	 * 0 : ﾆ棚・ｽ[ﾆ暖ﾆ但ﾆ探ﾆ辰ﾆ停・
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
				// EntityID窶堙固l窶慊ｾ
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
				// EntityList窶堙問堙娯徙ﾋ弯窶堙坂廳窶懌凪堙茨ｿｽ窶敘ｽﾅ｡窶堙・堙ｦ窶堋｢・ｽB
				registerModEntity.invoke(
						null, entityclass, entityName, lid,
						mod, trackingRange, updateFrequency, sendVelocityUpdate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// EntityList窶堙問堙娯徙ﾋ弯窶堙・
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
	 * Entity窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
	 */
	public static Entity getEntity(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(MMM_Helper.getInt(pData, pIndex));
	}

	/**
	 * 窶｢ﾃ擾ｿｽ窶晢ｿｽuavatar・ｽv窶堋ｩ窶堙ｧ窶冤窶堙ｰﾅｽﾃｦ窶堙ｨ・ｽo窶堋ｵ窶禿溪堙ｨ窶冤窶堙・堋ｵ窶堙・｢ﾃ披堋ｷ・ｽB
	 * avatar窶堋ｪ窶伉ｶ・ｽﾃ昶堋ｵ窶堙遺堋｢・ｽﾃｪ・ｽ窶｡窶堙最陳ｳ窶堙娯冤窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
	 * avatar窶堙孔ntityLivingﾅ津敘ﾂｷ・ｽB
	 */
	public static Entity getAvatarEntity(Entity pEntity){
		// littleMaid窶廃ﾆ坦・ｽ[ﾆ檀窶堋ｱ窶堋ｱ窶堋ｩ窶堙ｧ
		if (pEntity == null) return null;
		try {
			// ﾅｽﾃ暁ｽﾃｨ窶堙鯉ｿｽﾃｮ窶｢ﾃｱ窶堙ｰEntityLittleMaidAvatar窶堋ｩ窶堙ｧEntityLittleMaid窶堙問冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ
			Field field = pEntity.getClass().getField("avatar");
			pEntity = (EntityLivingBase)field.get(pEntity);
		} catch (NoSuchFieldException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		// 窶堋ｱ窶堋ｱ窶堙懌堙・
		return pEntity;
	}

	/**
	 * 窶｢ﾃ擾ｿｽ窶晢ｿｽumaidAvatar・ｽv窶堋ｩ窶堙ｧ窶冤窶堙ｰﾅｽﾃｦ窶堙ｨ・ｽo窶堋ｵ窶禿溪堙ｨ窶冤窶堙・堋ｵ窶堙・｢ﾃ披堋ｷ・ｽB
	 * maidAvatar窶堋ｪ窶伉ｶ・ｽﾃ昶堋ｵ窶堙遺堋｢・ｽﾃｪ・ｽ窶｡窶堙最陳ｳ窶堙娯冤窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
	 * maidAvatar窶堙孔ntityPlayerﾅ津敘ﾂｷ・ｽB
	 */
	public static Entity getAvatarPlayer(Entity entity) {
		// ﾆ抵ｿｽﾆ辰ﾆ檀窶堋ｳ窶堙ｱﾆ蛋ﾆ巽ﾆ鍛ﾆ誰
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
	 * ﾆ致ﾆ椎抵ｿｽ[ﾆ停樞堙姑辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堋ｩ窶堙ｧﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙ｰﾅ陳ｸ窶堙ｧ窶堋ｷ
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
			// ﾆ誰ﾆ椎ﾆ竪ﾆ辰ﾆ弾ﾆ達ﾆ置窶堋ｾ窶堙・陳ｸ窶堙ｧ窶堙遺堋｢
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
	 * ﾅｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ椎ﾆ池ﾆ淡ﾆ停｡ﾆ停懌堙ｦ窶堙ｨ窶堙ﾅ津・堋ｯ窶堙ｪ窶堙寂氾｡ﾅO窶堙ｰ窶愬窶堋ｰ窶堙・湛ﾆ暖ﾆ鍛ﾆ致
	 */
	public static void checkRevision(String pRev) {
		if (convRevision() < convRevision(pRev)) {
			// 窶廳・ｽ窶｡ﾆ弛・ｽ[ﾆ淡ﾆ停｡ﾆ停懌堙・堙坂堙遺堋｢窶堙娯堙・湛ﾆ暖ﾆ鍛ﾆ致
			ModLoader.getLogger().warning("you must check MMMLib revision.");
			throw new RuntimeException("The revision of MMMLib is old.");
		}
	}

	/**
	 * EntityList窶堙俄徙ﾋ弯窶堋ｳ窶堙ｪ窶堙・堋｢窶堋｢窶堙ｩEntity窶堙ｰ窶冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ・ｽB
	 */
	public static void replaceEntityList(Class pSrcClass, Class pDestClass) {
		// EntityList窶徙ﾋ弯・ｽﾃｮ窶｢ﾃｱ窶堙ｰ窶冰窶堋ｫﾅﾂｷ窶堋ｦ
		// ﾅ津・堋｢Entity窶堙・堙ﾆ湛ﾆ竹・ｽ[ﾆ停懌堙・堋ｫ窶堙ｩ窶堙ｦ窶堋､窶堙架・ｪ窶｢窶昶堙娯｢ﾂｨ窶堙坂愿ｱ・ｽd窶徙ﾋ弯
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
	 * ﾆ弛ﾆ辰ﾆ棚・ｽ[ﾆ停ぎ窶堙鯉ｿｽﾃ昶凖ｨEntity窶堙ｰ窶冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｧ窶堙ｪ窶堋ｽEntity窶堙問冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ・ｽB
	 * ﾅﾃｮ窶怒窶廬窶堙窺MMLibﾋ・闇O窶堋ｩ窶堙ｧ窶堙最津・堙寂堙ｪ窶堙遺堋｢・ｽB
	 */
	protected static void replaceBaiomeSpawn() {
		// ﾆ弛ﾆ辰ﾆ棚・ｽ[ﾆ停ぎ窶堙娯敖ｭ・ｽﾂｶ・ｽﾋ・費ｿｽ窶堙ｰ窶堙娯堙≫堙・堙ｩ
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
	 * ﾅｽ窶ｹ・ｽﾃｼ窶堙鯉ｿｽﾃｦ窶堙俄堋｢窶堙ｩ・ｽﾃ・ｿｽ窶ｰ窶堙窪ntity窶堙ｰ窶｢ﾃ披堋ｷ
	 * @param pEntity
	 * ﾅｽ窶ｹ窶彑
	 * @param pRange
	 * ﾅｽ窶ｹ・ｽﾃｼ窶堙娯猫ﾅ津ｸ窶ｹ窶披板｣
	 * @param pDelta
	 * ﾅｽﾅｾ・ｽ・ｽ窶｢ﾃ｢・ｽﾂｳ
	 * @param pExpand
	 * ﾅ椎ｸ窶冦窶氾戸・ｦ窶堙固g窶佚･窶敕才・・
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


	// Forge窶佚趣ｿｽﾃｴ

	/**
	 * Forge窶佚趣ｿｽﾃｴ窶廃窶堙姑抵ｿｽﾆ箪ﾆ鍛ﾆ檀
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
	 * ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙俄凖・ｰﾃ・津ｸ窶ｰﾃ岩堋ｪ・ｽﾃ昶堙ｩ窶堋ｩ窶堙ｰ窶敖ｻ窶凖ｨ窶堋ｷ窶堙ｩ・ｽB
	 * Forge窶佚趣ｿｽﾃｴ・ｽB
	 * @param pItemStack
	 * @return
	 */
	public static boolean hasEffect(ItemStack pItemStack) {
		// ﾆ筑ﾆ淡ClientSIDE窶堙・堋ｩﾅｽﾂｫ窶堙溪堙・堙吮堋ｵ窶堋｢・ｽB
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
	 * Block窶堙姑辰ﾆ停愴湛ﾆ耽ﾆ停愴湛窶堙ｰ窶冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ・ｽB
	 * static final窶堙娯｢ﾃ擾ｿｽ窶昶堙俄佚寂堋ｵ窶堙・ｿｽs窶堋､窶堙娯堙・orge窶堙・堙坂督ｳﾅ津ｸ・ｽB
	 * @param pOriginal
	 * @param pReplace
	 * @return
	 */
	public static boolean replaceBlock(Block pOriginal, Block pReplace) {
		if (isForge) {
			return false;
		}
		try {
			// Block窶堙茎tatic final窶｢ﾂｪ窶堙娯冰ﾅﾂｷ窶堋ｦ
			Field[] lfield = Block.class.getDeclaredFields();
			for (int li = 0; li < lfield.length; li++) {
				if (!Modifier.isStatic(lfield[li].getModifiers())) {
					// staticﾋ・闇O窶堙坂佚趣ｿｽﾃ崘O
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
	 * 16・ｽi・ｽ窶昶堙娯｢ﾂｶﾅｽﾅ｡窶氾ｱ窶堙ｰInt窶堙問｢ﾃ焦ﾂｷ窶堋ｷ窶堙ｩ・ｽB
	 * 0xffffffff窶佚趣ｿｽﾃｴ・ｽB
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
	 *  ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙会ｿｽﾃ昶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽ・ｽUﾅ停壺氾坂堙ｰﾅ陳ｩ窶堙ｩ
	 * @param pItemStack
	 * @return
	 */
	public static double getAttackVSEntity(ItemStack pItemStack) {
		AttributeModifier lam = (AttributeModifier)pItemStack.getAttributeModifiers().get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		return lam == null ? 0 : lam.getAmount(); 
	}

}
