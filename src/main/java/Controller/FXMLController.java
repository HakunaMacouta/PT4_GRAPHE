package Controller;

import Model.Graphe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLController {
    private Graphe g;
    @FXML private Pane pane;

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem proprieteSommet = new MenuItem("Tableau de propriétés du sommet");
    private MenuItem supprimerSommet = new MenuItem("Supprimer le sommet");
    private MenuItem etiquetteSommet = new MenuItem("Etiquette du sommet");
    private MenuItem copierEtiquetteSommet = new MenuItem("Copier l'étiquette du sommet");
    private MenuItem collerEtquetteSommet = new MenuItem("Coller l'étiquette au sommet");

    public void clicPane(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            contextMenu.getItems().addAll(proprieteSommet,etiquetteSommet,supprimerSommet,copierEtiquetteSommet,collerEtquetteSommet);
            contextMenu.show(pane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
    }

    public FXMLController(){
    }
    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'importation d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierImporter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DOT", "*.dot"),
                new FileChooser.ExtensionFilter("GRAPHML", "*.graphml")
        );
        fileChooser.showOpenDialog(null);
        g= new Graphe("ressources/sample.graphml");
    }

    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'exportation d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierExporter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DOT", "*.dot"),
                new FileChooser.ExtensionFilter("GRAPHML", "*.graphml")
        );
        fileChooser.showSaveDialog(null);
    }

    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'enregistrement d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierEnregistrer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DOT", "*.dot"),
                new FileChooser.ExtensionFilter("GRAPHML", "*.graphml")
        );
        fileChooser.showSaveDialog(null);
    }

    @FXML
    public void clickAjouterSommet() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AjoutSommet.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Sommet");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickAjouterArete() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AjoutArete.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Arete");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void clickSupprimer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SupprSommer.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Confirmer suppression");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Fonction traitant le ToggleButton permettant l'affichage ou non des sommets du graphe.
     */
    @FXML public void clickToggleSommet(ActionEvent event) {
        if(((ToggleButton)event.getSource()).isSelected()) {
            //TODO affichage Sommet
        } else {
            //TODO suppression de l'affichage sommet
        }
    }
    /**
     * Fonction traitant le ToggleButton permettant l'affichage ou non des aretes du graphe.
     */
    @FXML public void clickToggleArete(ActionEvent event) {
        if(((ToggleButton)event.getSource()).isSelected()) {
            //TODO affichage Arete
        } else {
            //TODO suppression de l'affichage arete
        }
    }
    /**
     * Fonction traitant le ToggleButton permettant l'affichage ou non des etiquettes du graphe.
     */
    @FXML public void clickToggleEtiquette(ActionEvent event) {
        if(((ToggleButton)event.getSource()).isSelected()) {
            //TODO affichage etiquette
        } else {
            //TODO suppression de l'affichage etiquette
        }
    }
    /**
     *
     */
    @FXML public void clickZoomMinus() {
        //TODO zoom arriere
    }

    /**
     *
     */
    @FXML public void clickZoomPlus() {
        //TODO zoom plus
    }
    /**
     *
     */
    @FXML public void clickCouleurFond(ActionEvent event) {

    }
    /**
     *
     */
    @FXML public void clickCouleurGraphe(ActionEvent event) {

    }

    /**
     *
     */
    @FXML public void clickCouleurElement(ActionEvent event) {

    }



}
