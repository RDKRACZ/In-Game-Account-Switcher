package ru.vidtu.ias;

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import ru.vidtu.ias.gui.IASConfigScreen;

public class IASGuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft mc) {}
	
	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen prev) {
		return new IASConfigScreen(prev);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return Collections.emptySet();
	}
}
