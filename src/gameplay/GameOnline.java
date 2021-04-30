
package gameplay;

import cartapackage.Carta;
import cartapackage.CartaMagica;
import cartapackage.CartaMonstro;
import cartapackage.Deck;
import cartapackage.DeckJogavel;
import networkpackage.ClienteTCP;
import networkpackage.ServidorTCP;
import java.io.IOException;
import java.util.Arrays;



public class GameOnline extends Game {
    
    private boolean isGameHost;
    ServidorTCP gameHost = null;
    ClienteTCP gameGuest = null;
    
    class Struct_movimentosInimigo{
        Struct_MovimentoMao jogadaMaoInimiga;
        byte listaComandos[];

        public Struct_movimentosInimigo(Carta carta, int indexCarta, byte[] listaComandos) {
            this.jogadaMaoInimiga = new Struct_MovimentoMao(carta, indexCarta);
            this.listaComandos = listaComandos;
        }
        public Struct_movimentosInimigo(){
            this.jogadaMaoInimiga = new Struct_MovimentoMao();
        }
    }
    
    
    public Player jogaPartida(DeckJogavel deckP1, ServidorTCP gameHost) throws IOException, ClassNotFoundException{
        isGameHost = true;
        this.gameHost = gameHost;
        
        Struct_movimentosInimigo movimentosInimigo = new Struct_movimentosInimigo();
        
        
        player1 = new Player(deckP1, "Player 1");//você
        player2 = new Player("Player 2");       //oponente
        todasCartas = new Carta[Deck.deckSize + Mesa.mesaSize];   //vai possui as cartas do seu deck + as cartas monstro da mesa do inimigo
        System.arraycopy(player1.deck.getCartasArray(), 0, todasCartas, 0, Deck.deckSize);

        player1.inicializar();
        turno =0;
        while(true){
            /*hora do player 1 (você) jogar*/
            if(player1.perdeu()) break;
            turno++;
            _jogaUmTurno(player1, player2);     //_jogaUmTurno já envia os pacotes com os movimentos e ações do jogador
            /*fim*/
            
            /*hora do player 2 (oponente) jogar*/
            if(player2.perdeu()) break;
            turno++;
            System.out.println("Esperando turno do oponente");
            //recebendo jogadas/movimentos do outro jogador
            Carta carta = gameHost.readCarta();
            int cartaIndex = Integer.parseInt(gameHost.readMessage());
            byte[] listaComandos = gameHost.readMessage().getBytes();
            
            movimentosInimigo.jogadaMaoInimiga.carta = carta;
            movimentosInimigo.jogadaMaoInimiga.index = cartaIndex;
            movimentosInimigo.listaComandos = listaComandos;
            
            _aplicaMovimentosDoInimigo(player1, player2, movimentosInimigo);
            /*fim*/
            

            player1.draw();
        }
        if(player1.perdeu()) return player2;
        else                return player1;
        
    }
        
    public Player jogaPartida(DeckJogavel deckP1, ClienteTCP gameGuest) throws IOException, ClassNotFoundException{
        isGameHost = false;
        this.gameGuest = gameGuest;
        
        Struct_movimentosInimigo movimentosInimigo = new Struct_movimentosInimigo();
        
        
        player1 = new Player(deckP1, "Player 2");//você
        player2 = new Player("Player 1");       //oponente
        todasCartas = new Carta[Deck.deckSize + Mesa.mesaSize];   //vai possui as cartas do seu deck + as cartas monstro da mesa do inimigo
        System.arraycopy(player1.deck.getCartasArray(), 0, todasCartas, 0, Deck.deckSize);
        
        player1.inicializar();
        turno =0;
        while(true){
            /*hora do player 2 (oponente) jogar*/
            
            if(player2.perdeu()) break;
            turno++;
            
            System.out.println("Esperando turno do oponente");
            //recebendo jogadas/movimentos do outro jogadorr
            Carta carta = gameGuest.readCarta();
            int cartaIndex = Integer.parseInt(gameGuest.readMessage());
            byte[] listaComandos = gameGuest.readMessage().getBytes();
            
            movimentosInimigo.jogadaMaoInimiga.carta = carta;
            movimentosInimigo.jogadaMaoInimiga.index = cartaIndex;
            movimentosInimigo.listaComandos = listaComandos;
            
            _aplicaMovimentosDoInimigo(player1, player2, movimentosInimigo);
            /*fim*/
            
            
            /*hora do player 1 (voce) jogar*/
            if(player1.perdeu()) break;
            turno++;
            _jogaUmTurno(player1, player2);     //_jogaUmTurno já envia os pacotes com os movimentos e ações do jogador
            /*fim*/

            player1.draw();
        }
        
        if(player1.perdeu()) return player2;
        else                return player1;
        
    }


