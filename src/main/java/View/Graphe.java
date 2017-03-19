package View;

import javafx.scene.Group;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 13/03/2017.
 */
public class Graphe {
    private Group canvas;
    private ZoomableScrollPane scrollPane;
    private List<Sommet> sommets;
    private List<Arete> aretes;
    public Graphe() {
        canvas = new Group();
        scrollPane = new ZoomableScrollPane(canvas);
        sommets = new ArrayList<Sommet>();
        aretes = new ArrayList<Arete>();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    public void chargerGraphe(Model.Graphe graphe) {
//        for(Model.Sommet s : graphe.getSommets()) {
//            sommets.add(new Sommet(Integer.toString(s.getId()),s.getForme(),s.getX(),s.getY()));
//        }
        for (Map.Entry<Model.Arete, Pair<Model.Sommet, Model.Sommet>> e : graphe.getM_extremites().entrySet()) {
            Sommet s1 = null;
            Sommet s2 = null;
            s1 = new Sommet(Integer.toString(e.getValue().getKey().getId()),
                    e.getValue().getKey().getForme(),
                    e.getValue().getKey().getX(),
                    e.getValue().getKey().getY());
            s2 = new Sommet(Integer.toString(e.getValue().getValue().getId()),
                    e.getValue().getValue().getForme(),
                    e.getValue().getValue().getX(),
                    e.getValue().getValue().getY());
            sommets.add(s1);
            sommets.add(s2);

            aretes.add(new Arete(s1,s2));

        }
        canvas.getChildren().addAll(sommets);
        canvas.getChildren().addAll(aretes);
        scrollPane.updateScrollPane(canvas);
    }

    public Group getCanvas() {
        return canvas;
    }

    public ZoomableScrollPane getScrollPane() {
        return scrollPane;
    }

    public List<Sommet> getSommets() {
        return sommets;
    }

    public List<Arete> getAretes() {
        return aretes;
    }
}