/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.aed1.aed1.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

public class AdvancedBevelBorder extends AbstractBorder {

    private Color topColor, rightColor, bottomColor, leftColor, lineColor;
    private int borderWidth;

    public AdvancedBevelBorder(Color topColor, Color rightColor, Color bottomColor, Color leftColor, Color lineColor, int borderWidth) {
        setTopColor(topColor);
        setRightColor(rightColor);
        setBottomColor(bottomColor);
        setLeftColor(leftColor);
        setLineColor(lineColor);
        setBorderWidth(borderWidth);
    }
    
    public AdvancedBevelBorder(Color color, int borderWidth) {
        Color corClara = color.brighter();
        Color corEscura = color.darker();
        setTopColor(corClara);
        setRightColor(corEscura);
        setBottomColor(corEscura);
        setLeftColor(corClara);
        setLineColor(corEscura);
        setBorderWidth(borderWidth);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);

        int h = height;
        int w = width;
        int bw = getBorderWidth();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);

        Polygon topPolygon = createPolygon(new Point(0, 0), new Point(w, 0), new Point(w - bw, bw), new Point(bw, bw), new Point(0, 0));
        g2.setColor(getTopColor());
        g2.fill(topPolygon);
        g2.setColor(getLineColor());
        g2.draw(topPolygon);

        Polygon rightPolygon = createPolygon(new Point(w - 1, 0), new Point(w - 1, h), new Point(w - bw - 1, h - bw), new Point(w - bw - 1, bw), new Point(w - 1, 0));
        g2.setColor(getRightColor());
        g2.fill(rightPolygon);
        g2.setColor(getLineColor());
        g2.draw(rightPolygon);

        Polygon bottomPolygon = createPolygon(new Point(0, h - 1), new Point(w, h - 1), new Point(w - bw, h - bw - 1), new Point(bw, h - bw - 1), new Point(0, h - 1));
        g2.setColor(getBottomColor());
        g2.fill(bottomPolygon);
        g2.setColor(getLineColor());
        g2.draw(bottomPolygon);

        Polygon leftPolygon = createPolygon(new Point(0, 0), new Point(0, h), new Point(bw, h - bw), new Point(bw, bw), new Point(0, 0));
        g2.setColor(getLeftColor());
        g2.fill(leftPolygon);
        g2.setColor(getLineColor());
        g2.draw(leftPolygon);

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(getBorderWidth(), getBorderWidth(), getBorderWidth() + 1, getBorderWidth() + 1);
    }

    private Polygon createPolygon(Point... points) {
        Polygon polygon = new Polygon();
        for (Point point : points) {
            polygon.addPoint(point.x, point.y);
        }
        return polygon;
    }

    public void swapBorderColors(){
        Color aux;
        aux = topColor;
        topColor= bottomColor;
        bottomColor = aux;
        aux = leftColor;
        leftColor = rightColor;
        rightColor = aux;
    }
    
    public Color getTopColor() {
        return topColor;
    }

    public void setTopColor(Color topColor) {
        this.topColor = topColor;
    }

    public Color getRightColor() {
        return rightColor;
    }

    public void setRightColor(Color rightColor) {
        this.rightColor = rightColor;
    }

    public Color getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(Color bottomColor) {
        this.bottomColor = bottomColor;
    }

    public Color getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(Color leftColor) {
        this.leftColor = leftColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

}
