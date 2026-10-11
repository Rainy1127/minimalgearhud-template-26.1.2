package dev.mihail.minimalgearhud.client;

import net.minecraft.world.entity.EquipmentSlot;

/**
 * Слоты, которые показывает виджет, в порядке отображения: руки, затем броня сверху вниз.
 */
public enum GearSlot {
    MAIN_HAND(EquipmentSlot.MAINHAND, true),
    OFF_HAND(EquipmentSlot.OFFHAND, true),
    HEAD(EquipmentSlot.HEAD, false),
    CHEST(EquipmentSlot.CHEST, false),
    LEGS(EquipmentSlot.LEGS, false),
    FEET(EquipmentSlot.FEET, false);

    private final EquipmentSlot equipmentSlot;
    private final boolean hand;

    GearSlot(EquipmentSlot equipmentSlot, boolean hand) {
        this.equipmentSlot = equipmentSlot;
        this.hand = hand;
    }

    public EquipmentSlot equipmentSlot() {
        return equipmentSlot;
    }

    public boolean isHand() {
        return hand;
    }
}
