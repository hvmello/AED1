
package gameplay;

import cartapackage.Carta;
import cartapackage.ListaDeCartas;


public class Mao extends ListaDeCartas{
    
    public static final short handSize = 5;
    

    public Mao(){
        super(handSize);
    }

    
    
    //sobrescrevendo pois na classe mãe as cartas ficavam ordenadas, aqui não precisa ( nem deve )
    @Override
    public void deleteCartaById( int id){
        
        int index = this.indexOf(id);
        this.deleteCartaAtIndex(index);
    }
    
    @Override
    public void deleteCartaAtIndex( int index){
        
        cartas[index] = null;
        length--;
    }
    
    /**
     * Adiciona carta no primeiro slot que encontrar vazio (null)
     * @param newCarta Carta a ser adicionada
     */
    @Override
    public void addCarta( Carta newCarta){
        int i=0;
        while(cartas[i]!= null && i < handSize){
            i++;
        }
        cartas[i] = newCarta;
        length++;
    }
    /**
     * Procura o índice do objeto carta 
     * @param carta Objeto a ser procurado
     * @return índice do objeto carta 
     */
    public int indexOf(Carta carta){
        for(int i=0;i<Mao.handSize;i++){
            if(carta == this.cartas[i])
                return i;
        }
        return -1;
    }
    
    
    public boolean isFull(){
        return (length >= handSize);
    }
    
    private int indexOf(int id){
        for(int i=0;i<length;i++){
            if(cartas[i].getId() == id)
                return i;
        }
        
        return -1;
    }
    
}
