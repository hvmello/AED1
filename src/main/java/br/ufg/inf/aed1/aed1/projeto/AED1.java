package br.ufg.inf.aed1.aed1.projeto;

import br.ufg.inf.aed1.aed1.carta.filemanager.FileManager;
import br.ufg.inf.aed1.aed1.carta.ListaDeCartas;
import br.ufg.inf.aed1.aed1.carta.*; 
import br.ufg.inf.aed1.aed1.gameplay.*; 
import br.ufg.inf.aed1.aed1.network.*; 
import br.ufg.inf.aed1.aed1.gui.utils.*; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;





public class AED1 {
    
    public static final int MAX_CARTAS = 100;
    public static  ListaDeCartas todasAsCartas = new ListaDeCartas(MAX_CARTAS);
    public static  ArrayList<CartaMagica> todasAsCartasMagicas = new ArrayList<CartaMagica>(MAX_CARTAS);
    public static  ArrayList<CartaMonstro> todasAsCartasMonstros = new ArrayList<CartaMonstro>(MAX_CARTAS);
    public static  ArrayList<Deck> todosOsDecks = new ArrayList<Deck>();
    public static Game game = new Game();
    private static final GameOnline gameOnline = new GameOnline();
    
    
  
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");        

        
        /******CARREGANDO CARTAS SALVAS********/

        System.out.println("Carregando todas as cartas");
        todasAsCartas = FileManager.readAllCartas();
        
        for(int i=0;i<todasAsCartas.length;i++){        //imprime todas as cartas carregadas
            Carta carta = todasAsCartas.getCartaAtIndex(i);
            if(carta instanceof CartaMonstro){
                CartaMonstro aux = (CartaMonstro)carta;
                System.out.println(aux.getNome() +" "+aux.getSet()+"-"+aux.getId()+" "+aux.getATK()+"/"+aux.getDEF()+" "+aux.getImageSrc());
            }
            if(carta instanceof CartaMagica){
                CartaMagica aux = (CartaMagica)carta;
                System.out.println(aux.getNome() +" "+aux.getSet()+"-"+aux.getId()+" "+aux.getTipoEfeitoMagico()+"/"+" "+aux.getImageSrc());
            }
        }
        /*FIM*/
        
        
        /******CARREGANDO DECKS SALVOS********/
        System.out.println("Carregando todos os decks");
        todosOsDecks = FileManager.readAllDecks(todasAsCartas);
        
        
        for(int i=0;i<todosOsDecks.size();i++){
            Deck deck = todosOsDecks.get(i);
            System.out.println(deck.getNome());
            /* imprime todas as cartas do deck
           for(int j=0; j < deck.length; j++){
            Carta aux = deck.getCartaAtIndex(j);
            System.out.println(aux.getNome() +" "+aux.getSet()+"-"+aux.getId()+" "+aux.getImageSrc());
           }*/
        }
        /*FIM*/
        
        MenuJFrame.main(args);
        
        
        int input=0;
        do{
            int subInput;
            System.out.println("1. Novo Jogo");
            System.out.println("2. Carregar Perfil");
            System.out.println("3. Editar Baralhos");
            System.out.println("4. Opcões");
            System.out.println("0. Sair");
            //input = scanner.nextInt();
            
            switch(input){
                case 1:
                    do{
                        System.out.println("1. Jogar multiplayer Local");
                        System.out.println("2. Jogar multiplayer Online");
                        System.out.println("0. Voltar");
                        subInput = scanner.nextInt();
                        switch (subInput) {
                            case 1:
                                jogaLocal();
                                break;
                            case 2:
                                jogaOnline();
                                break;
                            case 0:

                        }
                    }while(subInput != 0);
                    
                    
                    
                break;
                
                case 2:
                break;
                case 3:
                    
                    /*aqui deve limpar a tela para abrir novo submenu*/
                    do{
                        System.out.println("1. Editar um baralho existente");
                        System.out.println("2. Criar novo baralho");
                        System.out.println("3. Criar nova carta Monstro");
                        System.out.println("4. Criar nova carta Mágica");
                        System.out.println("0. Voltar");
                        subInput = scanner.nextInt();
                        switch (subInput) {
                            case 1:

                                break;
                            case 2:
                                criaDeckTxt();
                                break;
                            case 3:
                                try{
                                    criaCartaMonstro();
                                }catch(Exception ex){
                                    System.err.println(ex);
                                }
                            break;
                            case 4:
                                try{
                                    criaCartaMagica();
                                }catch(Exception ex){
                                    System.err.println(ex);
                                }

                            case 0:

                        }
                    }while(subInput != 0);
                    
                break;
            }
            
        }while(input != 0);
        
        
        System.out.println("Salvando todas as cartas");
        FileManager.writeAllCartas(todasAsCartas);

