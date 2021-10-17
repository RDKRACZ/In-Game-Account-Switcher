package the_fireplace.ias.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
/**
 * The button with the image on it.
 * @author The_Fireplace
 */
public class GuiButtonWithImage extends Button {
	private static final ResourceLocation customButtonTextures = new ResourceLocation("ias", "textures/gui/custombutton.png");
	private static final ResourceLocation customButtonTexturesClassic = new ResourceLocation("ias", "textures/gui/custombutton_classic.png");

	public GuiButtonWithImage(int x, int y, IPressable p) {
		super(x, y, 20, 20, "ButterDog", p);
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float delta) {
		if (this.visible) {
			Minecraft mc = Minecraft.getInstance();
			mc.textureManager.bindTexture(programmerArt(mc)?customButtonTexturesClassic:customButtonTextures);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = getYImage(isHovered);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(770, 771, 1, 0);
			RenderSystem.blendFunc(770, 771);
			blit(this.x, this.y, 0, k * 20, 20, 20);
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
		return mc.getResourcePackList().getEnabledPacks().stream().anyMatch(p -> p.getName().equals("Programmer Art"));
	}
}
