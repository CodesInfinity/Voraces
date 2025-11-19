Práctica 2: Algoritmos Voraces - Problema del Viajante (TSP) 🚀

Este repositorio contiene la solución a la Práctica 2 de la asignatura Algorítmica y Modelos de Computación (AMC). El proyecto implementa y compara diferentes Algoritmos Voraces (Greedy) para resolver el Problema del Viajante de Comercio (TSP) en un espacio euclídeo 2D.

📸 Captura de Pantalla

<img width="2548" height="1588" alt="image" src="https://github.com/user-attachments/assets/e7a32690-4568-42c1-9a00-4dff224f4d37" />

📋 Descripción

El objetivo es encontrar una ruta que visite un conjunto de ciudades exactamente una vez y regrese al punto de partida, minimizando la distancia total recorrida. Se han implementado estrategias unidireccionales y bidireccionales, tanto exhaustivas como optimizadas mediante poda.

La aplicación cuenta con una Interfaz Gráfica (GUI) completa desarrollada en Java Swing que permite visualizar los grafos, las rutas calculadas y realizar comparativas experimentales de rendimiento.

✨ Funcionalidades Principales

Generación de Datos: Creación de datasets aleatorios y carga de ficheros .tsp (formato TSPLIB).

4 Estrategias Voraces:

Unidireccional Exhaustivo: Búsqueda del vecino más cercano.

Bidireccional Exhaustivo: Construcción de la ruta desde ambos extremos simultáneamente.

Unidireccional con Poda: Optimización ordenando por coordenada X para descartar candidatos lejanos.

Bidireccional con Poda: La estrategia más eficiente implementada.

Visualización Avanzada:

Representación gráfica de nodos y aristas.

Indicadores de dirección (flechas) en la ruta.

Cuadrícula de fondo y escalado dinámico.

Experimentación:

Comparación de tiempos y costes entre estrategias.

Ejecución multihilo (SwingWorker) para evitar congelamientos de la interfaz.

Tablas de resultados integradas en la aplicación.

🛠️ Estructura del Proyecto

El código ha sido refactorizado siguiendo principios de diseño limpio y separación de responsabilidades:
```
src/amc/practica2/
├── algoritmos/          # Lógica de las estrategias (Patrón Strategy)
│   ├── EstrategiaTSP.java
│   ├── ExhaustivoUnidireccional.java
│   ├── ExhaustivoBidireccional.java
│   ├── PodaUnidireccional.java
│   └── PodaBidireccional.java
├── modelo/              # Clases de datos
│   ├── Punto.java
│   └── Resultado.java
├── vista/               # Interfaz Gráfica (Swing)
│   ├── VentanaPrincipal.java
│   └── PanelGrafo.java
└── Main.java            # Punto de entrada
```

🚀 Instalación y Ejecución

Requisitos
Java Development Kit (JDK) 8 o superior.

Clonar el repositorio:
```
git clone [https://github.com/CodesInfinity/Voraces.git](https://github.com/CodesInfinity/Voraces.git)

cd Voraces

Compilar el proyecto:

javac -d bin src/amc/practica2/*.java src/amc/practica2/**/*.java
```

Ejecutar:
```
java -cp bin amc.practica2.Main
```

🧪 Algoritmos Implementados

<img width="1542" height="784" alt="image" src="https://github.com/user-attachments/assets/6f50701f-76e9-43c7-b2c4-02a6b9031628" />

📄 Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulta el archivo LICENSE para más detalles.

Desarrollado por CodesInfinity para la asignatura de AMC.
