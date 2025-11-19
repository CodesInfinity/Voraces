/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import modelos.*;

/**
 *
 * @author agustinrodriguez
 */
public class PodaBidireccional implements EstrategiaTSP {
    @Override
    public String getNombre() { return "Bidireccional con Poda"; }

    @Override
    public Resultado resolver(List<Punto> entrada) {
        List<Punto> puntos = copiarLista(entrada);
        puntos.sort(Comparator.comparingDouble(Punto::getX));
        
        long inicio = System.nanoTime();
        long calculos = 0;
        LinkedList<Punto> ruta = new LinkedList<>();
        
        if (puntos.isEmpty()) return new Resultado(new ArrayList<>(), 0,0,0);

        Punto primero = puntos.get(0);
        primero.setVisitado(true);
        ruta.add(primero);
        int idxPrimero = 0;

        // Buscar segundo con poda
        Punto segundo = null;
        double minD = Double.MAX_VALUE;
        for(int i = idxPrimero + 1; i < puntos.size(); i++){
            Punto p = puntos.get(i);
            if(p.getX() - primero.getX() >= minD) break;
            double d = primero.distanciaA(p);
            calculos++;
            if(d < minD) { minD = d; segundo = p; }
        }
        // Fallback
        if (segundo == null && puntos.size() > 1) {
            segundo = puntos.get(1);
            minD = primero.distanciaA(segundo);
        }

        if(segundo != null) {
            segundo.setVisitado(true);
            ruta.add(segundo);
        }
        double distanciaTotal = minD;

        while (ruta.size() < puntos.size()) {
            Punto extIzq = ruta.getFirst();
            int idxIzq = puntos.indexOf(extIzq);
            Punto extDer = ruta.getLast();
            int idxDer = puntos.indexOf(extDer);

            // Buscar mejor para Izquierda
            Punto mejorIzq = null; double minIzq = Double.MAX_VALUE;
            // Derecha de Izq
            for(int i=idxIzq+1; i<puntos.size(); i++){
                Punto p = puntos.get(i);
                if(p.getX() - extIzq.getX() >= minIzq) break;
                if(!p.esVisitado()){
                    double d = extIzq.distanciaA(p);
                    calculos++;
                    if(d < minIzq) { minIzq = d; mejorIzq = p; }
                }
            }
            // Izquierda de Izq
            for(int i=idxIzq-1; i>=0; i--){
                Punto p = puntos.get(i);
                if(extIzq.getX() - p.getX() >= minIzq) break;
                if(!p.esVisitado()){
                    double d = extIzq.distanciaA(p);
                    calculos++;
                    if(d < minIzq) { minIzq = d; mejorIzq = p; }
                }
            }

            // Buscar mejor para Derecha
            Punto mejorDer = null; double minDer = Double.MAX_VALUE;
            // Derecha de Der
            for(int i=idxDer+1; i<puntos.size(); i++){
                Punto p = puntos.get(i);
                if(p.getX() - extDer.getX() >= minDer) break;
                if(!p.esVisitado()){
                    double d = extDer.distanciaA(p);
                    calculos++;
                    if(d < minDer) { minDer = d; mejorDer = p; }
                }
            }
            // Izquierda de Der
            for(int i=idxDer-1; i>=0; i--){
                Punto p = puntos.get(i);
                if(extDer.getX() - p.getX() >= minDer) break;
                if(!p.esVisitado()){
                    double d = extDer.distanciaA(p);
                    calculos++;
                    if(d < minDer) { minDer = d; mejorDer = p; }
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

        distanciaTotal += ruta.getLast().distanciaA(ruta.getFirst());
        calculos++;
        List<Punto> rutaFinal = new ArrayList<>(ruta);
        rutaFinal.add(ruta.getFirst());
        
        return new Resultado(rutaFinal, distanciaTotal, calculos, System.nanoTime() - inicio);
    }

    private List<Punto> copiarLista(List<Punto> src) {
        List<Punto> dest = new ArrayList<>();
        for (Punto p : src) dest.add(p.copiar());
        return dest;
    }
}