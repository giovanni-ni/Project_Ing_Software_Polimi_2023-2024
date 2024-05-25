package it.polimi.ingsw.view.Gui;

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

    ScenesName(String path){
        this.path = path;
    }
    public String getPath(){
        return path;
    }
}
