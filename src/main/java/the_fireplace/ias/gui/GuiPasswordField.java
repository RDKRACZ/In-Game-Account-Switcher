package the_fireplace.ias.gui;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class GuiPasswordField extends EditBox {
	public GuiPasswordField(Font fontrendererObj, int x, int y, int par5Width, int par6Height, Component s)
	{
		super(fontrendererObj, x, y, par5Width, par6Height, s);
		setFormatter((t, u) -> new TextComponent(StringUtils.repeat('*', t.length())).getVisualOrderText());
	}
	
	@Override
	public boolean keyPressed(int key, int oldkey, int mods) {
		return !Screen.isCopy(key) && !Screen.isCut(key) && super.keyPressed(key, oldkey, mods);
	}
}
