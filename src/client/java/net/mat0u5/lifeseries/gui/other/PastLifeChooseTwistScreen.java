package net.mat0u5.lifeseries.gui.other;

import net.mat0u5.lifeseries.gui.DefaultScreen;
import net.mat0u5.lifeseries.render.RenderUtils;
import net.mat0u5.lifeseries.utils.ClientUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PastLifeChooseTwistScreen extends DefaultScreen {

    public PastLifeChooseTwistScreen() {
        super(Text.of("Choose Twist"), 240, 80);
    }

    @Override
    public void init() {
        super.init();

        int firstX = startX + BG_WIDTH / 3-17;
        int secondsX = startX + (BG_WIDTH*2) / 3+17;

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("The Boogeyman"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife boogeyman");
                        })
                        .position(firstX - 50, startY + 27)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Secret Society"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife society");
                        })
                        .position(secondsX - 50, startY + 27)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Pick Randomly"), btn -> {
                            this.close();
                            ClientUtils.runCommand("/pastlife pickRandom");
                        })
                        .position(firstX - 50, startY + 52)
                        .size(100, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("No Twist"), btn -> {
                            this.close();
                        })
                        .position(secondsX - 50, startY + 52)
                        .size(100, 20)
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.drawTextCenter(context, textRenderer, Text.of("Choose Past Life session twist:"), centerX, startY + 10);
    }
}
