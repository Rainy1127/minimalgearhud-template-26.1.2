package dev.mihail.minimalgearhud.logic;

/**
 * Небольшие помощники для работы с цветами в формате ARGB / RGB.
 */
public final class Colors {

    private Colors() {}

    /** Линейная интерполяция двух RGB-цветов, {@code t} в диапазоне 0..1. */
    public static int lerp(int from, int to, double t) {
        double k = Math.max(0.0, Math.min(t, 1.0));
        int r = mix((from >> 16) & 0xFF, (to >> 16) & 0xFF, k);
        int g = mix((from >> 8) & 0xFF, (to >> 8) & 0xFF, k);
        int b = mix(from & 0xFF, to & 0xFF, k);
        return (r << 16) | (g << 8) | b;
    }

    /**
     * Добавляет альфа-канал.
     *
     * @param rgb   цвет 0xRRGGBB
     * @param alpha прозрачность 0..1
     * @return цвет 0xAARRGGBB
     */
    public static int withAlpha(int rgb, double alpha) {
        int a = (int) Math.round(Math.max(0.0, Math.min(alpha, 1.0)) * 255.0);
        return (a << 24) | (rgb & 0xFFFFFF);
    }

    private static int mix(int a, int b, double k) {
        return (int) Math.round(a + (b - a) * k);
    }
}
