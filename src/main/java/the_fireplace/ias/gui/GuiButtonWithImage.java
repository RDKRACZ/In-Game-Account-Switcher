package the_fireplace.ias.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
/**
 * The button with the image on it.
 * @author The_Fireplace
 */
public class GuiButtonWithImage extends Button {
	private static final ResourceLocation customButtonTextures = new ResourceLocation("ias", "textures/gui/custombutton.png");
	private static final ResourceLocation customButtonTexturesClassic = new ResourceLocation("ias", "textures/gui/custombutton_classic.png");

	public GuiButtonWithImage(int x, int y, OnPress p) {
		super(x, y, 20, 20, new TextComponent("ButterDog"), p);
	}
	
	@Override
	public void renderButton(PoseStack ms, int mouseX, int mouseY, float delta) {
		if (this.visible) {
			RenderSystem.setShaderTexture(0, programmerArt(Minecraft.getInstance())?customButtonTexturesClassic:customButtonTextures);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = getYImage(isHovered);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			RenderSystem.blendFunc(770, 771);
			blit(ms, this.x, this.y, 0, k * 20, 20, 20);
		}
	}
	
	/**
	 * Fabric loads programmer art texture automatically,
	 * on Forge we need to manually switch between textures. (or I don't know how to do it on Forge)
	 * So this is method to determine texture:<br>
	 * - <code>custombutton.png</code> for default pack. (<code>false</code>)<br>
	 * - <code>custombutton_classic.png</code> for programmer art. (<code>true</code>)
	 * @param mc Minecraft client instance
	 * @return <code>true</code> if client is using programmer art resourcepack, <code>false</code> otherwise
	 * @implNote I'm not sure about efficiency of this method, maybe I should cache it?
	 */
	public static boolean programmerArt(Minecraft mc) {
		return mc.getResourceManager().listPacks().anyMatch(p -> p.getName().equals("Programmer Art"));
	}
}
