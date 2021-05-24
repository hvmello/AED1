package br.ufg.inf.aed1.aed1.gameplay;

import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.ListaDeCartas;

public class Mao extends ListaDeCartas {

    public static final short HAND_SIZE = 5;

    public Mao() {
        super(HAND_SIZE);
    }

    // Sobrescrevendo pois na classe mãe as cartas ficavam ordenadas, aqui não precisa (nem deve)
    @Override
    public void deleteCartaById(int id) {

        int index = this.indexOf(id);
        this.deleteCartaAtIndex(index);
    }

    @Override
    public void deleteCartaAtIndex(int index) {

        cartas[index] = null;
        length--;
    }

    /**
     * Adiciona uma Carta primeiro slot que encontrar vazio (igual a null).
     *
     * @param newCarta a Carta a ser adicionada
     */
    @Override
    public void addCarta(Carta newCarta) {
        int i = 0;
        while (cartas[i] != null && i < HAND_SIZE) {
            i++;
        }
        cartas[i] = newCarta;
        length++;
    }

    /**
     * Busca e retorna o index de uma Carta especifica.
     *
     * @param carta a Carta a ser buscada
     * @return o index de `Carta` ou -1 caso ela nao exista
     */
    public int indexOf(Carta carta) {
        for (int i = 0; i < Mao.HAND_SIZE; i++) {
            if (carta == this.cartas[i]) {
                return i;
            }
        }
        return -1;
    }

    public boolean isFull() {
        return (length >= HAND_SIZE);
    }

    private int indexOf(int id) {
        for (int i = 0; i < length; i++) {
            if (cartas[i].getId() == id) {
                return i;
            }
        }

        return -1;
    }

}
