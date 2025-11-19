/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import modelos.*;

/**
 *
 * @author agustinrodriguez
 */
public class ExhaustivoBidireccional implements EstrategiaTSP {
    @Override
    public String getNombre() { return "Bidireccional Exhaustivo"; }

    @Override
    public Resultado resolver(List<Punto> entrada) {
        List<Punto> puntos = copiarLista(entrada);
        long inicio = System.nanoTime();
        long calculos = 0;
        
        LinkedList<Punto> ruta = new LinkedList<>();
        // Si hay menos de 3 puntos, delegamos al unidireccional o caso trivial
        if (puntos.size() < 3) {
            return new ExhaustivoUnidireccional().resolver(entrada);
        }

        // 1. Inicialización
        Punto primero = puntos.get(0);
        primero.setVisitado(true);
        ruta.add(primero);

        // Buscar el más cercano al primero
        Punto segundo = null;
        double minD = Double.MAX_VALUE;
        for (Punto p : puntos) {
            if (!p.esVisitado()) {
                double d = primero.distanciaA(p);
                calculos++;
                if (d < minD) {
                    minD = d;
                    segundo = p;
                }
            }
        }
        segundo.setVisitado(true);
        ruta.add(segundo);
        double distanciaTotal = minD;

        // 2. Bucle Principal (Expandir extremos)
        while (ruta.size() < puntos.size()) {
            Punto extremoIzq = ruta.getFirst();
            Punto extremoDer = ruta.getLast();

            Punto mejorIzq = null;
            double minIzq = Double.MAX_VALUE;
            
            Punto mejorDer = null;
            double minDer = Double.MAX_VALUE;

            for (Punto p : puntos) {
                if (!p.esVisitado()) {
                    double dI = extremoIzq.distanciaA(p);
                    calculos++;
                    if (dI < minIzq) { minIzq = dI; mejorIzq = p; }
                    
                    double dD = extremoDer.distanciaA(p);
                    calculos++;
                    if (dD < minDer) { minDer = dD; mejorDer = p; }
                }
            }

            if (minIzq < minDer) {
                mejorIzq.setVisitado(true);
                ruta.addFirst(mejorIzq);
                distanciaTotal += minIzq;
            } else {
                mejorDer.setVisitado(true);
                ruta.addLast(mejorDer);
                distanciaTotal += minDer;
            }
        }

        // Cerrar ciclo
        distanciaTotal += ruta.getLast().distanciaA(ruta.getFirst());
        calculos++;
        List<Punto> rutaFinal = new ArrayList<>(ruta);
        rutaFinal.add(ruta.getFirst());

        long tiempo = System.nanoTime() - inicio;
        return new Resultado(rutaFinal, distanciaTotal, calculos, tiempo);
    }

    private List<Punto> copiarLista(List<Punto> src) {
        List<Punto> dest = new ArrayList<>();
        for (Punto p : src) dest.add(p.copiar());
        return dest;
    }
}