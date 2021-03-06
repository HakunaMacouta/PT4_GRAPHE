package Model;

import com.sun.glass.ui.Size;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**Pattern
 *  Classe Graphe
 */
public class Graphe {
    /**
     * Représente le nom du graphe.
     */
    private String nom;

    /**
     * Représente la taille du graphe.
     */
    private Size taille;

    /**
     * Représente l'ensemble des sommets dans le graphe.
     */
    private ArrayList<Sommet> sommets;

    /**
     * Représente l'ensemble des arete dans le graphe.
     */
    private ArrayList<Arete> aretes;

    /**
     * Représente une map liant pour chaque sommet sa liste d'aretes.
     */
    private HashMap<Sommet, ArrayList<Arete> > incidentes;

    /**
     * Représente une map liant pour chaque arete sa paire de sommets.
     */
    private HashMap<Arete, Pair<Sommet,Sommet>> extremites;    // conteneur liant pour chaque arete sa paire de sommets

    /**
     *  Représente une valeur aléatoire.
     */
    private static Random rand = new Random();

    /**
     * Représente l'algorithme de représentation du graphe.
     */
    private AlgorithmeRepresentation algorithmeRepresentation;

    /**
     * Représente le chemin du grapge.
     */
    private String cheminGraphe;


    /**
     * Constructeur de la classe Graphe lisant un fichier .DOT ou .GRAPHML.
     * @param fichier chemin d'accès du fichier du graphe
     * @param largeurEcran valeur entière représentant la largeur de la fenêtre d'affichage
     */
    public Graphe (String fichier, int largeurEcran, int hauteurEcran) {
        nom = "";
        taille = new Size(10,10);
        sommets = new ArrayList<Sommet>();
        aretes = new ArrayList<Arete>();
        incidentes = new HashMap<Sommet, ArrayList<Arete>>();
        extremites = new HashMap<Arete, Pair<Sommet, Sommet>>();
        algorithmeRepresentation = new AlgorithmeRepresentation(this);
        cheminGraphe = fichier;

        if (cheminGraphe.matches(".+.gv$")) {
            chargerGrapheGV(fichier);
        }
        else if (cheminGraphe.matches(".+.graphml$")) {
            chargerGrapheGRAPHML(fichier);
        }
        setAlgorithmeRepresentation('c', largeurEcran, hauteurEcran);
    }

    /**
     * Constructeur par defaut de la classe Graphe.
     */
    public Graphe(){
        nom = "Mon Joli Graphe";
        taille = new Size(10,10);
        sommets = new ArrayList<Sommet>();
        aretes = new ArrayList<Arete>();
        incidentes = new HashMap<Sommet, ArrayList<Arete>>();
        extremites = new HashMap<Arete, Pair<Sommet, Sommet>>();
        algorithmeRepresentation = new AlgorithmeRepresentation(this);
    }

