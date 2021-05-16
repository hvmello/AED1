package br.ufg.inf.aed1.aed1.gui.jlabels;

import br.ufg.inf.aed1.aed1.gameplay.Mao;



public class MaoJLabel {
    CartaJLabel cartas[] = new CartaJLabel[Mao.HAND_SIZE];
    
    public CartaJLabel[] getLabels(){ return cartas;}
    
}

