package morales.david.desktop.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import morales.david.desktop.interfaces.Controller;
import morales.david.desktop.managers.ScreenManager;
import morales.david.desktop.managers.SocketManager;
import morales.david.desktop.models.packets.Packet;
import morales.david.desktop.models.packets.PacketBuilder;
import morales.david.desktop.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoursesMenuController implements Initializable, Controller {

    @FXML
    private Button coursesNavigationButton;

    @FXML
    private Button groupsNavigationButton;

    @FXML
    private Button subjectsNavigationButton;

    @FXML
    private StackPane viewPane;

    private String actualView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        actualView = "";

        Platform.runLater(() -> loadView("courses.fxml", "Cursos", null));
        coursesNavigationButton.getStyleClass().add("buttonPressed");

        Platform.runLater(() -> {

            Packet coursesRequestPacket = new PacketBuilder().ofType(Constants.REQUEST_COURSES).build();

            SocketManager.getInstance().sendPacketIO(coursesRequestPacket);

        });

    }

    @FXML
    public void handleButtonAction(MouseEvent event) {

        if(event.getSource() == coursesNavigationButton)
            loadView("courses.fxml", "Cursos", event);
        else if (event.getSource() == groupsNavigationButton)
            loadView("groups.fxml", "Grupos", event);
        else if (event.getSource() == subjectsNavigationButton)
            loadView("subjects.fxml", "Asignaturas", event);

    }

    private void removePressed() {

        coursesNavigationButton.getStyleClass().remove("buttonPressed");
        groupsNavigationButton.getStyleClass().remove("buttonPressed");
        subjectsNavigationButton.getStyleClass().remove("buttonPressed");

    }

    private void loadView(String view, String title, MouseEvent event) {

        Node newView = null;
        Node oldView = null;

        if(actualView.equalsIgnoreCase(view))
            return;

        if(event != null) {
            Button button = (Button) event.getSource();
            removePressed();
            button.getStyleClass().add("buttonPressed");
        }

        actualView = view;

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/" + view));
            newView = loader.load();

            if(viewPane.getChildren().size() > 0) {
                oldView = viewPane.getChildren().get(0);
                viewPane.getChildren().remove(oldView);
            }

            viewPane.getChildren().add(newView);

            ScreenManager.getInstance().getStage().setTitle(title + Constants.WINDOW_TITLE);

            ScreenManager.getInstance().setController(loader.getController());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
