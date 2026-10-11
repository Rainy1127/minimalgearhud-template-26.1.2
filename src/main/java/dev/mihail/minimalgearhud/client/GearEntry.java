package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.logic.Severity;
import net.minecraft.world.item.ItemStack;

/**
 * Готовая к отрисовке строка виджета. Все строки и числа считаются один раз за тик,
 * чтобы в кадре не создавать лишний мусор.
 *
 * @param slot     какой слот
 * @param stack    предмет (копия не нужна: стек только читается)
 * @param fraction доля оставшейся прочности 0..1
 * @param severity степень опасности
 * @param text     готовая подпись («73%», «340/468», «73%  ~680»)
 * @param mending  у предмета есть «Починка»
 * @param warning  предмет сейчас в состоянии предупреждения (учтены настройки, креатив и наблюдатель)
 */
public record GearEntry(
        GearSlot slot,
        ItemStack stack,
        double fraction,
        Severity severity,
        String text,
        boolean mending,
        boolean warning) {
}
