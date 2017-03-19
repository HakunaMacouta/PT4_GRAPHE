import Controller.FXMLController;
import View.Graphe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        /** GESTION DES NODES **/
        View.Graphe g = new Graphe();
        VBox root = new VBox();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        TabPane menu = loader.load();
        Pane pane = new Pane();
        root.getChildren().addAll(menu,g.getScrollPane(), pane);


        /** PARAMETRAGE DE LA FENETRE **/
        primaryStage.setTitle("Gestionnaire de graphe");
        primaryStage.setMaximized(true);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        FXMLController controller = (FXMLController)loader.getController();
        controller.setVbox(root);
        controller.setPane(pane);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
