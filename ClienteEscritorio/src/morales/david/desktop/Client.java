package morales.david.desktop;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import morales.david.desktop.managers.ScreenManager;
import morales.david.desktop.managers.SocketManager;
import morales.david.desktop.utils.Constants;

public class Client extends Application {

    public static final Gson GSON = new Gson();

    @Override
    public void start(Stage primaryStage) {

        SocketManager socketManager = SocketManager.getInstance();
        socketManager.setDaemon(true);
        socketManager.start();

        primaryStage.getIcons().add(new Image("/resources/images/schedule-icon-inverted.png"));

        ScreenManager screenManager = ScreenManager.getInstance();

        screenManager.setStage(primaryStage);

        screenManager.openScene("login.fxml", "Iniciar sesión" + Constants.WINDOW_TITLE);

        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
