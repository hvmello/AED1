package br.ufg.inf.aed1.aed1.gameplay;

import br.ufg.inf.aed1.aed1.carta.DeckJogavel;

import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import gui.utils.AdvancedBevelBorder;
import java.awt.Color;
import javax.swing.JLabel;

public class Player {
    public Mesa mesa; 
    public MesaJLabel mesaJLabel;
    public Mao mao; 
    public MaoJLabel maoJLabel;
    private JLabel caixaHP;
    
    public DeckJogavel deck;
    public String nickName="";
    
    public final static int WIN = 1, DEFEAT = 2, DRAW = 0, ATACK_CANCELED = -1;

    public int HP = 8000;

    public Player(DeckJogavel deck, String nickName){
            this.deck = deck;
            this.mao = new Mao();
            this.mesa = new Mesa();
            this.mesaJLabel = new MesaJLabel();
            this.maoJLabel = new MaoJLabel();
            this.nickName = nickName;
    }
    
    public Player(String nickName){
            this.mesa = new Mesa();
            this.nickName = nickName;
    }
    
    public void updateMaoIcon(int index){
        maoJLabel.getLabels()[index].setIcon(mao.cartas[index]);
    }
    public void updateMonstroIcon(int index){
        mesaJLabel.putMonstro(index, mesa.cartasMonstros[index]);
    }
    public void updateMagicaIcon(int index){
        mesaJLabel.putMagica(index, mesa.cartasMagicas[index]);
    }
    public void updateCaixaHP(){
        AdvancedBevelBorder borda = (AdvancedBevelBorder) caixaHP.getBorder();
        caixaHP.setText( String.format("<html><pre>%s<br/>HP: %4d</pre></html>", nickName, HP) );
        //caixaHP.setText("<html><div style=\"border-width: "+borda.getBorderWidth()+"px;\">"+nickName+"<br/>  HP: "+HP+"</div></html>");
    }
    public void switchCaixaHP( ){
        if(caixaHP.getBackground() == corCaixaEscura){
            AdvancedBevelBorder caixaTemp = (AdvancedBevelBorder) caixaHP.getBorder();
            caixaTemp.swapBorderColors();
            caixaTemp.setTopColor(caixaTemp.getTopColor().brighter());
            caixaTemp.setBottomColor(caixaTemp.getBottomColor().brighter());
            caixaTemp.setLeftColor(caixaTemp.getLeftColor().brighter());
            caixaTemp.setRightColor(caixaTemp.getRightColor().brighter());
            
            caixaHP.setBackground(corCaixaOriginal);            
        }else{
            AdvancedBevelBorder caixaTemp = (AdvancedBevelBorder) caixaHP.getBorder();
            caixaTemp.swapBorderColors();
            caixaTemp.setTopColor(caixaTemp.getTopColor().darker());
            caixaTemp.setBottomColor(caixaTemp.getBottomColor().darker());
            caixaTemp.setLeftColor(caixaTemp.getLeftColor().darker());
            caixaTemp.setRightColor(caixaTemp.getRightColor().darker());
            
            caixaHP.setBackground(corCaixaEscura);        
        }
        caixaHP.repaint();
    }

    
    //pega carta da mão, deleta ela(da mao)  e retorna ela
    public Carta pick(int index){
        Carta cartaEscolhida = mao.cartas[index];
        mao.deleteCartaAtIndex(index);
        return cartaEscolhida;
    }
    
    
    //ataca a carta alvo com a carta selecionada, retorna o resultado de quem ganhou
    public int ataca(int cartaAtacanteIndex,Player playerAlvo, int cartaAlvoIndex){
        CartaMonstro cartaAlvo = playerAlvo.mesa.cartasMonstros[cartaAlvoIndex];
        CartaMonstro cartaAtacante = this.mesa.cartasMonstros[cartaAtacanteIndex];
        
        int resultado;
        
        //A carta atacante deve ser revelada
        if(cartaAtacante.getModoCarta().isAtaqueBaixo() || cartaAtacante.getModoCarta().isDefesaBaixo()){
            cartaAtacante.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
        }

        if(playerAlvo.mesa.isCartasMonstrosEmpty()){
            playerAlvo.HP-= cartaAtacante.getATK();
            return WIN;
        }
        
        //A carta atacada tb deve ser revelada
       
        if(cartaAlvo.getModoCarta().isAtaqueBaixo()) cartaAlvo.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
        if(cartaAlvo.getModoCarta().isDefesaBaixo()) cartaAlvo.setModoCarta(CartaMonstro.ModoCarta.DEFESA_PARA_CIMA);
      
        
        //se carta atacada em modo de ataque
        if(cartaAlvo.getModoCarta().isAtaqueBaixo() || cartaAlvo.getModoCarta().isAtaqueCima()){
            int diferenca = cartaAtacante.getATK() - cartaAlvo.getATK();
            if(diferenca >= 0){     //caso o carta atacante ganhe
                int index;
                
                //verifica se tem trap antes, (ps: só é ativada quando o atacado vai perder, não há sentido em gastar a trap quando ele fosse ganhar)
                if(( index = playerAlvo.mesa.TrapMagicIndex())>= 0){
                    playerAlvo.mesa.cartasMagicas[index].ativaEfeito(null, this, cartaAtacanteIndex);
                    playerAlvo.mesa.removeMagica(index);
                    return ATACK_CANCELED;     
                }
                
                playerAlvo.HP -= diferenca;
                playerAlvo.mesa.removeMonstro(cartaAlvoIndex);
                System.out.println(cartaAtacante.getNome()+" destruiu a "+cartaAlvo.getNome()+" inimiga, dando "+diferenca+" de dano ao HP");
                resultado = WIN;
                //quando o ataque é o mesmo, ambas cartas sao destruidas
                if(diferenca==0){
                    this.mesa.removeMonstro(cartaAtacanteIndex);
                    System.out.println(cartaAtacante.getNome()+" foi destruida pela "+cartaAlvo.getNome()+" inimiga, recebendo "+diferenca+" de dano ao HP");
                    resultado = DRAW;
                }
            }
            else{                   //caso o carta atacante perca
                this.HP += diferenca;
                this.mesa.removeMonstro(cartaAtacanteIndex);
                System.out.println(cartaAtacante.getNome()+" foi destruida pela "+cartaAlvo.getNome()+" inimiga, recebendo "+ (-diferenca) +" de dano ao proprio HP");
                resultado = DEFEAT;
            }

        }
        else{
            int diferenca = cartaAtacante.getATK() - cartaAlvo.getDEF();
            if(diferenca >= 0){     //caso o carta atacante ganhe
                int index;
                
                //verifica se tem trap antes, (ps: só é ativada quando o atacado vai perder, não há sentido em gastar a trap quando ele fosse ganhar)
                if(( index = playerAlvo.mesa.TrapMagicIndex())>= 0){
                    playerAlvo.mesa.cartasMagicas[index].ativaEfeito(null, this, cartaAtacanteIndex);
                    playerAlvo.mesa.removeMagica(index);
                    return ATACK_CANCELED;
                }
                
                playerAlvo.mesa.removeMonstro(cartaAlvoIndex);
                System.out.println(cartaAtacante.getNome()+" destruiu a "+cartaAlvo.getNome()+" inimiga, dando nada de dano ao HP");
                resultado = WIN;
            }
            else{                   //caso o carta atacante perca
                this.HP += diferenca;
                this.mesa.removeMonstro(cartaAtacanteIndex);
                System.out.println(cartaAtacante.getNome()+" foi destruida pela "+cartaAlvo.getNome()+" inimiga, recebendo "+ (-diferenca) +" de dano ao proprio HP");
                resultado = DEFEAT;
            }
        }
        
        return resultado;
        
    }

