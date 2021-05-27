package br.ufg.inf.aed1.aed1.gui.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class ImageEditor {
    
     public static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    
    public static BufferedImage loadImage(String file) throws IOException {
        return ImageIO.read(new File(file));
    }
    
    public static ImageIcon getGrayScaled(ImageIcon img){
        
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp ccop = new ColorConvertOp(cs, null);
        
        return new ImageIcon(ccop.filter( toBufferedImage(img.getImage()), null));

    }
    
    public static ImageIcon loadGrayScaled(String file, int width, int height){
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp ccop = new ColorConvertOp(cs, null);
        
        Image newImage = null;
         try {
             newImage = ccop.filter( loadImage(file), null)
                     .getScaledInstance(width, height, Image.SCALE_SMOOTH);
         } catch (IOException ex) {
             Logger.getLogger(ImageEditor.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        return new ImageIcon(newImage);
    }
    
    public static Dimension getJPEGDimension(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);

        // check for SOI marker
        if (fis.read() != 255 || fis.read() != 216)
            throw new RuntimeException("SOI (Start Of Image) marker 0xff 0xd8 missing");

        Dimension d = null;

        while (fis.read() == 255) {
            int marker = fis.read();
            int len = fis.read() << 8 | fis.read();

            if (marker == 192) {
                fis.skip(1);

                int height = fis.read() << 8 | fis.read();
                int width = fis.read() << 8 | fis.read();

                d = new Dimension(width, height);
                break;
            }

            fis.skip(len - 2);
        }
        fis.close();
        return d;
    }
    
    
    public static void redimensionar(ImageIcon icon, int xLargura, int yAltura){
        icon.setImage(icon.getImage().getScaledInstance(xLargura, yAltura, Image.SCALE_SMOOTH));
    }
    
    public static void redimensionar(ImageIcon icon, Dimension dim){
        icon.setImage(icon.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
    }
    
    
    
    public static Icon getRedimensionado(String imgPath, int xLargura, int yAltura){
        ImageIcon img = new ImageIcon(imgPath);
        img.setImage(img.getImage().getScaledInstance(xLargura, yAltura, Image.SCALE_SMOOTH));
        return img;
    }
    
    public static Icon getRedimensionado(String imgPath, Dimension dim){
        ImageIcon img = new ImageIcon (imgPath);
        img.setImage(img.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
        return img;
    }
    
    public static ImageIcon getRedimensionado(ImageIcon icon, int xLargura, int yAltura){
        ImageIcon img = new ImageIcon (icon.getImage());
        img.setImage(img.getImage().getScaledInstance(xLargura, yAltura, Image.SCALE_SMOOTH));
        return img;
    }
    
    public static ImageIcon getRedimensionado(ImageIcon icon, Dimension dim){
        ImageIcon img = new ImageIcon (icon.getImage());
        img.setImage(img.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH));
        return img;
    }
}
