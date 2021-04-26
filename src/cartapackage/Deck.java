
package cartapackage;
import java.io.Serializable;
import java.util.ArrayList;


//deck usado para vizualizar e editar, não é jogável.
public class Deck extends ListaDeCartas implements Serializable {

    public static final short deckSize = 40;   //tamanho definitivo de um deck ( máximo geral, e mínimo para jogar -> podem existir decks incompletos em criação ainda, mas que não podem ser usados em jogo" )
    private String nome;
    
    
    public Deck(String nome){
        super(deckSize);
        this.nome = nome;    
    }
    public Deck(){
        super(deckSize);
    };

    @Override
    public void addCarta(Carta newCarta) {
        if (length == deckSize) {
            System.out.println("Deck já está cheio!");
            return;
        }
        super.addCarta(newCarta);
   
    }
    

    
    public String getNome(){ return nome; }
    public void setNome(String nome){ this.nome = nome; }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
