/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.List;

/**
 * Almacena el resultado de la ejecución de un algoritmo.
 * @author agustinrodriguez
 */
public class Resultado {
    private List<Punto> ruta;
    private double coste;
    private long distanciasCalculadas;
    private long tiempoNs;

    public Resultado(List<Punto> ruta, double coste, long distanciasCalculadas, long tiempoNs) {
        this.ruta = ruta;
        this.coste = coste;
        this.distanciasCalculadas = distanciasCalculadas;
        this.tiempoNs = tiempoNs;
    }

    public List<Punto> getRuta() { return ruta; }
    public double getCoste() { return coste; }
    public long getDistanciasCalculadas() { return distanciasCalculadas; }
    public long getTiempoNs() { return tiempoNs; }
}