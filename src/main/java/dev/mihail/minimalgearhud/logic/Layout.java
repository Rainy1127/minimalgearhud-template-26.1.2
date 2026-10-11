package dev.mihail.minimalgearhud.logic;

/**
 * Вычисляет, где на экране нарисовать виджет. Чистая арифметика.
 */
public final class Layout {

    /** Левый верхний угол виджета в GUI-координатах. */
    public record Placement(int x, int y) {}

    private Layout() {}

    /**
     * @param anchor  к какому краю прикреплён виджет
     * @param screenW ширина GUI в масштабированных пикселях
     * @param screenH высота GUI
     * @param width   ширина виджета (уже с учётом масштаба)
     * @param height  высота виджета (уже с учётом масштаба)
     * @param margin  отступ от края экрана
     * @param offsetX дополнительное смещение вправо (может быть отрицательным)
     * @param offsetY дополнительное смещение вниз
     * @return позиция левого верхнего угла; виджет всегда остаётся в пределах экрана
     */
    public static Placement place(Anchor anchor, int screenW, int screenH, int width, int height,
                                  int margin, int offsetX, int offsetY) {
        int x = switch (anchor.column()) {
            case 0 -> margin;
            case 1 -> (screenW - width) / 2;
            default -> screenW - width - margin;
        };
        int y = switch (anchor.row()) {
            case 0 -> margin;
            case 1 -> (screenH - height) / 2;
            default -> screenH - height - margin;
        };
        x += offsetX;
        y += offsetY;
        return new Placement(clampToScreen(x, width, screenW), clampToScreen(y, height, screenH));
    }

    private static int clampToScreen(int pos, int size, int screen) {
        if (size >= screen) {
            return 0;
        }
        return Math.max(0, Math.min(pos, screen - size));
    }
}
