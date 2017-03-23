package Controller;

import Model.Forme_Sommet;
import com.sun.glass.ui.Size;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by audreylentilhac on 18/03/2017.
 */
public class SommetController {
    protected ObservableList<Forme_Sommet> formes = FXCollections.observableArrayList(Forme_Sommet.values());
    SommetController(){
    }

    protected Size déterminationTailleRentrerParUtilisateur(String forme_sommet, TextField tailleSommet, Label messageErreur) {
        if (!tailleSommet.getText().contains(".")) {
            String[] elementsDansTaille = tailleSommet.getText().split(" ");
            if (elementsDansTaille.length > 0) {

                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcherNombre;
                Matcher matcherLettre;

                int limiteNombreValeur = 1;
                if (forme_sommet.equals("Rectangle")) {
                    limiteNombreValeur = 2;
                }

                int largeur = 0;
                int longueur = 0;
                int cptDetrompeur = 0; // Pour compter si il n'y a pas plus de valeurs que prévus
                int cptElement = 0;
                while (cptElement < elementsDansTaille.length && cptDetrompeur <= limiteNombreValeur) {
                    pattern = Pattern.compile("[0-9]+");
                    matcherNombre = pattern.matcher(elementsDansTaille[cptElement]);

                    pattern = Pattern.compile("[^\\d+\\.+\\ ]+");
                    matcherLettre = pattern.matcher(elementsDansTaille[cptElement]);

                    if (matcherNombre.find() && cptDetrompeur != -1 && !matcherLettre.find()) {
                        if (limiteNombreValeur == 2) {
                            if (cptDetrompeur == 0) {
                                largeur = Integer.parseInt(matcherNombre.group());
                            } else if (cptDetrompeur == 1) {
                                longueur = Integer.parseInt(matcherNombre.group());
                            }
                        }
                        else {
                            if (cptDetrompeur == 0) {
                                largeur = Integer.parseInt(matcherNombre.group());
                                longueur = Integer.parseInt(matcherNombre.group());
                            }
                        }

                        ++cptDetrompeur;
                    }
                    else {
                        messageErreur.setText("Erreur - Seul les valeurs entières sont acceptées pour la taille.");
                        cptDetrompeur = -1;
                    }

                    ++cptElement;
                }

                if (cptDetrompeur <= limiteNombreValeur) {
                    return new Size(largeur, longueur);

                } else {
                    messageErreur.setText("Erreur - Trop de valeurs (Seul la forme Rectangle nécessite 2 valeurs).");
                }
            } else {
                messageErreur.setText("Erreur - Une ou aucune valeur rentrée pour la taille du sommet.\n" +
                        "Les valeurs doivent être séparées par un espace (Exemple : 2, 2)");
            }
        }
        else {
            messageErreur.setText("Erreur - Seul les valeurs entières sont acceptées pour la taille.");
        }

        return null;
    }

    protected Pair<Float, Float> déterminationPositionRentrerParUtilisateur(TextField positionSommet, Label messageErreur) {

        String[] elementsDansPosition = positionSommet.getText().split(" ");
        if (elementsDansPosition.length > 1) {

            Pattern pattern;
            Matcher matcherNombre;
            Matcher matcherLettre;

            float coordX = 0;
            float coordY = 0;
            int cptDetrompeur = 0; // Pour compter si il n'y a pas plus de valeurs que prévus
            int cptElement = 0;
            while (cptElement < elementsDansPosition.length && cptDetrompeur != -1 && cptDetrompeur <= 2) {

                pattern = Pattern.compile("[0-9]+");
                matcherNombre = pattern.matcher(elementsDansPosition[cptElement]);

                pattern = Pattern.compile("[^\\d+\\.+\\ ]+");
                matcherLettre = pattern.matcher(elementsDansPosition[cptElement]);

                if (matcherNombre.find() && !matcherLettre.find()) {
                    if (cptDetrompeur == 0) {
                        coordX = Float.parseFloat(matcherNombre.group());
                    } else if (cptDetrompeur == 1) {
                        coordY = Float.parseFloat(matcherNombre.group());
                    }

                    ++cptDetrompeur;
                }
                else {
                    messageErreur.setText("Erreur - Seul les valeurs décimales sont acceptées pour la position.");
                    cptDetrompeur = -1; // Sortie de la boucle
                }

                ++cptElement;
            }

            if (cptDetrompeur <= 2 && cptDetrompeur != 1) {
                return new Pair<Float, Float>(coordX, coordY);

            }
            else if (cptDetrompeur != 1) {
                messageErreur.setText("Erreur - Trop de valeurs.");
            }

        } else {
            messageErreur.setText("Erreur - Une ou aucune valeur rentrée pour la position du sommet.\n" +
                    "Les valeurs doivent être séparées par un espace (Exemple : 2.2, 2.2)");
        }

        return null;
    }
    protected int déterminationIndiceRentrerParUtilisateur(TextField indiceSommet, Label messageErreur) {

        String indice = indiceSommet.getText();
        if (indice.length() > 0 && !indice.contains(".") && !indice.contains(",") && !indice.contains("-")) {
            return Integer.parseInt(indice);
        }
        else{
            messageErreur.setText("L'indice doit être une valeur entière positive. Ex: 2");
        }
        return -1;
    }
}
