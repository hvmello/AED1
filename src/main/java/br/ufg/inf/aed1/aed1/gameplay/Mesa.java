package br.ufg.inf.aed1.aed1.gameplay;

import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.CartaMagica;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import br.ufg.inf.aed1.aed1.carta.Deck;

import java.util.ArrayList;


public class Mesa {
    public CartaMonstro cartasMonstros[];
    public CartaMagica cartasMagicas[];
    
    public ArrayList<Carta> cemiterio;     //atualmente não esta em uso
    
    public static final int MESA_SIZE = 5;
    
    public Mesa(){
        cartasMonstros = new CartaMonstro[MESA_SIZE];
        cartasMagicas = new CartaMagica[MESA_SIZE];
        cemiterio = new ArrayList<Carta>(Deck.DECK_SIZE);
    }
    
    public void putMonstro(int index, CartaMonstro cartaMonstro){
        cartasMonstros[index] = cartaMonstro;
    }
    public void putMagica(int index, CartaMagica cartaMagica){
        cartasMagicas[index] = cartaMagica;
    }
    
    public boolean isCartasMonstrosEmpty(){
        for(int i=0;i<MESA_SIZE; i++){
            if(cartasMonstros[i] != null) return false;
        }
        return true;
    }
    
    
    //retorna o índice do primeiro encontro de uma carta mágica Trap ( armadilha), e -1 se não houver
    public int TrapMagicIndex(){
        for(int i=0; i<MESA_SIZE; i++){
            if(cartasMagicas[i]!= null && cartasMagicas[i].getTipoEfeitoMagico()== CartaMagica.TipoEfeitoMagico.TRAP)
                return i;
        }
        return -1;
    }
    
    public void removeMagica(int index){
        cartasMagicas[index] = null;
    }
    public void removeMonstro(int index){
        cemiterio.add(cartasMonstros[index]);
        cartasMonstros[index] = null;
    }
    
    //remove primeira ocorrencia encontrada da carta
    public void removeMonstro(Carta cartaMonstro){
        for(int i=0;i<MESA_SIZE; i++){
            if(cartasMonstros[i] != cartaMonstro){
                cemiterio.add(cartasMonstros[i]);
                cartasMonstros[i] = null;
                break;
            }
        }
    }
    
}
