package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.ResourcePack;
import net.minecraft.client.resources.data.MetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;

/**
 * �Â����\�[�X��ǂ݂��܂��邽�߂̃p�b�P�[�W�w��
 * �R�[�h��Modloader�̊ۃp�N��
 */
public class MMM_ModOldResourcePack implements ResourcePack {

	protected final Class modClass;

	public MMM_ModOldResourcePack(Class modClass) {
		this.modClass = modClass;
	}

	public InputStream getInputStream(ResourceLocation var1) throws IOException {
		return this.modClass.getResourceAsStream("" + var1.getResourcePath());
	}

	@Override
	public boolean resourceExists(ResourceLocation var1) {
		try {
			return this.getInputStream(var1) != null;
		} catch (IOException var3) {
			return false;
		}
	}

	public Set getResourceDomains() {
		return DefaultResourcePack.defaultResourceDomains;
	}

	public MetadataSection getPackMetadata(MetadataSerializer var1, String var2) throws IOException {
		return var1.parseMetadataSection(var2, new JsonObject());
	}

	public BufferedImage getPackImage() throws IOException {
		return ImageIO.read(DefaultResourcePack.class.getResourceAsStream(
				"/" + (new ResourceLocation("pack.png")).getResourcePath()));
	}

	public String getPackName() {
		return this.modClass.getSimpleName();
	}

}
