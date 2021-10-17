package the_fireplace.ias.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;
/**
 * The button with the image on it.
 * @author The_Fireplace
 */
public class GuiButtonWithImage extends ButtonWidget {
	private static final Identifier customButtonTextures = new Identifier("ias", "textures/gui/custombutton.png");

	public GuiButtonWithImage(int x, int y, PressAction p) {
		super(x, y, 20, 20, "ButterDog", p);
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float delta) {
		if (this.visible) {
			MinecraftClient mc = MinecraftClient.getInstance();
			mc.getTextureManager().bindTexture(customButtonTextures);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = getYImage(isHovered);
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			blit(this.x, this.y, 0, k * 20, 20, 20);
		}
	}
}
