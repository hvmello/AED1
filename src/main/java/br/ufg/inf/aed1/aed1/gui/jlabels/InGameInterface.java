/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.aed1.aed1.gui.jlabels;

import br.ufg.inf.aed1.aed1.carta.*;
import br.ufg.inf.aed1.aed1.gameplay.*;
import br.ufg.inf.aed1.aed1.gui.mouse.*;
import br.ufg.inf.aed1.aed1.gameplay.Mesa;

import com.towel.swing.img.JImagePanel;
import gui.utils.AdvancedBevelBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
//import javafx.animation.RotateTransition;

/**
 *
 * @author Heitor
 */
public class InGameInterface extends JFrame {

    public static Dimension dimensaoCarta = new Dimension(176, 246);
    public static Dimension dimensaoCartaMao = new Dimension(294, 428);

    JImagePanel panelAtual, panelInimigo;
    JFrame frame = new JFrame();
    PainelCartaInfo miniPainel;
    Game gameBackEnd;

    public InGameInterface(Game game) {

        gameBackEnd = game;

        Dimension dimensaoTela = new Dimension(1757, 1030);
        System.out.println(System.getProperty("user.dir"));
        File backGroundImage1 = new File("./src/Interface/Pics/Mesa_1030x1757.png");
        File backGroundImage2 = new File("./src/Interface/Pics/MesaInvertida_1030x1757.png");

        try {
            panelAtual = new JImagePanel(backGroundImage1);
            panelInimigo = new JImagePanel(backGroundImage2);
        } catch (IOException ex) {
            Logger.getLogger(InGameInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        frame.setPreferredSize(dimensaoTela);
        frame.setMinimumSize(dimensaoTela);
        frame.setMaximumSize(dimensaoTela);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /*
        BufferedImage imgEscalada = toBufferedImage(panel.getImage().getScaledInstance(dimensaoTela.width, dimensaoTela.height, Image.SCALE_SMOOTH));
        panel.setImage(imgEscalada);
         */
        panelAtual.setBackground(Color.BLACK);
        panelAtual.setFillType(JImagePanel.FillType.CENTER);
        //panel.setLayout(new GroupLayout(panel));
        panelAtual.setLayout(null);
        panelAtual.setSize(dimensaoTela);

        panelInimigo.setBackground(Color.black);
        panelInimigo.setFillType(JImagePanel.FillType.CENTER);
        panelInimigo.setLayout(null);
        panelInimigo.setSize(dimensaoTela);

        inputListener.setBounds(0, 0, 0, 0);
        frame.add(inputListener);
        frame.add(panelAtual);
        //frame.add(panelInimigo);

        frame.setResizable(false);
        InicializaLabels();
    }

    public void start() {
        frame.pack();
        for (int i = 0; i < Mao.HAND_SIZE; i++) {
            gameBackEnd.player1.updateMaoIcon(i);
            gameBackEnd.player2.updateMaoIcon(i);
        }
        gameBackEnd.player1.updateCaixaHP();
        gameBackEnd.player2.updateCaixaHP();
        gameBackEnd.player2.switchCaixaHP();    //como começa com o player 1, "vira"/coloca a caixa de hp "afundada"
        playerAtual = gameBackEnd.player1;
        playerInimigo = gameBackEnd.player2;
        vetorLabelAtual = playerAtual.maoJLabel.cartas;
        mudaEstadoJogo(EstadoJogo.Mao);

        frame.setVisible(true);

    }

    public void InicializaLabels() {

        int alturaCarta = 246, larguraCarta = 176;
        int posxPrimeiraCarta_MesaBaixo = 257;
        int posCartaPassada_MesaBaixo;

        posCartaPassada_MesaBaixo = posxPrimeiraCarta_MesaBaixo;
        for (int i = 0; i < Mesa.MESA_SIZE; i++) {
            gameBackEnd.player1.mesaJLabel.cartasMonstros[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player1.mesaJLabel.cartasMonstros[i].setOpaque(true);
            gameBackEnd.player1.mesaJLabel.cartasMonstros[i].setBackground(Color.green);
            gameBackEnd.player1.mesaJLabel.cartasMonstros[i].setBounds(posCartaPassada_MesaBaixo, 238, 246, 246);
            gameBackEnd.player1.mesaJLabel.cartasMonstros[i].setPosInicial(new Point(posCartaPassada_MesaBaixo, 238));

            panelAtual.add(gameBackEnd.player1.mesaJLabel.cartasMonstros[i]);

            gameBackEnd.player2.mesaJLabel.cartasMonstros[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player2.mesaJLabel.cartasMonstros[i].setOpaque(true);
            gameBackEnd.player2.mesaJLabel.cartasMonstros[i].setBackground(Color.green);
            gameBackEnd.player2.mesaJLabel.cartasMonstros[i].setBounds(posCartaPassada_MesaBaixo, 518, 246, 246);
            gameBackEnd.player2.mesaJLabel.cartasMonstros[i].setPosInicial(new Point(posCartaPassada_MesaBaixo, 518));

            panelInimigo.add(gameBackEnd.player2.mesaJLabel.cartasMonstros[i]);

            posCartaPassada_MesaBaixo = posCartaPassada_MesaBaixo + 2 + alturaCarta;    //2 é a distancia entre um label e outro
        }

        posCartaPassada_MesaBaixo = posxPrimeiraCarta_MesaBaixo = 335;
        for (int i = 0; i < Mesa.MESA_SIZE; i++) {
            gameBackEnd.player1.mesaJLabel.cartasMagicas[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player1.mesaJLabel.cartasMagicas[i].setOpaque(true);
            gameBackEnd.player1.mesaJLabel.cartasMagicas[i].setBackground(Color.BLUE);
            gameBackEnd.player1.mesaJLabel.cartasMagicas[i].setBounds(posCartaPassada_MesaBaixo, 521, 176, 246);
            gameBackEnd.player1.mesaJLabel.cartasMagicas[i].setPosInicial(new Point(posCartaPassada_MesaBaixo, 521));

            panelAtual.add(gameBackEnd.player1.mesaJLabel.cartasMagicas[i]);

            gameBackEnd.player2.mesaJLabel.cartasMagicas[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player2.mesaJLabel.cartasMagicas[i].setOpaque(true);
            gameBackEnd.player2.mesaJLabel.cartasMagicas[i].setBackground(Color.BLUE);
            gameBackEnd.player2.mesaJLabel.cartasMagicas[i].setBounds(posCartaPassada_MesaBaixo + 1, 235, 176, 246);
            gameBackEnd.player2.mesaJLabel.cartasMagicas[i].setPosInicial(new Point(posCartaPassada_MesaBaixo + 1, 235));

            panelInimigo.add(gameBackEnd.player2.mesaJLabel.cartasMagicas[i]);

            posCartaPassada_MesaBaixo = posCartaPassada_MesaBaixo + 50 + larguraCarta;  //50 é a distancia entre um label e outro
        }

        posCartaPassada_MesaBaixo = posxPrimeiraCarta_MesaBaixo = 150;
        for (int i = 0; i < Mao.HAND_SIZE; i++) {
            gameBackEnd.player1.maoJLabel.cartas[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player1.maoJLabel.cartas[i].setOpaque(false);
            gameBackEnd.player1.maoJLabel.cartas[i].setBackground(Color.RED);
            gameBackEnd.player1.maoJLabel.cartas[i].setBounds(posCartaPassada_MesaBaixo, 780, 294, 428);
            gameBackEnd.player1.maoJLabel.cartas[i].setPosInicial(new Point(posCartaPassada_MesaBaixo, 780));

            panelAtual.add(gameBackEnd.player1.maoJLabel.cartas[i], 0);

            gameBackEnd.player2.maoJLabel.cartas[i] = new CartaJLabel((Icon) null, JLabel.CENTER);
            gameBackEnd.player2.maoJLabel.cartas[i].setOpaque(false);            //pode tirar essa linha depois, esta aqui apenas para testes
            gameBackEnd.player2.maoJLabel.cartas[i].setBackground(Color.RED);   //pode tirar essa linha depois, esta aqui apenas para testes
            gameBackEnd.player2.maoJLabel.cartas[i].setBounds(posCartaPassada_MesaBaixo, 780, 294, 428);
            gameBackEnd.player2.maoJLabel.cartas[i].setPosInicial(new Point(posCartaPassada_MesaBaixo, 780));
            //gameBackEnd.player2.maoJLabel.cartas[i].setEnabled(false);
            //gameBackEnd.player2.maoJLabel.cartas[i].setVisible(false);

            //panelInimigo.add(gameBackEnd.player2.maoJLabel.cartas[i] , 0);
            posCartaPassada_MesaBaixo = posCartaPassada_MesaBaixo + 6 + 294;    //6 é a distancia entre um label e outro
        }

        //criando o label que é a mini janela com infos da carta
        miniPainel = new PainelCartaInfo(378, 75, 1001, 100);
        MatteBorder borda = BorderFactory.createMatteBorder(12, 11, 9, 11, new ImageIcon("./src/Interface/Pics/minhaBorda_100x1001.png"));
        miniPainel.setBorder(borda);
        panelAtual.add(miniPainel);

        Border activeHpBoxBorder = new AdvancedBevelBorder(Color.red.darker(), 5);
        Border secondaryHpBoxBorder = new AdvancedBevelBorder(Color.blue.darker(), 5);

        JLabel caixaHP = new JLabel();
        caixaHP.setBorder(activeHpBoxBorder);
        caixaHP.setBounds(75, 50, 150, 75);
        caixaHP.setBackground(Color.red.darker());
        caixaHP.setOpaque(true);
        caixaHP.setVerticalAlignment(SwingConstants.CENTER);
        caixaHP.setHorizontalAlignment(SwingConstants.CENTER);
        caixaHP.setFont(caixaHP.getFont().deriveFont(Font.BOLD, 22.0f));
        caixaHP.setForeground(new Color(240, 240, 240));
        gameBackEnd.player1.setCaixaHP(caixaHP);
        panelAtual.add(caixaHP);

        JLabel caixaHP2 = new JLabel();
        caixaHP2.setBorder(secondaryHpBoxBorder);
        caixaHP2.setBounds(1532, 50, 150, 75);
        caixaHP2.setBackground(Color.blue.darker());
        caixaHP2.setOpaque(true);
        caixaHP2.setVerticalAlignment(SwingConstants.CENTER);
        caixaHP2.setHorizontalAlignment(SwingConstants.CENTER);
        caixaHP2.setFont(caixaHP2.getFont().deriveFont(Font.BOLD, 22.0f));
        caixaHP2.setForeground(new Color(240, 240, 240));
        gameBackEnd.player2.setCaixaHP(caixaHP2);
        panelAtual.add(caixaHP2);

        //mapeando a entrada do teclado
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), InputKeys.DOWN_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), InputKeys.UP_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), InputKeys.LEFT_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), InputKeys.RIGHT_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), InputKeys.SPACEBAR_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), InputKeys.ENTER_KEY);
        inputListener.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), InputKeys.ESC_KEY);

    }

    /**
     * ***********KEY BINDINGS**********
     */
    private static enum InputKeys {
        DOWN_KEY, UP_KEY, LEFT_KEY, RIGHT_KEY, SPACEBAR_KEY, ENTER_KEY, ESC_KEY
    }

    private Action leftKey_MaoAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou esquerda na mao " + indexCartaAtual);
            if (indexCartaAtual > 0) {
                CartaJLabel label = playerAtual.maoJLabel.cartas[indexCartaAtual];
                label.setLocation(label.getPosInicial());

                indexCartaAtual--;
                label = playerAtual.maoJLabel.cartas[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 200);
                miniPainel.setCartaSource(playerAtual.mao.getCartaAtIndex(indexCartaAtual));
            }
        }
    };
    private Action rightKey_MaoAction = new AbstractAction(InputKeys.RIGHT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou direta na mao " + indexCartaAtual);
            if (indexCartaAtual < Mao.HAND_SIZE - 1) {
                CartaJLabel label = playerAtual.maoJLabel.cartas[indexCartaAtual];
                label.setLocation(label.getPosInicial());

                indexCartaAtual++;
                label = playerAtual.maoJLabel.cartas[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 200);
                miniPainel.setCartaSource(playerAtual.mao.getCartaAtIndex(indexCartaAtual));
            }
        }
    };
    private Action enterKey_MaoAction = new AbstractAction(InputKeys.ENTER_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou enter na mao " + indexCartaAtual);

            cartaSelecionada = playerAtual.pick(indexCartaAtual);
            playerAtual.updateMaoIcon(indexCartaAtual);
            vetorLabelAtual[indexCartaAtual].setLocation(vetorLabelAtual[indexCartaAtual].getPosInicial());

            if (cartaSelecionada instanceof CartaMonstro) {
                ((CartaMonstro) cartaSelecionada).setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
                mudaEstadoJogo(EstadoJogo.Mao_Mesa1);
            } else if (cartaSelecionada instanceof CartaMagica) {
                mudaEstadoJogo(EstadoJogo.Mao_Mesa2);
            }
            return;
        }
    };

    private Action UpDownKeys_MaoMesaAction = new AbstractAction(InputKeys.UP_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou cima ou baixo na mao-mesa " + indexCartaAtual);

            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            CartaMonstro cartaMon = (CartaMonstro) cartaSelecionada;

            if (cartaMon.isParaBaixo()) {

                if (cartaMon.isModoAtaque()) {
                    cartaMon.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_CIMA);
                    label.setIcon(new RotatedIcon(
                            getRedimensionado(cartaMon.getImageSrc(), dimensaoCarta),
                            0.0
                    ));
                } else {
                    cartaMon.setModoCarta(CartaMonstro.ModoCarta.DEFESA_PARA_CIMA);
                    label.setIcon(new RotatedIcon(
                            getRedimensionado(cartaMon.getImageSrc(), dimensaoCarta),
                            -90.0
                    ));
                }

            } else {

                if (cartaMon.isModoAtaque()) {
                    cartaMon.setModoCarta(CartaMonstro.ModoCarta.ATAQUE_PARA_BAIXO);
                    label.setIcon(new RotatedIcon(
                            getRedimensionado("./src/Interface/Pics/cartaParaBaixo.png", dimensaoCarta),
                            0.0
                    ));
                } else {
                    cartaMon.setModoCarta(CartaMonstro.ModoCarta.DEFESA_PARA_BAIXO);
                    label.setIcon(new RotatedIcon(
                            getRedimensionado("./src/Interface/Pics/cartaParaBaixo.png", dimensaoCarta),
                            -90.0
                    ));
                }

            }

        }
    };
    private Action leftKey_MaoMesaAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou esquerda na mao-mesa " + indexCartaAtual);
            if (indexCartaAtual > 0) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                Icon iconeAtual = label.getIcon();

                label.setLocation(label.getPosInicial());
                label.setIcon(iconeAnterior);

                indexCartaAtual--;
                label = vetorLabelAtual[indexCartaAtual];
                iconeAnterior = label.getIcon();
                label.setIcon(iconeAtual);
                label.setLocation(label.getX() + 15, label.getY() - 15);
                if (cartaSelecionada instanceof CartaMagica) {
                    miniPainel.setCartaSource(playerAtual.mesa.cartasMagicas[indexCartaAtual]);
                }
                if (cartaSelecionada instanceof CartaMonstro) {
                    miniPainel.setCartaSource(playerAtual.mesa.cartasMonstros[indexCartaAtual]);
                }
            }
        }
    };
    private Action rightKey_MaoMesaAction = new AbstractAction(InputKeys.RIGHT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou direita na mao-mesa " + indexCartaAtual);
            if (indexCartaAtual < Mesa.MESA_SIZE - 1) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                Icon iconeAtual = label.getIcon();

                label.setLocation(label.getPosInicial());
                label.setIcon(iconeAnterior);

                indexCartaAtual++;
                label = vetorLabelAtual[indexCartaAtual];
                iconeAnterior = label.getIcon();
                label.setIcon(iconeAtual);
                label.setLocation(label.getX() + 15, label.getY() - 15);
                if (cartaSelecionada instanceof CartaMagica) {
                    miniPainel.setCartaSource(playerAtual.mesa.cartasMagicas[indexCartaAtual]);
                }
                if (cartaSelecionada instanceof CartaMonstro) {
                    miniPainel.setCartaSource(playerAtual.mesa.cartasMonstros[indexCartaAtual]);
                }
            }
        }
    };
    private Action spaceKey_MaoMesaAction = new AbstractAction(InputKeys.SPACEBAR_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou espaço na mao-mesa " + indexCartaAtual);
            if (!(cartaSelecionada instanceof CartaMonstro)) {
                return;
            }

            /*
            Rectangle rect = new Rectangle (100, 40, 100, 100);
            
            RotateTransition teste = new RotateTransition(Duration.seconds(1.0), rect);
             */
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            RotatedIcon iconeAtual = (RotatedIcon) label.getIcon();

            if (((CartaMonstro) cartaSelecionada).isModoAtaque()) {
                iconeAtual.setDegrees(-90.0);
            } else {
                iconeAtual.setDegrees(0.0);
            }

            ((CartaMonstro) cartaSelecionada).girarCarta();
            iconeAtual.paintIcon(label, label.getGraphics(), label.getX(), label.getY());
            label.repaint();

        }
    };
    private Action enterKey_MaoMesaAction = new AbstractAction(InputKeys.ENTER_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou enter na mao-mesa " + indexCartaAtual);

            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            label.setLocation(label.getPosInicial());

            if (cartaSelecionada instanceof CartaMonstro) {
                playerAtual.mesa.putMonstro(indexCartaAtual, (CartaMonstro) cartaSelecionada);
            } else if (cartaSelecionada instanceof CartaMagica) {
                playerAtual.mesa.putMagica(indexCartaAtual, (CartaMagica) cartaSelecionada);
            }

            miniPainel.setCartaSource(cartaSelecionada);

            mudaEstadoJogo(EstadoJogo.MesaMinha);
        }
    };

    private Action leftKey_MesaAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou esquerda na mesa " + indexCartaAtual);
            if (indexCartaAtual > 0) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getPosInicial());
                //label.setBorder(null);

                indexCartaAtual--;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                /*
                label.setBorder(BorderFactory.createMatteBorder(12, 12, 10, 12, 
                        getRedimensionado(new ImageIcon("./src/Interface/Pics/borda-carta.png"),dimensaoCarta)
                
                ));
                 */
                if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMonstros) {
                    cartaSelecionada = playerAtual.mesa.cartasMonstros[indexCartaAtual];
                } else if (vetorLabelAtual == playerInimigo.mesaJLabel.cartasMonstros) {
                    cartaSelecionada = playerInimigo.mesa.cartasMonstros[indexCartaAtual];
                } else {
                    cartaSelecionada = playerAtual.mesa.cartasMagicas[indexCartaAtual];
                }

                miniPainel.setCartaSource(cartaSelecionada);
            }
        }
    };
    private Action rightKey_MesaAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou direita na mesa");
            if (indexCartaAtual < Mao.HAND_SIZE - 1) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getPosInicial());
                //label.setBorder(null);
                indexCartaAtual++;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                /*
                label.setBorder(BorderFactory.createMatteBorder(12, 12, 10, 12, 
                        getRedimensionado(new ImageIcon("./src/Interface/Pics/borda-carta.png"),dimensaoCarta)
                
                ));
                 */
                if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMonstros) {
                    cartaSelecionada = playerAtual.mesa.cartasMonstros[indexCartaAtual];
                } else if (vetorLabelAtual == playerInimigo.mesaJLabel.cartasMonstros) {
                    cartaSelecionada = playerInimigo.mesa.cartasMonstros[indexCartaAtual];
                } else {
                    cartaSelecionada = playerAtual.mesa.cartasMagicas[indexCartaAtual];
                }

                miniPainel.setCartaSource(cartaSelecionada);
            }
        }
    };
    private Action upKey_MesaAction = new AbstractAction(InputKeys.UP_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou cima na mesa " + indexCartaAtual);
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];

            if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMonstros) {
                /*
                panel = panelInimigo
                vetorLabelAtual = playerInimigo.mesaJLabel.cartasMonstros;  //vai pra ver a mesa inimiga
                 */
                label.setLocation(label.getPosInicial());
                vetorLabelAtual = playerInimigo.mesaJLabel.cartasMonstros;

                frame.remove(panelAtual);
                frame.add(panelInimigo);
                panelInimigo.add(miniPainel);
                panelInimigo.add(playerAtual.getCaixaHP());
                panelInimigo.add(playerInimigo.getCaixaHP());
                frame.pack();
                frame.repaint();
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                cartaSelecionada = playerInimigo.mesa.cartasMonstros[indexCartaAtual];

            } else if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMagicas) {
                label.setLocation(label.getPosInicial());
                vetorLabelAtual = playerAtual.mesaJLabel.cartasMonstros;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                cartaSelecionada = playerAtual.mesa.cartasMonstros[indexCartaAtual];
            }
            miniPainel.setCartaSource(cartaSelecionada);
        }
    };
    private Action downKey_MesaAction = new AbstractAction(InputKeys.DOWN_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou baixo na mesa " + indexCartaAtual);
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];

            if (vetorLabelAtual == playerInimigo.mesaJLabel.cartasMonstros) {
                label.setLocation(label.getPosInicial());
                vetorLabelAtual = playerAtual.mesaJLabel.cartasMonstros;

                frame.remove(panelInimigo);
                frame.add(panelAtual);
                panelAtual.add(miniPainel);
                panelAtual.add(playerAtual.getCaixaHP());
                panelAtual.add(playerInimigo.getCaixaHP());
                frame.pack();
                frame.repaint();
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                cartaSelecionada = playerAtual.mesa.cartasMonstros[indexCartaAtual];

            } else if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMonstros) {
                label.setLocation(label.getPosInicial());
                vetorLabelAtual = playerAtual.mesaJLabel.cartasMagicas;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                cartaSelecionada = playerAtual.mesa.cartasMagicas[indexCartaAtual];
            }
            miniPainel.setCartaSource(cartaSelecionada);
        }
    };
    private Action enterKey_MesaAction = new AbstractAction(InputKeys.ENTER_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou enter na mesa " + indexCartaAtual);

            if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMonstros) {
                System.out.println("escolheu uma carta monstro");
                CartaMonstro cartaMon = playerAtual.mesa.cartasMonstros[indexCartaAtual];

                if (cartaMon == null || cartaMon.isModoDefesa() || cartasQueJaAtacaram[indexCartaAtual] == true || gameBackEnd.turno == 1) {
                    return;
                }

                cartaSelecionada = playerAtual.mesa.cartasMonstros[indexCartaAtual];
                indexCartaSelecionada = indexCartaAtual;
                mudaEstadoJogo(EstadoJogo.Atacando);

            } else if (vetorLabelAtual == playerAtual.mesaJLabel.cartasMagicas) {

                if (playerAtual.mesa.cartasMagicas[indexCartaAtual] == null || playerAtual.mesa.cartasMagicas[indexCartaAtual].getTipoEfeitoMagico() == CartaMagica.TipoEfeitoMagico.TRAP) {
                    return;
                }
                cartaSelecionada = playerAtual.mesa.cartasMagicas[indexCartaAtual];
                //tentar colocar uma animação de ativando o campo seria massa

                ((CartaMagica) cartaSelecionada).ativarEfeito(gameBackEnd, null, -1);
                System.out.println("Campo alterado para " + ((CartaMagica) cartaSelecionada).getSubEfeitoStr());
                playerAtual.mesa.removeMagica(indexCartaAtual);
                playerAtual.updateMagicaIcon(indexCartaAtual);

            }
        }
    };
    private Action escKey_MesaAction = new AbstractAction(InputKeys.ESC_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou ESC na mesa " + indexCartaAtual);
            vetorLabelAtual[indexCartaAtual].setLocation(vetorLabelAtual[indexCartaAtual].getPosInicial());
            mudaEstadoJogo(EstadoJogo.ProximoTurno);

        }
    };

    private Action leftKey_AtacandoAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou esquerda na mesa inimiga " + indexCartaAtual);
            if (indexCartaAtual > 0) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getPosInicial());

                indexCartaAtual--;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                miniPainel.setCartaSource(playerInimigo.mesa.cartasMonstros[indexCartaAtual]);
            }
        }
    };
    private Action rightKey_AtacandoAction = new AbstractAction(InputKeys.LEFT_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou direita na mesa inimiga");
            if (indexCartaAtual < Mao.HAND_SIZE - 1) {
                CartaJLabel label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getPosInicial());

                indexCartaAtual++;
                label = vetorLabelAtual[indexCartaAtual];
                label.setLocation(label.getX(), label.getY() - 30);
                miniPainel.setCartaSource(playerInimigo.mesa.cartasMonstros[indexCartaAtual]);
            }
        }
    };
    private Action enterKey_AtacandoAction = new AbstractAction(InputKeys.ENTER_KEY.name()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("apertou enter na mesa inimiga " + indexCartaAtual);

            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            label.setLocation(label.getPosInicial());

            int resultado = playerAtual.ataca(indexCartaSelecionada, playerInimigo, indexCartaAtual);

            cartasQueJaAtacaram[indexCartaSelecionada] = true;
            playerAtual.mesaJLabel.cartasMonstros[indexCartaSelecionada].setDisabledIcon(loadGrayScaled(cartaSelecionada.getImageSrc(), dimensaoCarta.width, dimensaoCarta.height));
            playerAtual.mesaJLabel.cartasMonstros[indexCartaSelecionada].setEnabled(false);

            if (resultado == Player.WIN || resultado == Player.DRAW) {
                playerInimigo.updateMonstroIcon(indexCartaAtual);
                if (resultado == Player.DRAW) {
                    playerAtual.updateMonstroIcon(indexCartaSelecionada);
                }
            } else if (resultado == Player.DEFEAT) {
                playerAtual.updateMonstroIcon(indexCartaSelecionada);
            }

            playerInimigo.updateCaixaHP();
            playerAtual.updateCaixaHP();

            //volta para a mesa
            frame.remove(panelInimigo);
            frame.add(panelAtual);
            frame.pack();
            frame.repaint();
            mudaEstadoJogo(EstadoJogo.MesaMinha);
        }
    };

    /**
     * ************fim**********
     */
    Carta cartaSelecionada;
    int indexCartaAtual = 2;   //indice da carta/label que está nesse exato momento
    int indexCartaSelecionada;
    Icon iconeAnterior; //usado para manter o icone que já estava num label, quando estamos escolhendo onde colocar a carta
    boolean cartasQueJaAtacaram[] = new boolean[Mesa.MESA_SIZE];

    Player playerAtual, playerInimigo;
    CartaJLabel[] vetorLabelAtual;
    JComponent inputListener = new JComponent() {
    };

    private enum EstadoJogo {
        Mao, Mao_Mesa1, Mao_Mesa2, MesaMinha, Atacando, ProximoTurno
    }
    EstadoJogo estadoAtual;

    private void mudaEstadoJogo(EstadoJogo novoEstado) {
        System.out.println("mudando estado de " + estadoAtual + " para " + novoEstado);

        inputListener.getActionMap().clear();

        if (novoEstado == EstadoJogo.Mao) {
            gameBackEnd.turno++;
            vetorLabelAtual = playerAtual.maoJLabel.cartas;

            inputListener.getActionMap().put(InputKeys.LEFT_KEY, leftKey_MaoAction);
            inputListener.getActionMap().put(InputKeys.RIGHT_KEY, rightKey_MaoAction);
            inputListener.getActionMap().put(InputKeys.ENTER_KEY, enterKey_MaoAction);
        } else if (novoEstado == EstadoJogo.Mao_Mesa1) {
            vetorLabelAtual = playerAtual.mesaJLabel.cartasMonstros;
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            RotatedIcon iconeRotacionavel = new RotatedIcon(getRedimensionado(cartaSelecionada.getImageSrc(), dimensaoCarta), RotatedIcon.Rotate.ABOUT_CENTER);

            iconeAnterior = label.getIcon();
            label.setIcon(iconeRotacionavel);
            label.setLocation(label.getX() + 15, label.getY() - 15);

            inputListener.getActionMap().put(InputKeys.LEFT_KEY, leftKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.RIGHT_KEY, rightKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.UP_KEY, UpDownKeys_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.DOWN_KEY, UpDownKeys_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.SPACEBAR_KEY, spaceKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.ENTER_KEY, enterKey_MaoMesaAction);
        } else if (novoEstado == EstadoJogo.Mao_Mesa2) {
            vetorLabelAtual = playerAtual.mesaJLabel.cartasMagicas;
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];

            iconeAnterior = label.getIcon();
            label.setIcon(getRedimensionado(cartaSelecionada.getImageSrc(), dimensaoCarta));
            label.setLocation(label.getX() + 15, label.getY() - 15);

            inputListener.getActionMap().put(InputKeys.LEFT_KEY, leftKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.RIGHT_KEY, rightKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.ENTER_KEY, enterKey_MaoMesaAction);
        } else if (novoEstado == EstadoJogo.MesaMinha) {
            vetorLabelAtual = playerAtual.mesaJLabel.cartasMonstros;

            inputListener.getActionMap().put(InputKeys.LEFT_KEY, leftKey_MesaAction);
            inputListener.getActionMap().put(InputKeys.RIGHT_KEY, rightKey_MesaAction);
            inputListener.getActionMap().put(InputKeys.UP_KEY, upKey_MesaAction);
            inputListener.getActionMap().put(InputKeys.DOWN_KEY, downKey_MesaAction);
            inputListener.getActionMap().put(InputKeys.SPACEBAR_KEY, spaceKey_MaoMesaAction);
            inputListener.getActionMap().put(InputKeys.ENTER_KEY, enterKey_MesaAction);
            inputListener.getActionMap().put(InputKeys.ESC_KEY, escKey_MesaAction);

            panelAtual.add(miniPainel);
            panelAtual.add(playerAtual.getCaixaHP());
            panelAtual.add(playerInimigo.getCaixaHP());

            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            label.setLocation(label.getX(), label.getY() - 30);
        } else if (novoEstado == EstadoJogo.Atacando) {

            inputListener.getActionMap().put(InputKeys.LEFT_KEY, leftKey_AtacandoAction);
            inputListener.getActionMap().put(InputKeys.RIGHT_KEY, rightKey_AtacandoAction);
            inputListener.getActionMap().put(InputKeys.ENTER_KEY, enterKey_AtacandoAction);

            vetorLabelAtual = playerInimigo.mesaJLabel.cartasMonstros;
            CartaJLabel label = vetorLabelAtual[indexCartaAtual];
            label.setLocation(label.getX(), label.getY() - 30);

            panelInimigo.add(miniPainel);
            panelInimigo.add(playerAtual.getCaixaHP());
            panelInimigo.add(playerInimigo.getCaixaHP());

            frame.remove(panelAtual);
            frame.add(panelInimigo);
            frame.pack();
            frame.repaint();
        } else if (novoEstado == EstadoJogo.ProximoTurno) {

            playerAtual.updateMaoIcon(playerAtual.mao.indexOf(playerAtual.draw()));

            for (int i = 0; i < Mesa.MESA_SIZE; i++) {
                playerAtual.mesaJLabel.cartasMonstros[i].setEnabled(true);
            }
            cartasQueJaAtacaram = new boolean[Mesa.MESA_SIZE];

            //troca a referencias dos players
            trocaLabels();
            playerAtual.switchCaixaHP();
            playerInimigo.switchCaixaHP();

            frame.repaint();

            Object aux = playerAtual;
            playerAtual = playerInimigo;
            playerInimigo = (Player) aux;

            mudaEstadoJogo(EstadoJogo.Mao);
            estadoAtual = novoEstado;
            return; //para nao sobrescrever o estado com o valor ProximoTurno
        }

        estadoAtual = novoEstado;
    }

    /**
     * mantém os labels e suas posições nos dois frames troca os CartaJLabel dos
     * dois jogadores e então só atualiza os icones
     */
    private void trocaLabels() {

        //trocando os labels das maos e das mesas
        Object aux = playerAtual.mesaJLabel;
        playerAtual.mesaJLabel = playerInimigo.mesaJLabel;
        playerInimigo.mesaJLabel = (MesaJLabel) aux;

        aux = playerAtual.maoJLabel;
        playerAtual.maoJLabel = playerInimigo.maoJLabel;
        playerInimigo.maoJLabel = (MaoJLabel) aux;

        //colocando os novos icones corretos nos labels
        for (int i = 0; i < Mesa.MESA_SIZE; i++) {
            Icon icone = playerAtual.maoJLabel.cartas[i].getIcon();
            playerAtual.maoJLabel.cartas[i].setIcon(playerInimigo.maoJLabel.cartas[i].getIcon());
            playerInimigo.maoJLabel.cartas[i].setIcon(icone);

            icone = playerAtual.mesaJLabel.cartasMonstros[i].getIcon();
            playerAtual.mesaJLabel.cartasMonstros[i].setIcon(playerInimigo.mesaJLabel.cartasMonstros[i].getIcon());
            playerInimigo.mesaJLabel.cartasMonstros[i].setIcon(icone);

            icone = playerAtual.mesaJLabel.cartasMagicas[i].getIcon();
            playerAtual.mesaJLabel.cartasMagicas[i].setIcon(playerInimigo.mesaJLabel.cartasMagicas[i].getIcon());
            playerInimigo.mesaJLabel.cartasMagicas[i].setIcon(icone);
        }

    }

}
