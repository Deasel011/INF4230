/*
 * INF4230 - Intelligence artificielle
 * UQAM - Département d'informatique
 */

package planeteH_2;

import java.util.EventListener;

/**
 *
 */
public interface GrilleDisplayListener extends EventListener {

    public void caseClicked(int ligne, int colonne);

}
