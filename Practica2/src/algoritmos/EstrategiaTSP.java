/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package algoritmos;

import java.util.List;
import modelos.*;

/**
 * Interfaz común para todos los algoritmos del Viajante de Comercio.
 * @author agustinrodriguez
 */
public interface EstrategiaTSP {
    Resultado resolver(List<Punto> puntos);
    String getNombre();
}