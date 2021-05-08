package br.ufg.inf.aed1.aed1.carta;

import java.io.InvalidClassException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//https://github.com/orika-mapper/orika  --> orika source code / git repo
//http://grepcode.com/snapshot/repo1.maven.org/maven2/ma.glasnost.orika/orika-core/1.4.6   --> orika jar files
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;




// deck usado durante o gameplay
public class DeckJogavel extends Deck {
    

    //https://stackoverflow.com/questions/36196514/java-how-to-copy-attributes-of-an-object-to-another-object-having-the-same-att
    public DeckJogavel(Deck fonte){
    //copia os atributos, que possuem o mesmo nome, de fonte para a nova instancia de DeckJogavel
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Deck.class, DeckJogavel.class)
                .exclude("cartas")      /*quando permitia a copia padrão de cartas[], era criado um novo array com novos objetos ( Carta) dentro, ou seja, o mapeamento era recusivo até encontrar um tipo básico. E assim consequentemente, 
    além do overhead de criar objeto por objeto novamente, ao criar um novo objeto, o construtor chamado de Carta era o que recebia o tipo como uma String, causando erro por não encontrar um tipo com o mesmo nome*/
                .byDefault()
                .register();
        
        MapperFacade mapper = mapperFactory.getMapperFacade();
        
        mapper.map(fonte, this);
        
        Carta cartasNovas[] = new Carta[DECK_SIZE];
        for(int i=0; i<DECK_SIZE; i++){
            
            
            //aqui tenta clonar o objeto primeiro, se nao conseguir, cria um novo lendo direto do arquivo.
            //(problema, se a pessoa criou a carta e ja foi jogar, ela n vai existir nos arquivos ainda)
            try {
                cartasNovas[i] = (Carta) fonte.cartas[i].clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(DeckJogavel.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    cartasNovas[i] = FileManager.readCarta(fonte.cartas[i].getNome());
                } catch (InvalidClassException ex1) {
                    Logger.getLogger(DeckJogavel.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        
        this.setCartasArray(cartasNovas);
    }
    
    public void embaralha(){
        
        Random random = new Random();
		
	for (int i=0; i < (cartas.length - 1); i++) {
	//sorteia um índice
            int j = random.nextInt(cartas.length); 
			
            //troca o conteúdo dos índices i e j do vetor
            Carta temp = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = temp;
	}
    }
    
    @Override
    public void setCartasArray(Carta cartas[]){
        this.cartas = cartas;
        this.length = (short)cartas.length;
    }
}
