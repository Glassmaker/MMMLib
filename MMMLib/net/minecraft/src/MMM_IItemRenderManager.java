package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽp・ｽﾌ難ｿｽ・ｽ黹鯉ｿｽ・ｽ・ｽ_・ｽ[・ｽﾉ継・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽC・ｽ・ｽ・ｽ^・ｽ[・ｽt・ｽF・ｽ[・ｽX・ｽB
 * ・ｽ・ｽ・ｽ・ｽA・ｽp・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽﾈゑｿｽ・ｽﾄゑｿｽ・ｽ・ｽ・ｽ\・ｽb・ｽh・ｽ・ｽItem・ｽﾉ記・ｽq・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽﾎ難ｿｽ・ｽ・ｷ・ｽ・ｽB
 */
public interface MMM_IItemRenderManager {

	public static final int VM_FIRST_PERSON		= 0;
	public static final int VM_THERD_PERSON		= 1;
	public static final int VM_THERD_PERSON_INV	= 2;


	/**
	 * ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽﾌ描・ｽ・ｽﾌみ、・ｽﾊ置・ｽ竦ｳ・ｽﾍゑｿｽ・ｽﾈゑｿｽ・ｽB
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
