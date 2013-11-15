package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class MMM_ItemRenderer extends ItemRenderer {

	// ・ｽv・ｽ・ｽ・ｽC・ｽx・ｽ[・ｽg・ｽﾏ撰ｿｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽ謔､・ｽ・ｽ
	public Minecraft mc;
	public ItemStack itemToRender;
	public float equippedProgress;
	public float prevEquippedProgress;
	protected static ResourceLocation texGlint;


	public MMM_ItemRenderer(Minecraft minecraft) {
		super(minecraft);
		
		mc = minecraft;
		try {
			// ・ｽ・ｽ・ｽ・ｽﾟゑｿｽ・ｽe・ｽN・ｽX・ｽ`・ｽ・ｽ・ｽﾌ確・ｽ・ｽ
			texGlint = (ResourceLocation)ModLoader.getPrivateValue(ItemRenderer.class, null, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Minecraft getMC() {
		return mc;
	}

	public ItemStack getItemToRender() {
		return itemToRender;
	}

	public float getEquippedProgress() {
		return equippedProgress;
	}

	public float getPrevEquippedProgress() {
		return prevEquippedProgress;
	}

	@Override
	public void renderItem(EntityLivingBase entityliving, ItemStack itemstack, int i) {
		Item litem = itemstack.getItem();
		if (MMM_ItemRenderManager.isEXRender(litem)) {
			// ・ｽ・ｽ・ｽ黹鯉ｿｽ・ｽ・ｽ_・ｽ・ｽ
			MMM_ItemRenderManager lii = MMM_ItemRenderManager.getEXRender(litem);
			MMM_Client.setTexture(lii.getRenderTexture(itemstack));
			GL11.glPushMatrix();
			boolean lflag = lii.renderItem(entityliving, itemstack, i);
			GL11.glPopMatrix();
			if (lflag) {
				if (itemstack != null && itemstack.hasEffect() && i == 0) {
					GL11.glDepthFunc(GL11.GL_EQUAL);
					GL11.glDisable(GL11.GL_LIGHTING);
					MMM_Client.setTexture(texGlint);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
					float var14 = 0.76F;
					GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
					float var15 = 0.125F;
					
					GL11.glPushMatrix();
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glLoadIdentity();
					GL11.glScalef(var15, var15, var15);
					float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
					GL11.glTranslatef(var16, 0.0F, 0.0F);
					GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					lii.renderItem(entityliving, itemstack, 0);
//					renderItemIn2D(var6, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
					GL11.glPopMatrix();
					
					GL11.glPushMatrix();
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glLoadIdentity();
					GL11.glScalef(var15, var15, var15);
					var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
					GL11.glTranslatef(-var16, 0.0F, 0.0F);
					GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					lii.renderItem(entityliving, itemstack, 0);
//					renderItemIn2D(var6, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
					GL11.glPopMatrix();
					
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glLoadIdentity();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
				}
				return;
			}
		}
		super.renderItem(entityliving, itemstack, i);
	}

	@Override
	public void renderItemInFirstPerson(float f) {
		itemToRender = null;
		equippedProgress = 0.0F;
		prevEquippedProgress = 0.0F;
		
		try {
			// ・ｽ・ｽ・ｽ[・ｽJ・ｽ・ｽ・ｽﾏ撰ｿｽ・ｽ・ｽ・ｽm・ｽ・ｽ
			itemToRender = (ItemStack)ModLoader.getPrivateValue(ItemRenderer.class, this, 4);
			equippedProgress = (Float)ModLoader.getPrivateValue(ItemRenderer.class, this, 5);
			prevEquippedProgress = (Float)ModLoader.getPrivateValue(ItemRenderer.class, this, 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (itemToRender != null) {
			Item litem = itemToRender.getItem();
			if (MMM_ItemRenderManager.isEXRender(litem)) {
				if (MMM_ItemRenderManager.getEXRender(litem).renderItemInFirstPerson(MMM_Helper.mc.thePlayer, itemToRender, f)) {
					return;
				}
			}
		}
		
		super.renderItemInFirstPerson(f);
	}


}
