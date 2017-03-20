package planeteH_2.ia;

import planeteH_2.Grille;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Nice
 */
public class GrilleVerifX {
    protected int nombreGagnant = 5;
    protected boolean compteExact = true;
    protected int lastValue = 0;
    protected int count = 0;
    protected int quiGagne = 0;
    protected int biggestStreak = 1;
    protected int[] result = {
            0, //p1 Twos 0
            0, //player2Twos 1
            0, //p1 Threes 2
            0, //player2Threes 3
            0, //p1 fours 4
            0, //player2Fours 5
            0, //p1 Wins 6
            0 //player2Wins 7
    };
    protected Queue<Integer> etats;

    public GrilleVerifX(){
        etats = new LinkedList<>();
        etats.add(0);
        etats.add(0);
        etats.add(0);
        etats.add(0);
        etats.add(0);
    }

    public int[] trouveSuites(Grille grille) {
        quiGagne = biggestStreak = count = lastValue = 0; // reset status
        int [] table = result.clone();

        // horizontal
        for (int l = 0; l < grille.getData().length; l++) {
            for (int c = 0; c < grille.getData()[0].length; c++) {
                table = check(grille.getData()[l][c], table);
            }
            table = check(0, table);
        }

        // vertical
        for (int c = 0; c < grille.getData()[0].length; c++) {
            for (int l = 0; l < grille.getData().length; l++) {
                table = check(grille.getData()[l][c], table);
            }
            table = check(0, table);
        }

        // Diagonal \\\\\\\
        for (int c = -grille.getData().length; c < grille.getData()[0].length; c++) {
            int c2 = c;
            int l = 0;
            if (c2 < 0) {
                l = -c2;
                c2 = 0;
            }
            int oldL = l;
            for (; c2 < grille.getData()[0].length && l < grille.getData().length; c2++, l++) {
                if(Math.abs(l-oldL)>=2){
                    etats.remove();
                    etats.add(42);
                }
                oldL = l;
                table = check(grille.getData()[l][c2], table);
            }
            table = check(0,table);
        }

        // Diagonal //////
        for (int c = -grille.getData().length; c < grille.getData()[0].length; c++) {
            int c2 = c;
            int l = grille.getData().length - 1;
            if (c2 < 0) {
                l += c2;
                c2 = 0;
            }
            int oldL = l;
            for (; c2 < grille.getData()[0].length && l >= 0; c2++, l--) {
                if(Math.abs(l-oldL)>=2){
                    etats.remove();
                    etats.add(42);
                }
                oldL=l;
                table = check(grille.getData()[l][c2], table);
            }
            table = check(0, table);
        }

        return table;
    }

    private int [] check(int value, int [] table) {
        etats.remove();
        etats.add(value);
        if (value == lastValue) {
            count++;
        } else {
            if (lastValue > 0 && count == nombreGagnant-3 ) {
                quiGagne = lastValue;
                if(quiGagne == 1 && checkForOpenTwos()){
                    table[0]++;
                }else if(checkForOpenTwos()){
                    table[1]++;
                }
            }
            if (lastValue > 0 && count == nombreGagnant-2 ) {
                quiGagne = lastValue;
                if(quiGagne == 1 && checkForOpenThrees()){
                    table[1]=table[0]=0;
                    table[2]++;
                }else if(checkForOpenThrees()){
                    table[1]=table[0]=0;
                    table[3]++;
                }
            }
            if (lastValue > 0 && count == nombreGagnant-1 ) {
                quiGagne = lastValue;
                if(quiGagne == 1 && checkForOpenFours()){
                    table[3]=table[2]=table[1]=table[0]=0;
                    table[4]++;
                }else if(checkForOpenFours()){
                    table[3]=table[2]=table[1]=table[0]=0;
                    table[5]++;
                }
            }
            if (lastValue > 0 && (compteExact ? count == nombreGagnant : count >= nombreGagnant)) {
                quiGagne = lastValue;
                if(quiGagne == 1 && checkForFives()){
                    table[5]=table[4]=table[3]=table[2]=table[1]=table[0]=0;
                    table[6]++;
                }else if(checkForFives()){
                    table[5]=table[4]=table[3]=table[2]=table[1]=table[0]=0;
                    table[7]++;
                }
            }
            count = 1;
            lastValue = value;
        }
        return table;
    }

