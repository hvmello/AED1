package br.ufg.inf.aed1.aed1.gameplay;

import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.CartaMagica;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import br.ufg.inf.aed1.aed1.carta.Deck;
import br.ufg.inf.aed1.aed1.carta.DeckJogavel;
import java.util.Arrays;
import java.util.Scanner;


public class Game {
    

    public static enum TipoCampo { 
        NORMAL(0), FLOREST(1), OCEAN(2), MOUNTAIN(3), DESERT(4);
        
        private final int value;
        private TipoCampo(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public static enum TipoTrap { 
        MISS(0), COUNTER(1);
        
        private final int value;
        private TipoTrap(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    public TipoCampo campo = TipoCampo.NORMAL;
    //player main e inimigo respectivamente
    public Player player1, player2;
    public Carta todasCartas[];    //todas as cartas da partida (juncao dos dois decks dos jogadores)
    
    public int turno;
    
    protected final Scanner scanner = new Scanner(System.in);

    public void inicializa(DeckJogavel deckP1, DeckJogavel deckP2){
        player1 = new Player(deckP1, "Player 1");
        player2 = new Player(deckP2, "Player 2");
        
        todasCartas = new Carta[Deck.DECK_SIZE*2];   //vai possui as cartas dos dois decks dos jogadores
        System.arraycopy(player1.deck.getCartasArray(), 0, todasCartas, 0, Deck.DECK_SIZE);
        System.arraycopy(player2.deck.getCartasArray(), 0, todasCartas, Deck.DECK_SIZE, Deck.DECK_SIZE);

        player1.inicializar();
        player2.inicializar();
        
        
        turno =0;
    }

    
    public Player jogaPartida(DeckJogavel deckP1, DeckJogavel deckP2){

        inicializa(deckP1, deckP2);

        while(true){
            
            if(player1.perdeu()) break;
            turno++;
            _jogaUmTurno(player1, player2);
            
            if(player2.perdeu()) break;
            turno++;
            _jogaUmTurno(player2, player1);         

            player1.draw();
            player2.draw();
            
        }
        
        if(player1.perdeu()) return player2;
        else                return player1;
        
    }
    
    
    
    
    private void _jogaUmTurno(Player playerMain, Player playerInimigo){   
        
        
        _jogaEstado_mao(playerMain, playerInimigo);
        
        //caso não seja o primeiro turno ( nao pode atacar no primeiro turno, pois assim seria possivel atacar diretamente sem o outro jogador ainda sequer ter jogado.
        if(turno >1 )
            _jogaEstado_mesa(playerMain, playerInimigo);
        
        System.out.println("Fim do turno");

    }
    
    
    
                /********** Escolhendo a carta**************/
    protected Struct_MovimentoMao _jogaEstado_mao(Player playerMain, Player playerInimigo){
        Carta cartaP1;
        CartaMonstro cartaMon=null;
        CartaMagica cartaMag=null;
        int cartaIndex;
        
        System.out.println("HP "+ playerMain.nickName + " : "+playerMain.HP + "          HP "+playerInimigo.nickName +": "+playerInimigo.HP);
        System.out.println("Jogador "+ playerMain.nickName + ", Escolha uma carta da mao para colocar na mesa (-1 para nao colocar):");

        imprimeCartas(playerMain.mao.getCartasArray(), Mao.HAND_SIZE, false, false);
        
        cartaIndex = scanner.nextInt();
        if(cartaIndex == -1){
            return new Struct_MovimentoMao(null,cartaIndex);
        }
        cartaP1 = playerMain.pick(cartaIndex);
        System.out.println("carta escolhida: "+cartaP1.getNome());
        
        //Escolhendo modo da carta
        if(cartaP1 != null && cartaP1 instanceof CartaMonstro){
            cartaMon = (CartaMonstro)cartaP1;
            System.out.println("digite 'c' para virada para cima ou 'b' para virada para baixo");
            String modo;
            scanner.nextLine(); modo = scanner.next();
            if(modo.contains("c")) cartaMon.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
            else cartaMon.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_BAIXO);
        }
        
        //Escolhendo onde colocar a carta
        System.out.println("Escolha onde deseja colocar a carta (colocar em cima de uma carta irá exclui-la)");
        if(cartaP1 instanceof CartaMonstro){
            imprimeCartas(playerMain.mesa.cartasMonstros, Mesa.MESA_SIZE, true, false);
            cartaIndex = scanner.nextInt();
            playerMain.mesa.putMonstro(cartaIndex, cartaMon);
        }
        
        if(cartaP1 instanceof CartaMagica){
            cartaMag = (CartaMagica)cartaP1;
            imprimeCartas(playerMain.mesa.cartasMagicas, Mesa.MESA_SIZE, false, false);
            cartaIndex = scanner.nextInt();
            playerMain.mesa.putMagica(cartaIndex, cartaMag);
        }

        return new Struct_MovimentoMao(cartaP1,cartaIndex);
    }
    
    
        /********** atacando carta inimiga ( ou diretamente o inimigo)***************/
    private void _jogaEstado_mesa(Player playerMain, Player playerInimigo) {
        Carta cartaP1;
        CartaMonstro cartaMon;
        CartaMagica cartaMag;
        
        boolean cartasQueAtacaram[] = new boolean[playerMain.mesa.MESA_SIZE];
        int cartaIndex;
        
        Arrays.fill(cartasQueAtacaram, false);
        
        /* o jogador ainda quiser atacar (se possivel) ou mudar a carta para modo defesa-ataque */
        while(true){
            System.out.println("Jogador "+ playerMain.nickName + ", Escolha uma carta da sua mesa que deseja usar ( -1 para finalizar o turno )");
            for(int i=0 ; i < Mesa.MESA_SIZE;i++){
                cartaMon = playerMain.mesa.cartasMonstros[i];
                
                if( cartaMon != null){ 
                    System.out.print("    "+i+". "+cartaMon.getNome()+"  "+cartaMon.getATK()+"/"+cartaMon.getDEF());
                    if(cartaMon.getModoCarta().isAtaqueBaixo() || cartaMon.getModoCarta().isAtaqueCima()){
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
            for(int i=0;i<Mesa.MESA_SIZE;i++){
                cartaMag = playerMain.mesa.cartasMagicas[i];
                if(cartaMag != null){
                    System.out.println("    "+(i+Mesa.MESA_SIZE)+". "+cartaMag.getNome()+"  "+cartaMag.getTipoEfeitoMagico());
                }else{
                    System.out.println("    "+(i+Mesa.MESA_SIZE)+" . ...");
                }
            }
            
            cartaIndex = scanner.nextInt();
            if(cartaIndex == -1 ) break;
            
            //usando as cartas magicas
            if(cartaIndex >= Mesa.MESA_SIZE){
                cartaIndex -= Mesa.MESA_SIZE;
                cartaMag = playerMain.mesa.cartasMagicas[cartaIndex];
                if(cartaMag.getTipoEfeitoMagico() == CartaMagica.TipoEfeitoMagico.CAMPO){
                    cartaMag.aplicarEfeito(this, playerMain, -1);
                    playerMain.mesa.removeMagica(cartaIndex);
                }else{
                    System.out.println("Essa carta Magica não é ativavel manualmente!");
                }
                continue;
            }
            
            if(cartasQueAtacaram[cartaIndex] == false){
                cartaMon = playerMain.mesa.cartasMonstros[cartaIndex];
                System.out.println("Digite 1 para usa-la para atacar e 0 para mudar seu modo defesa-ataque");
                if(scanner.nextInt() == 1){
                    if(cartaMon.getModoCarta().isAtaqueBaixo() || cartaMon.getModoCarta().isAtaqueCima()){
                        _jogaEstado_atacaMesaInimiga(playerMain, playerInimigo, cartaIndex);
                        cartasQueAtacaram[cartaIndex]=true;
                        if(playerMain.perdeu() || playerInimigo.perdeu()) break;
                    }else{
                        System.out.println("A carta deve estar em modo de ataque para atacar!");
                    }
                }else{
                    giraCarta(cartaMon);
                }
            }else{
                System.out.println("Carta já atacou! escolha outra ou finalize o turno.");
            }

        }
        
    }
    
    public void giraCarta(CartaMonstro cartaMonstro){
        cartaMonstro.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
    }
    
    //retorna o indice da carta inimiga atacada
    protected int _jogaEstado_atacaMesaInimiga(Player playerMain, Player playerInimigo, int cartaEscolhidaAtacanteIndex){
        Carta cartaP2 = null;
        int cartaIndex;

        System.out.println("Jogador "+ playerMain.nickName + ", Escolha uma carta da mesa inimiga que deseja atacar");

        imprimeCartas(playerInimigo.mesa.cartasMonstros,Mesa.MESA_SIZE, true, true);
        
        cartaIndex = scanner.nextInt();
        if(playerInimigo.mesa.isCartasMonstrosEmpty())
            System.out.println("Ataque direto!");
        else
            cartaP2 = playerInimigo.mesa.cartasMonstros[cartaIndex];
        
        System.out.println("carta escolhida: "+cartaP2);

        playerMain.ataca(cartaEscolhidaAtacanteIndex, playerInimigo, cartaIndex);
        
        return cartaIndex;
    }
    

    private void imprimeCartas(Carta cartas[], int length, boolean showCartaMode, boolean showCartaParaBaixo){
        int i=0;
        Carta carta;
        CartaMagica cartaMag;
        CartaMonstro cartaMon;
        for( i =0; i<cartas.length;i++){
            carta = cartas[i];
            if(carta == null){
                System.out.println("    "+i+". ...");
                continue;
            }
            
            if(carta instanceof CartaMonstro){
                cartaMon = (CartaMonstro) carta;
                if(showCartaParaBaixo && (cartaMon.getModoCarta().isAtaqueBaixo() || cartaMon.getModoCarta().isDefesaBaixo()))
                    System.out.print("    "+i+". ???");
                else
                    System.out.print("    "+i+". "+carta.getNome()+"  "+cartaMon.getATK()+"/"+cartaMon.getDEF());

                if(showCartaMode){
                    if(cartaMon.getModoCarta().isAtaqueBaixo() || cartaMon.getModoCarta().isAtaqueCima())
                        System.out.print("   - A");
                    else
                        System.out.print("   - D");
                }
                System.out.println("");
            }
            if(carta instanceof CartaMagica){
                cartaMag = (CartaMagica) carta;
                if(showCartaParaBaixo)
                    System.out.println("    "+i+". ???");
                else
                    System.out.println("    "+i+". "+carta.getNome()+"  "+cartaMag.getTipoEfeitoMagico());
            }

        }
        for( ; i<length;i++){
            System.out.println("    "+i+". ...");
        }
    }
    
}

class Struct_MovimentoMao{
    public Carta carta;
    public int index;

    public Struct_MovimentoMao(Carta carta, int index) {
        this.carta = carta;
        this.index = index;
    }
    public Struct_MovimentoMao(){}
}
