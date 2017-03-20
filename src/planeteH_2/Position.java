/*
 * INF4230 - Intelligence artificielle.
 * UQAM - Département d'informatique
 */


package planeteH_2;

/**
 *
 */
public class Position {
    public Position(){
        
    }
    public Position(int l, int c){
        ligne = l;
        colonne = c;
    }
    @Override
    public String toString(){
        return ligne + " " + colonne;
    }
    public int ligne, colonne;
}
