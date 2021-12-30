package the_fireplace.ias;

import java.util.List;
import java.util.Optional;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ScreenEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.vidtu.ias.Config;
import ru.vidtu.ias.gui.IASConfigScreen;
import ru.vidtu.ias.utils.Converter;
import ru.vidtu.ias.utils.Expression;
import ru.vidtu.ias.utils.SkinRenderer;
import the_fireplace.ias.gui.GuiAccountSelector;
import the_fireplace.ias.gui.GuiButtonWithImage;
/**
 * @author The_Fireplace
 */
@Mod("ias")
public class IAS {
	public static final Gson GSON = new Gson();
	public static final Logger LOG = LogManager.getLogger("IAS");
	private static int textX, textY;
	public IAS() {
		ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((mc, s) -> new IASConfigScreen(s)));
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInitialize);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void postInitialize(FMLLoadCompleteEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Config.load(mc);
		Converter.convert(mc);
		SkinRenderer.loadAllAsync(mc, false, () -> {});
	}
	
	@SubscribeEvent
	public void onGuiInit(ScreenEvent.InitScreenEvent.Post event) {
		if (event.getScreen() instanceof TitleScreen) {
			try {
				if (StringUtils.isNotBlank(Config.textX) && StringUtils.isNotBlank(Config.textY)) {
					textX = (int) new Expression(Config.textX.replace("%width%", Integer.toString(event.getScreen().width)).replace("%height%", Integer.toString(event.getScreen().height))).parse(0);
					textY = (int) new Expression(Config.textY.replace("%width%", Integer.toString(event.getScreen().width)).replace("%height%", Integer.toString(event.getScreen().height))).parse(0);
				} else {
					textX = event.getScreen().width / 2;
					textY = event.getScreen().height / 4 + 48 + 72 + 12 + 22;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				textX = event.getScreen().width / 2;
				textY = event.getScreen().height / 4 + 48 + 72 + 12 + 22;
			}
			if (Config.showOnTitleScreen) {
				int btnX = event.getScreen().width / 2 + 104;
				int btnY = event.getScreen().height / 4 + 48 + 60;
				try {
					if (StringUtils.isNotBlank(Config.btnX) && StringUtils.isNotBlank(Config.btnY)) {
						btnX = (int) new Expression(Config.btnX.replace("%width%", Integer.toString(event.getScreen().width)).replace("%height%", Integer.toString(event.getScreen().height))).parse(0);
						btnY = (int) new Expression(Config.btnY.replace("%width%", Integer.toString(event.getScreen().width)).replace("%height%", Integer.toString(event.getScreen().height))).parse(0);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					btnX = event.getScreen().width / 2 + 104;
					btnY = event.getScreen().height / 4 + 48 + 60;
				}
				event.addListener(new GuiButtonWithImage(btnX, btnY, btn -> event.getScreen().getMinecraft().setScreen(new GuiAccountSelector(event.getScreen()))));
			}
		}
		if (event.getScreen() instanceof JoinMultiplayerScreen) {
			if (Config.showOnMPScreen) {
				event.addListener(new GuiButtonWithImage(event.getScreen().width / 2 + 4 + 76 + 79, event.getScreen().height - 28, btn -> {
					event.getScreen().getMinecraft().setScreen(new GuiAccountSelector(event.getScreen()));
				}));
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiRender(ScreenEvent.DrawScreenEvent.Post event) {
		if (event.getScreen() instanceof TitleScreen) {
			Minecraft mc = event.getScreen().getMinecraft();
			Screen.drawCenteredString(event.getPoseStack(), mc.font, new TranslatableComponent("ias.loggedinas",
					mc.getUser().getName()), textX, textY, 0xFFCC8888);
		}
		if (event.getScreen() instanceof JoinMultiplayerScreen) {
			Minecraft mc = event.getScreen().getMinecraft();
			if (mc.getUser().getAccessToken().equals("0") || mc.getUser().getAccessToken().equals("-")) {
				List<FormattedCharSequence> list = mc.font.split(new TranslatableComponent("ias.offlinemode"), event.getScreen().width);
				for (int i = 0; i < list.size(); i++) {
					mc.font.drawShadow(event.getPoseStack(), list.get(i), event.getScreen().width / 2 - mc.font.width(list.get(i)) / 2, i * 9 + 1, 16737380);
				}
			}
		}
	}
}
