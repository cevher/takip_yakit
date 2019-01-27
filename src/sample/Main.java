package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class Main extends Application {
    private Stage mainStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        this.mainStage = primaryStage;
        primaryStage.setOnCloseRequest(confirmCloseEventHandler);

        Button closeButton = new Button("Close Application");
        closeButton.setOnAction(event ->
                primaryStage.fireEvent(
                        new WindowEvent(
                                primaryStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                )
        );

        primaryStage.setTitle("Yakıt Takip Sistemi");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Değişiklikleri kaydettiğinizden emin olun"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        Button cancelBtn= (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.CANCEL
        );
        exitButton.setText("Kapat");
        cancelBtn.setText("İptal");
        closeConfirmation.setTitle("Dikkat");
        closeConfirmation.setHeaderText("Programı kapatmak üzeresiniz");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(mainStage);

        // normally, you would just use the default alert positioning,
        // but for this simple sample the main stage is small,
        // so explicitly position the alert so that the main window can still be seen.


        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };
    public static void main(String[] args) {
        Locale.setDefault(new Locale("tr", "TR"));

        launch(args);
    }
}
