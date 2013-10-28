package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * �A�C�e���p�̓��ꃌ���_�[�Ɍp��������C���^�[�t�F�[�X�B
 * ����A�p�������Ă��Ȃ��Ă����\�b�h��Item�ɋL�q����Ă���Γ��삷��B
 */
public interface MMM_IItemRenderManager {

	public static final int VM_FIRST_PERSON		= 0;
	public static final int VM_THERD_PERSON		= 1;
	public static final int VM_THERD_PERSON_INV	= 2;


	/**
	 * �A�C�e���̕`��̂݁A�ʒu�␳�͂��Ȃ��B
	 * @param pEntity
	 * @param pItemStack
	 * @param pIndex
	 * @return
	 */
	public boolean renderItem(Entity pEntity, ItemStack pItemStack, int pIndex);
//	public boolean renderItemInFirstPerson(float pDeltaTimepRenderPhatialTick, MMM_ItemRenderer pItemRenderer);
	public boolean renderItemInFirstPerson(Entity pEntity, ItemStack pItemStack, float pDeltaTimepRenderPhatialTick);
	public boolean renderItemWorld(ItemStack pItemStack);
	public ResourceLocation getRenderTexture(ItemStack pItemStack);
	public boolean isRenderItem(ItemStack pItemStack);
	public boolean isRenderItemInFirstPerson(ItemStack pItemStack);
	public boolean isRenderItemWorld(ItemStack pItemStack);

}
