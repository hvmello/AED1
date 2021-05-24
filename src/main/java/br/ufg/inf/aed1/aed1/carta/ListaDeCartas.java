package br.ufg.inf.aed1.aed1.carta;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;

// Classe ListaDeCartas, serve como referencia abstrata para as outras classes.
public /*abstract*/ class ListaDeCartas implements Cloneable, Serializable {

    public Carta cartas[];
    public short length = 0;

    public ListaDeCartas(int size) {
        cartas = new Carta[size];
    }

    public ListaDeCartas() {
    }

    /**
     * Adiciona uma Carta em `cartas[]`, mantendo este array ainda ordenado de
     * forma crescente segundo o id de suas Cartas.
     * <p>
     * Time Complexity: O(n).
     *
     * @param newCarta a Carta a ser adicionada em `cartas[]`
     */
    public void addCarta(Carta newCarta) {
        int j;

        /* 
         * Insere `newCarta` ja na posição ordenada (por id), baseado-se
         * no algoritmo de ordenacao Insertion Sort.
         */
        for (j = length - 1; (j >= 0) && (newCarta.getId() < cartas[j].getId()); j--) {
            cartas[j + 1] = cartas[j];
        }
        this.cartas[j + 1] = newCarta;
        length++;
    }

    /**
     * Deleta a Carta com id igual a `id` e move as Cartas à sua direita para
     * uma posição à esquerda.
     * <p>
     * Time Complexity: O(n).
     *
     * @param id o id da Carta que deseja deletar
     * @see indexOf()
     * @see deleteCartaAtIndex()
     */
    public void deleteCartaById(int id) {
        int index = this.indexOf(id);
        deleteCartaAtIndex(index);
    }

    /**
     * Deleta a Carta na posição `index` e move todas as cartas à direita da
     * Carta a ser removida para uma posição à esquerda.
     * <p>
     * Time Complexity: O(n).
     *
     * @param index o index da carta que deseja deletar
     */
    public void deleteCartaAtIndex(int index) {
        for (int j = index; j < length - 1; j++) {
            cartas[j] = cartas[j + 1];
        }
        cartas[--length] = null;
    }

    /**
     * Retorna uma Carta em `cartas[]` na posicao `index`.
     * <p>
     * Time Complexity: O(1).
     *
     * @throws IndexOutOfBoundsException Se `index` for menor do que 0 ou maior
     * do que `length` - 1
     *
     * @param index
     * @return uma Carta na posição `index`
     */
    public Carta getCartaAtIndex(int index) {
        if (index < 0 || index > length - 1) {
            throw new IndexOutOfBoundsException();
        }
        return cartas[index];
    }

    /**
     * Retorna um Carta com o seu id igual a `id` ou null caso ela nao exista em
     * `cartas[]
     * <p>
     * Time Complexity: O(lg(n)).
     *
     * @param id o id da Carta que deseja buscar
     * @return uma Carta com id igual a`id` ou null caso ela nao exista
     * @see this.indexof()
     */
    public Carta getCartaById(int id) {
        int index = this.indexOf(id);

        return (index >= 0) ? cartas[index] : null;
    }

    /**
     * Retorna uma ListaDeCartas contendo todas as Cartas com set igual a `set`.
     * <p>
     * Time Complexity: O(n).
     *
     * @param set o set das Cartas que se deseja buscar
     * @return uma ListaDeCartas das Cartas com o seu set igual a `set
     */
    public ListaDeCartas getSetCartas(String set) {
        ListaDeCartas cartasDoSet = new ListaDeCartas(0);
        if (this.length == 0) {
            return cartasDoSet;
        }

        ArrayList<Carta> temp = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            if (cartas[i].getSet().equals(set)) {
                temp.add(cartas[i]);
            }
        }

        cartasDoSet.setCartasArray(temp.toArray(cartasDoSet.getCartasArray()));
        return cartasDoSet;
    }

    /**
     * Retorna o index de uma carta de acordo com seu id, fazendo uma busca
     * binária em `cartas[]`.
     * <p>
     * Time Complexity: O(lg(n)).
     *
     * @param id o id da carta que deseja buscar
     * @return o index de uma carta com seu id igual a`id` ou -1 caso ela nao
     * exista
     * @see Arrays.sort()
     */
    private int indexOf(int id) {
        int sup = length - 1, inf = 0, meio;
        while (inf <= sup) {
            meio = (sup + inf) / 2;

            if (cartas[meio].getId() == id) {
                return meio;
            }

            if (id < cartas[meio].getId()) {
                sup = meio - 1;
            } else {
                inf = meio + 1;
            }
        }

        return -1;
    }

    public Carta[] getCartasArray() {
        return cartas;
    }

    // Atualiza o array de cartas e o seu tamanho para o mesmo do array passado
    public void setCartasArray(Carta cartas[]) {
        this.cartas = cartas;
        this.length = (short) cartas.length;
        Arrays.sort(this.cartas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
