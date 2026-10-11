#!/usr/bin/env python3
"""Генерирует en_us.json и ru_ru.json из одной таблицы, чтобы ключи не расходились с GearHudConfig."""
import json, pathlib

OUT = pathlib.Path(__file__).parent.parent / "src/main/resources/assets/minimalgearhud/lang"
P = "minimalgearhud.configuration."

# ключ конфига: (EN название, EN подсказка, RU название, RU подсказка)
CFG = {
    "general": ("General", "Main switches.", "Основное", "Главные переключатели."),
    "enabled": ("Enabled", "Master switch for the widget and warnings. Can also be toggled in game with a key.",
                "Включено", "Главный выключатель виджета и предупреждений. В игре переключается клавишей."),
    "visibility": ("Show items", "ALWAYS: all items with durability. DAMAGED_ONLY: only damaged ones. WARNINGS_ONLY: only items that need attention.",
                   "Какие предметы", "ALWAYS: все с прочностью. DAMAGED_ONLY: только повреждённые. WARNINGS_ONLY: только те, что требуют внимания."),
    "hideInScreens": ("Hide in menus", "Hide the widget while a menu or inventory is open. Chat keeps it visible.",
                      "Прятать в меню", "Прятать виджет при открытом меню или инвентаре. В чате он остаётся."),

    "content": ("Content", "What the widget lists.", "Содержимое", "Что показывает виджет."),
    "showArmor": ("Armor", "Show worn armor pieces that have durability.", "Броня", "Показывать надетую броню с прочностью."),
    "showMainHand": ("Main hand", "Show the main-hand item if it has durability.", "Основная рука", "Показывать предмет в основной руке, если у него есть прочность."),
    "showOffHand": ("Off hand", "Show the off-hand item if it has durability (shield, tools).", "Вторая рука", "Показывать предмет во второй руке (щит, инструменты)."),
    "showUsesEstimate": ("Remaining uses", "Show an approximate number of remaining uses (~N) for held items. Takes Unbreaking into account.",
                         "Остаток использований", "Показывать примерное число оставшихся использований (~N) для предметов в руках. Учитывает «Прочность»."),
    "showMendingMarker": ("Mending marker", "Show a small green + on items that have Mending.", "Значок «Починки»", "Показывать маленький зелёный + на предметах с «Починкой»."),

    "layout": ("Layout", "Where and how big the widget is.", "Расположение", "Где и какого размера виджет."),
    "anchor": ("Anchor", "Which part of the screen the widget is attached to.", "Привязка", "К какой части экрана прикреплён виджет."),
    "offsetX": ("Offset X", "Extra horizontal shift in GUI pixels (positive = right).", "Смещение по X", "Дополнительный сдвиг по горизонтали в пикселях GUI (плюс = вправо)."),
    "offsetY": ("Offset Y", "Extra vertical shift in GUI pixels (positive = down).", "Смещение по Y", "Дополнительный сдвиг по вертикали в пикселях GUI (плюс = вниз)."),
    "scale": ("Scale", "Widget size multiplier.", "Масштаб", "Множитель размера виджета."),
    "backgroundOpacity": ("Background opacity", "Opacity of the dark panel behind the widget (0 = none).", "Прозрачность фона", "Непрозрачность тёмной подложки за виджетом (0 = без подложки)."),

    "style": ("Style", "How durability is drawn.", "Оформление", "Как отображается прочность."),
    "displayMode": ("Text format", "PERCENT, REMAINING, REMAINING_OF_MAX or BAR_ONLY.", "Формат текста", "PERCENT, REMAINING, REMAINING_OF_MAX или BAR_ONLY."),
    "showBar": ("Durability bar", "Draw a thin colored bar under the text.", "Полоска прочности", "Рисовать тонкую цветную полоску под текстом."),
    "colorScheme": ("Color scheme", "CLASSIC (green to red), COLORBLIND (blue to orange) or MONOCHROME.", "Цветовая схема", "CLASSIC (зелёный-красный), COLORBLIND (синий-оранжевый) или MONOCHROME."),

    "warning": ("Warning", "The pulsing low-durability warning.", "Предупреждение", "Пульсирующее предупреждение о низкой прочности."),
    "warningEnabled": ("Enable warning", "Pulse a warning when an item is about to break.", "Включить предупреждение", "Пульсировать, когда предмет вот-вот сломается."),
    "lowPercent": ("Soft warning below (%)", "Below this durability percentage a soft warning starts.", "Мягкое предупреждение ниже (%)", "Ниже этого процента прочности начинается мягкое предупреждение."),
    "criticalPercent": ("Urgent warning below (%)", "Below this percentage the warning becomes urgent. An item that breaks on its next use is always urgent.",
                        "Срочное предупреждение ниже (%)", "Ниже этого процента предупреждение становится срочным. Предмет, который сломается при следующем использовании, всегда срочный."),
    "warnArmor": ("Warn about armor", "Also warn about armor (including elytra).", "Предупреждать о броне", "Предупреждать и о броне (включая элитры)."),
    "warnOffHand": ("Warn about off hand", "Also warn about the off-hand item.", "Предупреждать о второй руке", "Предупреждать и о предмете во второй руке."),
    "vignette": ("Screen-edge glow", "Pulse a soft glow along the screen edges.", "Свечение по краям", "Пульсирующее мягкое свечение по краям экрана."),
    "vignetteStrength": ("Glow strength", "Maximum brightness of the screen-edge glow.", "Яркость свечения", "Максимальная яркость свечения по краям."),
    "pulseSpeed": ("Pulse speed", "Pulse speed multiplier.", "Скорость пульса", "Множитель скорости пульса."),
    "reducedMotion": ("Reduced motion", "Show a steady glow instead of a pulsing one (helps if flashing bothers you).",
                      "Без мерцания", "Ровное свечение вместо пульсирующего (если мерцание мешает)."),
    "sound": ("Warning sound", "Play a short sound when an item enters the warning state.", "Звуковой сигнал", "Короткий звук, когда предмет переходит в состояние предупреждения."),
    "soundVolume": ("Sound volume", "Warning sound volume.", "Громкость сигнала", "Громкость звукового сигнала."),
    "soundCooldownSeconds": ("Sound cooldown (s)", "Minimum seconds between two warning sounds.", "Пауза между сигналами (с)", "Минимум секунд между двумя звуковыми сигналами."),
}

