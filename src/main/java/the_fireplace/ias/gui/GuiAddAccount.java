package the_fireplace.ias.gui;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import ru.vidtu.iasfork.msauth.MSAuthScreen;
import the_fireplace.ias.account.ExtendedAccountData;

/**
 * The GUI where the alt is added
 * @author The_Fireplace
 * @author evilmidget38
 */
public class GuiAddAccount extends AbstractAccountGui {

	public GuiAddAccount(Screen prev)
	{
		super(prev, new TranslationTextComponent("ias.addaccount"));
	}
	
	@Override
	public void init() {
		super.init();
		addButton(new Button(width / 2 - 60, height / 3 * 2, 120, 20, I18n.format("ias.msauth.btn"), btn -> minecraft.displayGuiScreen(new MSAuthScreen(this))));
	}

	@Override
	public void complete()
	{
		AltDatabase.getInstance().getAlts().add(new ExtendedAccountData(getUsername(), getPassword(), getUsername()));
	}
}
