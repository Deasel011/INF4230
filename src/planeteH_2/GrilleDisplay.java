/*
 * INF4230 - Intelligence artificielle
 * UQAM - Département d'informatique
 */

package planeteH_2;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 */
public class GrilleDisplay extends JPanel {
    
    protected Grille grille = new Grille(8, 8);
    
    private static final int tailleCase = 30;
    
    public GrilleDisplay(){
        setBackground(Color.white);
        setMinimumSize(new Dimension(200, 200));
        setPreferredSize(new Dimension(200, 200));
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
//Remarque: Ici, toutes les cases nons jouables du monde H contiennent comme valeur (grille.data[l][c]) le compteur 
//qui s'incrémente à partir de 3 (3,4,5,...). Les cases vides contiennent 0. 
    void setGrille(Grille g){
        grille = g;
        int compteur =3;
        int depart = grille.data.length /2;
        Boolean p = grille.data.length % 2 ==0;
        for(int i= depart; i< depart + 5;i++)
            {
              for(int j=0; j<grille.data.length; j++)
                 {
                   if (p)
                   {
                     if(j != depart -1 && j != depart ) 
                     {
                       grille.getData() [j][i] = (byte)compteur++;
                     }
                   }
                   else 
                   {
                     if(j != depart  ) 
                     {
                       grille.getData() [j][i] = (byte)compteur++;
                     }
                   }
                       
                 }
            } 
        Dimension d = new Dimension(grille.data[0].length * tailleCase,
                                    grille.data.length * tailleCase);
        setMinimumSize(d);
        setPreferredSize(d);
        
        repaint();
    }
    
    public void setCaseListener(GrilleDisplayListener l){
        listener = l;
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_PRESSED){
            if(listener!=null && grille!=null){
                int l = e.getY() / tailleCase;
                int c = e.getX() / tailleCase;
            int nbligne = grille.data.length;
            Boolean pair = nbligne % 2 ==0;
            Boolean zone = test (nbligne, pair, l, c);
                if(l<grille.data.length && c<grille.data[0].length && zone)
                    listener.caseClicked(l, c);

           
            }
        }
    }
    public Boolean test (int nbligne, Boolean p, int l, int c)
    {       int d = nbligne /2;
            for(int i= d; i< d + 5;i++)
            {
              for(int j=0; j<nbligne; j++)
                 {
                   if (p)
                   {
                     if(j != d -1 && j != d ) 
                     {
                       if(l==j && c==i) return false;
                     }
                   }
                   else 
                   {
                     if(j != d  ) 
                     {
                       if(l==j && c==i) return false;
                     }
                   }
                       
                 }
            } 
       return true;
    }
    
    public GrilleDisplayListener listener = null;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(grille!=null){
            int nbligne = grille.data.length;
            int nbcol = grille.data[0].length;
            
            for(int l=0;l<=nbligne;l++)
            { g.drawLine(0, l*tailleCase, nbcol*tailleCase, l*tailleCase);
              
            }
            for(int c=0;c<=nbcol;c++)
                g.drawLine(c*tailleCase, 0, c*tailleCase, nbligne*tailleCase);

//Cette boucle s'occupe du coloriage des cases non jouables de la planete H.
            int depart = nbligne /2;
            Boolean pair = nbligne % 2 ==0;
            for(int i= depart; i< depart + 5;i++)
            {
              for(int j=0; j<nbligne; j++)
                 {
                   if (pair)
                   {
                     if(j != depart -1 && j != depart ) 
                     {
                       g.setColor(Color.black);
                       g.fillRect(i*tailleCase, j*tailleCase, tailleCase, tailleCase);
                     }
                   }
                   else 
                   {
                     if(j != depart  ) 
                     {
                       g.setColor(Color.black);
                       g.fillRect(i*tailleCase, j*tailleCase, tailleCase, tailleCase);
                     }
                   }
                       
                 }
            } 
            
            
            g.setColor(Color.gray.brighter());
            for(Position p : grille.positionsToHighlight){
                g.fillRect(p.colonne*tailleCase, p.ligne*tailleCase, tailleCase, tailleCase);
            }
//Cette boucle s'occupe du coloriage de la grille.
//Les cases contenant les bombes (couleur rouge pour le poseur de bombes) ont la valeur 1.  
//Les cases contenant les pions magiques (couleur verte pour le Htepien) ont la valeur 2. 
            for(int l=0;l<nbligne;l++)
                for(int c=0;c<nbcol;c++){
                    switch(grille.data[l][c]){
                        case 0:
                            continue;
                        case 1:
                            g.setColor(Color.red);
                            break;
                        case 2:
                            g.setColor(Color.green);
                            break;
                        default:
                            continue;
                    }
                    
                    int x1 = 2 + c*tailleCase;
                    int y1 = 2 + l*tailleCase;
                    g.fillOval(x1, y1, tailleCase-4, tailleCase-4);
                    g.setColor(Color.black);
                    g.drawOval(x1, y1, tailleCase-4, tailleCase-4);
                }
        }
    }
}
