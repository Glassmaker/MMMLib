package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * �}�[�J�[��\�����܂��B
 */
public class MMM_EntityDummy extends Entity {
	
	private int livecount;
	private final int maxlivecount = 16;
	private int entityColor;
	public Entity entityOwner;
	/**
	 * �L���
	 */
	public static boolean isEnable = false;
	
	public static List<MMM_EntityDummy> appendList = new ArrayList<MMM_EntityDummy>();


	public MMM_EntityDummy(World world, int color, Entity owner) {
		super(world);
		livecount = maxlivecount;
		entityColor = color;
//		setSize(1F, 1F);
		entityOwner = owner;
	}
	
	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void onUpdate() {
//		super.onUpdate();
		
		if (--livecount < 0 || !isEnable) {
			setDead();
		}
	}

	public float getAlpha(float max) {
		if (livecount >= 0) {
			return max * (float)livecount / (float)maxlivecount;
		} else {
			return 0F;
		}
	}

	public int getColor() {
		return entityColor;
	}

	public boolean setOwnerdEntityDead(Entity entity) {
		if (entityOwner == entity) {
			setDead();
			return true;
		}
		return false;
	}

	/**
	 * �w�肳�ꂽ�I�[�i�[�ɑΉ�����}�[�J�[���폜���܂��B
	 */
	public static void clearDummyEntity(Entity entity) {
		if (!isEnable) return;
		if (!MMM_Helper.isClient) return;
		
		List<Entity> liste = entity.worldObj.loadedEntityList;
		for (Entity entity1 : liste) {
			if (entity1 instanceof MMM_EntityDummy) {
				((MMM_EntityDummy)entity1).setOwnerdEntityDead(entity);
			}
		}
	}

	/**
	 * �}�[�J�[��\������
	 */
	public static void setDummyEntity(Entity owner, int color, double posx, double posy, double posz) {
		if (!isEnable) return;
		if (!MMM_Helper.isClient) return;
		
		// �T�[�o�[���ł����Ă΂�Ȃ����ۂ�
		if (owner.worldObj.isRemote) {
			mod_MMM_MMMLib.Debug("L");
		}
		
		MMM_EntityDummy ed = new MMM_EntityDummy(MMM_Client.getMCtheWorld(), color, owner);
		ed.setPosition(posx, posy, posz);
		appendList.add(ed);
	}

}
