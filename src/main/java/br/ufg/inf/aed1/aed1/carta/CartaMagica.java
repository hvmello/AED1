package br.ufg.inf.aed1.aed1.carta;

import br.ufg.inf.aed1.aed1.gameplay.Game;
import br.ufg.inf.aed1.aed1.gameplay.Player;
import br.ufg.inf.aed1.aed1.projeto.GetCardImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartaMagica extends Carta implements EfeitoInterface {

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

    private final Map<TipoEfeitoMagico, EfeitoInterface> mapEfeitos = new EnumMap<>(TipoEfeitoMagico.class);

    public CartaMagica(Game.TipoCampo tipoCampo, int id, String set, String nome, String descricao, String imageSrc) {
        super(id, set, nome, descricao);
        this.tipoEfeitoMagico = TipoEfeitoMagico.CAMPO;
        this.tipoCampo = tipoCampo;
        //_configurarEfeitos();

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
        //_configurarEfeitos();

        String imgSrcFinal;

        try {
            imgSrcFinal = GetCardImage.httpGetImage(this, imageSrc);
            this.setImageSrc(imgSrcFinal);
        } catch (Exception exception) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

    @Override
    public void aplicarEfeito(Game game, Player targetPlayer, int targetIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    public TipoEfeitoMagico getTipoEfeitoMagico() {
        return tipoEfeitoMagico;
    }

}
