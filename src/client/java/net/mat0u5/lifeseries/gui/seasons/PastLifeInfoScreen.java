package net.mat0u5.lifeseries.gui.seasons;

import net.mat0u5.lifeseries.gui.DefaultScreen;
import net.mat0u5.lifeseries.render.RenderUtils;
import net.mat0u5.lifeseries.utils.TextColors;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class PastLifeInfoScreen extends DefaultScreen {

    private static final String pastLifeInfoText =
            "§n§lIMPORTANT:§r\n"+
            "The main twist of Past Life is that every session is played on a different version of Minecraft. That, obviously, cannot be done with this one mod.\n" +
            "I've made a separate project on Modrinth which has §nonly§r the Past Life mod for each of the versions used in the original series.\n" +
            "Click the button below to go to that mod page.\n" +
            "§7§o§nKeep in mind that the setup is quite difficult for the earliest mc versions.§r\n\n"+
            "§8Past Life is still fully playable here (with this mod), it just won't have the different versions aspect.";

    protected PastLifeInfoScreen() {
        super(Text.of("Past Life Info"), 410, 210);
    }

    @Override
    public void init() {
        super.init();

        String buttonText = "Open Past Life Mod Page";

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal(buttonText), btn -> {
                            this.close();
                            Util.getOperatingSystem().open("https://modrinth.com/mod/past-life");
                        })
                        .position(centerX - 90, endY - 25)
                        .size(180, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Close"), btn -> {
                            this.close();;
                        })
                        .position(endX - 70, endY - 25)
                        .size(60, 20)
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.drawTextCenterScaled(context, this.textRenderer, Text.of("§0Past Life"), centerX, startY + 7, 2f, 2f);
        RenderUtils.drawTextLeftWrapLines(context, this.textRenderer, TextColors.PASTEL_RED, Text.of(pastLifeInfoText), startX + 12, startY + 30, BG_WIDTH-30, 6);
    }
}
