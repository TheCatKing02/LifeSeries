package net.mat0u5.lifeseries.gui.other;

import net.mat0u5.lifeseries.gui.DefaultScreen;
import net.mat0u5.lifeseries.network.NetworkHandlerClient;
import net.mat0u5.lifeseries.render.RenderUtils;
import net.mat0u5.lifeseries.utils.enums.PacketNames;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ChooseWildcardScreen extends DefaultScreen {

    public ChooseWildcardScreen() {
        super(Text.literal("Choose Wildcard Screen"), 230, 150);
    }

    @Override
    protected void init() {
        super.init();
        int oneThirdX = startX + BG_WIDTH / 3;
        int twoThirdX = startX + (BG_WIDTH / 3)*2;

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Size Shifting"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"size_shifting");
                        })
                        .position(oneThirdX - 50, startY  + 40)
                        .size(80, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Hunger"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"hunger");
                        })
                        .position(oneThirdX - 50, startY  + 65)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Snails"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"snails");
                        })
                        .position(oneThirdX - 50, startY  + 90)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Time Dilation"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"time_dilation");
                        })
                        .position(oneThirdX - 50, startY  + 115)
                        .size(80, 20)
                        .build()
        );


        /*
            Second column
         */

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Trivia"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"trivia");
                        })
                        .position(twoThirdX - 30, startY  + 40)
                        .size(80, 20)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Mob Swap"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"mob_swap");
                        })
                        .position(twoThirdX - 30, startY  + 65)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Superpowers"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"superpowers");
                        })
                        .position(twoThirdX - 30, startY  + 90)
                        .size(80, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Callback"), btn -> {
                            this.close();
                            NetworkHandlerClient.sendStringPacket(PacketNames.SELECTED_WILDCARD,"callback");
                        })
                        .position(twoThirdX - 30, startY  + 115)
                        .size(80, 20)
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        String prompt = "Select the Wildcard for this session.";
        RenderUtils.drawTextCenter(context, this.textRenderer, Text.of(prompt), centerX, startY + 20);
    }
}