    private void _jogaUmTurno(Player playerMain, Player playerInimigo) throws IOException{
        Struct_MovimentoMao jogadaMao;
        byte listaComandos[];
        
            jogadaMao = _jogaEstado_mao(playerMain, playerInimigo);
            if(isGameHost){
                gameHost.sendCarta(jogadaMao.carta);
                gameHost.sendMessage(String.valueOf(jogadaMao.index));
            }else{
                gameGuest.sendCarta(jogadaMao.carta);
                gameGuest.sendMessage(String.valueOf(jogadaMao.index));
            }
                
            //caso não seja o primeiro turno ( nao pode atacar no primeiro turno, pois assim seria possivel atacar diretamente sem o outro jogador ainda sequer ter jogado.
            if(turno >1 ){
                listaComandos = _jogaEstado_mesa(playerMain, playerInimigo);
                if(isGameHost){
                    gameHost.sendMessage( new String(listaComandos) );
                }else{
                    gameGuest.sendMessage( new String(listaComandos) );
                }
            }else{
                gameHost.sendMessage( new String(new byte[0]) );
            }

        
        System.out.println("Fim do turno");    
    }
    
    //retorna uma lista dos comandos realizados
    private byte[] _jogaEstado_mesa(Player playerMain, Player playerInimigo) {
        CartaMonstro carta;
        CartaMagica cartaMag;

        /*para cada carta da mesa, pode ter um movimento, onde cada byte o representa: 
            1° byte (byte[3*indiceDomovimento]) = indica o indice da carta da mesa a qual corresponde(sendo de 0-4 para monstros e 5-9 para magias )
            2° byte (byte[3*indiceDomovimento + 1]) = indica qual ação foi feita a tal carta: 0-> nada; 1-> ataque; 2-> girou carta ( para defesa ou ataque)
            3° byte (byte[3*indiceDomovimento +2]) = indica qual carta do inimigo atacou, ou para qual modo girou a carta (0 defesa, 1 ataque)
        
        nota: em caso de magia, o 2° e 3° bytes não são relevantes/levados em conta.
        */
        byte comandosFeitos[] = new byte[25 * 3];    //os comandos(até 25) são armazenados na ordem que são realizados
        int totalDeComandos = 0;
        
        boolean cartasQueAtacaram[] = new boolean[playerMain.mesa.mesaSize];
        int cartaIndex;
        
        Arrays.fill(cartasQueAtacaram, false);
        
        /* o jogador ainda quiser atacar (se possivel) ou mudar a carta para modo defesa-ataque */
        while(true){
            System.out.println("Jogador "+ playerMain.nickName + ", Escolha uma carta da sua mesa que deseja usar ( -1 para finalizar o turno )");
            for(int i=0 ; i<Mesa.mesaSize;i++){
                carta = playerMain.mesa.cartasMonstros[i];
                
                if( carta != null){ 
                    System.out.print("    "+i+". "+carta.getNome()+"  "+carta.getATK()+"/"+carta.getDEF());
                    if(carta.modoCarta == Ataque_paraBaixo || carta.modoCarta == Ataque_paraCima){
                        System.out.print("   - A");
                    }else{
                        System.out.print("   - D");
                    }
                    if(cartasQueAtacaram[i])
                        System.out.print(" -  X");
                    
                    System.out.println("");
                }
                else{
                    System.out.println("    "+i+". ...");
                }
            }
            
            for(int i=0;i<Mesa.mesaSize;i++){
                cartaMag = playerMain.mesa.cartasMagicas[i];
                if(cartaMag != null){
                    System.out.println("    "+(i+Mesa.mesaSize)+". "+cartaMag.getNome()+"  "+cartaMag.getTipoEfeito()+"-"+cartaMag.getSubEfeitoStr());
                }else{
                    System.out.println("    "+(i+Mesa.mesaSize)+" . ...");
                }
            }
            
            cartaIndex = scanner.nextInt();
            if(cartaIndex == -1 ) break;
            
            //usando as cartas magicas
            if(cartaIndex >= Mesa.mesaSize){
                cartaIndex -= Mesa.mesaSize;
                cartaMag = playerMain.mesa.cartasMagicas[cartaIndex];
                if(cartaMag.getTipoEfeito() == CartaMagica.TipoEfeito.Campo){
                    cartaMag.ativaEfeito(this, playerMain, -1);
                    playerMain.mesa.removeMagica(cartaIndex);
                    
                    comandosFeitos[totalDeComandos*3 + 0] = (byte)(cartaIndex + Mesa.mesaSize);
                    totalDeComandos++;
                }else{
                    System.out.println("Essa carta Magica não é ativavel manualmente!");
                }
                
                continue;
            }
            
            if(cartasQueAtacaram[cartaIndex] == false){
                carta = playerMain.mesa.cartasMonstros[cartaIndex];
                System.out.println("Digite 1 para usa-la para atacar e 0 para mudar seu modo defesa-ataque");
                if(scanner.nextInt() == 1){
                    if(carta.isModoAtaque()){
                        int cartaInimigaIndex = _jogaEstado_atacaMesaInimiga(playerMain, playerInimigo, cartaIndex);
                        cartasQueAtacaram[cartaIndex]=true;
                        
                        comandosFeitos[totalDeComandos*3 ] = (byte)cartaIndex;
                        comandosFeitos[totalDeComandos*3 +1] = 1;
                        comandosFeitos[totalDeComandos*3 +2] = (byte)cartaInimigaIndex;
                        totalDeComandos++;
                        
                        if(playerMain.perdeu() || playerInimigo.perdeu()) break;
                        
                    }else{
                        System.out.println("A carta deve estar em modo de ataque para atacar!");
                    }
                }else{
                    carta.giraCarta();
                    comandosFeitos[totalDeComandos*3 ] = (byte)cartaIndex;
                    comandosFeitos[totalDeComandos*3 +1] = 2;
                    comandosFeitos[totalDeComandos*3 +2] = 0;
                    totalDeComandos++;
                }
            }else{
                System.out.println("Carta já atacou! escolha outra ou finalize o turno.");
            }
            
        }
        
        //cria novo array de bytes apenas com o tamanho necessário 
        byte retorno[] = new byte[totalDeComandos*3];
        java.lang.System.arraycopy(comandosFeitos, 0, retorno, 0, totalDeComandos*3);
        System.out.println("tamanho da lista de comandos: "+retorno.length );
        for(byte b: retorno){
            System.out.print(b+" ");
        }
        return retorno;
    }
    