    /**
     * Charge les fichiers .GV.
     * @param fichier Représente le path du fichier à charger.
     */
    private void chargerGrapheGV(String fichier){

        try{
            InputStream ips=new FileInputStream(fichier);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne = br.readLine();
            String [] decoup = ligne.split(" ");

            if(decoup[0].equals("graph") || decoup[0].equals("digraph") ){
                nom = decoup[1];
            }
            else {
                throw new Exception("Error syntax in the .gv file : '"+fichier+"'.");
            }
            while ((ligne=br.readLine())!=null){
                ligne = ligne.trim();
                String[] elementLigne = ligne.split(" ");

                // Creation sommet :
                // "node1" [ ... ]
                // Après le split sur un espace
                // [0] -> "node1"
                // [1] -> [
                // [2 à n-1] -> ...
                // [n] -> ];
                // ---------------------------
                // Creation arete :
                // "node1" -> "node2" [ ... ]
                // Après le split sur un espace
                // [0] -> "node1"
                // [1] -> ->
                // [2] -> "node2"
                // [3 à n-1] -> ...
                // [n] -> ];
                if (elementLigne.length > 1) {

                    if (elementLigne[1].equals("[")) { // Creation sommet

                        String elementSommet = ligne.split("\\[+.+;$")[0].trim();
                        int idImportationSommet = Integer.parseInt(elementSommet.split("node")[1].replace("\"", ""));

                        verificationSommetDoublonParIdImportation(idImportationSommet, true, ligne);// Pas de doublon

                    } else if (elementLigne[1].equals("->")) { // Creation arete

                        creationAreteImportation(ligne);
                    }
                }
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Méthode permettant d'initialiser les attributs d'un sommet.
     * @param sommet Représente le sommet au quel il faut ajouter les attributs.
     * @param ligne Représente la ligne dans le fichier qui concerne le sommet.
     * @param existe Représente un boolean indiquant si le sommet se trouve déjà dans la liste des sommets ou non.
     */
    private void ajoutAttributsSommet(Sommet sommet, String ligne, boolean existe) {
        if (sommet != null) {

            Pattern pattern = Pattern.compile("label=\"+.+\""); // label du sommet
            Matcher matcher = pattern.matcher(ligne); // On le cherche dans la ligne

            if (matcher.find()) { // Si le label existe
                String label = matcher.group().split("=\"")[1];
                label = label.replaceAll("\"$", "");
                label = label.replace(" ", "_");
                sommet.setTag(label);
            }

            pattern = Pattern.compile("shape=+[A-Z a-z]+ "); // forme du sommet
            matcher = pattern.matcher(ligne); // On le cherche dans la ligne

            if (matcher.find()) {
                String shape = matcher.group().split("=")[1];
                sommet.setForme(shape);
            }

            if (!existe) {
                sommets.add(sommet);
            }
        }
    }

    /**
     * Méthode permettant d'initialiser les attributs d'une arête.
     * @param ligne Représente la ligne dans le fichier qui concerne l'arête.
     */
    private void ajoutAttributsArete(String ligne) {

        Arete arete = aretes.get(aretes.size()-1); // On récupère la dernière car dernière ajoutée

        Pattern pattern = Pattern.compile("label=\"+.+\""); // label du sommet
        Matcher matcher = pattern.matcher(ligne); // On le cherche dans la ligne

        if (matcher.find()) { // Si le label existe
            String tag = matcher.group().split("=\"")[1];
            tag = tag.replaceAll("\"$", "");
            tag = tag.replace(" ", "_");
            arete.setTag(tag);
        }
    }

    /**
     * Méthode permettant de créer des arêtes lors de l'importation.
     * @param ligne Représente la ligne concernant l'arête.
     */
    private void creationAreteImportation(String ligne) {
        String elementSommet = ligne.split("\\[+.+;$")[0].trim();
        String[] sommet = elementSommet.split("->");
        int idImportationSommetSource = Integer.parseInt(sommet[0].split("node")[1].replace("\"", "").trim());
        int idImportationSommetDestination = Integer.parseInt(sommet[1].split("node")[1].replace("\"", "").trim());

        if (!verificationDoublonAreteParId(true, idImportationSommetSource, idImportationSommetDestination)) {
            Sommet sommetSource = verificationSommetDoublonParIdImportation(idImportationSommetSource, false, null);
            Sommet sommetDestination = verificationSommetDoublonParIdImportation(idImportationSommetDestination, false, null);

            ajouterArete(sommetSource, sommetDestination);

            ajoutAttributsArete(ligne);
        }
    }

    /**
     * Méthode permettant de vérifier si une arête n'est pas déjà existante.
     * @param parIdImportation Représente le fait de savoir si on passe la recherche via l'id d'importation ou par l'id du graphe.
     * @param idSommetSource Représente le sommet source de l'arête.
     * @param idSommetDestination Représente le sommet destinataire de l'arête.
     * @return Retroune vrai si il y a un doublon et faux dans le cas contraire.
     */
    private boolean verificationDoublonAreteParId(boolean parIdImportation, int idSommetSource, int idSommetDestination) {

        boolean doublonArete = false;
        if (!aretes.isEmpty()) {
            int cptArete = 0;
            while (cptArete < aretes.size() && !doublonArete) {
                if (parIdImportation) {
                    if ((aretes.get(cptArete).getEntree().getIdImportation() == idSommetSource &&
                            aretes.get(cptArete).getSortie().getIdImportation() == idSommetDestination) || (
                            aretes.get(cptArete).getEntree().getIdImportation() == idSommetDestination &&
                                    aretes.get(cptArete).getSortie().getIdImportation() == idSommetSource)) {
                        doublonArete = true;
                    }
                }
                else {
                    if ((aretes.get(cptArete).getEntree().getId() == idSommetSource &&
                            aretes.get(cptArete).getSortie().getId() == idSommetDestination) || (
                            aretes.get(cptArete).getEntree().getId() == idSommetDestination &&
                                    aretes.get(cptArete).getSortie().getId() == idSommetSource)) {
                        doublonArete = true;
                    }
                }

                ++cptArete;
            }
        }

        return doublonArete;
    }

    /**
     * Méthode permettant de vérifier si le sommet n'existe pas déjà grâce à son id d'importation.
     * @param idImportation Représente l'id qu'a le sommet dans le fichier.
     * @param ajoutAttributs Représente le fait de vouloir où non lui rajouter des attributs
     * @param ligne Représente la ligne où il faut chercher les éléments pour ajouter des attributs aux sommets.
     * @return Retourne le sommet correspondant si il y a doublon sinon il retourne null.
     */
    private Sommet verificationSommetDoublonParIdImportation(int idImportation, boolean ajoutAttributs, String ligne) {

        boolean existe = false;
        int cpt = 0;

        Sommet sommet = null;
        while (!existe && cpt < sommets.size()) {
            if (sommets.get(cpt).getIdImportation() == idImportation) {
                existe = true;
                sommet = sommets.get(cpt);
            } else {
                cpt++;
            }
        }

        if (!existe && !ajoutAttributs) {
            sommet = new Sommet(idImportation);
            sommets.add(sommet);
        }
        else if (!existe) {
            sommet = new Sommet(idImportation);
        }

        if (!ajoutAttributs) {
            return sommet;
        }
        else {
            ajoutAttributsSommet(sommet, ligne, existe);

            return null;
        }
    }

    /**
     * Charge les fichiers .GRAPHML.
     * @param fichier Représente le chemin du fichier contenant le graphe.
     */
    private void chargerGrapheGRAPHML(String fichier) {

        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;

            Pattern patternIdImportation = Pattern.compile("\\d+$");

            Matcher matcherSrc;
            Matcher matcherDest;

            while ((ligne = br.readLine()) != null) {
                if (ligne.contains("<node ") && ligne.contains("id=\"")) {

                    String [] recupId = ligne.split("id=\"");
                    String idImportation = recupId[1].split("\"")[0];

                    matcherSrc = patternIdImportation.matcher(idImportation);

                    if (matcherSrc.find()) {
                        verificationSommetDoublonParIdImportation(Integer.parseInt(matcherSrc.group()), false, null);
                    }
                }

                else if (ligne.contains("<edge ") && ligne.contains("source=\"") && ligne.contains("target=\"")) {

                    String[] recupSource = ligne.split("source=\"");
                    matcherSrc = patternIdImportation.matcher(recupSource[1].split("\"")[0]);

                    String[] recupTarget = ligne.split("target=\"");
                    matcherDest = patternIdImportation.matcher(recupTarget[1].split("\"")[0]);

                    if (matcherSrc.find() && matcherDest.find()) {

                        if (!verificationDoublonAreteParId(true, Integer.parseInt(matcherSrc.group()), Integer.parseInt(matcherDest.group()))) {
                            Sommet sommetSource = verificationSommetDoublonParIdImportation(Integer.parseInt(matcherSrc.group()), false, null);
                            Sommet sommetDestination = verificationSommetDoublonParIdImportation(Integer.parseInt(matcherDest.group()), false, null);

                            ajouterArete(sommetSource, sommetDestination);
                        }
                    }
                }
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Permet de sauvegarde le graphe en cours.
     * Le choix de l'algotithme se fait en fonction du format choisi.
     * @param chemin_sauvegarde Représente le chemin où sera sauvegardé le fichier au format .dot ou .graphml.
     * @return Retourne vrai si la sauvegarde c'est bien passé ou faux dans le cas contraire.
     */
    public boolean sauvegarderGraphe (String chemin_sauvegarde) {

        if (chemin_sauvegarde == null) {
            chemin_sauvegarde = cheminGraphe;
        }

        if (chemin_sauvegarde.matches(".+.gv$")) {
            return sauvegarderGrapheGv(chemin_sauvegarde);
        }
        else if (chemin_sauvegarde.matches(".+.graphml$")) {
            return sauvegarderGrapheGraphml(chemin_sauvegarde);
        }

        return false;
    }


    /**
     * Permet de sauvegarder un graphe au format .gv.
     * @param chemin_sauvegarde Représente le chemin où sera sauvegardé le fichier au format .gv.
     * @return Retourne vrai si la sauvegarde c'est bien passé ou faux dans le cas contraire.
     */
    private boolean sauvegarderGrapheGv(String chemin_sauvegarde) {

        try {
            FileWriter fileWriter = new FileWriter (chemin_sauvegarde);
            BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
            PrintWriter fichierSortie = new PrintWriter (bufferedWriter);

            fichierSortie.println ("graph \"" + nom + "\" {");

            fichierSortie.println("\tsize=\"" + taille.width + "," + taille.height + "\"");

            if (!sommets.isEmpty()) {

                for (Sommet sommet : sommets) {
                    fichierSortie.println("\t\"node" + sommet.getId() + "\" [ label=\"" + sommet.getTag() + "\" shape=" + sommet.getForme() + " ];");
                }
            }

            if (!extremites.isEmpty() && !aretes.isEmpty()) {
                for(Arete arete : aretes) {

                    String temp = "\t\"node" + extremites.get(arete).getKey().getId() + "\" ->  \"node" + extremites.get(arete).getValue().getId() + "\" [ ";
                    if (!arete.getTag().equals("")) {
                        temp += "label=\"" + arete.getTag() + "\" ];";
                    }
                    else {
                        temp += " ];";
                    }

                    fichierSortie.println(temp);

                }
            }

            fichierSortie.println("}");

            fichierSortie.close();
            bufferedWriter.close();
            fileWriter.close();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return false;
    }


    /**
     * Permet de sauvegarder un graphe au format .graphml.
     * @param chemin_sauvegarde Représente le chemin où sera sauvegardé le fichier au format .graphml.
     * @return Retour vrai si la sauvegarde c'est bien passé ou faux dans le cas contraire.
     */
    private boolean sauvegarderGrapheGraphml (String chemin_sauvegarde) {

        try {
            FileWriter fileWriter = new FileWriter (chemin_sauvegarde);
            BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
            PrintWriter fichierSortie = new PrintWriter (bufferedWriter);

            fichierSortie.println ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");


            fichierSortie.println("\t<graph id="+ nom +" edgedefault=\"undirected\">");

            if (!sommets.isEmpty()) {
                for (Sommet sommet : sommets) {
                    fichierSortie.println("\t\t<node id=\"" + sommet.getTag() + "\" />");
                }
            }

            if (!extremites.isEmpty() && !aretes.isEmpty()) {
                for(Arete arete : aretes) {
                    fichierSortie.println("\t\t<edge id=\"" + arete.getId() + "\" source=\""+ extremites.get(arete).getKey().getId() +"\" target=\"" + extremites.get(arete).getValue().getId() + "\" />");

                }
            }

            fichierSortie.println("\t</graph>");
            fichierSortie.println("</graphml>");


            fichierSortie.close();
            bufferedWriter.close();
            fileWriter.close();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        return false;
    }


    /**
     * Méthode permettant de trouver un sommet via son id.
     * @param id Représente l'id à chercher dans le graphe.
     * @return Représente le sommet trouver via l'id et null si l'id ne correspond à aucun sommet.
     */
    public Sommet trouverSommetParID(int id) {
        boolean trouve = false;
        int cpt = 0;
        Sommet res = null;
        while(!trouve && cpt < sommets.size()) {
            if(sommets.get(cpt).getId() == id) {
                trouve = true;
                res = sommets.get(cpt);
            }
            else
                cpt++;
        }
        return res;
    }


    /**
     * Permet d'obtenir les sommets voisins d'un sommet.
     * @param sommet_origine Représente le sommet de référence pour déterminer si les autres sommets sont voisins.
     * @return Retourne la liste des sommets voisins.
     */
    public ArrayList<Sommet> sommetsVoisins(Sommet sommet_origine){
        ArrayList<Sommet> voisinage = new ArrayList<Sommet>();
        try {
            for (Arete arete : incidentes.get(sommet_origine)) {
                if (source(arete).getTag().equals(sommet_origine.getTag())) {
                    voisinage.add(destination(arete));
                }
            }
        }
        catch (NullPointerException e){
            return null;
        }
        return voisinage;
    }

    /**
     * Permet d'obtenir le sommet source d'une arête.
     * @param arete Représente l'arete sur laquelle on cherche le sommet source.
     * @return Retourne le sommet source.
     */
    public Sommet source(Arete arete){

        return extremites.get(arete).getKey();
    }

    /**
     * Permet d'obtenir le sommet destination d'une arête.
     * @param arete Représente l'arete sur laquelle on cherche le sommet destination.
     * @return Retourne le sommet de destination.
     */
    public Sommet destination(Arete arete){
        return extremites.get(arete).getValue();
    }

    /**
     * Permet d'obtenir la liste des sommets non voisins.
     * @param voisins Représente la liste de sommets de référence pour déterminer les sommets n'étant pas voisins.
     * @return Retourne la liste des sommets non voisins.
     */
    public ArrayList<Sommet> sommetsNonVoisins(ArrayList<Sommet> voisins, Sommet sommetOrigine){
        ArrayList<Sommet> nonVoisins = new ArrayList<Sommet>();
        try {
            for (Sommet s : sommets) {
                if (!voisins.contains(s) && s != sommetOrigine)
                    nonVoisins.add(s);
            }
        }
        catch (NullPointerException e){
            return null;
        }
        return nonVoisins;
    }
    /**
     * Permet d'ajouter un sommet dans le graphe.
     * Le boolean de retour permet de gérer les erreurs d'ajouts (aspect graphique).
     * @param sommet Représente le sommet à ajouter.
     * @param tailleFenetre Représente la taille de la fenêtre d'affichage.
     * @return Retourne vrai si l'ajout du sommet c'est fait ou faux dans le cas contraire.
     */
    public boolean ajouterSommet(Sommet sommet, Size tailleFenetre){

        if (verificationPossibiliteAjoutSommet(sommet.getX(), sommet.getY(), tailleFenetre)) {
            sommets.add(sommet);
            incidentes.put(sommet, new ArrayList<Arete>());
        }
        else {
            return false;
        }

        return true;
    }


    /**
     * Permet de vérifier que les coordonnées du sommet sont correctes et qu'il n'existe pas un sommet à ces coordonnées.
     * @param coord_y_sommet Représente la coordonnée en y, à vérifier, du sommet.
     * @param coord_x_sommet Représente la coordonnée en x, à vérifier, du sommet.
     * @return Retourne vrai on peut ajouter un nouveau sommet ou faux dans le cas contraire.
     */
    private boolean verificationPossibiliteAjoutSommet(float coord_x_sommet, float coord_y_sommet, Size tailleFentre) {

        if (verificationCoordonneesValide(coord_x_sommet, coord_y_sommet, tailleFentre)) {
            Iterator<Sommet> iterateur_sommet = sommets.iterator();
            boolean trouver = false;

            while (!trouver && iterateur_sommet.hasNext()) {
                Sommet sommet_temp = iterateur_sommet.next();

                if (sommet_temp.getX() == coord_x_sommet && sommet_temp.getY() == coord_y_sommet) {
                    trouver = true;
                }
            }

            if (trouver) {
                return false;
            }
        }
        else {
            return false;
        }

        return true;
    }

    /**
     * Permet de supprimer un sommet du graphe.
     * @param sommet Représente le sommet à supprimer du graphe.
     */
    public void supprimerSommet(Sommet sommet){

        if(sommet != null) {

            if (incidentes.containsKey(sommet)) {

                // Obligé de passer par une méthode car problème d'accès concurrents sur incidentes.
                int cpt = 0;
                int size = incidentes.get(sommet).size();

                while (cpt < size) {
                    supprimerArete(incidentes.get(sommet).get(0));
                    ++cpt;
                }

                incidentes.remove(sommet);
            }

            sommets.remove(sommet);
        }
    }

    /**
     * Permet d'ajouter une arete au graphe et qui se lie à 2 sommets distinct.
     * Le boolean de retour permet de gérer les erreurs d'ajouts (aspect graphique).
     * @param sommet_1 Représente le premier sommet à lier avec l'arete.
     * @param sommet_2 Représente le second sommet à lier avec l'arete.
     * @return Retourne vrai si l'ajout de l'arete ce fait ou faux dans le cas contraire.
     */
    public boolean ajouterArete(Sommet sommet_1, Sommet sommet_2) {

        if (verificationPossibiliteAjoutArete(sommet_1, sommet_2)) {
            Arete arete = new Arete(sommet_1, sommet_2);
            aretes.add(arete);

            extremites.put(arete, new Pair<Sommet,Sommet>(sommet_1, sommet_2));
            lierAreteAuSommet(arete, sommet_1);
            lierAreteAuSommet(arete, sommet_2);
        }
        else {
            return false;
        }

        return true;
    }


    /**
     * Permet de vérifier si les sommets en paramètre sont différents et si ils sont bien initialisé.
     * La condition de la création de l'arete se fait exclusivement grâce aux sommets.
     * @param sommet_1 Représente le premier sommet qui sera lié à l'arete.
     * @param sommet_2 Représente le second sommet qui sera lié à l'arete.
     * @return Retourne vrai si on peut ajouter une nouvelle arete ou faux dans le cas contraire.
     */
    private boolean verificationPossibiliteAjoutArete(Sommet sommet_1, Sommet sommet_2) {

        return !((sommet_1 == null || sommet_2 == null) || sommet_1.equals(sommet_2) || verificationDoublonAreteParId(false, sommet_1.getId(), sommet_2.getId()));
    }

    /**
     * Permet de lier l'arete à un sommet.
     * @param arete Représente l'arete à lier.
     * @param sommet Représente le sommet auquel l'arete doit être liée.
     */
    private void lierAreteAuSommet (Arete arete, Sommet sommet) {

        if (!incidentes.containsKey(sommet)) {

            ArrayList<Arete> setArete = new ArrayList<Arete>();
            setArete.add(arete);
            incidentes.put(sommet, setArete);
        } else {
            incidentes.get(sommet).add(arete);
        }
    }

    /**
     * Permet de supprimer une arete du graphe.
     * @param arete Représente l'arete à supprimer.
     */
    public void supprimerArete(Arete arete) {

        if(arete != null) {

            incidentes.get(extremites.get(arete).getKey()).remove(arete); // Représente le premier sommet de la Pair dans la HashMap m_extremites
            incidentes.get(extremites.get(arete).getValue()).remove(arete); // Représente le second sommet de la Pair dans la HashMap m_extremites

            extremites.remove(arete);
            aretes.remove(arete);
        }
    }

    /**
     * Permet de savoir si les coordonnées en paramètre son valide dans la fenêtre d'affichage du graphe.
     * @param coord_x Représente la coordonnée x à vérifier.
     * @param coord_y Représente la coordonnée y à vérifier.
     * @return Retourne vrai si les coordonnées sont valide ou faux le cas contraire.
     */
    private boolean verificationCoordonneesValide(float coord_x, float coord_y, Size tailleFenetre) {
        return coord_x >= 0 && coord_y >= 0 && coord_x <= tailleFenetre.width && coord_y <= tailleFenetre.height;

    }

    /**
     * Méthode permettant d'affecter le degré du sommet (son nombre d'arêtes) à l'indice du sommet.
     * @param sommet Représente le sommet a modifier.
     */
    public void setIndiceDegre(Sommet sommet){
        try {
            sommet.setIndice(incidentes.get(sommet).size());
        }
        catch (NullPointerException e) {
            sommet.setIndice(0);
        }
    }

    /**
     * Méthode permettant d'affecter une valeur aléatoire à l'indice du sommet.
     * @param sommet Représente le sommet a modifier.
     */
    private void setIndiceAleatoire (Sommet sommet){
        sommet.setIndice(rand.nextInt());
    }

    /**
     * Méthode permmetant d'affecter pour chaque sommet du graphe son degré à son indice.
     */
    public void setIndiceDegre(){
        for (Sommet s: sommets) {
            setIndiceDegre(s);
        }
    }

    /**
     * Méthode permmetant d'affecter pour chaque sommet du graphe une valeur aléatoire à son indice.
     */
    public void setIndiceAleatoire(){
        for (Sommet s: sommets) {
            setIndiceAleatoire(s);
        }
    }

    /**
     * Méthode permettant de récupérer l'indice maximal de tous les sommets du graphe.
     * @return Retourne l'indice maximum des sommets.
     */
    public int indiceMaxSommet(){
        int i = sommets.size();
        int max = sommets.get(0).getIndice();
        while(--i >= 0) {
            max = (sommets.get(i).getIndice() > max) ? sommets.get(i).getIndice() : max;
        }

        return max;
    }

    /**
     * Méthode permettant de récupérer l'indice minimal de tous les sommets du graphe.
     * @return Retourne l'indice minimum des sommets.
     */
    public int indiceMinSommet(){
        int i = sommets.size();
        int min = sommets.get(0).getIndice();
        while(--i >= 0) {
            min = (sommets.get(i).getIndice() < min) ? sommets.get(i).getIndice() : min;
        }
        return min;
    }

    /**
     * Méthode permettant de récupérer l'indice maximal de toutes les arêtes du graphe.
     * @return Retourne l'indice maximum des arêtes.
     */
    public int indiceMaxArete(){
        int i = aretes.size();
        int max = aretes.get(0).getIndice();
        while(--i >= 0) {
            max = (aretes.get(i).getIndice() > max) ? aretes.get(i).getIndice() : max;
        }
        return max;
    }

    /**
     *Méthode permettant de récupérer l'indice minimal de toutes les aretes du graphe.
     * @return Retourne l'indice minimum des arêtes.
     */
    public int indiceMinArete(){
        int i = aretes.size();
        int min = aretes.get(0).getIndice();
        while(--i >= 0) {
            min = (aretes.get(i).getIndice() < min) ? aretes.get(i).getIndice() : min;
        }
        return min;
    }


    /**
     * Méthode permettant de changer la couleur de tous les sommet du graphe
     * en fonction d'une couleur minimale et d'une couleur maximale
     * et de la valeur de l'indice de chaque sommet.
     * @param cmin Représente le min intervalle de couleur.
     * @param cmax Représente la max intervalle de couleur.
     */
    public void changerCouleurSommets(Color cmin, Color cmax){
        for (Sommet s : sommets) {
            changerCouleurSommet(s, cmin, cmax);
        }
    }

    /**
     * Méthode permettant de changer la couleur d'un sommet en fonction d'une couleur
     * minimale et d'une couleur maximale et de la valeur de l'indice du sommet.
     * @param sommet Représente le sommet sur lequel on doit modifier la couleur.
     * @param cmin Représente le min intervalle de couleur.
     * @param cmax Représente la max intervalle de couleur.
     */
    private void changerCouleurSommet (Sommet sommet, Color cmin, Color cmax){
        if (indiceFixe()) {
            int valeur = sommet.getIndice();
            int indiceMax = indiceMaxSommet();
            int indiceMin = indiceMinSommet();
            double rouge = intensite(valeur, cmax.getRed(), cmin.getRed(), indiceMax, indiceMin);
            double vert = intensite(valeur, cmax.getGreen(), cmin.getGreen(), indiceMax, indiceMin);
            double bleu = intensite(valeur, cmax.getBlue(), cmin.getBlue(), indiceMax, indiceMin);
            sommet.setCouleur(new Color(rouge, vert, bleu, 1.));
        }
    }

    /**
     * Méthode permettant de changer la couleur de toutes les aretes du graphe
     * en fonction d'une couleur minimale et d'une couleur maximale
     * et de la valeur du poids de chaque arete.
     * @param cmin Représente le min intervalle de couleur.
     * @param cmax Représente la max intervalle de couleur.
     */
    public void changerCouleurAretes(Color cmin, Color cmax){
        for (Arete a : aretes) {
            changerCouleurArete(a, cmin, cmax);
        }
    }

    /**
     * Méthode permettant de changer la couleur d'une arete en fonction d'une couleur
     * minimale et d'une couleur maximale et de la valeur du poids de l'arête.
     * @param arete Représente l'arete à modifier.
     * @param cmin Représente le min intervalle de couleur.
     * @param cmax Représente la max intervalle de couleur.
     */
    public void changerCouleurArete (Arete arete, Color cmin, Color cmax){
            int valeur = arete.getIndice();
            int indiceMax = indiceMaxArete();
            int indiceMin = indiceMinArete();
            double rouge = intensite(valeur, cmax.getRed(), cmin.getRed(), indiceMax, indiceMin);
            double vert = intensite(valeur, cmax.getGreen(), cmin.getGreen(), indiceMax, indiceMin);
            double bleu = intensite(valeur, cmax.getBlue(), cmin.getBlue(), indiceMax, indiceMin);
            arete.setCouleur(new Color(rouge, vert, bleu, 1.));
    }


    /**
     * Méthode permettant grâce à une fonction mathematique de generer une couleur pour une arete ou un sommet
     * en fonction de la valeur de son indice suivant un intervalle.
     * @param valeur Représente l'indice du sommet.
     * @param cmin Représente le min intervalle de couleur.
     * @param cmax Représente la max intervalle de couleur.
     * @return Retourne l'intensité calculé.
     */
    private double intensite(int valeur, double cmax, double cmin, int indiceMax, int indiceMin){
        if (indiceMax != indiceMin) {
            if (cmin - cmax <= 0 && cmin <=1 && cmax<=1){
                return ((((double)valeur - indiceMin) / ((double)indiceMax - (double)indiceMin)) * (cmin - cmax) + cmax);
            }
            else if (cmin >= 1 && cmax >= 1){
                return ((((double)valeur - indiceMin) / ((double)indiceMax - (double)indiceMin)) * (cmax - cmin) + cmin);
            }
            else
                return ((((double)valeur - indiceMin) / ((double)indiceMax - (double)indiceMin)) * (cmax - cmin) + cmin);
        }
        else
            return cmin;
    }


    /**
     * Méthode permettant de changer la taille des sommets en fonction d'un minimum et d'un maximum.
     * @param sommet Représente le sommet à modifier.
     * @param maxSommet Représente la taille maximum qu'un sommet peut avoir.
     * @param minSommet Représente la taille minimum qu'un sommet peut avoir.
     */
    private void changerTailleSommet(Sommet sommet, float maxSommet, float minSommet){
        if (indiceFixe()) {
            int valeur = sommet.getIndice();
            int indiceMax = indiceMaxSommet();
            int indiceMin = indiceMinSommet();
            int largeur = (int) intensite(valeur, maxSommet, minSommet, indiceMax, indiceMin);
            Size taille;
            if (!sommet.getForme().equals(Forme_Sommet.Rectangle)) {
                taille = new Size(largeur, largeur);
            }
            else {
                taille = new Size(largeur, sommet.getTaille().height);
            }

            sommet.setTaille(taille);
        }
    }

    /**
     * Méthode permettant de changer l'épaisseur des arêtes en fonction d'un minimum et d'un maximum.
     * @param arete Représente l'arête à modifier.
     * @param maxArete Représente l'épaisseur maximum que peut avoir une arête.
     * @param minArete Représente l'épaisseur minimum que peut avoir une arête.
     */
    private void changerTailleArete(Arete arete, float maxArete, float minArete){
            int valeur = arete.getIndice();
            int indiceMax = indiceMaxArete();
            int indiceMin = indiceMinArete();
            int largeur = (int) intensite(valeur, maxArete, minArete, indiceMax, indiceMin);
            arete.setEpaisseur(largeur);
    }


    /**
     * Méthode permettant de changer la taille du graphe (sommets et arêtes).
     * @param maxSommet Représente la taille maximum qu'un sommet peut avoir.
     * @param minSommet Représente la taille minimum qu'un sommet peut avoir.
     * @param maxArete Représente l'épaisseur maximum que peut avoir une arête.
     * @param minArete Représente l'épaisseur minimum que peut avoir une arête.
     */
    public void changerTailleGraphe(float maxSommet, float minSommet, float maxArete, float minArete){
        for (Sommet s : sommets){
            changerTailleSommet(s, maxSommet, minSommet);
        }
        for (Arete a : aretes){
            changerTailleArete(a, maxArete, minArete);
        }
    }

    /**
     * Méthode permettant de vérifier si l'indice des sommets ont été initialisé.
     * @return Retourne vrai si ils sont initialisés et faux dans le cas contraire.
     */
    public boolean indiceFixe (){
        int i = 0;
        for (Sommet s : sommets){
            i += s.getIndice();
        }
        return (i != 0);
    }

    /**
     * Méthode permettant de modifier la représentation du graphe suivant l'algorithme choisi.
     * @param algorithme Représente l'algoritme choisi.
     * @param largeurEcran Représente la largeur de l"écran.
     */
    public void setAlgorithmeRepresentation(char algorithme, int largeurEcran, int hauteurEcran){
        switch (algorithme){
            case 'a' :{
                algorithmeRepresentation.distributionAleatoire(largeurEcran, hauteurEcran);
                break;
            }
            case 'c': {
                algorithmeRepresentation.distributionCirculaire(largeurEcran, hauteurEcran);
                break;
            }
            case 'f': {
                algorithmeRepresentation.distributionModeleForces();
                break;
            }
            default:
                break;
        }
    }

    // Accesseur et Mutateurs

    public ArrayList<Sommet> getSommets() {

        return sommets;
    }

    public void setSommets(ArrayList<Sommet> sommets) {

        this.sommets = sommets;
    }

    public ArrayList<Arete> getAretes() {

        return aretes;
    }

    public void setAretes(ArrayList<Arete> aretes) {

        this.aretes = aretes;
    }

    public HashMap<Sommet, ArrayList<Arete>> getIncidentes() {

        return incidentes;
    }

    public HashMap<Arete, Pair<Sommet, Sommet>> getM_extremites() {

        return extremites;
    }

    public Size getTaille() {
        return taille;
    }

    public void setTaille(Size taille) {
        this.taille = taille;
    }

}
