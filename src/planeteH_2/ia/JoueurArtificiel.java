package planeteH_2.ia;

/*
 * Si vous utilisez Java, vous devez modifier ce fichier-ci.
 *
 * Vous pouvez ajouter d'autres classes sous le package planeteH_2.ia.
 *
 * Prénom Nom    (CODE00000001)
 * Prénom Nom    (CODE00000002)
 */

import planeteH_2.Grille;
import planeteH_2.GrilleVerificateur;
import planeteH_2.Joueur;
import planeteH_2.Position;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class JoueurArtificiel implements Joueur {
    private final GrilleVerificateur verificateur = new GrilleVerificateur();
    private final GrilleVerifX verificateurX = new GrilleVerifX();
    private final Random random = new Random();
    private int CurrentPlayer=1;

    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans JoueurArtificiel.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package planeteH_2.ia.
     * Vous ne pouvez pas modifier les fichiers directement dans planeteH_2., car ils seront écrasés.
     * 
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        CurrentPlayer = grille.nbLibre()%2+1;
//        System.out.println(CurrentPlayer);
        return minimaxDecision(grille);
    }

    @Override
    public String getAuteurs() {
        return "Prénom1 Nom1 (CODE00000001)  et  Prénom2 Nom2 (CODE00000002)";
    }

    public Position minimaxDecision(Grille grille){
        HashMap<Grille,Integer> actions = generateGridMap(grille);
        for(HashMap.Entry<Grille, Integer> action : actions.entrySet()){
            Grille current = action.getKey();
            action.setValue(minValue(current,Integer.MIN_VALUE,Integer.MAX_VALUE, 3));

//                System.out.println("Value of this grid:" + action.getValue());
//            System.out.println();
//                printGrid(current);
        }


        return getMove(grille,getMaxGrid(actions));
    }

    public int maxValue(Grille grille, int alpha, int beta, int depth){
        int winner = getWinner(grille);
        if(winner != 0){
            return getWinnerScore(winner);
        } else if(isTerminalState(grille)){
            return 0;
        }

        if(cutoffTest(depth)){
            return getUtilityOfGrid(grille);
        }

        int value = Integer.MIN_VALUE;
        ArrayList<Grille> actions = generateNextGrids(grille);
        for(Grille action : actions){
            value = Math.max(value, minValue(action, alpha, beta, --depth));
            if (value >= beta){
                return value;
            }
            alpha = Math.max(alpha,value);
        }
        return value;
    }

    public int minValue(Grille grille, int alpha, int beta, int depth){
        int winner = getWinner(grille);
        if(winner != 0){
            return getWinnerScore(winner);
        }else if(isTerminalState(grille)){
            return 0;
        }

        if(cutoffTest(depth)){
//            System.out.println();
//            printGrid(grille);
            return getUtilityOfGrid(grille);
        }

        int value = Integer.MAX_VALUE;
        ArrayList<Grille> actions = generateNextGrids(grille);
        for(Grille action: actions){
            value = Math.min(value, maxValue(action, alpha, beta, --depth));
            if(value <= alpha)
                return value;
            beta = Math.min(beta,value);
        }
        return value;
    }

    public int getWinner(Grille grille){
        return verificateur.determineGagnant(grille);
    }

    public int getWinnerScore(int player){
        return (CurrentPlayer == player)?1000:-1000;
    }

    public Position getMove(Grille originalGrid, Grille newGrid){
        int largeur = originalGrid.getData()[0].length;
        int l=0,c=0;
        for(int i=0;i<originalGrid.getSize();i++){
            c=i%largeur;
            if(i!=0&&c==0)
                l++;

            if(originalGrid.get(l,c)!=newGrid.get(l,c)){
                return new Position(l,c);
            }
        }
        return new Position(0,0);
    }

    public Grille getMaxGrid(HashMap<Grille, Integer> actions){
        HashMap.Entry<Grille, Integer> maxEntry = null;

        for (HashMap.Entry<Grille, Integer> action : actions.entrySet())
        {
            if (maxEntry == null || action.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = action;
            }
        }

//        System.out.println("Value of action taken:"+maxEntry.getValue());
        return maxEntry.getKey();
    }

    public HashMap<Grille,Integer> generateGridMap(Grille grille){
        HashMap<Grille,Integer> result = new HashMap<>();
        ArrayList<Grille> grilles = generateNextGrids(grille);
        for(Grille temp : grilles){
            result.put(temp,0);
        }

        return result;
    }

    public ArrayList<Grille> generateNextGrids(Grille grille){

        int nbLibre = grille.nbLibre();
        ArrayList<Grille> grilles = new ArrayList<>();
        int prochainJoueur = (nbLibre%2+1)==1?1:2;
        int largeur=grille.getData()[0].length;

        ArrayList<Integer> casesvides=genererCasesVide(grille);
        int index=0;
        for(int casevide : casesvides){
            if(hasNearbyToken((casevide-casevide%largeur)/largeur,casevide%largeur, grille)) {
                Grille temp = grille.clone();
                temp.set((casevide - casevide % largeur) / largeur,
                        casevide % largeur,
                        prochainJoueur);
                grilles.add(temp);
            }
        }

        return grilles;
    }

