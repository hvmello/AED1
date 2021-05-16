
package br.ufg.inf.aed1.aed1.gui.jlabels;

import br.ufg.inf.aed1.aed1.carta.Carta;
import gui.utils.ImageEditor;
import java.awt.Point;
import javax.swing.Icon;
import javax.swing.JLabel;


public class CartaJLabel extends JLabel{
    private Point posInicial;
    private boolean isSelected=false;
    
    public CartaJLabel(String text){ super(text); }
    public CartaJLabel(Icon image){ super(image); }
    public CartaJLabel(Icon image, int horizontalAllignment){ super(image, horizontalAllignment); }
    public CartaJLabel(String text, int horizontalAllignment){ super(text, horizontalAllignment); }
    public CartaJLabel(String text, Icon image, int horizontalAllignment){ super(text, image, horizontalAllignment); }
    
    public void setIcon(Carta carta){
        
        if(carta == null){
            super.setIcon(null);
            return;
        }

        super.setIcon(ImageEditor.getRedimensionado(carta.getImageSrc(), InGameInterface.dimensaoCartaMao));

    }
    
    public Point getPosInicial() {
        return posInicial;
    }
    public void setPosInicial(Point posInicial) {
        this.posInicial = posInicial;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    public void setIsSelected(boolean isClicked) {
        this.isSelected = isClicked;
    }
    

}
