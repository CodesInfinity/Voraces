/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelos.*;

/**
 *
 * @author agustinrodriguez
 */
public class PodaUnidireccional implements EstrategiaTSP {
    @Override
    public String getNombre() { return "Unidireccional con Poda"; }

    @Override
    public Resultado resolver(List<Punto> entrada) {
        List<Punto> puntos = copiarLista(entrada);
        // Ordenar por X para aplicar poda
        puntos.sort(Comparator.comparingDouble(Punto::getX));
        
        long inicio = System.nanoTime();
        long calculos = 0;
        
        List<Punto> ruta = new ArrayList<>();
        if (puntos.isEmpty()) return new Resultado(ruta, 0, 0, 0);

        Punto actual = puntos.get(0); 
        actual.setVisitado(true);
        ruta.add(actual);
        int indiceActual = 0; // Índice en la lista ordenada

        double distanciaTotal = 0;

        while (ruta.size() < puntos.size()) {
            Punto mejorCandidato = null;
            double minDistancia = Double.MAX_VALUE;

            // Barrido Derecha
            for (int i = indiceActual + 1; i < puntos.size(); i++) {
                Punto p = puntos.get(i);
                // PODA
                if ((p.getX() - actual.getX()) >= minDistancia) break;
                
                if (!p.esVisitado()) {
                    double d = actual.distanciaA(p);
                    calculos++;
                    if (d < minDistancia) {
                        minDistancia = d;
                        mejorCandidato = p;
                    }
                }
            }

            // Barrido Izquierda
            for (int i = indiceActual - 1; i >= 0; i--) {
                Punto p = puntos.get(i);
                // PODA
                if ((actual.getX() - p.getX()) >= minDistancia) break;

                if (!p.esVisitado()) {
                    double d = actual.distanciaA(p);
                    calculos++;
                    if (d < minDistancia) {
                        minDistancia = d;
                        mejorCandidato = p;
                    }
                }
            }

            if (mejorCandidato != null) {
                mejorCandidato.setVisitado(true);
                ruta.add(mejorCandidato);
                distanciaTotal += minDistancia;
                actual = mejorCandidato;
                // Actualizar índice para la siguiente iteración
                indiceActual = puntos.indexOf(actual); 
            }
        }

        distanciaTotal += actual.distanciaA(ruta.get(0));
        calculos++;
        ruta.add(ruta.get(0));

        return new Resultado(ruta, distanciaTotal, calculos, System.nanoTime() - inicio);
    }
    
    private List<Punto> copiarLista(List<Punto> src) {
        List<Punto> dest = new ArrayList<>();
        for (Punto p : src) dest.add(p.copiar());
        return dest;
    }
}