        System.out.println("Salvando todas os decks");
        FileManager.writeAllDecks(todosOsDecks);
        
        System.out.println("num de cartas: " + todasAsCartas.length);
        System.out.println("num de decks: " + todosOsDecks.size());  
        
        /************fim*************/
        

        
        
        
        
        
/*********************TESTES*******************************/
        
        
        
        //CartaMagica cartaMagTeste = new CartaMagica("Fake Trap", "TEST", 1, Game.TipoTrap.Miss, "http://uploads2.yugioh.com/card_images/2092/detail/1380.jpg?1385102986");
        /*
        Game partida = new Game();
        CartaMagica cartaCampo = new CartaMagica("Fake Trap", "TEST", 1, Game.TipoCampo.Dark, "http://uploads2.yugioh.com/card_images/2092/detail/1380.jpg?1385102986");
        
        //partida.player1 = new Player(deck, "nome ");
        //partida.player1.mesa.putMagica(0, cartaCampo);
        cartaCampo.ativaEfeito(partida, null, -1);
        
        System.out.println(partida.campo);
        */
        
        /* testando conexão do server com cliente*/
/*
        ServidorTCP serverTest = new ServidorTCP();
        ClienteTCP clientTest = new ClienteTCP();
        
        
        if(scanner.nextInt() == 1){
            serverTest.initialize();
            String msgRecebida;
            do{
                msgRecebida = serverTest.readMessage();
                System.out.println("Mensagem recebida: "+msgRecebida);
                serverTest.sendMessage("respondendo a mensagem \""+msgRecebida+"\"");
            }while(!msgRecebida.equals("sair"));
            
            
            Carta testeCarta = (Carta)serverTest.readCarta();
            
            System.out.println(testeCarta);
            if(testeCarta instanceof CartaMonstro){
                CartaMonstro cartaMon = (CartaMonstro)testeCarta;
                System.out.println(testeCarta.getNome()+" " + cartaMon.getATK()+" "+cartaMon.getDEF()+" "+cartaMon.getSet()+"-"+cartaMon.getId()+" "+cartaMon.getTipo());
                System.out.println(cartaMon.getEstrelas() + " "+cartaMon.getImageSrc());
                System.out.println(cartaMon.getModoCarta()+" "+cartaMon.getDescricao());
            }
            
            if(testeCarta instanceof CartaMagica){
                CartaMagica cartaMag = (CartaMagica)testeCarta;
                System.out.println(cartaMag.getNome()+" "+cartaMag.getSet()+"-"+cartaMag.getId());
                System.out.println(cartaMag.getSubEfeitoStr()+" "+cartaMag.getImageSrc());
                System.out.println(cartaMag.getDescricao());
            }
            

            String temp = serverTest.readMessage();
            System.out.println(temp.getBytes().length);
            
            serverTest.close();
        }
        else{
            for(String ip:clientTest.getNetworkIPs()){
                System.out.println(ip);
            }
            clientTest.connect("192.168.25.10");
            String msg;
            scanner.nextLine();
            do{
                msg = scanner.nextLine();
                clientTest.sendMessage(msg);
                System.out.println("msg enviada");
                System.out.println("Resposta: "+clientTest.readMessage());
            }while(!msg.equals("sair"));
            
            
            clientTest.sendCarta(todasAsCartas.getCartaAtIndex(1));
            Carta testeCarta = todasAsCartas.getCartaAtIndex(1);
            if(testeCarta instanceof CartaMonstro){
                CartaMonstro cartaMon = (CartaMonstro)testeCarta;
                System.out.println(testeCarta.getNome()+" " + cartaMon.getATK()+" "+cartaMon.getDEF()+" "+cartaMon.getSet()+"-"+cartaMon.getId()+" "+cartaMon.getTipo());
                System.out.println(cartaMon.getEstrelas() + " "+cartaMon.getImageSrc());
                System.out.println(cartaMon.getModoCarta()+" "+cartaMon.getDescricao());
            }
            
            if(testeCarta instanceof CartaMagica){
                CartaMagica cartaMag = (CartaMagica)testeCarta;
                System.out.println(cartaMag.getNome()+" "+cartaMag.getSet()+"-"+cartaMag.getId());
                System.out.println(cartaMag.getSubEfeitoStr()+" "+cartaMag.getImageSrc());
                System.out.println(cartaMag.getDescricao());
            }
            
            byte byteTest[] = {3,4};
            clientTest.sendMessage( new String(byteTest));
            
            
            clientTest.close();
        }

    */    

    }
    
    public static void jogaLocal(){
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");        

        DeckJogavel deckP1,deckP2;
        int escolha,i=0;

        System.out.println("Jogador 1, escolha o deck que deseja jogar:");
        for(Deck deck : todosOsDecks){
            System.out.println(i+". "+deck.getNome());
            i++;
        }
        escolha = scanner.nextInt();
        deckP1 = new DeckJogavel(todosOsDecks.get(escolha));



        System.out.println("Jogador 2, escolha o deck que deseja jogar:");
        i=0;
        for(Deck deck : todosOsDecks){
            System.out.println(i+". "+deck.getNome());
            i++;
        }
        escolha = scanner.nextInt();
        deckP2 = new DeckJogavel(todosOsDecks.get(escolha));

        System.out.println("iniciando partida");
        game.jogaPartida(deckP1, deckP2);
    }
    
    public static void jogaOnline() throws IOException, ClassNotFoundException{
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        Player playerVencedor=null;
        
        
        System.out.println("1. Criar Partida");
        System.out.println("2. Conectar-se a uma Partida");
        System.out.println("0. voltar");
        switch(scanner.nextInt()){
            case 1: 
                playerVencedor = jogaOnlineHost();
        
            break;
            case 2:
                playerVencedor = jogaOnlineGuest();

            break;
            case 0:
                
        }
        
        if(playerVencedor != null){
            System.out.println(" O jogador "+playerVencedor.nickName+" venceu!!!");
        }
        
    }
    
    public static Player jogaOnlineHost() throws ClassNotFoundException{
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        DeckJogavel deckP1;
        Player playerVencedor=null;
        int escolha,i=0;
        
        ServidorTCP partidaHost = new ServidorTCP();
        
        try {
            System.out.println("Aguardando conexão...(lim "+ServidorTCP.TIME_OUT/1000 +"seg)");
            partidaHost.initialize();
        }catch (java.io.InterruptedIOException e) {
            //System.err.println( "Timed Out ("+ServidorTCP.TimeOut+" ms)!" );
            JOptionPane.showMessageDialog(null, "Tempo exedido ("+ServidorTCP.TIME_OUT+" ms)!", "Erro de conexão", JOptionPane.WARNING_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
            try {
                partidaHost.close();
            } catch (IOException ex) {
                Logger.getLogger(AED1.class.getName()).log(Level.SEVERE, null, ex);
            }
              return null;
        }catch (IOException ex) {
            System.err.println( ex.getClass()+"Erro! "+ex.getCause() );
        }

        System.out.println("Escolha o deck que deseja jogar:");
        for(Deck deck : todosOsDecks){
            System.out.println(i+". "+deck.getNome());
            i++;
        }
        escolha = scanner.nextInt();
        deckP1 = new DeckJogavel(todosOsDecks.get(escolha));
        try{
            playerVencedor = gameOnline.jogaPartida(deckP1, partidaHost);
        }catch(IOException ex){
            System.err.println( "Conexão perdida! " );
        }
        try {
            partidaHost.close();
        } catch (IOException ex) {
            Logger.getLogger(AED1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return playerVencedor;
        
    }
    
    public static Player jogaOnlineGuest(){
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        DeckJogavel deckP1;
        Player playerVencedor=null;
        int escolha,i=0;
        
        
        
                ClienteTCP partidaGuest = new ClienteTCP();
                System.out.println("Digite o ip do Host da partida");
                for(String ip:partidaGuest.getNetworkIP()){
                    System.out.println(ip+" foi encontrado na rede");
                }
                try {
                    partidaGuest.connect(scanner.nextLine());
                } catch (IOException ex) {
                    System.err.println( "Esse IP não possui um socket aguardando conexão! " );
                    return null ;
                }
                
                System.out.println("Escolha o deck que deseja jogar:");
                for(Deck deck : todosOsDecks){
                    System.out.println(i+". "+deck.getNome());
                    i++;
                }
                escolha = scanner.nextInt();
                deckP1 = new DeckJogavel(todosOsDecks.get(escolha));
                try{
                    playerVencedor = gameOnline.jogaPartida(deckP1, partidaGuest);
                }catch(IOException ex){
                    System.err.println( "Conexão perdida! " );
                } catch (ClassNotFoundException ex) {
            Logger.getLogger(AED1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            partidaGuest.close();
        } catch (IOException ex) {
            Logger.getLogger(AED1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return playerVencedor;
    }
    
    
    public static void criaCartaMonstro() throws Exception{
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        String nome, tipoMonstro, set, path, tipoAtributo, descricao;
        int atk, def, id;
        
        System.out.print("Nome: ");   nome = scanner.nextLine();
        System.out.print("Set: ");    set = scanner.next();
        System.out.print("Descricao: ");    descricao = scanner.next();
        
        do{
            System.out.print("Id:  ");    id = scanner.nextInt();
            ListaDeCartas cartasDoSet = todasAsCartas.getSetCartas(set);
            if(cartasDoSet.length > 0 && cartasDoSet.getCartaById(id) != null) 
                throw new Exception("Já existe uma carta com esse ID neste set");
            else    
                break;
        }while(true);
            
        System.out.print("Atk: ");    atk = scanner.nextInt();
        System.out.print("Def: ");    def = scanner.nextInt();
        scanner.nextLine();//limpando o buffer
        System.out.print("Tipo Monstro: ");   tipoMonstro = scanner.nextLine(); 
        System.out.print("Tipo Atributo: ");   tipoAtributo = scanner.nextLine();
        System.out.print("Url da Imagem: "); path= scanner.nextLine();
        
        todasAsCartas.addCarta(new CartaMonstro(id, set, nome, descricao, atk, def, 
                CartaMonstro.TipoMonstro.valueOf(tipoMonstro), CartaMonstro.TipoAtributo.valueOf(tipoAtributo), path)); 
    }
    
   
    
    public static void criaCartaMagica() throws Exception{
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        String nome, tipoMagicaStr,tipoSubMagicaStr, set, path, descricao;
     
        int id;
        
        System.out.print("Nome: ");   nome = scanner.nextLine();
        System.out.print("Set: ");    set = scanner.next();
        
        do{
            System.out.print("Id:  ");    id = scanner.nextInt();
            ListaDeCartas cartasDoSet = todasAsCartas.getSetCartas(set);
            if(cartasDoSet.length > 0 && cartasDoSet.getCartaById(id) != null) 
                throw new Exception("Já existe uma carta com esse ID neste set");
            else    
                break;
        }while(true);
            
        System.out.print("Tipo de carta magica: "); 
        scanner.nextLine();//limpando o buffer
        tipoMagicaStr = scanner.nextLine();
        if(tipoMagicaStr.equals("Campo"))   System.out.print("Tipo do Campo: "); 
        if(tipoMagicaStr.equals("Trap"))    System.out.print("Tipo da Trap: ");
        tipoSubMagicaStr = scanner.nextLine();
        
        System.out.println("Decricao: "); descricao = scanner.nextLine();
        System.out.print("Url da Imagem: "); path= scanner.nextLine();
        
        if(tipoMagicaStr.equals( CartaMagica.TipoEfeitoMagico.CAMPO.name()) )
            todasAsCartas.addCarta(new CartaMagica(Game.TipoCampo.valueOf(tipoSubMagicaStr), id, set, nome, path, ""));
        if(tipoMagicaStr.equals( CartaMagica.TipoEfeitoMagico.TRAP.name()) )
            todasAsCartas.addCarta(new CartaMagica(Game.TipoTrap.valueOf(tipoSubMagicaStr), id, set, nome, path, ""));
    }
    
    //User story criar deck - 2º sprint
    public static void criaDeckTxt(){
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        String nome;
        Deck deck;
        int idCarta, tipoCarta;

        System.out.print("Nome: ");   nome = scanner.nextLine();
        for(Deck deckIt: todosOsDecks){
            if(deckIt.getNome().equals(nome)){
                System.err.println("Esse Deck já existe!");
                return;
            }
        }
        deck = new Deck(nome);

        for( int i=0; i< Deck.DECK_SIZE;i++){
            Carta carta;
            CartaMagica cartaMag;
            CartaMonstro cartaMon;
            
            //if(scanner.nextInt() == 0){
                System.out.printf(" SET- ID   ATK  DEF  - Nome\n");
                //System.out.println(" SET-ID    Tipo - Efeito ");
                for(int j=0; j<todasAsCartas.length; j++){
                    carta = todasAsCartas.getCartaAtIndex(j);
                    if(carta instanceof CartaMonstro){
                        cartaMon = (CartaMonstro)carta;
                        System.out.printf("%4s-%3d   %4d %4d - %s\n", cartaMon.getSet(), cartaMon.getId(), cartaMon.getATK(), cartaMon.getDEF(), cartaMon.getNome() );
                    }
                    if(carta instanceof CartaMagica){
                        cartaMag = (CartaMagica)carta;
                        System.out.printf("%4s-%3d   %5s - %s\n", cartaMag.getSet(), cartaMag.getId(), cartaMag.getTipoEfeitoMagico(), cartaMag.getNome() );
                    }
                }

                System.out.println("Informe o id das cartas que deseja adicionar ao deck");
                idCarta = scanner.nextInt();
                carta = todasAsCartas.getCartaById(idCarta);
                if(carta != null){
                    deck.addCarta(carta);
                    System.out.println(carta.getNome()+" adicionado(a)");
                }else{
                    System.out.println("Id de carta não existe!");
                    i--;
                }

        }
        
        todosOsDecks.add(deck);
        
        System.out.println("Deck "+deck.getNome()+" criado com sucesso!");
    }
    
    

    
    public void editaDeck(Deck deck) throws Exception{
        if(true) throw new UnsupportedOperationException("Not supported yet.");
        /*
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        Carta carta;
        int index;
        
        do{
            System.out.printf(" ID   ATK  DEF  - Nome\n");
            for(int j=0; j<todasAsCartas.length; j++){
                carta = todasAsCartas.getCartaAtIndex(j);
                System.out.printf("%3d   %4d %4d - %s\n", carta.getId(), carta.getATK(), carta.getDEF(), carta.getNome() );            
            }
            System.out.println("Digite o indice da carta do deck "+ deck.getNome() +"que deseja trocar por outra (-1 PARA SAIR)");
            index = scanner.nextInt();
            
            
        }while(index != -1);
        */
    }
    
}