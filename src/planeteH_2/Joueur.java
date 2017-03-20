/*
 * INF4230 - Intelligence artificielle
 * UQAM - Département d'informatique
 */

package planeteH_2;

/**
 *
 */
public interface Joueur {

    public Position getProchainCoup(Grille g, int delais);
    
    public String   getAuteurs();
    
}
