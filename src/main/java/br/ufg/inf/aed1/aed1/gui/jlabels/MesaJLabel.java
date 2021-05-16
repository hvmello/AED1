package br.ufg.inf.aed1.aed1.gui.jlabels;


import br.ufg.inf.aed1.aed1.carta.CartaMagica;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import br.ufg.inf.aed1.aed1.gameplay.Mesa;
import gui.utils.ImageEditor;

import javax.swing.Icon;

public class MesaJLabel {
    public CartaJLabel cartasMonstros[];
    public CartaJLabel cartasMagicas[];
    
    public MesaJLabel(){
        cartasMonstros = new CartaJLabel[Mesa.mesaSize];
        cartasMagicas = new CartaJLabel[Mesa.mesaSize];
    }
    
    public void putMonstro(int index, CartaMonstro cartaMonstro){
        if(cartaMonstro == null){
            cartasMonstros[index].setIcon((Icon)null);
            cartasMonstros[index].setDisabledIcon((Icon)null);
        }
        else{
            cartasMonstros[index].setDisabledIcon(ImageEditor.loadGrayScaled(cartaMonstro.getImageSrc(), InGameInterface.dimensaoCarta.width, InGameInterface.dimensaoCarta.height));
            cartasMonstros[index].setIcon(ImageEditor.getRedimensionado(cartaMonstro.getImageSrc(),InGameInterface.dimensaoCarta));
        }
    }
    public void putMagica(int index, CartaMagica cartaMagica){
        if(cartaMagica == null)
            cartasMagicas[index].setIcon((Icon)null);
        else
            cartasMagicas[index].setIcon(ImageEditor.getRedimensionado(cartaMagica.getImageSrc(),InGameInterface.dimensaoCarta));
    }
    
}