//    public int calculateGridUtilityValue(Grille grille){
//        int[] hasStreak = verificateurX.trouveOuvert(grille);
//        if(hasStreak[0]!=0){
//            return getInARowUtilityOfGrid(grille, hasStreak[1], hasStreak[0]);
//        }
//        return 0;
//    }

    public int getUtilityOfGrid(Grille grille){
        int [] table =verificateurX.trouveSuites(grille);
        int result = 0;

        if(CurrentPlayer == 1) {
            result += table[0];
            result += -table[1];
            result += table[2]*5;
            result += -table[3]*5;
            result += table[4]*18;
            result += -table[5]*18;
            result += table[6]*1000;
            result -= -table[7]*1000;
        }else{
            result += -table[0];
            result += table[1];
            result += -table[2]*5;
            result += table[3]*5;
            result += -table[4]*18;
            result += table[5]*18;
            result += -table[6]*1000;
            result -= table[7]*1000;
        }
        return result;
    }

//    public int getWinnerUtilityOfGrid(Grille grille){
////        System.out.println(CurrentPlayer);
//        if(CurrentPlayer == 1){
//            if(grille.nbLibre()%2==0){
////                System.out.println("Winner utility: 100");
//                return 100;
//            }else{
////                System.out.println("Winner utility: -100");
//                return 90;
//            }
//        }else{
//            if(grille.nbLibre()%2==1){
////                System.out.println("Winner utility: 100");
//                return 100;
//            }else{
////                System.out.println("Winner utility: -100");
//                printGrid(grille);
//                return 90;
//            }
//        }
//    }

