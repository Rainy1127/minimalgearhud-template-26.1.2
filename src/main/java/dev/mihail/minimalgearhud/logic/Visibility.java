package dev.mihail.minimalgearhud.logic;

/**
 * Какие предметы вообще показывать в виджете («умное скрытие»).
 */
public enum Visibility {
    /** Все предметы с прочностью. */
    ALWAYS,
    /** Только повреждённые (прочность ниже максимума). */
    DAMAGED_ONLY,
    /** Только предметы, которым уже нужно внимание (LOW или CRITICAL). */
    WARNINGS_ONLY;

    /**
     * @param remaining оставшаяся прочность
     * @param max       максимальная прочность
     * @param severity  степень опасности
     */
    public boolean shows(int remaining, int max, Severity severity) {
        return switch (this) {
            case ALWAYS -> true;
            case DAMAGED_ONLY -> remaining < max;
            case WARNINGS_ONLY -> severity.isWarning();
        };
    }
}
