package dev.mihail.minimalgearhud.logic;

/**
 * Угол или край экрана, к которому «прикреплён» виджет.
 */
public enum Anchor {
    TOP_LEFT(0, 0),
    TOP_CENTER(1, 0),
    TOP_RIGHT(2, 0),
    MIDDLE_LEFT(0, 1),
    MIDDLE_RIGHT(2, 1),
    BOTTOM_LEFT(0, 2),
    BOTTOM_RIGHT(2, 2);

    /** 0 — слева, 1 — по центру, 2 — справа. */
    private final int column;
    /** 0 — сверху, 1 — по центру, 2 — снизу. */
    private final int row;

    Anchor(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public boolean isLeft() {
        return column == 0;
    }

    public boolean isRight() {
        return column == 2;
    }

    public boolean isTop() {
        return row == 0;
    }

    public boolean isBottom() {
        return row == 2;
    }

    int column() {
        return column;
    }

    int row() {
        return row;
    }
}