//    public int getInARowUtilityOfGrid(Grille grille, int biggestStreak, int playerWhoHasStreak){
////        int[] returnVal ={0,2,4,6,8,10};
////        if(CurrentPlayer == 1){
////            return (playerWhoHasStreak==1)?returnVal[biggestStreak]:returnVal[biggestStreak]-1;
////        }else{
////            return (playerWhoHasStreak==2)?returnVal[biggestStreak]:returnVal[biggestStreak]-1;
////        }
//        int[] returnVal ={0,20,40,60,80,100};
//        if(CurrentPlayer == 1){
//            if(grille.nbLibre()%2==0){
////                System.out.println(biggestStreak +" with Player1LoseStreak :" + (-returnVal[biggestStreak]));
////                printGrid(grille);
//                return -returnVal[biggestStreak];
//            }else{
////                System.out.println(biggestStreak +" with Player1WinStreak :" + (returnVal[biggestStreak]));
////                printGrid(grille);
//                return returnVal[biggestStreak];
//            }
//        }else{
////            System.out.println(CurrentPlayer);
//            if(grille.nbLibre()%2==1){
////                System.out.println(biggestStreak +" with Player2WinStreak :" + (returnVal[biggestStreak]));
////                printGrid(grille);
//                return returnVal[biggestStreak];
//            }else{
////                System.out.println(biggestStreak +" with Player2LoseStreak :" + (-returnVal[biggestStreak]));
////                printGrid(grille);
//                return -returnVal[biggestStreak];
//            }
//        }
//    }


    public boolean isTerminalState(Grille grille){
        return grille.nbLibre()==0;
    }

    public boolean cutoffTest(int testValue){
        if(testValue <=0){
            return true;
        }else
            return false;
    }

    public ArrayList<Integer> genererCasesVide(Grille grille){
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int nbcol = grille.getData()[0].length;
        for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    casesvides.add(l*nbcol+c);
        return casesvides;
    }

    public void printGrid(Grille grille){
        for(int i=0;i<grille.getData().length;i++){
            for(int j=0;j<grille.getData()[0].length;j++){
                System.out.print("["+grille.get(i,j)+"]");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean hasNearbyToken(int ligne, int colonne, Grille grille){
        int largeur = grille.getData()[0].length;
        int hauteur = grille.getData().length;

        return tokAtLine(ligne, colonne, grille)
                || (((ligne + 1) < hauteur) && tokAtLine(ligne + 1, colonne, grille))
                || (((ligne + 2) < hauteur) && tokAtLine(ligne + 2, colonne, grille))
                || (((ligne - 1) >= 0) && tokAtLine(ligne - 1, colonne, grille))
                || (((ligne - 2) >= 0) && tokAtLine(ligne - 2, colonne, grille))
                || (((colonne + 1) < largeur) && tokAtLine(ligne, colonne + 1, grille))
                || (((colonne + 1) < largeur) && ((ligne + 1) < hauteur) && tokAtLine(ligne + 1, colonne + 1, grille))
                || (((colonne + 1) < largeur) && ((ligne + 2) < hauteur) && tokAtLine(ligne + 2, colonne + 1, grille))
                || (((colonne + 1) < largeur) && ((ligne - 1) >= 0) && tokAtLine(ligne - 1, colonne + 1, grille))
                || (((colonne + 1) < largeur) && ((ligne - 2) >= 0) && tokAtLine(ligne - 2, colonne + 1, grille))
                || (((colonne + 2) < largeur) && tokAtLine(ligne, colonne + 2, grille))
                || (((colonne + 2) < largeur) && ((ligne + 1) < hauteur) && tokAtLine(ligne + 1, colonne + 2, grille))
                || (((colonne + 2) < largeur) && ((ligne + 2) < hauteur) && tokAtLine(ligne + 2, colonne + 2, grille))
                || (((colonne + 2) < largeur) && ((ligne - 1) >= 0) && tokAtLine(ligne - 1, colonne + 2, grille))
                || (((colonne + 2) < largeur) && ((ligne - 2) >= 0) && tokAtLine(ligne - 2, colonne + 2, grille))
                || (((colonne - 1) >= 0) && tokAtLine(ligne, colonne - 1, grille))
                || (((colonne - 1) >= 0) && ((ligne + 1) < hauteur) && tokAtLine(ligne + 1, colonne - 1, grille))
                || (((colonne - 1) >= 0) && ((ligne + 2) < hauteur) && tokAtLine(ligne + 2, colonne - 1, grille))
                || (((colonne - 1) >= 0) && ((ligne - 1) >= 0) && tokAtLine(ligne - 1, colonne - 1, grille))
                || (((colonne - 1) >= 0) && ((ligne - 2) >= 0) && tokAtLine(ligne - 2, colonne - 1, grille))
                || (((colonne - 2) >= 0) && tokAtLine(ligne, colonne - 2, grille))
                || (((colonne - 2) >= 0) && ((ligne + 1) < hauteur) && tokAtLine(ligne + 1, colonne - 2, grille))
                || (((colonne - 2) >= 0) && ((ligne + 2) < hauteur) && tokAtLine(ligne + 2, colonne - 2, grille))
                || (((colonne - 2) >= 0) && ((ligne - 1) >= 0) && tokAtLine(ligne - 1, colonne - 2, grille))
                || (((colonne - 2) >= 0) && ((ligne - 2) >= 0) && tokAtLine(ligne - 2, colonne - 2, grille));
    }

    private boolean tokAtLine(int ligne,int colonne,Grille grille){
        return grille.get(ligne,colonne)==1||grille.get(ligne,colonne)==2;
    }
}