def build(lang):
    i = 0 if lang == "en" else 2
    d = {
        "key.minimalgearhud.toggle": ("Toggle Gear HUD", "Показать / спрятать Gear HUD")[0 if lang == "en" else 1],
        "key.category.minimalgearhud.main": "Minimal Gear HUD",
        "minimalgearhud.message.shown": ("Gear HUD: shown", "Gear HUD: показан")[0 if lang == "en" else 1],
        "minimalgearhud.message.hidden": ("Gear HUD: hidden", "Gear HUD: скрыт")[0 if lang == "en" else 1],
        "minimalgearhud.configuration.title": ("%s Settings", "Настройки %s")[0 if lang == "en" else 1],
        "minimalgearhud.configuration.section.minimalgearhud.client.toml": ("%s Settings", "Настройки %s")[0 if lang == "en" else 1],
        "minimalgearhud.configuration.section.minimalgearhud.client.toml.title": ("%s Settings", "Настройки %s")[0 if lang == "en" else 1],
    }
    for key, vals in CFG.items():
        d[P + key] = vals[i]
        d[P + key + ".tooltip"] = vals[i + 1]
    return d

for lang, fname in (("en", "en_us.json"), ("ru", "ru_ru.json")):
    (OUT / fname).write_text(json.dumps(build(lang), ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("wrote", fname, len(build(lang)), "keys")
