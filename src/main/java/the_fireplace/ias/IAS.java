package the_fireplace.ias;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
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
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, s) -> new IASConfigScreen(s));
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
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof MainMenuScreen) {
			try {
				if (StringUtils.isNotBlank(Config.textX) && StringUtils.isNotBlank(Config.textY)) {
					textX = (int) new Expression(Config.textX.replace("%width%", Integer.toString(event.getGui().width)).replace("%height%", Integer.toString(event.getGui().height))).parse(0);
					textY = (int) new Expression(Config.textY.replace("%width%", Integer.toString(event.getGui().width)).replace("%height%", Integer.toString(event.getGui().height))).parse(0);
				} else {
					textX = event.getGui().width / 2;
					textY = event.getGui().height / 4 + 48 + 72 + 12 + 22;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				textX = event.getGui().width / 2;
				textY = event.getGui().height / 4 + 48 + 72 + 12 + 22;
			}
			if (Config.showOnTitleScreen) {
				int btnX = event.getGui().width / 2 + 104;
				int btnY = event.getGui().height / 4 + 48 + 60;
				try {
					if (StringUtils.isNotBlank(Config.btnX) && StringUtils.isNotBlank(Config.btnY)) {
						btnX = (int) new Expression(Config.btnX.replace("%width%", Integer.toString(event.getGui().width)).replace("%height%", Integer.toString(event.getGui().height))).parse(0);
						btnY = (int) new Expression(Config.btnY.replace("%width%", Integer.toString(event.getGui().width)).replace("%height%", Integer.toString(event.getGui().height))).parse(0);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					btnX = event.getGui().width / 2 + 104;
					btnY = event.getGui().height / 4 + 48 + 60;
				}
				event.addWidget(new GuiButtonWithImage(btnX, btnY, btn -> event.getGui().getMinecraft().setScreen(new GuiAccountSelector(event.getGui()))));
			}
		}
		if (event.getGui() instanceof MultiplayerScreen) {
			if (Config.showOnMPScreen) {
				event.addWidget(new GuiButtonWithImage(event.getGui().width / 2 + 4 + 76 + 79, event.getGui().height - 28, btn -> {
					event.getGui().getMinecraft().setScreen(new GuiAccountSelector(event.getGui()));
				}));
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.getGui() instanceof MainMenuScreen) {
			Minecraft mc = event.getGui().getMinecraft();
			Screen.drawCenteredString(event.getMatrixStack(), mc.font, new TranslationTextComponent("ias.loggedinas",
					mc.getUser().getName()), textX, textY, 0xFFCC8888);
		}
		if (event.getGui() instanceof MultiplayerScreen) {
			Minecraft mc = event.getGui().getMinecraft();
			if (mc.getUser().getAccessToken().equals("0") || mc.getUser().getAccessToken().equals("-")) {
				List<IReorderingProcessor> list = mc.font.split(new TranslationTextComponent("ias.offlinemode"), event.getGui().width);
				for (int i = 0; i < list.size(); i++) {
					mc.font.drawShadow(event.getMatrixStack(), list.get(i), event.getGui().width / 2 - mc.font.width(list.get(i)) / 2, i * 9 + 1, 16737380);
				}
			}
		}
	}
}
