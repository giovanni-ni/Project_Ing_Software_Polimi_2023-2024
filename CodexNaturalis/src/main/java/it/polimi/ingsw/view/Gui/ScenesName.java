package it.polimi.ingsw.view.Gui;

/**
 * Enum representing different scenes in the GUI application with their corresponding FXML file paths.
 */
public enum ScenesName {
    ASKCONNECTION("AskConnectiontype.fxml"),
    ASKNICKNAME("AskNickname.fxml"),
    EASYJOIN("easyJoin.fxml"),
    START("Start.fxml"),
    STARTMENU("StartMenu.fxml"),
    BOARD("Board.fxml"),
    WAITING("Waiting.fxml"),
    PREPARE("Prepare.fxml");

    private final String path;

    /**
     * Constructor for ScenesName enum.
     *
     * @param path The file path of the corresponding FXML file.
     */
    ScenesName(String path) {
        this.path = path;
    }

    /**
     * Getter for the file path of the FXML file associated with the scene.
     *
     * @return The file path of the FXML file.
     */
    public String getPath() {
        return path;
    }
}
