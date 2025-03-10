package src.java.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private double zoomFactor;
    private double offsetX;
    private double offsetY;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        zoomFactor = 1.0;
        offsetX = 0.0;
        offsetY = 0.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int newWidth = (int) (image.getWidth() * zoomFactor);
            int newHeight = (int) (image.getHeight() * zoomFactor);

            g.drawImage(image, (int) offsetX, (int) offsetY, newWidth, newHeight, this);
        }
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }
    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
        repaint();
    }
    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
        repaint();
    }

    public void changeImage(BufferedImage newImage) {
        this.image = newImage;
        repaint();
    }

    public void repaintPixel(int x, int y, Color c) {
        this.image.setRGB(x, y, c.getRGB());
        repaint();
    }

    public void increaseZoomFactor(MouseWheelEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();

        double newZoomFactor = zoomFactor * ((e.getPreciseWheelRotation() < 0) ? 1.1 : 0.9);

        offsetX = (offsetX - mouseX) * (newZoomFactor / zoomFactor) + mouseX;
        offsetY = (offsetY - mouseY) * (newZoomFactor / zoomFactor) + mouseY;

        zoomFactor = newZoomFactor;
        repaint();
    }
}