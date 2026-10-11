package dev.mihail.minimalgearhud.logic;

/**
 * Вся арифметика прочности в одном месте, без зависимостей от Minecraft,
 * поэтому её можно тестировать обычным JUnit.
 *
 * <p>В Minecraft предмет хранит {@code damage} (сколько потрачено) и {@code maxDamage}
 * (полный запас). Оставшаяся прочность = {@code maxDamage - damage}.
 */
public final class DurabilityMath {

    private DurabilityMath() {}

    /** Оставшаяся прочность, всегда в диапазоне {@code [0, maxDamage]}. */
    public static int remaining(int damage, int maxDamage) {
        if (maxDamage <= 0) {
            return 0;
        }
        int clamped = Math.max(0, Math.min(damage, maxDamage));
        return maxDamage - clamped;
    }

    /** Доля оставшейся прочности в диапазоне {@code [0, 1]}. Для недамажимых предметов — 1. */
    public static double fraction(int damage, int maxDamage) {
        if (maxDamage <= 0) {
            return 1.0;
        }
        return remaining(damage, maxDamage) / (double) maxDamage;
    }

    /** Следующее использование сломает предмет (осталась 1 единица прочности или меньше). */
    public static boolean breaksOnNextUse(int remaining) {
        return remaining <= 1;
    }

    /**
     * Определяет степень опасности предмета.
     *
     * @param remaining оставшаяся прочность
     * @param max       максимальная прочность
     * @param t         пороги
     */
    public static Severity severity(int remaining, int max, Thresholds t) {
        if (max <= 0) {
            return Severity.OK;
        }
        if (breaksOnNextUse(remaining)) {
            return Severity.CRITICAL;
        }
        double f = remaining / (double) max;
        if (f < t.critical()) {
            return Severity.CRITICAL;
        }
        if (f < t.low()) {
            return Severity.LOW;
        }
        return Severity.OK;
    }

    /**
     * Примерное число оставшихся использований инструмента с учётом «Прочности» (Unbreaking).
     *
     * <p>Для инструментов шанс не потратить прочность равен {@code level / (level + 1)},
     * то есть в среднем одно «списание» растягивается на {@code level + 1} использований.
     * Это оценка: некоторые действия тратят больше 1 единицы (например, меч по блоку).
     *
     * @return оценка, либо {@code -1}, если предмет без прочности
     */
    public static int estimateUses(int remaining, int unbreakingLevel) {
        if (remaining < 0) {
            return -1;
        }
        long uses = (long) remaining * (Math.max(0, unbreakingLevel) + 1L);
        return (int) Math.min(uses, Integer.MAX_VALUE);
    }
}