    private void _aplicaMovimentosDoInimigo(Player playerMain, Player playerInimigo, Struct_movimentosInimigo movimentosInimigo) {
        CartaMonstro cartaMon; CartaMagica cartaMag;
        Carta cartaInimiga = (Carta) movimentosInimigo.jogadaMaoInimiga.carta;
        int cartaIndex = movimentosInimigo.jogadaMaoInimiga.index;
        byte listaComandos[] = movimentosInimigo.listaComandos;
        
        if(cartaInimiga instanceof CartaMonstro){
            cartaMon = (CartaMonstro) cartaInimiga;
            
                /*********aplica a escolha da carta para colocar na mesa*********/
            if(cartaMon != null && cartaIndex != -1){
                System.out.println("inimigo colocou carta Monstro "+cartaMon.getNome()+" na posição "+cartaIndex);
                playerInimigo.mesa.cartasMonstros[cartaIndex] = cartaMon;
                this.todasCartas[Deck.deckSize + cartaIndex] = cartaMon;
            }
        }
        if(cartaInimiga instanceof CartaMagica){
            cartaMag = (CartaMagica) cartaInimiga;
            
                /*********aplica a escolha da carta para colocar na mesa*********/
            if(cartaMag != null && cartaIndex != -1){
                System.out.println("inimigo colocou carta Magica "+cartaMag.getNome()+" na posição "+cartaIndex);
                playerInimigo.mesa.cartasMagicas[cartaIndex] = cartaMag;
            }
        }
        

        
        System.out.print("Array de byte recebido dos comandos: ");
        for(byte b: listaComandos){
            System.out.print( b+" ");
        }
        System.out.println("");
        
        
        
        /************aplica os movimentos/ações feitos pelo jogador na mesma ordem realizada**********/
        for(int i=0; i < listaComandos.length/3; i++){
            int indiceDaCartaUsada, indiceDaCartaAtacada;
            indiceDaCartaUsada = listaComandos[i*3];
            
            //carta Magica
            if(indiceDaCartaUsada >= Mesa.mesaSize){
                indiceDaCartaUsada -= Mesa.mesaSize;
                System.out.print("Inimigo escolheu a carta "+playerInimigo.mesa.cartasMagicas[indiceDaCartaUsada]+" da pos "+indiceDaCartaUsada);
                playerInimigo.mesa.cartasMagicas[indiceDaCartaUsada].ativaEfeito(this, playerMain, -1);
                playerInimigo.mesa.removeMagica(indiceDaCartaUsada);
                continue;
            }
            
            System.out.print("Inimigo escolheu a carta "+playerInimigo.mesa.cartasMonstros[indiceDaCartaUsada]+" da pos "+indiceDaCartaUsada);
            //comando foi de ataque
            if(listaComandos[i*3 +1] == 1){
                indiceDaCartaAtacada = listaComandos[i*3 +2];
                System.out.println(" para atacar a sua carta da pos "+indiceDaCartaAtacada);
                playerInimigo.ataca(indiceDaCartaUsada, playerMain, indiceDaCartaAtacada);
            }
            if(listaComandos[i*3 +1] == 2){
                System.out.print(" para girar do modo "+playerInimigo.mesa.cartasMonstros[indiceDaCartaUsada].modoCarta);
                playerInimigo.mesa.cartasMonstros[indiceDaCartaUsada].giraCarta();
                System.out.println(" para "+playerInimigo.mesa.cartasMonstros[indiceDaCartaUsada].modoCarta);
            }
        }
        
    }
    
}