    /**
     * Compra uma carta do topo do deck se tiver espaço na mão, caso contrário não faz nada
     * @return Carta comprada
     */
    public Carta draw() {
        if(mao.isFull()) return null;
        
        Carta topo = deck.getCartaAtIndex(deck.length - 1);
        mao.addCarta(topo);
        deck.deleteCartaAtIndex(deck.length - 1);
        //updateMaoIcon(mao.indexOf(topo));
        System.out.println("carta Sacada: "+topo);
        return topo;
    }
    // inicializa as coisas do jogador, embaralhando o deck e comprando 5 cartas 
    public void inicializar(){
        deck.embaralha();
        for(int i=0; i< Mao.HAND_SIZE; i++){
            //System.out.println("sacando uma carta");
            draw(); //saca uma carta do deck, adicionando-a à mão e removendo do topo do deck.
        }
    }
    
    public boolean perdeu(){
        return (HP <= 0);
    }


    public void setMao(Mao mao){ this.mao = mao;}
    public Mao getMao(){ return mao;}
    public void setDeck(DeckJogavel deck){ this.deck = deck;}
    public DeckJogavel getDeckJogavel(){ return deck;}
    public int getHP(){ return HP;}
    public JLabel getCaixaHP() { return caixaHP; }
    public void setCaixaHP(JLabel caixaHP) { 
        this.caixaHP = caixaHP; 
        corCaixaOriginal = caixaHP.getBackground();
        corCaixaEscura = corCaixaOriginal.darker();
        if(caixaHP.getBorder() != null && caixaHP.getBorder() instanceof AdvancedBevelBorder){
            AdvancedBevelBorder caixaTemp = (AdvancedBevelBorder) caixaHP.getBorder();
            corCaixaTop = caixaTemp.getTopColor();
            corCaixaBottom = caixaTemp.getBottomColor();
            corCaixaLeft = caixaTemp.getLeftColor();
            corCaixaRight = caixaTemp.getRightColor();
        }
    }
    
    Color corCaixaOriginal, corCaixaEscura;
    Color corCaixaTop, corCaixaBottom, corCaixaLeft, corCaixaRight;

}
