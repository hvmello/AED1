package br.ufg.inf.aed1.aed1.carta;

public class CartaMagica extends Carta {

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
    
    
    public CartaMagica(int id, String set, String nome, String descricao) {
        super(id, set, nome, descricao);
    }

    public TipoEfeitoMagico getTipoEfeitoMagico() {
        return tipoEfeitoMagico;
    }    
}

