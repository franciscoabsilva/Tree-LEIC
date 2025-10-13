import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppFX extends Application {
    private ArvoreLEIC arvore; 

    @Override
    public void start(Stage primaryStage) throws IOException {
    
        FXMLLoader fxmlLoader = new FXMLLoader(AppFX.class.getResource("ArvoreView.fxml"));
        
        // Carrega o FXML
        Scene scene = new Scene(fxmlLoader.load());

        // Obtém o Controller e injeta o Stage e a Scene principal
        ArvoreController controller = fxmlLoader.getController();
        controller.setCurrentStage(primaryStage);
        
        // ESSENCIAL: Passar a Scene principal para que o Controller possa voltar a ela
        controller.setMenuPrincipalScene(scene);

        primaryStage.setTitle("Árvore Genealógica - JavaFX");
        primaryStage.getIcons().add(new javafx.scene.image.Image(AppFX.class.getResourceAsStream("icon.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}