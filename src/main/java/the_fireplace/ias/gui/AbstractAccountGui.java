package the_fireplace.ias.gui;

import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import ru.vidtu.ias.Config;
import ru.vidtu.ias.account.Account;
import ru.vidtu.ias.account.AuthException;
import ru.vidtu.ias.account.MojangAccount;
import ru.vidtu.ias.account.OfflineAccount;
import ru.vidtu.ias.gui.MSAuthScreen;
import ru.vidtu.ias.utils.Auth;
import ru.vidtu.ias.utils.SkinRenderer;
import the_fireplace.ias.IAS;

/**
 * Screen for adding Mojang and Offline accounts.
 * @author evilmidget38
 * @author The_Fireplace
 */
public class AbstractAccountGui extends Screen {
	public final Screen prev;
	private TextFieldWidget username;
	private TextFieldWidget password;
	private ButtonWidget complete;
	private boolean logging;
	private Consumer<Account> handler;
	private List<String> error;
	
	public AbstractAccountGui(Screen prev, Text title, Consumer<Account> handler) {
		super(title);
		this.prev = prev;
		this.handler = handler;
	}
	
	@Override
	public void init() {
		complete = addButton(new ButtonWidget(this.width / 2 - 152, this.height - 28, 150, 20, this.title.asFormattedString(), btn -> end()));
		addButton(new ButtonWidget(this.width / 2 + 2, this.height - 28, 150, 20, I18n.translate("gui.cancel"), btn -> minecraft.openScreen(prev)));
		username = addButton(new TextFieldWidget(font, this.width / 2 - 100, 60, 200, 20, I18n.translate("ias.username")));
		username.setMaxLength(512);
		password = addButton(new GuiPasswordField(font, this.width / 2 - 100, 90, 200, 20, I18n.translate("ias.password")));
		password.setMaxLength(512);
		complete.active = false;
		addButton(new ButtonWidget(this.width / 2 - 50, this.height / 3 * 2, 100, 20, I18n.translate("ias.msauth.btn"), btn -> minecraft.openScreen(new MSAuthScreen(prev, handler))));
	}
	
	@Override
	public void render(int mx, int my, float delta) {
		renderBackground();
		drawCenteredString(font, this.title.asFormattedString(), this.width / 2, 7, -1);
		drawCenteredString(font, I18n.translate("ias.username"), this.width / 2 - 130, 66, -1);
		drawCenteredString(font, I18n.translate("ias.password"), this.width / 2 - 130, 96, -1);
		if (error != null) {
			for (int i = 0; i < error.size(); i++) {
				drawCenteredString(font, error.get(i), this.width / 2, 114 + i * 10, 0xFFFF0000);
				if (i > 6) break; //Exceptions can be very large.
			}
		}
		super.render(mx, my, delta);
	}
	
	@Override
	public boolean keyPressed(int key, int oldkey, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			minecraft.openScreen(prev);
			return true;
		}
		if (key == GLFW.GLFW_KEY_ENTER) {
			if (username.isFocused()) {
				username.method_1876(false); //setFocused
				password.method_1876(true); //setFocused
				return true;
			}
			if (password.isFocused() && complete.active) {
				end();
				return true;
			}
		}
		return super.keyPressed(key, oldkey, mods);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public void tick() {
		complete.active = !username.getText().trim().isEmpty() && !logging;
		complete.setMessage(!username.getText().trim().isEmpty() && password.getText().isEmpty()?this.title.copy().append(" ").append(new TranslatableText("ias.offline")).asFormattedString():this.title.asFormattedString());
		username.active = password.active = !logging;
		username.tick();
		password.tick();
		super.tick();
	}

	public void end() {
		if (password.getText().isEmpty()) {
			String name = username.getText();
			if (Config.accounts.stream().anyMatch(acc -> acc.alias().equalsIgnoreCase(name))) {
				error = font.wrapStringToWidthAsList(I18n.translate("ias.auth.alreadyexists"), width - 10);
				return;
			}
			logging = true;
			new Thread(() -> {
				SkinRenderer.loadSkin(minecraft, name, null, false);
				minecraft.execute(() -> {
					if (minecraft.currentScreen == this) {
						handler.accept(new OfflineAccount(username.getText()));
						minecraft.openScreen(prev);
					}
				});
				logging = false;
			}).start();
		} else {
			String name = username.getText();
			String pwd = password.getText(); 
			logging = true;
			new Thread(() -> {
				try {
					MojangAccount ma = Auth.authMojang(name, pwd);
					SkinRenderer.loadSkin(minecraft, ma.alias(), ma.uuid(), false);
					if (Config.accounts.stream().anyMatch(acc -> acc.alias().equalsIgnoreCase(name)))
						throw new AuthException(new TranslatableText("ias.auth.alreadyexists"));
					minecraft.execute(() -> {
						if (minecraft.currentScreen == this) {
							handler.accept(ma);
							minecraft.openScreen(prev);
						}
					});
				} catch (AuthException ae) {
					IAS.LOG.warn("Unable to add account (expected exception)", ae);
					minecraft.execute(() -> error = font.wrapStringToWidthAsList(ae.getText().asFormattedString(), width - 10));
				} catch (Throwable t) {
					IAS.LOG.warn("Unable to add account (unexpected exception)", t);
					minecraft.execute(() -> error = font.wrapStringToWidthAsList(I18n.translate("ias.auth.unknown", t.getLocalizedMessage()), width - 10));
				}
				logging = false;
			}, "IAS Mojang Auth Thread").start();
		}
	}
}
