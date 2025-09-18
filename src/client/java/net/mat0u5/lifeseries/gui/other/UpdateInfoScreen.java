package net.mat0u5.lifeseries.gui.other;

import net.mat0u5.lifeseries.MainClient;
import net.mat0u5.lifeseries.gui.DefaultScreen;
import net.mat0u5.lifeseries.render.RenderUtils;
import net.mat0u5.lifeseries.utils.TextColors;
import net.mat0u5.lifeseries.utils.other.TextUtils;
import net.mat0u5.lifeseries.utils.versions.UpdateChecker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class UpdateInfoScreen extends DefaultScreen {
    private String versionName;
    private String description;
    private Text dismissText = Text.of("Dismiss for this update");
    private int textWidth = 0;
    private ButtonWidget dismissButton;

    public UpdateInfoScreen(String versionName, String description) {
        super(Text.of("New Life Series Update"), 400, 225, 0, +10);
        this.versionName = versionName;
        this.description = description.replace("\r","");
    }

    public boolean isInCheckboxRegion(int x, int y) {
        return (x >= endX - textWidth/2-2-40) && (x <= endX + textWidth/2+1-40)
                && y >= startY - 23 && y <= startY + 3;
    }

    @Override
    protected void init() {
        super.init();
        textWidth = textRenderer.getWidth(dismissText) + 5;

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Join Discord").withColor(TextColors.PASTEL_WHITE),btn -> {
                            Util.getOperatingSystem().open("https://discord.gg/QWJxfb4zQZ");
                        })
                        .position(startX + 5, endY - 25)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Full Changelog").withColor(TextColors.PASTEL_WHITE), btn -> {
                            Util.getOperatingSystem().open(UpdateChecker.getChangelogLink());
                        })
                        .position(endX - 80 - 5, endY - 25)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Download on Modrinth"), btn -> {
                            this.close();
                            Util.getOperatingSystem().open("https://modrinth.com/mod/life-series"); //Same as having a text with a click event, but that doesnt work in GUIs
                        })
                        .position(centerX - 85, endY - 25)
                        .size(170, 20)
                        .build()
        );
        dismissButton = this.addDrawableChild(
                ButtonWidget.builder(dismissText, btn -> {
                            MainClient.clientConfig.setProperty("ignore_update", String.valueOf(UpdateChecker.version));
                            this.close();
                        })
                        .position(endX - textWidth/2-40, startY - 20)
                        .size(textWidth, 16)
                        .build()
        );
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY) {
        if (isInCheckboxRegion(mouseX, mouseY)) {
            context.fill(endX - textWidth/2-3-40, startY - 23, endX + textWidth/2+3-40, startY, TextColors.BLACK);
            context.fill(endX - textWidth/2-2-40, startY - 22, endX + textWidth/2+2-40, startY - 1, TextColors.GUI_BACKGROUND);
            dismissButton.visible = true;
        }
        else {
            dismissButton.visible = false;
        }
        super.renderBackground(context, mouseX, mouseY);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.drawTextCenter(context, this.textRenderer, Text.of("§0§nA new Life Series mod update is available!"), centerX, startY + 7);
        RenderUtils.drawTextLeft(context, this.textRenderer, TextUtils.formatLoosely("§0§nChangelog in version §l{}§0:",versionName), startX + 7, startY + 25 + textRenderer.fontHeight);
        RenderUtils.drawTextLeftWrapLines(context, this.textRenderer, DEFAULT_TEXT_COLOR, Text.of(description), startX + 7, startY + 30 + textRenderer.fontHeight*2, backgroundWidth-14, 5);
    }
}