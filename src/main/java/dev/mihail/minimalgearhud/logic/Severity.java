package dev.mihail.minimalgearhud.logic;

/**
 * Насколько близок предмет к поломке. Чистая Java, без зависимостей от Minecraft.
 */
public enum Severity {
    /** Всё в порядке. */
    OK,
    /** Прочности мало: мягкое предупреждение. */
    LOW,
    /** Предмет вот-вот сломается: заметное предупреждение. */
    CRITICAL;

    public boolean isWarning() {
        return this != OK;
    }

    /** Более «тяжёлая» из двух степеней. */
    public static Severity worst(Severity a, Severity b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }
}
