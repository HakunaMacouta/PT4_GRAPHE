package Controller;

import Model.Graphe;
import Model.Sommet;
import View.Arete;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe AjoutAreteController
 * permettant de gérer le controle sur l'ajout d'une arête au graphe
 */
public class AjoutAreteController extends FXMLController {

    /**
     * Représentent respectivement le sommet entrant sélectionné et le sommet sortant sélectionné
     * pour l'ajout d'arête entre ces deux sommets
     */
    private String sommetEntreSelectionne, sommetSortieSelectionne;

    /**
     * Représente l'ensemble des sommets du graphe
     * pour en sélectionner un comme sommet entrant
     */
    @FXML private ListView<String> listViewSommetsE;

    /**
     * Représente l'ensemble des sommets du graphe
     * pour en sélectionner un comme sommet sortant
     */
    @FXML private ListView<String> listViewSommetsS;

    /**
     * Représente le message d'erreur à afficher dans la fenêtre
     * en cas d'erreur de saisie de l'utilisateur
     */
    @FXML private Label erreurAjoutArete;

    /**
     * Constructeur de l'affichage de la fenêtre d'ajout d'une arête
     * @param graphe représente le graphe du Model
     * @param grapheView représente le graphe de la View
     * @throws IOException lève une exception
     */
    AjoutAreteController(Graphe graphe, View.Graphe grapheView) throws IOException {
        super();
        this.grapheModel = graphe;
        this.grapheView = grapheView;
        if (this.grapheModel != null) {
            FXMLLoader fxmlLoaderPopUp = new FXMLLoader(getClass().getResource("/fxml/AjoutArete.fxml"));
            popUpWindow.setTitle("Ajouter Arête");
            if (popUpWindow.getScene() == null) {
                fxmlLoaderPopUp.setRoot(this);
                fxmlLoaderPopUp.setController(this);
                popUpWindow.setScene(new Scene((Parent) fxmlLoaderPopUp.load()));
            }
            popUpWindow.show();
            List<String> sommetsStr = new ArrayList<String>();

            for (Sommet sommet : graphe.getSommets()) {
                sommetsStr.add("tag : " + sommet.getTag() + " (id : " + sommet.getId() + " )");
            }

            ListProperty<String> listProperty = new SimpleListProperty<String>();
            listProperty.set(FXCollections.observableArrayList(sommetsStr));

            listViewSommetsE.itemsProperty().bind(listProperty);
            listViewSommetsS.itemsProperty().bind(listProperty);

            listViewSommetsE.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> ov,
                                            String old_val, String new_val) {
                            sommetEntreSelectionne = new_val;
                        }
                    });

            listViewSommetsS.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> ov,
                                            String old_val, String new_val) {
                            sommetSortieSelectionne = new_val;
                        }
                    });
        }
    }

    /**
     * Fonction d'ajout d'une arête dans le Model et mise à jour de la vue si la saisie est correcte.
     */
    @FXML public void ajouterArete() {
        if (sommetEntreSelectionne != null && sommetSortieSelectionne != null) {

            int idSommetEntre = Integer.parseInt(sommetEntreSelectionne.split(" ")[5]);
            int idSommetSortie = Integer.parseInt(sommetSortieSelectionne.split(" ")[5]);

            if (!grapheModel.ajouterArete(grapheModel.trouverSommetParID(idSommetEntre), grapheModel.trouverSommetParID(idSommetSortie))) {
                erreurAjoutArete.setText("Erreur - Arete existante ou 2 \nsommets identiques sélectionnés.");
            }
            else {
                Arete arete = new View.Arete(grapheView.rechercheSommetParId(idSommetEntre), grapheView.rechercheSommetParId(idSommetSortie));
                grapheView.getAretes().add(arete);
                grapheView.getCanvas().getChildren().add(0, arete); // Pour affichage en dessous des sommets
                grapheView.getScrollPane().updateScrollPane(grapheView.getCanvas());
                popUpWindow.close();
            }
        }
        else {
            erreurAjoutArete.setText("Erreur - Sélectionnez 2 sommets.");
        }
    }

    /**
     * Fonction fermant la fenêtre d'ajout d'arête au clic sur "Annuler"
     */
    @FXML public void fermerAjoutArete() {
        popUpWindow.close();
    }
}
