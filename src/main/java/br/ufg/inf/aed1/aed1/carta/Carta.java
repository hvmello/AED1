package br.ufg.inf.aed1.aed1.carta;

public class Carta implements Cloneable{

    protected int id;           // Id da carta em relacao ao seu set
    protected String set = "";  // Fonte: https://forum.yugiohcardmaker.net/topic/7296-set-id/
    protected String nome;
    protected String descricao = "";
    protected String imageSrc = null;

    public Carta(int id, String set, String nome, String descricao) {
        this.id = id;
        this.set = set;
        this.nome = nome;
        this.descricao = descricao;
    }
    
    public int getId() {
        return id;
    }
    
    public String getSet() {
        return set;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImageSrc() {
        return imageSrc;
    }
    
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public Carta clone() throws CloneNotSupportedException {
        return (Carta)super.clone();
    }
    
    
}

