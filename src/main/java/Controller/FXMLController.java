package Controller;

import Model.Graphe;
import View.Arete;
import View.Sommet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class FXMLController {

    private Parent Pop_up_view;
    private Stage pop_up_window;

    private Graphe g;

    public FXMLController() {
        pop_up_window = new Stage();
        pop_up_window.initModality(Modality.APPLICATION_MODAL);
        pop_up_window.setResizable(false);
    }
    @FXML private StackPane subPane;

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem proprieteSommet = new MenuItem("Tableau de propriétés du sommet");
    private MenuItem supprimerSommet = new MenuItem("Supprimer le sommet");
    private MenuItem etiquetteSommet = new MenuItem("Etiquette du sommet");
    private MenuItem copierEtiquetteSommet = new MenuItem("Copier l'étiquette du sommet");
    private MenuItem collerEtquetteSommet = new MenuItem("Coller l'étiquette au sommet");

    public void clicPane(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(proprieteSommet,etiquetteSommet,supprimerSommet,copierEtiquetteSommet,collerEtquetteSommet);

            proprieteSommet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProprietesSommetTab.fxml"));
                        Parent root1 = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.show();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            supprimerSommet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clickSupprimer();
                }
            });

            etiquetteSommet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EtiquetteSommet.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.show();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            copierEtiquetteSommet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //TODO Il faut pouvoir récupérer un sommet
                }
            });

            collerEtquetteSommet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //TODO Il faut pouvoir récupérer un sommet
                }
            });

            contextMenu.show(subPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        } else {
            //Actions autre que clic droit

            contextMenu.hide();
        }
    }


    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'importation d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierImporter() {
        FileChooser fileChooser = createFileChooser("Importer");
        File file = fileChooser.showOpenDialog(null);
        if (file != null){
            this.g = new Graphe(file.getAbsolutePath(), 600);
        }
    }

    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'exportation d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierExporter() {
        FileChooser fileChooser = createFileChooser("Exporter");
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            this.g.sauvegarderGraphe(file.getAbsolutePath());
        }
    }

    private FileChooser createFileChooser(String exporter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(exporter);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GV", "*.gv"),
                new FileChooser.ExtensionFilter("GRAPHML", "*.graphml")
        );
        return fileChooser;
    }

    /**
     * Fonction ouvrant une fenetre (FileChooser) permettant l'enregistrement d'un fichier dans le logiciel.
     */
    @FXML public void clickFichierEnregistrer() {
        this.g.sauvegarderGraphe(null);
    }

    /**
     * Fonction permettant d'appliquer une distribution aléatoire des positions des sommets du graphe
     */
    @FXML public void clickRepresentationAleatoire() {
        this.g.setAlgorithmeRepresentation('a',600);
    }

    /**
     * Fonction permettant d'appliquer une distribution circulaire des positions des sommets du graphe
     */
    @FXML public void clickRepresentationCirculaire() {
        this.g.setAlgorithmeRepresentation('c',600);
    }

    /**
     * Fonction permettant d'appliquer une distribution aléatoire des positions des sommets du graphe
     */
    @FXML
    public void clickRepresentationForces() {
        this.g.setAlgorithmeRepresentation('f',600);
    }

    @FXML
    public void clickToggleRepresentation(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ModeleRepresentation.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Choix du modèle de la représentation");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
            pop_up_window.setTitle("Ajouter Arête");
            pop_up_window.setScene(new Scene((Parent)fxmlLoader.load()));
            pop_up_window.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void clickSupprimer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SupprSommer.fxml"));
            pop_up_window.setTitle("Confirmer suppression");
            pop_up_window.setScene(new Scene((Parent)fxmlLoader.load()));
            pop_up_window.show();
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

    /**
     *
     */
    @FXML public void clickOptionGraphe() {
        try {
            pop_up_window.setTitle("Options Graphe");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TailleGraphe.fxml"));
            pop_up_window.setScene(new Scene((Parent)fxmlLoader.load()));
            pop_up_window.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}
