package dev.mihail.minimalgearhud.logic;

/**
 * Решает, когда проиграть звуковой сигнал: один раз при ухудшении состояния,
 * но не чаще, чем раз в {@code cooldown} (чтобы не спамить при быстром переключении слотов).
 *
 * <p>Класс хранит небольшое состояние, поэтому у каждого игрового окружения свой экземпляр.
 */
public final class WarningTrigger {

    private Severity last = Severity.OK;
    private long lastFiredAt = Long.MIN_VALUE;
    private boolean hasFired = false;

    /**
     * @param current    текущая наихудшая степень опасности
     * @param nowMillis  текущее время, мс
     * @param cooldownMs минимальный интервал между сигналами
     * @return {@code true}, если сейчас нужно проиграть сигнал
     */
    public boolean update(Severity current, long nowMillis, long cooldownMs) {
        boolean worsened = current.ordinal() > last.ordinal();
        last = current;
        if (!worsened) {
            return false;
        }
        if (hasFired && nowMillis - lastFiredAt < cooldownMs) {
            return false;
        }
        hasFired = true;
        lastFiredAt = nowMillis;
        return true;
    }

    /** Сбросить состояние (вход в мир, смена игрока). */
    public void reset() {
        last = Severity.OK;
        hasFired = false;
        lastFiredAt = Long.MIN_VALUE;
    }
}
