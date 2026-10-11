package dev.mihail.minimalgearhud.logic;

/**
 * Анимация пульсации предупреждения. Чистая функция от времени, поэтому легко тестируется
 * и не хранит состояния.
 */
public final class Pulse {

    /** Период пульса при {@link Severity#LOW}, мс (при скорости 1.0). */
    public static final double LOW_PERIOD_MS = 2000.0;
    /** Период пульса при {@link Severity#CRITICAL}, мс (при скорости 1.0). */
    public static final double CRITICAL_PERIOD_MS = 900.0;

    /** Максимальная яркость при LOW: мягкое предупреждение. */
    public static final double LOW_PEAK = 0.55;
    /** Максимальная яркость при CRITICAL. */
    public static final double CRITICAL_PEAK = 1.0;

    /** Яркость, когда пульс отключён (режим «без мерцания»). */
    public static final double STEADY_LOW = 0.30;
    public static final double STEADY_CRITICAL = 0.55;

    private Pulse() {}

    /**
     * @param severity      степень опасности
     * @param nowMillis     текущее время в миллисекундах (любой монотонный источник)
     * @param speed         множитель скорости (ограничивается диапазоном 0.25..4)
     * @param reducedMotion {@code true}: вместо пульса постоянное ровное свечение
     * @return интенсивность в диапазоне 0..1 (0 — ничего не рисовать)
     */
    public static double intensity(Severity severity, long nowMillis, double speed, boolean reducedMotion) {
        if (severity == Severity.OK) {
            return 0.0;
        }
        boolean critical = severity == Severity.CRITICAL;
        if (reducedMotion) {
            return critical ? STEADY_CRITICAL : STEADY_LOW;
        }
        double clampedSpeed = Math.max(0.25, Math.min(speed, 4.0));
        double period = (critical ? CRITICAL_PERIOD_MS : LOW_PERIOD_MS) / clampedSpeed;
        double phase = (nowMillis % (long) Math.ceil(period)) / period;
        // плавная волна 0..1..0, без резких скачков
        double wave = 0.5 - 0.5 * Math.cos(phase * 2.0 * Math.PI);
        double peak = critical ? CRITICAL_PEAK : LOW_PEAK;
        return clamp01(wave * peak);
    }

    private static double clamp01(double v) {
        return v < 0 ? 0 : Math.min(v, 1);
    }
}
