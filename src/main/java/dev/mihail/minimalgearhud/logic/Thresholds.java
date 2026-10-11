package dev.mihail.minimalgearhud.logic;

/**
 * Пороги предупреждений в долях от максимальной прочности (0..1).
 *
 * @param low      ниже этой доли предмет считается {@link Severity#LOW}
 * @param critical ниже этой доли предмет считается {@link Severity#CRITICAL}
 */
public record Thresholds(double low, double critical) {

    public static final Thresholds DEFAULT = new Thresholds(0.10, 0.05);

    public Thresholds {
        if (Double.isNaN(low) || Double.isNaN(critical)) {
            throw new IllegalArgumentException("thresholds must be numbers");
        }
        low = clamp01(low);
        critical = clamp01(critical);
        // critical не может быть выше low, иначе LOW никогда не наступит
        if (critical > low) {
            critical = low;
        }
    }

    /** Создаёт пороги из процентов (например, 10 и 5). */
    public static Thresholds ofPercent(double lowPercent, double criticalPercent) {
        return new Thresholds(lowPercent / 100.0, criticalPercent / 100.0);
    }

    private static double clamp01(double v) {
        return v < 0 ? 0 : Math.min(v, 1);
    }
}
