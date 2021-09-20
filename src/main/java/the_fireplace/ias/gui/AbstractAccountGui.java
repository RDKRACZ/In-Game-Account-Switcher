package the_fireplace.ias.gui;

import org.lwjgl.glfw.GLFW;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import the_fireplace.iasencrypt.EncryptionTools;

/**
 * @author evilmidget38
 * @author The_Fireplace
 */
public abstract class AbstractAccountGui extends Screen {
	public final Screen prev;
	private TextFieldWidget username;
	private GuiPasswordField password;
	private ButtonWidget complete;
	protected boolean hasUserChanged = false;

	public AbstractAccountGui(Screen prev, Text actionString) {
		super(actionString);
		this.prev = prev;
	}
	
	@Override
	protected void init() {
		addButton(complete = new ButtonWidget(this.width / 2 - 152, this.height - 28, 150, 20, this.title.asFormattedString(), btn -> {
			complete();
			escape();
		}));
		addButton(new ButtonWidget(this.width / 2 + 2, this.height - 28, 150, 20, I18n.translate("gui.cancel"), btn -> escape()));
		addButton(username = new TextFieldWidget(this.font, this.width / 2 - 100, 60, 200, 20, ""));
		username.setMaxLength(512);
		addButton(password = new GuiPasswordField(this.font, this.width / 2 - 100, 90, 200, 20, ""));
		password.setMaxLength(512);
		complete.active = false;
	}
	
	@Override
	public void render(int mx, int my, float delta) {
		renderBackground();
		this.drawCenteredString(font, this.title.asFormattedString(), this.width / 2, 7, -1);
		this.drawCenteredString(font, I18n.translate("ias.username"), this.width / 2 - 130, 66, -1);
		this.drawCenteredString(font, I18n.translate("ias.password"), this.width / 2 - 130, 96, -1);
		super.render(mx, my, delta);
	}
	
	@Override
	public boolean keyPressed(int key, int oldkey, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			escape();
		} else if (key == GLFW.GLFW_KEY_ENTER) {
			if (username.isFocused()) {
				username.setSelected(false);
				password.setSelected(true);
			} else if (password.isFocused() && complete.active) {
				complete();
				escape();
			}
		} else {
			if (username.isFocused()) hasUserChanged = true;
		}
		return super.keyPressed(key, oldkey, mods);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public void tick() {
		complete.active = canComplete();
		username.tick();
		password.tick();
		super.tick();
	}

	private void escape(){
		minecraft.openScreen(prev);
	}

	public String getUsername()
	{
		return username.getText();
	}

	public String getPassword()
	{
		return password.getText();
	}

	public void setUsername(String username)
	{
		this.username.setText(username);
	}

	public void setPassword(String password)
	{
		this.password.setText(password);
	}

	protected boolean accountNotInList(){
		for(AccountData data : AltDatabase.getInstance().getAlts())
			if(EncryptionTools.decode(data.user).equals(getUsername()))
				return false;
		return true;
	}

	public boolean canComplete()
	{
		return getUsername().length() > 0 && accountNotInList();
	}

	public abstract void complete();
}
