package br.ufg.inf.aed1.aed1.carta;

// Deck usado para visualizar e editar, não é jogável.
public class Deck extends ListaDeCartas {

    public static final short DECK_SIZE = 40;  // Tamanho definitivo de um deck ( máximo geral, e mínimo para jogar -> podem existir decks incompletos em criação ainda, mas que não podem ser usados em jogo" )
    private String nome;

    public Deck(String nome) {
        super(DECK_SIZE);
        this.nome = nome;
    }

    public Deck() {
        super(DECK_SIZE);
    }

    @Override
    public void addCarta(Carta newCarta) {
        if (length == DECK_SIZE) {
            System.out.println("Deck já está cheio!");
            return;
        }
        super.addCarta(newCarta);

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
