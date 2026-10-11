package dev.mihail.minimalgearhud.config;

import dev.mihail.minimalgearhud.logic.Anchor;
import dev.mihail.minimalgearhud.logic.ColorScheme;
import dev.mihail.minimalgearhud.logic.DisplayMode;
import dev.mihail.minimalgearhud.logic.Visibility;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Клиентский конфиг (config/minimalgearhud-client.toml). Подписи и подсказки
 * берутся из lang-файлов по ключам {@code minimalgearhud.configuration.*}.
 *
 * <p>Значения читаются в {@link GearHudSettings#read()} один раз за тик,
 * а не в каждом кадре.
 */
public final class GearHudConfig {

    private static final String KEY = "minimalgearhud.configuration.";

    public static final ModConfigSpec SPEC;

    // general
    static final ModConfigSpec.BooleanValue ENABLED;
    static final ModConfigSpec.EnumValue<Visibility> VISIBILITY;
    static final ModConfigSpec.BooleanValue HIDE_IN_SCREENS;

    // content
    static final ModConfigSpec.BooleanValue SHOW_ARMOR;
    static final ModConfigSpec.BooleanValue SHOW_MAIN_HAND;
    static final ModConfigSpec.BooleanValue SHOW_OFF_HAND;
    static final ModConfigSpec.BooleanValue SHOW_USES_ESTIMATE;
    static final ModConfigSpec.BooleanValue SHOW_MENDING_MARKER;

    // layout
    static final ModConfigSpec.EnumValue<Anchor> ANCHOR;
    static final ModConfigSpec.IntValue OFFSET_X;
    static final ModConfigSpec.IntValue OFFSET_Y;
    static final ModConfigSpec.DoubleValue SCALE;
    static final ModConfigSpec.DoubleValue BACKGROUND_OPACITY;

    // style
    static final ModConfigSpec.EnumValue<DisplayMode> DISPLAY_MODE;
    static final ModConfigSpec.BooleanValue SHOW_BAR;
    static final ModConfigSpec.EnumValue<ColorScheme> COLOR_SCHEME;

    // warning
    static final ModConfigSpec.BooleanValue WARNING_ENABLED;
    static final ModConfigSpec.IntValue LOW_PERCENT;
    static final ModConfigSpec.IntValue CRITICAL_PERCENT;
    static final ModConfigSpec.BooleanValue WARN_ARMOR;
    static final ModConfigSpec.BooleanValue WARN_OFF_HAND;
    static final ModConfigSpec.BooleanValue VIGNETTE;
    static final ModConfigSpec.DoubleValue VIGNETTE_STRENGTH;
    static final ModConfigSpec.DoubleValue PULSE_SPEED;
    static final ModConfigSpec.BooleanValue REDUCED_MOTION;
    static final ModConfigSpec.BooleanValue SOUND;
    static final ModConfigSpec.DoubleValue SOUND_VOLUME;
    static final ModConfigSpec.IntValue SOUND_COOLDOWN_SECONDS;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.translation(KEY + "general").push("general");
        ENABLED = b.translation(KEY + "enabled")
                .comment("Master switch for the whole HUD widget and warnings. Can also be toggled in game with a key.")
                .define("enabled", true);
        VISIBILITY = b.translation(KEY + "visibility")
                .comment("Which items are listed: ALWAYS, DAMAGED_ONLY or WARNINGS_ONLY.")
                .defineEnum("visibility", Visibility.ALWAYS);
        HIDE_IN_SCREENS = b.translation(KEY + "hideInScreens")
                .comment("Hide the widget while a menu or inventory is open (chat keeps it visible).")
                .define("hideInScreens", true);
        b.pop();

        b.translation(KEY + "content").push("content");
        SHOW_ARMOR = b.translation(KEY + "showArmor")
                .comment("Show worn armor pieces that have durability.")
                .define("showArmor", true);
        SHOW_MAIN_HAND = b.translation(KEY + "showMainHand")
                .comment("Show the item in the main hand if it has durability.")
                .define("showMainHand", true);
        SHOW_OFF_HAND = b.translation(KEY + "showOffHand")
                .comment("Show the item in the off hand if it has durability (shield, tools...).")
                .define("showOffHand", true);
        SHOW_USES_ESTIMATE = b.translation(KEY + "showUsesEstimate")
                .comment("Show an approximate number of remaining uses (~N) for held items. Takes Unbreaking into account.")
                .define("showUsesEstimate", true);
        SHOW_MENDING_MARKER = b.translation(KEY + "showMendingMarker")
                .comment("Show a small green + next to items that have Mending.")
                .define("showMendingMarker", true);
        b.pop();

        b.translation(KEY + "layout").push("layout");
        ANCHOR = b.translation(KEY + "anchor")
                .comment("Which part of the screen the widget is attached to.")
                .defineEnum("anchor", Anchor.MIDDLE_LEFT);
        OFFSET_X = b.translation(KEY + "offsetX")
                .comment("Extra horizontal shift in GUI pixels (positive = right).")
                .defineInRange("offsetX", 0, -2000, 2000);
        OFFSET_Y = b.translation(KEY + "offsetY")
                .comment("Extra vertical shift in GUI pixels (positive = down).")
                .defineInRange("offsetY", 0, -2000, 2000);
        SCALE = b.translation(KEY + "scale")
                .comment("Widget size multiplier.")
                .defineInRange("scale", 1.0, 0.5, 3.0);
        BACKGROUND_OPACITY = b.translation(KEY + "backgroundOpacity")
                .comment("Opacity of the dark panel behind the widget (0 = none).")
                .defineInRange("backgroundOpacity", 0.35, 0.0, 1.0);
        b.pop();

        b.translation(KEY + "style").push("style");
        DISPLAY_MODE = b.translation(KEY + "displayMode")
                .comment("How durability is written: PERCENT, REMAINING, REMAINING_OF_MAX or BAR_ONLY.")
                .defineEnum("displayMode", DisplayMode.PERCENT);
        SHOW_BAR = b.translation(KEY + "showBar")
                .comment("Draw a thin colored durability bar under the text.")
                .define("showBar", true);
        COLOR_SCHEME = b.translation(KEY + "colorScheme")
                .comment("CLASSIC (green to red), COLORBLIND (blue to orange) or MONOCHROME.")
                .defineEnum("colorScheme", ColorScheme.CLASSIC);
        b.pop();

        b.translation(KEY + "warning").push("warning");
        WARNING_ENABLED = b.translation(KEY + "warningEnabled")
                .comment("Pulse a warning when an item is about to break.")
                .define("warningEnabled", true);
        LOW_PERCENT = b.translation(KEY + "lowPercent")
                .comment("Below this durability percentage a soft warning starts.")
                .defineInRange("lowPercent", 10, 1, 100);
        CRITICAL_PERCENT = b.translation(KEY + "criticalPercent")
                .comment("Below this durability percentage the warning becomes urgent. An item that breaks on its next use is always urgent.")
                .defineInRange("criticalPercent", 5, 0, 100);
        WARN_ARMOR = b.translation(KEY + "warnArmor")
                .comment("Also warn about armor (including elytra).")
                .define("warnArmor", true);
        WARN_OFF_HAND = b.translation(KEY + "warnOffHand")
                .comment("Also warn about the off-hand item.")
                .define("warnOffHand", true);
        VIGNETTE = b.translation(KEY + "vignette")
                .comment("Pulse a soft glow along the screen edges.")
                .define("vignette", true);
        VIGNETTE_STRENGTH = b.translation(KEY + "vignetteStrength")
                .comment("Maximum brightness of the screen-edge glow.")
                .defineInRange("vignetteStrength", 0.5, 0.05, 1.0);
        PULSE_SPEED = b.translation(KEY + "pulseSpeed")
                .comment("Pulse speed multiplier.")
                .defineInRange("pulseSpeed", 1.0, 0.25, 3.0);
        REDUCED_MOTION = b.translation(KEY + "reducedMotion")
                .comment("Show a steady glow instead of a pulsing one (helps if flashing bothers you).")
                .define("reducedMotion", false);
        SOUND = b.translation(KEY + "sound")
                .comment("Play a short sound when an item enters the warning state.")
                .define("sound", false);
        SOUND_VOLUME = b.translation(KEY + "soundVolume")
                .comment("Warning sound volume.")
                .defineInRange("soundVolume", 0.5, 0.0, 1.0);
        SOUND_COOLDOWN_SECONDS = b.translation(KEY + "soundCooldownSeconds")
                .comment("Minimum seconds between two warning sounds.")
                .defineInRange("soundCooldownSeconds", 8, 1, 120);
        b.pop();

        SPEC = b.build();
    }

    private GearHudConfig() {}
}
