package br.ufg.inf.aed1.aed1.carta;

import br.ufg.inf.aed1.aed1.projeto.GetCardImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartaMonstro extends Carta {

    public static enum TipoMonstro {
        BESTA, HUMANO, ANJO, DEMONIO;
    }

    public static enum TipoAtributo {
        DARK, LIGHT, WIND, EARTH, WATER, FIRE;
    }

    public static enum ModoCarta {
        ATAQUE_PARA_CIMA, ATAQUE_PARA_BAIXO, DEFESA_PARA_CIMA, DEFESA_PARA_BAIXO;

        public boolean isAtaqueCima() {
            return this.equals(ModoCarta.ATAQUE_PARA_CIMA);
        }

        public boolean isAtaqueBaixo() {
            return this.equals(ModoCarta.ATAQUE_PARA_BAIXO);
        }

        public boolean isDefesaCima() {
            return this.equals(ModoCarta.DEFESA_PARA_CIMA);
        }

        public boolean isDefesaBaixo() {
            return this.equals(ModoCarta.DEFESA_PARA_BAIXO);
        }
    }

    private TipoMonstro tipoMonstro;
    private TipoAtributo tipoAtributo;
    private ModoCarta modoCarta;
    private int quantidadeEstrelas = 0;
    private int ATK;
    private int DEF;

    public CartaMonstro(int id, String set, String nome, String descricao, int ATK, int DEF,
            TipoMonstro tipoMonstro, TipoAtributo tipoAtributo, String imageSrc) {

        super(id, set, nome, descricao);

        this.ATK = ATK;
        this.DEF = DEF;
        this.tipoMonstro = tipoMonstro;
        this.tipoAtributo = tipoAtributo;

        String imgSrcFinal;

        try {
            imgSrcFinal = GetCardImage.httpGetImage(this, imageSrc);
            this.setImageSrc(imgSrcFinal);
        } catch (Exception exception) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    public CartaMonstro(String nome, int ATK, int DEF, String tipo, String atributo, String set, int id, String imgPath, String descricao) {
        super(id, set, nome, descricao);
        String imageSrcFinal;

        this.ATK = ATK;
        this.DEF = DEF;
        try {
            this.setTipoMonstro(TipoMonstro.valueOf(tipo));
            this.tipoAtributo = TipoAtributo.valueOf(atributo);
        } catch (IllegalArgumentException ex) {
            System.err.print(ex);
        }

        try {
            imageSrcFinal = GetCardImage.httpGetImage(this, imgPath);
            this.setImageSrc(imageSrcFinal);
        } catch (Exception ex) {
            Logger.getLogger(Carta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void virarParaCima() {
        if (isModoAtaque()) {
            modoCarta = ModoCarta.ATAQUE_PARA_CIMA;
        } else {
            modoCarta = ModoCarta.DEFESA_PARA_CIMA;
        }
    }

    public void virarParaBaixo() {
        if (isModoAtaque()) {
            modoCarta = ModoCarta.DEFESA_PARA_BAIXO;
        } else {
            modoCarta = ModoCarta.ATAQUE_PARA_BAIXO;
        }
    }

    public void girarCarta() {
        switch (modoCarta) {
            case ATAQUE_PARA_BAIXO:
                modoCarta = ModoCarta.DEFESA_PARA_BAIXO;
                break;
            case ATAQUE_PARA_CIMA:
                modoCarta = ModoCarta.DEFESA_PARA_CIMA;
                break;
            case DEFESA_PARA_BAIXO:
                modoCarta = ModoCarta.ATAQUE_PARA_BAIXO;
                break;
            case DEFESA_PARA_CIMA:
                modoCarta = ModoCarta.ATAQUE_PARA_CIMA;
                break;
            default:
                break;
        }
    }

    public boolean isModoAtaque() {
        return modoCarta == ModoCarta.ATAQUE_PARA_BAIXO || modoCarta == ModoCarta.ATAQUE_PARA_CIMA;
    }

    public boolean isModoDefesa() {
        return modoCarta == ModoCarta.DEFESA_PARA_BAIXO || modoCarta == ModoCarta.DEFESA_PARA_CIMA;
    }

    public boolean isParaCima() {
        return modoCarta == ModoCarta.ATAQUE_PARA_CIMA || modoCarta == ModoCarta.DEFESA_PARA_CIMA;
    }

    public boolean isParaBaixo() {
        return modoCarta == ModoCarta.ATAQUE_PARA_BAIXO || modoCarta == ModoCarta.DEFESA_PARA_BAIXO;
    }
    
    public void setTipoMonstro(TipoMonstro tipoMonstro) {
        this.tipoMonstro = tipoMonstro;
    }

    public TipoMonstro getTipoMonstro() {
        return tipoMonstro;
    }

    public TipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(TipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public ModoCarta getModoCarta() {
        return modoCarta;
    }

    public void setModoCarta(ModoCarta modoCarta) {
        this.modoCarta = modoCarta;
    }

    public int getQuantidadeEstrelas() {
        return quantidadeEstrelas;
    }

    public void setQuantidadeEstrelas(int quantidadeEstrelas) {
        this.quantidadeEstrelas = quantidadeEstrelas;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int ATK) {
        this.ATK = ATK;
    }

    public int getDEF() {
        return DEF;
    }

    public void setDEF(int DEF) {
        this.DEF = DEF;
    }

}
