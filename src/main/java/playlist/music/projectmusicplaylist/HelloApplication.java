package playlist.music.projectmusicplaylist;

import javafx.application.Application;
import playlist.music.services.FXRouter;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "", 610, 630);
        stage.setResizable(false);
        configRoute();
        FXRouter.goTo("music-playlist");
    }

    public static void configRoute()
    {
        String viewPath = "playlist/music/views/";
        FXRouter.when("music-playlist", viewPath + "music-playlist.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
