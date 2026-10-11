package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.MinimalGearHud;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Все клиентские подписки на события в одном месте.
 * Шина (мод-шина или игровая) определяется автоматически по типу события.
 */
@EventBusSubscriber(modid = MinimalGearHud.MOD_ID, value = Dist.CLIENT)
public final class ClientModEvents {

    private static final Identifier VIGNETTE_LAYER = Identifier.fromNamespaceAndPath(MinimalGearHud.MOD_ID, "warning_vignette");
    private static final Identifier WIDGET_LAYER = Identifier.fromNamespaceAndPath(MinimalGearHud.MOD_ID, "gear_widget");

    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.registerCategory(ClientKeys.CATEGORY);
        event.register(ClientKeys.TOGGLE);
    }

    @SubscribeEvent
    public static void registerLayers(RegisterGuiLayersEvent event) {
        // Свечение по краям рисуется под хотбаром, чтобы не закрашивать сам HUD.
        event.registerAbove(VanillaGuiLayers.AFTER_CAMERA_DECORATIONS, VIGNETTE_LAYER, new WarningVignetteLayer());
        // Виджет под чатом и списком игроков: они должны оставаться читаемыми поверх него.
        event.registerBelow(VanillaGuiLayers.CHAT, WIDGET_LAYER, new GearHudLayer());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        GearTracker.tick(Minecraft.getInstance());
    }
}
