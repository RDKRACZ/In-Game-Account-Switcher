package ru.vidtu.ias.gui;

import org.apache.commons.lang3.StringUtils;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import ru.vidtu.ias.Config;

/**
 * Screen for editing IAS config.
 * @author VidTu
 */
public class IASConfigScreen extends Screen {
	public final Screen prev;
	public CheckboxButton caseS, mpscreen, titlescreen;
	public TextFieldWidget textX, textY, btnX, btnY;
	public IASConfigScreen(Screen prev) {
		super(new StringTextComponent("config/ias.json"));
		this.prev = prev;
	}
	
	@Override
	public void init() {
		addButton(caseS = new CheckboxButton(width / 2 - font.width(new TranslationTextComponent("ias.cfg.casesensitive")) / 2 - 24, 40, 20, 20, new TranslationTextComponent("ias.cfg.casesensitive"), Config.caseSensitiveSearch));
		addButton(mpscreen = new CheckboxButton(width / 2 - font.width(new TranslationTextComponent("ias.cfg.mpscreen")) / 2 - 24, 60, 20, 20, new TranslationTextComponent("ias.cfg.mpscreen"), Config.showOnMPScreen));
		addButton(titlescreen = new CheckboxButton(width / 2 - font.width(new TranslationTextComponent("ias.cfg.titlescreen")) / 2 - 24, 80, 20, 20, new TranslationTextComponent("ias.cfg.titlescreen"), Config.showOnTitleScreen));
		addButton(textX = new TextFieldWidget(font, width / 2 - 100, 110, 98, 20, new StringTextComponent("X")));
		addButton(textY = new TextFieldWidget(font, width / 2 + 2, 110, 98, 20, new StringTextComponent("Y")));
		addButton(btnX = new TextFieldWidget(font, width / 2 - 100, 152, 98, 20, new StringTextComponent("X")));
		addButton(btnY = new TextFieldWidget(font, width / 2 + 2, 152, 98, 20, new StringTextComponent("Y")));
		addButton(new Button(width / 2 - 75, height - 24, 150, 20, new TranslationTextComponent("gui.done"), btn -> {
			minecraft.setScreen(prev);
		}));
		textX.setValue(StringUtils.trimToEmpty(Config.textX));
		textY.setValue(StringUtils.trimToEmpty(Config.textY));
		btnX.setValue(StringUtils.trimToEmpty(Config.btnX));
		btnY.setValue(StringUtils.trimToEmpty(Config.btnY));
	}
	
	@Override
	public void removed() {
		Config.caseSensitiveSearch = caseS.selected();
		Config.showOnMPScreen = mpscreen.selected();
		Config.showOnTitleScreen = titlescreen.selected();
		Config.textX = textX.getValue();
		Config.textY = textY.getValue();
		Config.btnX = btnX.getValue();
		Config.btnY = btnY.getValue();
		Config.save(minecraft);
	}
	
	@Override
	public void tick() {
		btnX.visible = titlescreen.selected();
		btnY.visible = titlescreen.selected();
		textX.tick();
		textY.tick();
		btnX.tick();
		btnY.tick();
		textX.setSuggestion(textX.getValue().isEmpty()?"X":"");
		textY.setSuggestion(textY.getValue().isEmpty()?"Y":"");
		btnX.setSuggestion(btnX.getValue().isEmpty()?"X":"");
		btnY.setSuggestion(btnY.getValue().isEmpty()?"Y":"");
		super.tick();
	}
	
	@Override
	public void render(MatrixStack ms, int mx, int my, float delta) {
		renderBackground(ms);
		drawCenteredString(ms, font, this.title, width / 2, 10, -1);
		drawCenteredString(ms, font, new TranslationTextComponent("ias.cfg.textpos"), width / 2, 100, -1);
		if (titlescreen.selected()) drawCenteredString(ms, font, new TranslationTextComponent("ias.cfg.btnpos"), width / 2, 142, -1);
		super.render(ms, mx, my, delta);
	}
}
