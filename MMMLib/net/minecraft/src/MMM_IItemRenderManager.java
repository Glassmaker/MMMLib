package net.minecraft.src;

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
	public boolean renderItemWorld();
	public ResourceLocation getRenderTexture();
	public boolean isRenderItem();
	public boolean isRenderItemInFirstPerson();
	public boolean isRenderItemWorld();

}
