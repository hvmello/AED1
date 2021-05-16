package br.ufg.inf.aed1.ae1.gui.mouse;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class TestMouse extends JFrame implements MouseListener, MouseMotionListener {

private JPanel panel = new JPanel(null);    
private JLabel label1 = new JLabel();
private JLabel label2 = new JLabel();
private int mouseX = 200;
private int mouseY = 100;
private boolean drag = false;

private Point lastMousePos = new Point();

public TestMouse() {
    this.add(panel);
    panel.setBackground(Color.WHITE);
    Dimension size = label1.getPreferredSize();
    label1.setBounds(510, 300, size.width, size.height);

    
    //label.setText(label.getX() + ", " + label.getY());
    int x = label1.getLocation().x;
    int y = label1.getLocation().y;
    System.out.println(x);
    System.out.println(y);
    
    
    panel.add(label1);
    label1.setOpaque(true); 
    label1.setBackground(Color.BLUE);
    label1.setBounds(mouseX, mouseY, 100, 50);
    label1.addMouseMotionListener(this);
    label1.addMouseListener(this);

    panel.add(label2);
    label2.setOpaque(true); 
    label2.setBackground(Color.RED);
    label2.setBounds(mouseX + 200, mouseY, 100, 50);
    label2.addMouseMotionListener(this);
    label2.addMouseListener(this);
}
    
@Override
public void mousePressed(MouseEvent e) {
    if (e.getSource() == label1) {
        drag = true;
            lastMousePos.x = e.getX();
        lastMousePos.y = e.getY();
    } 
}

@Override
public void mouseReleased(MouseEvent e) {
    drag = false;
}

@Override
public void mouseDragged(MouseEvent e) 
{

        Point diferenca = new Point(e.getPoint().x - lastMousePos.x, e.getPoint().y - lastMousePos.y);
        JComponent jc = (JComponent)e.getSource();
        jc.setLocation(jc.getX() + diferenca.x, jc.getY() + diferenca.y);
    
    
}

boolean wasIn = false;
//int posYOriginal = jc.getY()
public void mouseEntered(MouseEvent e) {
    if(!wasIn){
        JComponent jc = (JComponent)e.getSource();
        jc.setLocation(jc.getX() , jc.getY()+15);
        wasIn = true;
    }

}
public void mouseExited(MouseEvent e) {
        JComponent jc = (JComponent)e.getSource();
        jc.setLocation(jc.getX() , jc.getY()-15);
        wasIn=false;
}

public void mouseMoved(MouseEvent e) {}
public void mouseClicked(MouseEvent e) {}

public static void main(String[] args) {
    TestMouse frame = new TestMouse();
    frame.setVisible(true);
    frame.setSize(600, 600);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
}
}