package br.ufg.inf.aed1.aed1.carta;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;


// Classe ListaDeCartas, serve como referencia para as outras classes.
public /*abstract*/ class ListaDeCartas implements Cloneable, Serializable {
    
    public Carta cartas[];
    public short length = 0;
    
    public ListaDeCartas(int size){ cartas = new Carta[size]; }
    public ListaDeCartas(){}
    
    
    
    /**
     * Adiciona carta à lista, mantendo-a ordenada
     * @param newCarta Carta a ser adicionada
     */
    public void addCarta( Carta newCarta){
        int j;

        //Insere carta já na posição ordenada ( ordenada por id ), baseado no insertion sort.
        for (j = length-1; (j >= 0) && ( newCarta.getId() < cartas[j].getId() ); j--) {
            cartas[j + 1] = cartas[j];
        }
        this.cartas[j+1] = newCarta;
        length++;
    }
    
    //retorna true se a carta foi deletada com sucesso e false caso contrário ( caso não tenha sido encontrada )
    public void deleteCartaById( int id){
        int index = this.indexOf(id);
        deleteCartaAtIndex(index);
    }
    
    /**
     * Remove a carta do índice escolhido.
     * Move todas as cartas a direita da removida uma posição para esquerda.
     * @param index 
     */
    public void deleteCartaAtIndex( int index){        
        for(int j=index; j<length-1; j++){
            cartas[j] = cartas[j+1];
        }
        cartas[--length] = null;
    }
    
    public Carta getCartaAtIndex(int index){
        if (index >= length || index < 0 ){
            throw new IndexOutOfBoundsException();
        }
        return cartas[index];
    }
    
    
    //retorna objeto Carta com o id indicado, e null se não existir
    public Carta getCartaById(int id){
        int index = this.indexOf(id);
        if(index < 0) return null;
        return cartas[index];
    }
    
    
    //retorna nova ListaDeCartas com todas as cartas do SET 'set'
    public ListaDeCartas getSetCartas(String set){
        ListaDeCartas cartasDoSet = new ListaDeCartas(0);
        if( this.length ==0) return cartasDoSet;
        
        ArrayList<Carta> temp = new ArrayList<>();
        
        for(int i=0; i<length;i++){
            if(cartas[i].getSet().equals(set)){
                temp.add(cartas[i]);
            }
        }
        
        cartasDoSet.setCartasArray( temp.toArray(cartasDoSet.getCartasArray()) );
        return cartasDoSet;
    }
    
    //passa o id de uma carta e retorna o índice dela no baralho
    private int indexOf(int id){
        //usa busca binária O(log n)
        int sup = length-1, inf = 0, meio;
        while(inf <= sup){
            meio = (sup+inf)/2;
            if(cartas[meio].getId() == id)
                return meio;
            
            if (id < cartas[meio].getId())
               sup = meio-1;
            else
               inf = meio+1;
        }
        //não encontrou
        return -1;
    }
    
    public Carta[] getCartasArray(){
        return cartas;
    }
    //atualiza o array de cartas, e o seu tamanho para o mesmo do array passado
    public void setCartasArray(Carta cartas[]){
        this.cartas = cartas;
        this.length = (short)cartas.length;
        Arrays.sort(this.cartas);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
    
}
