package dev.mihail.minimalgearhud;

import dev.mihail.minimalgearhud.config.GearHudConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Точка входа. Мод строго клиентский ({@code dist = Dist.CLIENT}): на выделенном сервере
 * этот класс даже не загружается, а серверу мод не нужен.
 */
@Mod(value = MinimalGearHud.MOD_ID, dist = Dist.CLIENT)
public final class MinimalGearHud {

    public static final String MOD_ID = "minimalgearhud";

    public MinimalGearHud(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, GearHudConfig.SPEC);
        // Экран настроек в списке модов (Mods -> Minimal Gear HUD -> Config)
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (modContainer, parent) -> new ConfigurationScreen(modContainer, parent));
    }
}
