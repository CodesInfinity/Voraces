/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import modelos.Punto;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agustinrodriguez
 */
public class PanelGrafo extends JPanel {
    private List<Punto> datos = new ArrayList<>();
    private List<Punto> ruta = new ArrayList<>();

    private static final Color COLOR_PUNTO = new Color(0, 0, 200);
    private static final Color COLOR_INICIO = new Color(0, 180, 60);
    private static final Color COLOR_RUTA = new Color(220, 50, 50);
    private static final Color COLOR_GRID = new Color(230, 230, 230);

    public PanelGrafo() {
        setBackground(Color.WHITE);
        setBorder(new TitledBorder("Visualización del Grafo"));
    }

    public void setDatos(List<Punto> datos, List<Punto> ruta) {
        this.datos = datos;
        this.ruta = ruta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (datos.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.drawString("No hay datos cargados.", getWidth() / 2 - 60, getHeight() / 2);
            return;
        }

        // Calcular escalas
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (Punto p : datos) {
            if (p.getX() < minX) minX = p.getX();
            if (p.getX() > maxX) maxX = p.getX();
            if (p.getY() < minY) minY = p.getY();
            if (p.getY() > maxY) maxY = p.getY();
        }

        int padding = 50;
        double w = getWidth() - 2.0 * padding;
        double h = getHeight() - 2.0 * padding;
        double escalaX = w / (maxX - minX + 0.0001);
        double escalaY = h / (maxY - minY + 0.0001);

        // 1. Dibujar Grid
        g2.setColor(COLOR_GRID);
        int gridSize = 50;
        for (int x = padding; x <= getWidth() - padding; x += gridSize)
            g2.drawLine(x, padding, x, getHeight() - padding);
        for (int y = padding; y <= getHeight() - padding; y += gridSize)
            g2.drawLine(padding, y, getWidth() - padding, y);

        g2.setColor(Color.GRAY);
        g2.drawRect(padding, padding, (int) w, (int) h);

        // 2. Dibujar Ruta con Flechas
        if (!ruta.isEmpty()) {
            g2.setColor(COLOR_RUTA);
            g2.setStroke(new BasicStroke(2f));
            for (int i = 0; i < ruta.size() - 1; i++) {
                Punto p1 = ruta.get(i);
                Punto p2 = ruta.get(i + 1);
                int x1 = padding + (int) ((p1.getX() - minX) * escalaX);
                int y1 = padding + (int) ((p1.getY() - minY) * escalaY);
                int x2 = padding + (int) ((p2.getX() - minX) * escalaX);
                int y2 = padding + (int) ((p2.getY() - minY) * escalaY);

                g2.drawLine(x1, y1, x2, y2);
                dibujarFlecha(g2, x1, y1, x2, y2);
            }
        }

        // 3. Dibujar Puntos
        int size = Math.max(4, Math.min(12, 500 / datos.size()));
        for (Punto p : datos) {
            int x = padding + (int) ((p.getX() - minX) * escalaX);
            int y = padding + (int) ((p.getY() - minY) * escalaY);

            boolean esInicio = !ruta.isEmpty() && ruta.get(0).getId() == p.getId();
            g2.setColor(esInicio ? COLOR_INICIO : COLOR_PUNTO);

            int r = esInicio ? size + 4 : size;
            g2.fill(new Ellipse2D.Double(x - r / 2.0, y - r / 2.0, r, r));
            g2.setColor(Color.WHITE);
            g2.draw(new Ellipse2D.Double(x - r / 2.0, y - r / 2.0, r, r));

            if (datos.size() < 100) {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(String.valueOf(p.getId()), x + r / 2 + 2, y - r / 2);
            }
        }
    }

    private void dibujarFlecha(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1, dy = y2 - y1;
        double angulo = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        if (len < 20) return;

        int xm = (x1 + x2) / 2;
        int ym = (y1 + y2) / 2;

        AffineTransform txOriginal = g2.getTransform();
        g2.translate(xm, ym);
        g2.rotate(angulo);

        Polygon flecha = new Polygon();
        flecha.addPoint(-6, -6);
        flecha.addPoint(6, 0);
        flecha.addPoint(-6, 6);
        g2.fill(flecha);

        g2.setTransform(txOriginal);
    }
}
