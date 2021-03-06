import Model.Forme_Sommet;
import Model.Sommet;
import com.sun.glass.ui.Size;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mathieu on 06/02/2017.
 */
public class SommetTest {
    @Test
    public void testCreationSommet() {
        Sommet sommet= new Sommet("s1");
        assertEquals(sommet.getTag(),"s1");

        Color couleur = Color.web("000000");
        assertEquals(sommet.getCouleur(), couleur);

        String size = new Size(5,5).toString();
        assertEquals(sommet.getTaille().toString(),size);
    }

    @Test
    public void testSetTag() {
        Sommet sommet= new Sommet("s1",10,10);
        assertEquals(sommet.getTag(),"s1");

        sommet.setTag("s2");
        assertEquals(sommet.getTag(),"s2");
    }

    @Test
    public void testSetCouleur() {
        Sommet sommet= new Sommet("s1",10,10);
        Color couleur = Color.web("000000");
        assertEquals(sommet.getCouleur(), couleur);

        couleur = Color.web("010101");
        sommet.setCouleur(couleur);
        assertEquals(sommet.getCouleur(),couleur);
    }

    @Test
    public void testSetCoordonnees() {
        Sommet sommet= new Sommet("s1",10,10);
        assertEquals(sommet.getX(),10,0);
        assertEquals(sommet.getY(),10,0);

        sommet.setX(13);
        sommet.setY(15);
        assertEquals(sommet.getX(),13,0);
        assertEquals(sommet.getY(),15,0);
    }
    @Test
    public void testSetIndice() {
        Sommet sommet = new Sommet("s1",10,10);
        assertEquals(sommet.getIndice(),0);

        sommet.setIndice(5);
        assertEquals(sommet.getIndice(),5);
    }

    @Test
    public void testSetForme() {
        Sommet sommet = new Sommet("s1",10,10);
        assertEquals(sommet.getForme(), Forme_Sommet.Cercle);

        sommet.setForme(Forme_Sommet.Losange);
        assertEquals(sommet.getForme(),Forme_Sommet.Losange);
    }

    @Test
    public void testTaille() {
        Sommet sommet = new Sommet("UnSommet");
        assertEquals(sommet.getTaille().height,5,0);
        assertEquals(sommet.getTaille().width,5,0);

        Size size = new Size(15,15);
        sommet.setTaille(size);

        assertEquals(sommet.getTaille().height,15,0);
        assertEquals(sommet.getTaille().width,15,0);
    }

    @Test
    public void testFormeSommet() {
        Sommet sommet = new Sommet("UnSommet");
        assertEquals(Forme_Sommet.Cercle,sommet.getForme());

        sommet.setForme(Forme_Sommet.Triangle);
        assertEquals(Forme_Sommet.Triangle,sommet.getForme());

        assertEquals(Forme_Sommet.getFormeViaTexte("Rectangle"),Forme_Sommet.Rectangle);
        assertEquals(Forme_Sommet.getFormeViaTexte("Triangle"),Forme_Sommet.Triangle);
        assertEquals(Forme_Sommet.getFormeViaTexte("Losange"),Forme_Sommet.Losange);
        assertEquals(Forme_Sommet.getFormeViaTexte("Cercle"),Forme_Sommet.Cercle);
        assertEquals(Forme_Sommet.getFormeViaTexte("Unknown"),Forme_Sommet.Cercle);


        sommet.setForme("Rectangle");
        assertEquals(sommet.getForme(),Forme_Sommet.Rectangle);

        sommet.setForme("circle");
        assertEquals(sommet.getForme(),Forme_Sommet.Cercle);

        sommet.setForme("Triangle");
        assertEquals(sommet.getForme(),Forme_Sommet.Triangle);

        sommet.setForme("Losange");
        assertEquals(sommet.getForme(),Forme_Sommet.Losange);

        sommet.setForme("unknown");
        assertEquals(sommet.getForme(),Forme_Sommet.Cercle);
    }
}