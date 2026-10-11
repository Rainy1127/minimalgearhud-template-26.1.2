package dev.mihail.minimalgearhud.logic;

/**
 * Как показывать прочность текстом рядом с иконкой.
 */
public enum DisplayMode {
    /** Например, «73%». */
    PERCENT,
    /** Например, «340». */
    REMAINING,
    /** Например, «340/468». */
    REMAINING_OF_MAX,
    /** Только полоска, без текста. */
    BAR_ONLY
}
