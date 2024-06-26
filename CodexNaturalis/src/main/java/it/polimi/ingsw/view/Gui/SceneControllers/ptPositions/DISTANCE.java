package it.polimi.ingsw.view.Gui.SceneControllers.ptPositions;

/**
 * Enum representing various distances used in GUI positioning in PointTable.
 */
public enum DISTANCE {
    COULUMN0(15),
    DISTANCEC(69),
    COULUMN1(49.5),
    LINE0(512),
    DISTANCEL(62);

    public final double data;

    /**
     * Constructor for DISTANCE enum.
     *
     * @param data The double value associated with the enum constant.
     */
    DISTANCE(double data) {
        this.data = data;
    }
}
