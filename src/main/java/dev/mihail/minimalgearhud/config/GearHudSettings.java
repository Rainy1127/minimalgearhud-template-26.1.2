package dev.mihail.minimalgearhud.config;

import dev.mihail.minimalgearhud.logic.Anchor;
import dev.mihail.minimalgearhud.logic.ColorScheme;
import dev.mihail.minimalgearhud.logic.DisplayMode;
import dev.mihail.minimalgearhud.logic.Thresholds;
import dev.mihail.minimalgearhud.logic.Visibility;

/**
 * Неизменяемый «снимок» настроек. Конфиг читается один раз за тик,
 * а код отрисовки работает с готовыми значениями без обращений к ModConfigSpec.
 */
public record GearHudSettings(
        boolean enabled,
        Visibility visibility,
        boolean hideInScreens,
        boolean showArmor,
        boolean showMainHand,
        boolean showOffHand,
        boolean showUsesEstimate,
        boolean showMendingMarker,
        Anchor anchor,
        int offsetX,
        int offsetY,
        float scale,
        float backgroundOpacity,
        DisplayMode displayMode,
        boolean showBar,
        ColorScheme colorScheme,
        boolean warningEnabled,
        Thresholds thresholds,
        boolean warnArmor,
        boolean warnOffHand,
        boolean vignette,
        float vignetteStrength,
        float pulseSpeed,
        boolean reducedMotion,
        boolean sound,
        float soundVolume,
        long soundCooldownMillis) {

    /** Читает текущие значения конфига. Вызывать только когда {@code SPEC.isLoaded()}. */
    public static GearHudSettings read() {
        return new GearHudSettings(
                GearHudConfig.ENABLED.get(),
                GearHudConfig.VISIBILITY.get(),
                GearHudConfig.HIDE_IN_SCREENS.get(),
                GearHudConfig.SHOW_ARMOR.get(),
                GearHudConfig.SHOW_MAIN_HAND.get(),
                GearHudConfig.SHOW_OFF_HAND.get(),
                GearHudConfig.SHOW_USES_ESTIMATE.get(),
                GearHudConfig.SHOW_MENDING_MARKER.get(),
                GearHudConfig.ANCHOR.get(),
                GearHudConfig.OFFSET_X.get(),
                GearHudConfig.OFFSET_Y.get(),
                GearHudConfig.SCALE.get().floatValue(),
                GearHudConfig.BACKGROUND_OPACITY.get().floatValue(),
                GearHudConfig.DISPLAY_MODE.get(),
                GearHudConfig.SHOW_BAR.get(),
                GearHudConfig.COLOR_SCHEME.get(),
                GearHudConfig.WARNING_ENABLED.get(),
                Thresholds.ofPercent(GearHudConfig.LOW_PERCENT.get(), GearHudConfig.CRITICAL_PERCENT.get()),
                GearHudConfig.WARN_ARMOR.get(),
                GearHudConfig.WARN_OFF_HAND.get(),
                GearHudConfig.VIGNETTE.get(),
                GearHudConfig.VIGNETTE_STRENGTH.get().floatValue(),
                GearHudConfig.PULSE_SPEED.get().floatValue(),
                GearHudConfig.REDUCED_MOTION.get(),
                GearHudConfig.SOUND.get(),
                GearHudConfig.SOUND_VOLUME.get().floatValue(),
                GearHudConfig.SOUND_COOLDOWN_SECONDS.get() * 1000L);
    }
}
