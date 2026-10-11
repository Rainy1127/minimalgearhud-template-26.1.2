package dev.mihail.minimalgearhud.logic;

/**
 * Цветовые схемы. Значения цветов — 0xRRGGBB без альфа-канала.
 *
 * <p>Схема {@link #COLORBLIND} построена на палитре Окабе-Ито и различима при дейтеранопии
 * и протанопии: вместо пары «зелёный/красный» используются «голубой/оранжевый».
 */
public enum ColorScheme {
    CLASSIC(0x55FF55, 0xFFFF55, 0xFF5555, 0xFF2A2A),
    COLORBLIND(0x56B4E9, 0xE69F00, 0xD55E00, 0xD55E00),
    MONOCHROME(0xFFFFFF, 0xBBBBBB, 0x777777, 0xFFFFFF);

    private final int good;
    private final int mid;
    private final int bad;
    private final int warningTint;

    ColorScheme(int good, int mid, int bad, int warningTint) {
        this.good = good;
        this.mid = mid;
        this.bad = bad;
        this.warningTint = warningTint;
    }

    /** Цвет для доли прочности {@code fraction} (1 — полная, 0 — пустая). */
    public int colorFor(double fraction) {
        double f = Math.max(0.0, Math.min(fraction, 1.0));
        if (f >= 0.5) {
            return Colors.lerp(mid, good, (f - 0.5) * 2.0);
        }
        return Colors.lerp(bad, mid, f * 2.0);
    }

    /** Цвет подсветки/виньетки при предупреждении. */
    public int warningTint() {
        return warningTint;
    }
}
