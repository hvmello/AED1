package br.ufg.inf.aed1.aed1.carta.filemanager;

import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.CartaMagica;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import br.ufg.inf.aed1.aed1.carta.Deck;
import br.ufg.inf.aed1.aed1.carta.ListaDeCartas;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

    private static final String DECKS_PATH = "./files/decks";
    private static final String CARTAS_PATH = "./files/cartas";

    private static FileInputStream fileInStream;
    private static ObjectInputStream objInStream;
    private static FileOutputStream fileOutStream;
    private static ObjectOutputStream objOutStream;

    public static void writeCarta(Carta carta) {

        try {
            fileOutStream = new FileOutputStream(CARTAS_PATH + "/" + carta.getNome() + ".ser");
            objOutStream = new ObjectOutputStream(fileOutStream);

            if (carta instanceof CartaMonstro) {
                objOutStream.writeObject((CartaMonstro) carta);
            }
            if (carta instanceof CartaMagica) {
                objOutStream.writeObject((CartaMagica) carta);
            }

            objOutStream.close();
            fileOutStream.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void writeAllCartas(ListaDeCartas cartas) {

        for (int i = 0; i < cartas.length; i++) {
            writeCarta(cartas.getCartaAtIndex(i));
        }

    }

    public static Carta readCarta(String nome) throws InvalidClassException {

        try {
            fileInStream = new FileInputStream(CARTAS_PATH + "/" + nome + ".ser");
            objInStream = new ObjectInputStream(fileInStream);
            Carta cartaLida = (Carta) objInStream.readObject();

            fileInStream.close();
            objInStream.close();
            
            return cartaLida;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidClassException ex) {
            System.out.println(ex.getClass() + ": A carta não pode ser carregada, por ser de uma versão antiga incompatível");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static ListaDeCartas readAllCartas() throws InvalidClassException {

        /*
         * Salva em `cartaFiles[]` os nomes dos arquivos correspondentes aos 
         * objetos de Carta serializados e que, portanto, terminam com ".ser".
         */
        String cartaFiles[] = new File(CARTAS_PATH).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith(".ser");
            }
        });

        ListaDeCartas todasCartas = new ListaDeCartas(); //TODO SETAR O MAXIMO DE CARTAS QUANDO O O PACOTE DE PROJETO ESTIVER PRONTO
        Carta cartaLida;

        if (cartaFiles == null) {
            return todasCartas;
        }

        System.out.println("Numero de cartas: " + cartaFiles.length);

        try {
            for (String file : cartaFiles) {
                fileInStream = new FileInputStream(CARTAS_PATH + "/" + file);
                objInStream = new ObjectInputStream(fileInStream);
                
                cartaLida = (Carta) objInStream.readObject();
                todasCartas.addCarta(cartaLida);

                fileInStream.close();
                objInStream.close();

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidClassException ex) {
            System.out.println(ex.getClass() + ": Algumas cartas não puderam ser carregadas, por serem de uma versão antiga incompatível");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return todasCartas;
    }

    public static void writeDeck(Deck deck) {
        try {
            fileOutStream = new FileOutputStream(DECKS_PATH + "/" + deck.getNome() + ".ser");
            objOutStream = new ObjectOutputStream(fileOutStream);

            objOutStream.writeObject(deck);
            objOutStream.close();
            fileOutStream.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeAllDecks(List<Deck> decks) {

        decks.forEach((deck) -> {
            writeDeck(deck);
        });

    }

    public static Deck readDeck(String nome, ListaDeCartas todasAsCartas) throws InvalidClassException {
        String deckFiles[] = new File(DECKS_PATH).list();

        for (String nomeArq : deckFiles) {
            if (nomeArq.equals(nome)) {

                try {
                    fileInStream = new FileInputStream(DECKS_PATH + "/" + nomeArq);
                    objInStream = new ObjectInputStream(fileInStream);
                    Deck deckLido = (Deck) objInStream.readObject();

                    fileInStream.close();
                    objInStream.close();
                    return deckLido;

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidClassException ex) {
                    System.out.println(ex.getClass() + ": O Deck não pode ser carregado, por ser de uma versão antiga incompatível");
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return null;
    }

    public static List<Deck> readAllDecks(ListaDeCartas todasAsCartas) throws InvalidClassException {

        /*
         * Salva em `deckFiles[]` os nomes dos arquivos correspondentes aos 
         * objetos de Deck serializados e que, portanto, terminam com ".ser".
         */
        String deckFiles[] = new File(DECKS_PATH).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith(".ser");
            }

        });

        List<Deck> decksLidos = new ArrayList<>(deckFiles.length);
        try {
            for (String file : deckFiles) {
                fileInStream = new FileInputStream(DECKS_PATH + "/" + file);
                objInStream = new ObjectInputStream(fileInStream);
                Deck deck = (Deck) objInStream.readObject();

                decksLidos.add(deck);

                fileInStream.close();
                objInStream.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidClassException ex) {
            System.out.println(ex.getClass() + ": Alguns Deck não puderão ser carregados, por serem de uma versão antiga incompatível");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return decksLidos;
    }

    public static String getDecksPath() {
        return DECKS_PATH;
    }

    public static String getCartasPath() {
        return CARTAS_PATH;
    }

}
