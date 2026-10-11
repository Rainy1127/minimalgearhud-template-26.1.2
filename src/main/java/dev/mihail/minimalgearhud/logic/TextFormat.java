package dev.mihail.minimalgearhud.logic;

import java.util.Locale;

/**
 * Форматирование подписей прочности. Только ASCII, чтобы не зависеть от шрифта игры.
 */
public final class TextFormat {

    private TextFormat() {}

    /** Основная подпись по выбранному режиму. Для {@link DisplayMode#BAR_ONLY} — пустая строка. */
    public static String label(DisplayMode mode, int remaining, int max) {
        return switch (mode) {
            case PERCENT -> percent(remaining, max);
            case REMAINING -> Integer.toString(Math.max(0, remaining));
            case REMAINING_OF_MAX -> Math.max(0, remaining) + "/" + Math.max(0, max);
            case BAR_ONLY -> "";
        };
    }

    /**
     * Проценты с округлением. Если прочность ещё есть, но округление дало бы 0%,
     * показываем «&lt;1%», чтобы не обманывать игрока; полную прочность не округляем вниз до 99%.
     */
    public static String percent(int remaining, int max) {
        if (max <= 0) {
            return "100%";
        }
        if (remaining <= 0) {
            return "0%";
        }
        if (remaining >= max) {
            return "100%";
        }
        long rounded = Math.round(remaining * 100.0 / max);
        if (rounded <= 0) {
            return "<1%";
        }
        if (rounded >= 100) {
            return "99%";
        }
        return rounded + "%";
    }

    /** Подпись оценки числа использований, например «~340» или «~12.3k». */
    public static String uses(int estimate) {
        if (estimate < 0) {
            return "";
        }
        return "~" + compact(estimate);
    }

    /** Компактная запись числа: 9999 -> «9999», 12345 -> «12.3k». */
    public static String compact(int value) {
        if (value < 10_000) {
            return Integer.toString(value);
        }
        if (value < 1_000_000) {
            return String.format(Locale.ROOT, "%.1fk", value / 1000.0);
        }
        return String.format(Locale.ROOT, "%.1fM", value / 1_000_000.0);
    }
}
