package dev.mihail.minimalgearhud.client;

import dev.mihail.minimalgearhud.config.GearHudConfig;
import dev.mihail.minimalgearhud.config.GearHudSettings;
import dev.mihail.minimalgearhud.logic.DurabilityMath;
import dev.mihail.minimalgearhud.logic.Severity;
import dev.mihail.minimalgearhud.logic.TextFormat;
import dev.mihail.minimalgearhud.logic.WarningTrigger;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.Nullable;

/**
 * Раз в клиентский тик собирает снимок экипировки и решает, нужно ли предупреждение.
 * Отрисовка только читает готовый снимок, ничего не считая в кадре.
 *
 * <p>Класс клиентский: ссылается на {@link Minecraft}, поэтому используется
 * только из клиентских обработчиков событий.
 */
public final class GearTracker {

    private static final WarningTrigger TRIGGER = new WarningTrigger();

    private static @Nullable GearHudSettings settings;
    private static GearSnapshot snapshot = GearSnapshot.EMPTY;
    private static boolean hiddenByKey;

    private GearTracker() {}

    /** Актуальные настройки, либо {@code null}, пока конфиг не загружен. */
    public static @Nullable GearHudSettings settings() {
        return settings;
    }

    public static GearSnapshot snapshot() {
        return snapshot;
    }

    /** Вызывается из {@code ClientTickEvent.Post}. */
    public static void tick(Minecraft mc) {
        boolean toggled = false;
        while (ClientKeys.TOGGLE.consumeClick()) {
            hiddenByKey = !hiddenByKey;
            toggled = true;
        }

        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            snapshot = GearSnapshot.EMPTY;
            TRIGGER.reset();
            return;
        }
        if (!GearHudConfig.SPEC.isLoaded()) {
            snapshot = GearSnapshot.EMPTY;
            return;
        }

        GearHudSettings s = GearHudSettings.read();
        settings = s;

        if (toggled) {
            mc.gui.setOverlayMessage(Component.translatable(
                    hiddenByKey ? "minimalgearhud.message.hidden" : "minimalgearhud.message.shown"), false);
        }

        if (!s.enabled() || hiddenByKey) {
            snapshot = GearSnapshot.EMPTY;
            TRIGGER.update(Severity.OK, nowMillis(), s.soundCooldownMillis());
            return;
        }

        Holder<Enchantment> unbreaking = lookup(player, Enchantments.UNBREAKING);
        Holder<Enchantment> mending = lookup(player, Enchantments.MENDING);

        // В творческом режиме и у наблюдателя предметы не ломаются: предупреждать незачем.
        boolean warningsActive = s.warningEnabled() && !player.isCreative() && !player.isSpectator();

        List<GearEntry> entries = new ArrayList<>(GearSlot.values().length);
        Severity worst = Severity.OK;

        for (GearSlot slot : GearSlot.values()) {
            if (!isSlotShown(slot, s)) {
                continue;
            }
            ItemStack stack = player.getItemBySlot(slot.equipmentSlot());
            GearEntry entry = describe(slot, stack, s, warningsActive, unbreaking, mending);
            if (entry == null) {
                continue;
            }
            entries.add(entry);
            if (entry.warning()) {
                worst = Severity.worst(worst, entry.severity());
            }
        }

        snapshot = entries.isEmpty() ? GearSnapshot.EMPTY : new GearSnapshot(List.copyOf(entries), worst);

        boolean fire = TRIGGER.update(worst, nowMillis(), s.soundCooldownMillis());
        if (fire && s.sound() && s.soundVolume() > 0.0f) {
            float pitch = worst == Severity.CRITICAL ? 0.7f : 1.0f;
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING.value(), pitch, s.soundVolume()));
        }
    }

    /** Текущее время в миллисекундах от монотонного источника. */
    public static long nowMillis() {
        return System.nanoTime() / 1_000_000L;
    }

    private static boolean isSlotShown(GearSlot slot, GearHudSettings s) {
        return switch (slot) {
            case MAIN_HAND -> s.showMainHand();
            case OFF_HAND -> s.showOffHand();
            default -> s.showArmor();
        };
    }

    private static boolean warnsAbout(GearSlot slot, GearHudSettings s) {
        return switch (slot) {
            case MAIN_HAND -> true;
            case OFF_HAND -> s.warnOffHand();
            default -> s.warnArmor();
        };
    }

    private static @Nullable GearEntry describe(GearSlot slot, ItemStack stack, GearHudSettings s,
                                                boolean warningsActive,
                                                @Nullable Holder<Enchantment> unbreaking,
                                                @Nullable Holder<Enchantment> mending) {
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return null;
        }
        int max = stack.getMaxDamage();
        if (max <= 0) {
            return null;
        }
        int remaining = DurabilityMath.remaining(stack.getDamageValue(), max);
        Severity severity = DurabilityMath.severity(remaining, max, s.thresholds());
        if (!s.visibility().shows(remaining, max, severity)) {
            return null;
        }

        String label = TextFormat.label(s.displayMode(), remaining, max);
        if (slot.isHand() && s.showUsesEstimate()) {
            int level = enchantLevel(stack, unbreaking);
            String uses = TextFormat.uses(DurabilityMath.estimateUses(remaining, level));
            label = label.isEmpty() ? uses : label + "  " + uses;
        }

        boolean hasMending = s.showMendingMarker() && enchantLevel(stack, mending) > 0;
        boolean warning = warningsActive && severity.isWarning() && warnsAbout(slot, s);
        return new GearEntry(slot, stack, DurabilityMath.fraction(stack.getDamageValue(), max), severity, label, hasMending, warning);
    }

    private static int enchantLevel(ItemStack stack, @Nullable Holder<Enchantment> enchantment) {
        if (enchantment == null) {
            return 0;
        }
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        return enchantments.getLevel(enchantment);
    }

    private static @Nullable Holder<Enchantment> lookup(LocalPlayer player, ResourceKey<Enchantment> key) {
        try {
            return player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
        } catch (RuntimeException e) {
            // Датапак мог убрать зачарование: тогда просто считаем уровень равным нулю.
            return null;
        }
    }
}
