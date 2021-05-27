package br.ufg.inf.aed1.aed1.carta;

import br.ufg.inf.aed1.aed1.gameplay.Game;
import br.ufg.inf.aed1.aed1.gameplay.Player;
import br.ufg.inf.aed1.aed1.projeto.GetCardImage;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartaMagica extends Carta implements Serializable {

    public static enum TipoEfeitoMagico {
        CAMPO(0), TRAP(1);

        private final int value;

        private TipoEfeitoMagico(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private TipoEfeitoMagico tipoEfeitoMagico;
    private Game.TipoCampo tipoCampo;
    private Game.TipoTrap tipoTrap;

    public interface EfeitoInterface {

        void aplicarEfeito(Game game, Player targetPlayer, int targetCartaIndex);
    }

    private final Map<TipoEfeitoMagico, EfeitoInterface> mapEfeitos = new EnumMap<>(TipoEfeitoMagico.class);

    public CartaMagica(Game.TipoCampo tipoCampo, int id, String set, String nome, String descricao, String imageSrc) {
        super(id, set, nome, descricao);
        this.tipoEfeitoMagico = TipoEfeitoMagico.CAMPO;
        this.tipoCampo = tipoCampo;
        this.configurarEfeitos();

        String imgSrcFinal;

        try {
            imgSrcFinal = GetCardImage.httpGetImage(this, imageSrc);
            this.setImageSrc(imgSrcFinal);
        } catch (Exception exception) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

    public CartaMagica(Game.TipoTrap tipoTrap, int id, String set, String nome, String descricao, String imageSrc) {
        super(id, set, nome, descricao);
        this.tipoEfeitoMagico = TipoEfeitoMagico.TRAP;
        this.tipoTrap = tipoTrap;
        this.configurarEfeitos();

        try {

            this.setImageSrc(GetCardImage.httpGetImage(this, imageSrc));
        } catch (Exception exception) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    public CartaMagica(String nome, String set, int id, Game.TipoCampo efeito, String imgPath, String descricao) {
        super(id, set, nome, descricao);
        this.tipoEfeitoMagico = TipoEfeitoMagico.CAMPO;
        this.tipoCampo = efeito;
        this.configurarEfeitos();

        try {
            String temp;
            temp = GetCardImage.httpGetImage(this, imgPath);
            this.setImageSrc(temp);
        } catch (Exception ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Erro ao gerar imagem!");
        }
    }

    public CartaMagica(String nome, String set, int id, Game.TipoTrap efeito, String imgPath, String descricao) {
        super(id, set, nome, descricao);
        this.tipoEfeitoMagico = TipoEfeitoMagico.TRAP;
        this.tipoTrap = efeito;
        this.configurarEfeitos();

        try {
            String temp;
            temp = GetCardImage.httpGetImage(this, imgPath);
            this.setImageSrc(temp);
        } catch (Exception ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Erro ao gerar imagem!");
        }
    }

    private void configurarEfeitos() {

        // Aplica os efeitos de campo
        mapEfeitos.put(TipoEfeitoMagico.CAMPO, (EfeitoInterface) (Game game, Player targetPlayer, int targetCartaIndex) -> {
            Game.TipoCampo campoAntigo = game.campo;
            CartaMonstro cartaMonstro;
            game.campo = tipoCampo;

            // Percorre todas cartas monstro
            for (Carta carta : game.todasCartas) {

                if (carta instanceof CartaMonstro) {
                    cartaMonstro = (CartaMonstro) carta;

                    // Remove o efeito de campo antigo
                    switch (campoAntigo) {
                        case FLOREST:
                            editarEfeitoFlorest(cartaMonstro, -1);
                            break;
                        case OCEAN:
                            editarEfeitoOcean(cartaMonstro, -1);
                            break;
                        case MOUNTAIN:
                            editarEfeitoMountain(cartaMonstro, -1);
                            break;
                        case DESERT:
                            editarEfeitoDesert(cartaMonstro, -1);
                            break;
                        default:
                            break;
                    }

                    // Aplica o efeito do campo da carta
                    switch (tipoCampo) {
                        case FLOREST:
                            editarEfeitoFlorest(cartaMonstro, 1);
                            break;
                        case OCEAN:
                            editarEfeitoOcean(cartaMonstro, 1);
                            break;
                        case MOUNTAIN:
                            editarEfeitoMountain(cartaMonstro, 1);
                            break;
                        case DESERT:
                            editarEfeitoDesert(cartaMonstro, 1);
                            break;
                        default:
                            break;
                    }
                }

            }
        });

        // Aplica os efeitos de trap
        mapEfeitos.put(TipoEfeitoMagico.TRAP, new EfeitoInterface() {
            @Override
            public void aplicarEfeito(Game game, Player targetPlayer, int targetCartaIndex) {
                if (tipoTrap == Game.TipoTrap.MISS) {
                    // Nao faz nada
                }
                if (tipoTrap == Game.TipoTrap.COUNTER) {
                    // Destroi a carta que esta atacando
                    targetPlayer.mesa.removeMonstro(targetCartaIndex);
                }
            }
        });
    }

    public String getSubEfeitoStr() {
        if (tipoEfeitoMagico == TipoEfeitoMagico.CAMPO) {
            return tipoCampo.name();
        }
        if (tipoEfeitoMagico == TipoEfeitoMagico.TRAP) {
            return tipoTrap.name();
        }
        return null;
    }

    public void editarEfeitoFlorest(CartaMonstro cartaMonstro, int peso) {
        CartaMonstro.TipoAtributo tipoAtributo = cartaMonstro.getTipoAtributo();

        if (tipoAtributo == CartaMonstro.TipoAtributo.DARK || tipoAtributo == CartaMonstro.TipoAtributo.EARTH) {
            cartaMonstro.setATK(cartaMonstro.getATK() + 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() + 300 * peso);
        } else if (tipoAtributo == CartaMonstro.TipoAtributo.WIND || tipoAtributo == CartaMonstro.TipoAtributo.LIGHT) {
            cartaMonstro.setATK(cartaMonstro.getATK() - 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() - 300 * peso);
        }
    }

    public void editarEfeitoOcean(CartaMonstro cartaMonstro, int peso) {
        CartaMonstro.TipoAtributo tipoAtributo = cartaMonstro.getTipoAtributo();

        if (tipoAtributo == CartaMonstro.TipoAtributo.WATER) {
            cartaMonstro.setATK(cartaMonstro.getATK() + 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() + 300 * peso);
        } else if (tipoAtributo == CartaMonstro.TipoAtributo.FIRE) {
            cartaMonstro.setATK(cartaMonstro.getATK() - 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() - 300 * peso);
        }
    }

    public void editarEfeitoMountain(CartaMonstro cartaMonstro, int peso) {
        CartaMonstro.TipoAtributo tipoAtributo = cartaMonstro.getTipoAtributo();

        if (tipoAtributo == CartaMonstro.TipoAtributo.WIND || tipoAtributo == CartaMonstro.TipoAtributo.LIGHT) {
            cartaMonstro.setATK(cartaMonstro.getATK() + 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() + 300 * peso);
        } else if (tipoAtributo == CartaMonstro.TipoAtributo.EARTH || tipoAtributo == CartaMonstro.TipoAtributo.DARK) {
            cartaMonstro.setATK(cartaMonstro.getATK() - 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() - 300 * peso);
        }
    }

    public void editarEfeitoDesert(CartaMonstro cartaMonstro, int peso) {
        CartaMonstro.TipoAtributo tipoAtributo = cartaMonstro.getTipoAtributo();

        if (tipoAtributo == CartaMonstro.TipoAtributo.FIRE) {
            cartaMonstro.setATK(cartaMonstro.getATK() + 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() + 300 * peso);
        } else if (tipoAtributo == CartaMonstro.TipoAtributo.WATER) {
            cartaMonstro.setATK(cartaMonstro.getATK() - 300 * peso);
            cartaMonstro.setDEF(cartaMonstro.getDEF() - 300 * peso);
        }
    }

    public TipoEfeitoMagico getTipoEfeitoMagico() {
        return tipoEfeitoMagico;
    }

    public void ativarEfeito(Game game, Player targetPlayer, int targetCartaIndex) {
        mapEfeitos.get(tipoEfeitoMagico).aplicarEfeito(game, targetPlayer, targetCartaIndex);
        System.out.print("Carta magica " + tipoEfeitoMagico + " ");
        if (tipoEfeitoMagico == TipoEfeitoMagico.TRAP) {
            System.out.println(tipoTrap + " Ativada!");
        }
        if (tipoEfeitoMagico == TipoEfeitoMagico.CAMPO) {
            System.out.println(tipoCampo + " Ativada!");
        }
    }

    public void aplicarEfeito(Game game, Player targetPlayer, int targetCartaIndex) {
        if (tipoTrap == Game.TipoTrap.MISS) {
            // Nao faz nada
        }
        if (tipoTrap == Game.TipoTrap.COUNTER) {
            // Destroi a carta que esta atacando
            targetPlayer.mesa.removeMonstro(targetCartaIndex);
        }
    }
}
