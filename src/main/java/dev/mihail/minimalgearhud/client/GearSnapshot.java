package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.logic.Severity;
import java.util.List;

/**
 * Снимок экипировки на текущий тик.
 *
 * @param entries строки виджета в порядке отображения
 * @param worst   наихудшая степень опасности среди предметов, о которых нужно предупреждать
 */
public record GearSnapshot(List<GearEntry> entries, Severity worst) {

    public static final GearSnapshot EMPTY = new GearSnapshot(List.of(), Severity.OK);
}
