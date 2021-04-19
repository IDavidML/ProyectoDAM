package morales.david.desktop.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import morales.david.desktop.interfaces.Controller;
import morales.david.desktop.managers.ScreenManager;
import morales.david.desktop.managers.SocketManager;
import morales.david.desktop.models.packets.Packet;
import morales.david.desktop.models.packets.PacketBuilder;
import morales.david.desktop.models.packets.PacketType;
import morales.david.desktop.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, Controller {

    @FXML
    private Button homeNavigationButton;

    @FXML
    private Button teachersNavigationButton;

    @FXML
    private Button coursesGroupsNavigationButton;

    @FXML
    private Button classroomsNavigationButton;

    @FXML
    private Button scheduleNavigationButton;

    @FXML
    private Button importNavigationButton;

    @FXML
    private Button disconnectNavigationButton;

    @FXML
    private BorderPane viewPane;

    private String actualView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        actualView = "";

        Platform.runLater(() -> loadView("home.fxml", "Inicio", null));

        Platform.runLater(() -> {

            Packet classroomsRequestPacket = new PacketBuilder().ofType(PacketType.CLASSROOMS.getRequest()).build();
            SocketManager.getInstance().sendPacketIO(classroomsRequestPacket);

            Packet teachersRequestPacket = new PacketBuilder().ofType(PacketType.TEACHERS.getRequest()).build();
            SocketManager.getInstance().sendPacketIO(teachersRequestPacket);

            Packet credentialsRequestPacket = new PacketBuilder().ofType(PacketType.CREDENTIALS.getRequest()).build();
            SocketManager.getInstance().sendPacketIO(credentialsRequestPacket);

            Packet coursesRequestPacket = new PacketBuilder().ofType(PacketType.COURSES.getRequest()).build();
            SocketManager.getInstance().sendPacketIO(coursesRequestPacket);

            Packet subjectsRequestPacket = new PacketBuilder().ofType(PacketType.SUBJECTS.getRequest()).build();
            SocketManager.getInstance().sendPacketIO(subjectsRequestPacket);

        });

    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if(event.getSource() == homeNavigationButton)
            loadView("home.fxml", "Inicio", event);
        else if (event.getSource() == teachersNavigationButton)
            loadView("teachers/teachersmenu.fxml", "Profesores", event);
        else if (event.getSource() == coursesGroupsNavigationButton)
            loadView("courses/coursesmenu.fxml", "Cursos", event);
        else if (event.getSource() == classroomsNavigationButton)
            loadView("classrooms.fxml", "Aulas", event);
        else if (event.getSource() == scheduleNavigationButton)
            loadView("schedules.fxml", "Horarios", event);
        else if (event.getSource() == importNavigationButton)
            loadView("import.fxml", "Importar", event);
        else if(event.getSource() == disconnectNavigationButton)
            disconnect();

    }

    private void disconnect() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro que quieres desconectarte?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Vas a salir de la aplicación");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            Packet disconnectRequestPacket = new PacketBuilder()
                    .ofType(PacketType.DISCONNECT.getRequest())
                    .build();

            SocketManager.getInstance().sendPacketIO(disconnectRequestPacket);

        }

    }

    private void removePressed() {

        homeNavigationButton.getStyleClass().remove("buttonPressed");
        teachersNavigationButton.getStyleClass().remove("buttonPressed");
        coursesGroupsNavigationButton.getStyleClass().remove("buttonPressed");
        classroomsNavigationButton.getStyleClass().remove("buttonPressed");
        scheduleNavigationButton.getStyleClass().remove("buttonPressed");
        importNavigationButton.getStyleClass().remove("buttonPressed");

    }

    private void loadView(String view, String title, MouseEvent event) {

        Node newView = null;
        Node oldView = null;

        if(actualView.equalsIgnoreCase(view))
            return;

        if(event != null) {
            Button button = (Button) event.getSource();
            removePressed();
            if(button != homeNavigationButton) {
                button.getStyleClass().add("buttonPressed");
            }
        }

        actualView = view;

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/" + view));
            newView = loader.load();

            viewPane.setCenter(newView);

            ScreenManager.getInstance().getStage().setTitle(title + Constants.WINDOW_TITLE);

            ScreenManager.getInstance().setController(loader.getController());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
