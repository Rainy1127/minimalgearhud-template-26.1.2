package dev.mihail.minimalgearhud.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.mihail.minimalgearhud.MinimalGearHud;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Клавиши мода. Регистрируются в {@link ClientModEvents}.
 */
public final class ClientKeys {

    /** Раздел в настройках управления: «Minimal Gear HUD». Ключ перевода: key.category.minimalgearhud.main */
    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(MinimalGearHud.MOD_ID, "main"));

    /** Показать / спрятать виджет и предупреждения. */
    public static final KeyMapping TOGGLE = new KeyMapping(
            "key.minimalgearhud.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY);

    private ClientKeys() {}
}