    public boolean checkForOpenTwos(){
        int [] list = new int[5];
        int [] sampleOne = {0,1,1,0};
        int [] sampleTwo = {0,2,2,0};

        int i=0;
        for(Integer etat : etats){
            list[i]=etat;
            i++;
        }
        if (list[0] == sampleOne[0]
                && list[1] == sampleOne[1]
                && list[2] == sampleOne[2]
                && list[3] == sampleOne[3]){
            return true;
        }
        if (list[1] == sampleOne[0]
                && list[2] == sampleOne[1]
                && list[3] == sampleOne[2]
                && list[4] == sampleOne[3]){
            return true;
        }
        if (list[0] == sampleTwo[0]
                && list[1] == sampleTwo[1]
                && list[2] == sampleTwo[2]
                && list[3] == sampleTwo[3]){
            return true;
        }
        if (list[1] == sampleTwo[0]
                && list[2] == sampleTwo[1]
                && list[3] == sampleTwo[2]
                && list[4] == sampleTwo[3]){
            return true;
        }
        return false;
    }

    public boolean checkForOpenThrees(){
        int [] list = new int[5];
        int [] sampleOne = {0,1,1,1,0};
        int [] sampleTwo = {0,2,2,2,0};

        int i=0;
        for(Integer etat : etats){
            list[i]=etat;
            i++;
        }

        if (list[0] == sampleOne[0]
                && list[1] == sampleOne[1]
                && list[2] == sampleOne[2]
                && list[3] == sampleOne[3]
                && list[4] == sampleOne[4]){
            return true;
        }
        if (list[0] == sampleTwo[0]
                && list[1] == sampleTwo[1]
                && list[2] == sampleTwo[2]
                && list[3] == sampleTwo[3]
                && list[4] == sampleTwo[4]){
            return true;
        }
        return false;
    }

    public boolean checkForOpenFours(){
        int [] list = new int[5];
        int [] sampleOne = {0,1,1,1,1};
        int [] sampleTwo = {0,2,2,2,2};
        int [] sampleOne2 = {1,1,1,1,0};
        int [] sampleTwo2 = {2,2,2,2,0};

        int i=0;
        for(Integer etat : etats){
            list[i]=etat;
            i++;
        }

        if (list[0] == sampleOne[0]
                && list[1] == sampleOne[1]
                && list[2] == sampleOne[2]
                && list[3] == sampleOne[3]
                && list[4] == sampleOne[4]){
            return true;
        }
        if (list[0] == sampleTwo[0]
                && list[1] == sampleTwo[1]
                && list[2] == sampleTwo[2]
                && list[3] == sampleTwo[3]
                && list[4] == sampleTwo[4]){
            return true;
        }
        if (list[0] == sampleOne2[0]
                && list[1] == sampleOne2[1]
                && list[2] == sampleOne2[2]
                && list[3] == sampleOne2[3]
                && list[4] == sampleOne2[4]){
            return true;
        }
        if (list[0] == sampleTwo2[0]
                && list[1] == sampleTwo2[1]
                && list[2] == sampleTwo2[2]
                && list[3] == sampleTwo2[3]
                && list[4] == sampleTwo2[4]){
            return true;
        }
        return false;
    }
    public boolean checkForFives(){
        int [] list = new int[5];
        int [] sampleOne = {1,1,1,1,1};
        int [] sampleTwo = {2,2,2,2,2};

        int i=0;
        for(Integer etat : etats){
            System.out.print(etat);
            list[i]=etat;
            i++;
        }

        if (list[0] == sampleOne[0]
                && list[1] == sampleOne[1]
                && list[2] == sampleOne[2]
                && list[3] == sampleOne[3]
                && list[4] == sampleOne[4]){
            return true;
        }
        if (list[0] == sampleTwo[0]
                && list[1] == sampleTwo[1]
                && list[2] == sampleTwo[2]
                && list[3] == sampleTwo[3]
                && list[4] == sampleTwo[4]){
            return true;
        }
        return false;
    }
}
