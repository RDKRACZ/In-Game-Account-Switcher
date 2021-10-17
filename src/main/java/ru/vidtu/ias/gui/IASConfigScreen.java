package ru.vidtu.ias.gui;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
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
		addButton(caseS = new CheckboxButton(width / 2 - font.getStringWidth(I18n.format("ias.cfg.casesensitive")) / 2 - 24, 40, 20, 20, I18n.format("ias.cfg.casesensitive"), Config.caseSensitiveSearch));
		addButton(mpscreen = new CheckboxButton(width / 2 - font.getStringWidth(I18n.format("ias.cfg.mpscreen")) / 2 - 24, 60, 20, 20, I18n.format("ias.cfg.mpscreen"), Config.showOnMPScreen));
		addButton(titlescreen = new CheckboxButton(width / 2 - font.getStringWidth(I18n.format("ias.cfg.titlescreen")) / 2 - 24, 80, 20, 20, I18n.format("ias.cfg.titlescreen"), Config.showOnTitleScreen));
		addButton(textX = new TextFieldWidget(font, width / 2 - 100, 110, 98, 20, "X"));
		addButton(textY = new TextFieldWidget(font, width / 2 + 2, 110, 98, 20, "Y"));
		addButton(btnX = new TextFieldWidget(font, width / 2 - 100, 152, 98, 20, "X"));
		addButton(btnY = new TextFieldWidget(font, width / 2 + 2, 152, 98, 20, "Y"));
		addButton(new Button(width / 2 - 75, height - 24, 150, 20, I18n.format("gui.done"), btn -> {
			minecraft.displayGuiScreen(prev);
		}));
		textX.setText(StringUtils.trimToEmpty(Config.textX));
		textY.setText(StringUtils.trimToEmpty(Config.textY));
		btnX.setText(StringUtils.trimToEmpty(Config.btnX));
		btnY.setText(StringUtils.trimToEmpty(Config.btnY));
	}
	
	@Override
	public void removed() {
		Config.caseSensitiveSearch = caseS.isChecked();
		Config.showOnMPScreen = mpscreen.isChecked();
		Config.showOnTitleScreen = titlescreen.isChecked();
		Config.textX = textX.getText();
		Config.textY = textY.getText();
		Config.btnX = btnX.getText();
		Config.btnY = btnY.getText();
		Config.save(minecraft);
	}
	
	@Override
	public void tick() {
		btnX.visible = titlescreen.isChecked();
		btnY.visible = titlescreen.isChecked();
		textX.tick();
		textY.tick();
		btnX.tick();
		btnY.tick();
		textX.setSuggestion(textX.getText().isEmpty()?"X":"");
		textY.setSuggestion(textY.getText().isEmpty()?"Y":"");
		btnX.setSuggestion(btnX.getText().isEmpty()?"X":"");
		btnY.setSuggestion(btnY.getText().isEmpty()?"Y":"");
		super.tick();
	}
	
	@Override
	public void render(int mx, int my, float delta) {
		renderBackground();
		drawCenteredString(font, this.title.getFormattedText(), width / 2, 10, -1);
		drawCenteredString(font, I18n.format("ias.cfg.textpos"), width / 2, 100, -1);
		if (titlescreen.isChecked()) drawCenteredString(font, I18n.format("ias.cfg.btnpos"), width / 2, 142, -1);
		super.render(mx, my, delta);
	}
}
