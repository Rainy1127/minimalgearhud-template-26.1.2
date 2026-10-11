package dev.mihail.minimalgearhud.logic;

/**
 * Математика мягкого свечения по краям экрана.
 *
 * <p>Свечение рисуется вложенными рамками: рамка №0 самая внешняя и самая яркая,
 * каждая следующая тоньше по яркости. Так без текстур получается плавный градиент к центру.
 */
public final class Vignette {

    /** Сколько вложенных рамок рисуется. */
    public static final int STEPS = 14;

    /** Максимальная непрозрачность самой внешней рамки (чтобы экран не «краснел» целиком). */
    public static final double MAX_EDGE_ALPHA = 0.6;

    private Vignette() {}

    /**
     * @param index     номер рамки, 0 — самая внешняя
     * @param steps     общее число рамок
     * @param intensity текущая интенсивность пульса 0..1 (см. {@link Pulse})
     * @param strength  настройка игрока 0..1
     * @return непрозрачность рамки 0..1
     */
    public static double stripAlpha(int index, int steps, double intensity, double strength) {
        if (steps <= 0 || index < 0 || index >= steps) {
            return 0.0;
        }
        double t = 1.0 - (double) index / steps; // 1 у края, к центру убывает
        double a = t * t * clamp01(intensity) * clamp01(strength) * MAX_EDGE_ALPHA;
        return clamp01(a);
    }

    /** Толщина одной рамки в пикселях (минимум 1), зависит от меньшей стороны экрана. */
    public static int stepSize(int screenW, int screenH, int steps) {
        if (steps <= 0) {
            return 1;
        }
        int shortSide = Math.max(1, Math.min(screenW, screenH));
        int thickness = Math.max(8, (int) Math.round(shortSide * 0.12));
        return Math.max(1, (int) Math.ceil(thickness / (double) steps));
    }

    private static double clamp01(double v) {
        return v < 0 ? 0 : Math.min(v, 1);
    }
}
