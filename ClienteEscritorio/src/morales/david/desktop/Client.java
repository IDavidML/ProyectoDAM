package morales.david.desktop;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import morales.david.desktop.controllers.LoginController;
import morales.david.desktop.managers.ScreenManager;
import morales.david.desktop.managers.SocketManager;
import morales.david.desktop.models.packets.Packet;
import morales.david.desktop.models.packets.PacketBuilder;
import morales.david.desktop.models.packets.PacketType;
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

        primaryStage.setOnCloseRequest(event -> {

            event.consume();

            Packet exitRequestPacket = new PacketBuilder()
                    .ofType(PacketType.EXIT.getRequest())
                    .build();

            if(ScreenManager.getInstance().getController() instanceof LoginController) {

                SocketManager.getInstance().sendPacketIO(exitRequestPacket);

            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro que quieres salir?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Vas a salir de la aplicación");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {

                    SocketManager.getInstance().sendPacketIO(exitRequestPacket);

                }

            }

        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}
