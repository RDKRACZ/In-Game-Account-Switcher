package ru.vidtu.ias.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.resource.language.I18n;
import ru.vidtu.ias.Config;
import the_fireplace.ias.gui.GuiAccountSelector;
import the_fireplace.ias.gui.GuiButtonWithImage;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
	private MultiplayerScreenMixin() {
		super(null);
	}

	@Inject(method = "render", at = @At("TAIL"))
	public void onRender(int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (minecraft.getSession().getAccessToken().equals("0") || minecraft.getSession().getAccessToken().equals("-")) {
			List<String> list = font.wrapStringToWidthAsList(I18n.translate("ias.offlinemode"), width);
			for (int i = 0; i < list.size(); i++) {
				drawCenteredString(font, list.get(i), width / 2, i * 9 + 1, 16737380);
			}
		}
	}
	
	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		if (Config.showOnMPScreen) {
			addButton(new GuiButtonWithImage(this.width / 2 + 4 + 76 + 79, height - 28, btn -> {
				minecraft.openScreen(new GuiAccountSelector(this));
			}));
		}
	}
}
