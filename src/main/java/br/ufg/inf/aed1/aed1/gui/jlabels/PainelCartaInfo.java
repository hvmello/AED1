
package br.ufg.inf.aed1.aed1.gui.jlabels;


import br.ufg.inf.aed1.aed1.carta.Carta;
import br.ufg.inf.aed1.aed1.carta.CartaMagica;
import br.ufg.inf.aed1.aed1.carta.CartaMonstro;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class PainelCartaInfo extends JLabel{
    private JLabel bloco1,bloco1_2, bloco2, bloco2_2;
    private Font font, font2;
    public PainelCartaInfo(int x, int y, int width, int height){
        try {
            File fontFile = new File("./src\\Interface\\Utils/Pixellari.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            fontFile = new File("./src\\Interface\\Utils/pixelmix.ttf");
            font2 = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(font);
            genv.registerFont(font2);
        } catch (IOException | FontFormatException ex) {
            Logger.getLogger(PainelCartaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setBackground(Color.BLACK);
        setOpaque(true);
        setBounds(x, y, width, height);
        bloco1 = new JLabel();                      bloco1_2 = new JLabel();                      
        bloco1.setHorizontalAlignment(LEFT);        bloco1_2.setHorizontalAlignment(LEFT);        
        bloco1.setVerticalAlignment(TOP);           bloco1_2.setVerticalAlignment(CENTER);        
        bloco1.setOpaque(false);                                                                
        bloco1.setForeground(Color.white);          bloco1_2.setForeground(Color.white);          
        bloco1.setFont(font.deriveFont(18.0f));     bloco1_2.setFont(font.deriveFont(18.0f));     
        
        bloco2 = new JLabel();                      bloco2_2 = new JLabel();                  
        bloco2.setHorizontalAlignment(LEFT);        bloco2_2.setHorizontalAlignment(LEFT);    
        bloco2.setVerticalAlignment(TOP);           bloco2_2.setVerticalAlignment(TOP);       
        bloco2.setOpaque(false);                     bloco2_2.setOpaque(true);                 
        bloco2.setForeground(Color.white);          bloco2_2.setForeground(Color.white);
        bloco2.setFont(font2.deriveFont(16.0f));    bloco2_2.setFont(font2.deriveFont(16.0f));   
        
        
        bloco1.setBounds(0, 0, width/2, 18);
        bloco1_2.setBounds(0, 18, width/2, height-18);
        bloco2.setBounds(width/2, 0, width/2, height);
        
        this.add(bloco1,0);
        this.add(bloco1_2,1);
        this.add(bloco2,1);
    }
    
    public void setCartaSource(Carta carta){
        String texto1="",texto1_2="", texto2="";
        
        if(carta instanceof CartaMonstro){
            texto1 = String.format("<html><b> %.40s - %.7s </b></html>", carta.getNome(), ((CartaMonstro) carta).getTipoAtributo());
            texto1_2 = String.format("<html><i> %s </i></html>", carta.getDescricao());
            texto2 = String.format("<html><body style='width: %s;height: %s'>"
                    +"<table style='width:100%%; height:100%%'>"
                    +"<tbody style='width:100%%; height:100%%'>"
                         +"<tr>"
                            +"<td  valign='middle' style='width:58%%;' ><br/>ATK: %4d<br/><br/> DEF: %4d</td>"
                            +"<td  valign='top' align='left' ><br/>Atribute: %s </td>"
                        +"</tr>"
                    +"</tbody>"
                    +"</table>"
                    + "</body></html>",bloco2.getWidth(), bloco2.getHeight(), ((CartaMonstro) carta).getATK(), ((CartaMonstro) carta).getDEF());
            System.out.println(texto2);
            
        }else if(carta instanceof CartaMagica){
            texto1 = String.format("<html><b> %.40s - Magia </b></html>", carta.getNome());
            texto1_2 = String.format("<html><i> %s </i></html>", carta.getDescricao());
            texto2 = String.format("<html><br/> %s</html>", ((CartaMagica) carta).getTipoEfeitoMagico());
        }
        
        bloco1.setText(texto1);
        bloco1_2.setText(texto1_2);
        bloco2.setText(texto2);
        
    }
    
    public void setBorder(Border border){
        if(border instanceof MatteBorder){
            int bottom = ((MatteBorder) border).getBorderInsets(this).bottom;
            int top = ((MatteBorder) border).getBorderInsets(this).top;
            int left = ((MatteBorder) border).getBorderInsets(this).left;
            int right = ((MatteBorder) border).getBorderInsets(this).right;
            int metadeLargura = (this.getWidth()/2);
            bloco1.setBounds(left+2, top, metadeLargura-left , 20);
            bloco1_2.setBounds(left+2, top+18, metadeLargura-left , this.getHeight()-top-bottom-18);
            bloco2.setBounds(metadeLargura+46, top, metadeLargura -right -45, this.getHeight()-top-bottom);
        }
        super.setBorder(border);
    }
    
    
}
