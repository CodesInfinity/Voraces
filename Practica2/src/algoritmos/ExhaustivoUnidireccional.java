/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import java.util.ArrayList;
import java.util.List;
import modelos.*;

/**
 *
 * @author agustinrodriguez
 */
public class ExhaustivoUnidireccional implements EstrategiaTSP {
    @Override
    public String getNombre() { return "Unidireccional Exhaustivo"; }

    @Override
    public Resultado resolver(List<Punto> entrada) {
        List<Punto> puntos = copiarLista(entrada);
        long inicio = System.nanoTime();
        long calculos = 0;
        
        List<Punto> ruta = new ArrayList<>();
        if (puntos.isEmpty()) return new Resultado(ruta, 0, 0, 0);

        Punto actual = puntos.get(0);
        actual.setVisitado(true);
        ruta.add(actual);

        double distanciaTotal = 0;

        while (ruta.size() < puntos.size()) {
            Punto mejorCandidato = null;
            double minDistancia = Double.MAX_VALUE;

            for (Punto p : puntos) {
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
            }
        }

        // Cerrar ciclo
        distanciaTotal += actual.distanciaA(ruta.get(0));
        calculos++;
        ruta.add(ruta.get(0));

        long tiempo = System.nanoTime() - inicio;
        return new Resultado(ruta, distanciaTotal, calculos, tiempo);
    }

    private List<Punto> copiarLista(List<Punto> src) {
        List<Punto> dest = new ArrayList<>();
        for (Punto p : src) dest.add(p.copiar());
        return dest;
    }
}
