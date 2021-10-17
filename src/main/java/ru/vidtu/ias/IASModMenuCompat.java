package ru.vidtu.ias;

import java.util.function.Function;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import ru.vidtu.ias.gui.IASConfigScreen;

public class IASModMenuCompat implements ModMenuApi {
	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return screen -> new IASConfigScreen(screen);
	}
	
	@Override
	public String getModId() {
		return "ias";
	}
}
