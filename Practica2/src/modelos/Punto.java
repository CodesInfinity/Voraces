/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 * Representa ubicación en el mapa.
 * Implementa Comparable para facilitar la ordenación por coordenada X (usado en la poda).
 * @author agustinrodriguez
 */
public class Punto implements Comparable<Punto> {
    private int id;
    private double x;
    private double y;
    private boolean visitado;

    public Punto(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.visitado = false;
    }

    // Constructor de copia
    public Punto copiar() {
        return new Punto(this.id, this.x, this.y);
    }

    public double distanciaA(Punto p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    @Override
    public int compareTo(Punto o) {
        return Double.compare(this.x, o.x);
    }

    // Getters y Setters
    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean esVisitado() { return visitado; }
    public void setVisitado(boolean visitado) { this.visitado = visitado; }
}