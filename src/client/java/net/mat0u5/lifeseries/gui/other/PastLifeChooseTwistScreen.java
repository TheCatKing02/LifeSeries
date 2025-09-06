package net.mat0u5.lifeseries.gui.other;

import net.mat0u5.lifeseries.MainClient;
import net.mat0u5.lifeseries.gui.DefaultSmallScreen;
import net.mat0u5.lifeseries.render.RenderUtils;
import net.mat0u5.lifeseries.utils.ClientUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PastLifeChooseTwistScreen extends DefaultSmallScreen {

    public PastLifeChooseTwistScreen() {
        super(Text.of("Choose Twist"), 1.5f, 1.2f);
    }

    @Override
    public void init() {
        super.init();

        int thirdX = startX + BG_WIDTH / 3;
        int third2X = startX + (BG_WIDTH*2) / 3;

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("The Boogeyman"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife boogeyman");
                        })
                        .position(thirdX - 50, startY + 40)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Secret Society"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife society");
                        })
                        .position(third2X - 50, startY + 40)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Pick Randomly"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife pickRandom");
                        })
                        .position(thirdX - 50, startY + 70)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("No Twist"), btn -> {
                            this.close();
                        })
                        .position(third2X - 50, startY + 70)
                        .size(100, 20)
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.drawTextCenter(context, textRenderer, Text.of("Choose Past Life session twist:"), centerX, startY + 10);
    }
}
