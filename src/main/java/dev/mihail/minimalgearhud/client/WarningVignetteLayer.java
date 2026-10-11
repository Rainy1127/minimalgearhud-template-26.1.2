package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.config.GearHudSettings;
import dev.mihail.minimalgearhud.logic.Colors;
import dev.mihail.minimalgearhud.logic.Pulse;
import dev.mihail.minimalgearhud.logic.Vignette;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.neoforge.client.gui.GuiLayer;

/**
 * Мягкое пульсирующее свечение по краям экрана, когда предмет вот-вот сломается.
 * Рисуется вложенными прямоугольными рамками с убывающей прозрачностью, без текстур.
 */
public final class WarningVignetteLayer implements GuiLayer {

    /** Рамки тусклее этого порога не рисуем: их всё равно не видно. */
    private static final double MIN_VISIBLE_ALPHA = 0.004;

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        GearHudSettings s = GearTracker.settings();
        if (s == null || !s.warningEnabled() || !s.vignette()) {
            return;
        }
        GearSnapshot snapshot = GearTracker.snapshot();
        if (!snapshot.worst().isWarning()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        // F1 прячет весь интерфейс; поверх меню и инвентаря свечение не нужно.
        if (mc.options.hideGui || mc.screen != null) {
            return;
        }

        double intensity = Pulse.intensity(snapshot.worst(), GearTracker.nowMillis(), s.pulseSpeed(), s.reducedMotion());
        if (intensity <= 0.0) {
            return;
        }

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int steps = Vignette.STEPS;
        int step = Vignette.stepSize(width, height, steps);
        int tint = s.colorScheme().warningTint();

        for (int i = 0; i < steps; i++) {
            double alpha = Vignette.stripAlpha(i, steps, intensity, s.vignetteStrength());
            if (alpha < MIN_VISIBLE_ALPHA) {
                continue;
            }
            int color = Colors.withAlpha(tint, alpha);
            int offset = i * step;
            int innerTop = offset + step;
            int innerBottom = height - offset - step;
            if (innerTop >= innerBottom) {
                break; // рамки сошлись в центре маленького окна
            }
            // верх и низ на всю ширину
            graphics.fill(offset, offset, width - offset, innerTop, color);
            graphics.fill(offset, innerBottom, width - offset, height - offset, color);
            // лево и право между ними, чтобы углы не рисовались дважды
            graphics.fill(offset, innerTop, offset + step, innerBottom, color);
            graphics.fill(width - offset - step, innerTop, width - offset, innerBottom, color);
        }
    }
}
