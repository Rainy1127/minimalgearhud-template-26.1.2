package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.config.GearHudSettings;
import dev.mihail.minimalgearhud.logic.Colors;
import dev.mihail.minimalgearhud.logic.Layout;
import dev.mihail.minimalgearhud.logic.Pulse;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.joml.Matrix3x2fStack;

/**
 * Виджет: руки и броня с иконками, процентами и полоской прочности.
 * Строка предмета, которому грозит поломка, подсвечивается пульсирующей рамкой.
 */
public final class GearHudLayer implements GuiLayer {

    private static final int PADDING = 3;
    private static final int ICON = 16;
    private static final int GAP = 4;
    private static final int ROW_HEIGHT = 18;
    private static final int ROW_GAP = 2;
    private static final int BAR_WIDTH = 32;
    private static final int BAR_HEIGHT = 2;
    private static final int SCREEN_MARGIN = 4;

    private static final int BAR_BACKGROUND = 0xFF2A2A2A;
    private static final int MENDING_COLOR = 0xFF55FF55;

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        GearHudSettings s = GearTracker.settings();
        if (s == null) {
            return;
        }
        List<GearEntry> entries = GearTracker.snapshot().entries();
        if (entries.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        // Модовые слои сами не знают про F1, поэтому проверяем вручную.
        if (mc.options.hideGui) {
            return;
        }
        if (s.hideInScreens() && mc.screen != null && !(mc.screen instanceof ChatScreen)) {
            return;
        }

        Font font = mc.font;
        boolean bar = s.showBar();

        int textWidth = 0;
        for (GearEntry entry : entries) {
            textWidth = Math.max(textWidth, font.width(entry.text()));
        }
        int rightColumn = Math.max(textWidth, bar ? BAR_WIDTH : 0);
        int contentWidth = ICON + (rightColumn > 0 ? GAP + rightColumn : 0);
        int width = contentWidth + PADDING * 2;
        int count = entries.size();
        int height = PADDING * 2 + count * ROW_HEIGHT + (count - 1) * ROW_GAP;

        float scale = s.scale();
        Layout.Placement placement = Layout.place(
                s.anchor(), graphics.guiWidth(), graphics.guiHeight(),
                Math.round(width * scale), Math.round(height * scale),
                SCREEN_MARGIN, s.offsetX(), s.offsetY());

        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate((float) placement.x(), (float) placement.y());
        pose.scale(scale, scale);
        try {
            drawPanel(graphics, s, entries, font, width, height);
        } finally {
            pose.popMatrix();
        }
    }

    private static void drawPanel(GuiGraphicsExtractor graphics, GearHudSettings s, List<GearEntry> entries,
                                  Font font, int width, int height) {
        if (s.backgroundOpacity() > 0.01f) {
            graphics.fill(0, 0, width, height, Colors.withAlpha(0x000000, s.backgroundOpacity()));
        }

        long now = GearTracker.nowMillis();
        boolean bar = s.showBar();
        int y = PADDING;
        for (GearEntry entry : entries) {
            int x = PADDING;

            if (entry.warning()) {
                double k = Pulse.intensity(entry.severity(), now, s.pulseSpeed(), s.reducedMotion());
                if (k > 0.01) {
                    int tint = s.colorScheme().warningTint();
                    graphics.fill(x - 2, y - 1, width - PADDING + 2, y + ROW_HEIGHT + 1, Colors.withAlpha(tint, 0.45 * k));
                }
            }

            graphics.item(entry.stack(), x, y + 1);
            if (entry.mending()) {
                graphics.text(font, "+", x + 11, y - 1, MENDING_COLOR, true);
            }

            int textX = x + ICON + GAP;
            int color = 0xFF000000 | s.colorScheme().colorFor(entry.fraction());
            boolean hasText = !entry.text().isEmpty();

            if (hasText) {
                int textY = bar ? y + 2 : y + (ROW_HEIGHT - font.lineHeight) / 2;
                graphics.text(font, entry.text(), textX, textY, color, true);
            }
            if (bar) {
                int barY = hasText ? y + 13 : y + (ROW_HEIGHT - BAR_HEIGHT) / 2;
                graphics.fill(textX, barY, textX + BAR_WIDTH, barY + BAR_HEIGHT, BAR_BACKGROUND);
                int filled = (int) Math.round(BAR_WIDTH * entry.fraction());
                if (entry.fraction() > 0.0) {
                    filled = Math.max(1, filled); // почти пустую полоску всё равно видно
                }
                graphics.fill(textX, barY, textX + filled, barY + BAR_HEIGHT, color);
            }

            y += ROW_HEIGHT + ROW_GAP;
        }
    }
}
