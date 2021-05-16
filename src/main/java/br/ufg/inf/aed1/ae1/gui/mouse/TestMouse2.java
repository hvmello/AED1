
package br.ufg.inf.aed1.ae1.gui.mouse;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//@SuppressWarnings("serial")
public class TestMouse2 extends JFrame implements MouseListener, MouseMotionListener, KeyListener {

private JPanel panel = new JPanel(null);
private JLabel label1 = new JLabel((Icon)null, JLabel.CENTER);
private JLabel label2 = new JLabel((Icon)null, JLabel.CENTER);
private JLabel label3 = new JLabel((Icon)null, JLabel.CENTER);  //esse é o unico label retangular que rotacionar redimencionando da forma correta na posição correta
private int mouseX = 100;
private int mouseY = 0;
private boolean drag = false;
private JLabel ultimoLabel;    /*label que vai "conter" o ultimo label clicado*/
private Point lastMousePos = new Point();

boolean modoAtaque1 = true;
boolean modoAtaque2 = true;
boolean modoAtaque3 = true;

//icone com imagem da carta
ImageIcon carta = new ImageIcon("C:\\Users\\Heito\\Documents\\NetBeansProjects\\ATPGC\\Files\\img_Cartas\\"         + "sdgs.jpg");


public TestMouse2() {
    this.add(panel);
    this.addKeyListener(this);               
   
    panel.setBackground(Color.WHITE);
    panel.add(label1, 0);
    label1.setOpaque(true); 
    label1.setBackground(Color.BLUE);
    label1.setBounds(mouseX, mouseY, 610, 610);   /*dimensao da carta*/
    label1.addMouseMotionListener(this);
    label1.addMouseListener(this);      
    label1.setIcon(carta);          
    
    
    
    panel.add(label2, 1);
    label2.setOpaque(true); 
    label2.setBackground(Color.RED);
    label2.setBounds(mouseX + 200, mouseY, 610, 610);
    label2.addMouseMotionListener(this);
    label2.addMouseListener(this);
    label2.setIcon(carta);
    
    panel.add(label3, 2);
    label3.setOpaque(true); 
    label2.setBackground(Color.RED);
    label3.setBounds(mouseX + 400, mouseY+100, 419, 610);
    label3.addMouseMotionListener(this);
    label3.addMouseListener(this);
    label3.setIcon(carta);
    
}
public void keyTyped(KeyEvent ke){
    


}

/*key listener pra tecla pressionada
  pega o ultimo label e rotaciona o icone do label dpois rotaciona as dimensoes
  talvez tenha que ajustar o x e y do label pq ele nao rotaciona pelo centro 
  saca */
public void keyPressed(KeyEvent ke){
         if(ke.getKeyCode() == KeyEvent.VK_SPACE){
             /*essa funçao cria um novo icone com a imagem rotacionada*/
             Icon rotacionado = null;
             if(ultimoLabel == label1){
                 if(modoAtaque1)
                    rotacionado = new RotatedIcon(carta, RotatedIcon.Rotate.UP);
                 else
                     rotacionado = carta;
                modoAtaque1 = !modoAtaque1;                
             }
             if(ultimoLabel == label2){
                 if(modoAtaque2)
                    rotacionado = new RotatedIcon(carta, RotatedIcon.Rotate.UP);
                 else
                     rotacionado = carta;
                modoAtaque2 = !modoAtaque2;
             }
             if(ultimoLabel == label3){
                 if(modoAtaque3)
                    rotacionado = new RotatedIcon(carta, RotatedIcon.Rotate.UP);
                 else
                     rotacionado = carta;
                modoAtaque3 = !modoAtaque3;
                int posX = ultimoLabel.getY()+(ultimoLabel.getHeight()-ultimoLabel.getWidth())/2;
                int posY = ultimoLabel.getX()+(ultimoLabel.getWidth()-ultimoLabel.getHeight())/2;
                ultimoLabel.setBounds(posY, posX, ultimoLabel.getHeight(), ultimoLabel.getWidth());
                //ultimoLabel.setSize(ultimoLabel.getHeight(), ultimoLabel.getWidth());

             }

             ultimoLabel.setIcon(rotacionado);
             //ultimoLabel.setSize(ultimoLabel.getHeight(), ultimoLabel.getWidth());
             
         }
    }
public void keyReleased(KeyEvent ke){
}

@Override
/*mouse listener pra verificar se ta dentro dos labels e quando clica 
  seta a variavel ultimoLabel pra o label q foi clicado */
public void mousePressed(MouseEvent e) {
    if (e.getSource() == label1 || e.getSource() == label2 || e.getSource() == label3 ) {
        lastMousePos.x = e.getX();
        lastMousePos.y = e.getY();
        ultimoLabel = (JLabel) e.getSource();
    }
}

@Override
public void mouseReleased(MouseEvent e) {
    
}

@Override
public void mouseDragged(MouseEvent e) 
{
        Point diferenca = new Point(e.getPoint().x - lastMousePos.x, e.getPoint().y - lastMousePos.y);
        JComponent jc = (JComponent)e.getSource();
        jc.setLocation(jc.getX() + diferenca.x, jc.getY() + diferenca.y);
    
    
}
public void mouseMoved(MouseEvent e) {}
public void mouseEntered(MouseEvent e) {}
public void mouseExited(MouseEvent e) {}
public void mouseClicked(MouseEvent e) {}

public static void main(String[] args) {
    
    TestMouse2 frame = new TestMouse2();
    frame.setVisible(true);
    frame.setSize(700, 500);
    frame.setResizable(true);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}

}