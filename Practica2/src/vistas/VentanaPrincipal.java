/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modelos.*;
import algoritmos.*;

/**
 *
 * @author agustinrodriguez
 */
public class VentanaPrincipal extends JFrame {

    private PanelGrafo panelGrafo;
    private JTextArea areaLog;
    private JLabel etiquetaEstado;
    private JProgressBar barraProgreso;
    private JButton[] botones;

    private List<Punto> datosActuales = new ArrayList<>();
    private List<Punto> rutaActual = new ArrayList<>();

    private static final Font FUENTE_MONO = new Font("Monospaced", Font.BOLD, 12);

    public VentanaPrincipal() {
        setTitle("Práctica 2 - Algoritmos Voraces (AMC)");
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Panel Izquierdo
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelLateral.setPreferredSize(new Dimension(250, 0));

        botones = new JButton[7];
        panelLateral.add(crearEtiquetaSeccion("Gestión de Datos"));
        botones[0] = crearBoton("Crear TSP Aleatorio", e -> crearTspAleatorio());
        botones[1] = crearBoton("Cargar Dataset", e -> cargarDataset());
        botones[6] = crearBoton("Limpiar Lienzo", e -> limpiarLienzo());

        panelLateral.add(botones[0]);
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(botones[1]);
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(botones[6]);
        panelLateral.add(Box.createVerticalStrut(15));

        panelLateral.add(crearEtiquetaSeccion("Visualización"));
        botones[2] = crearBoton("Comprobar Estrategias", e -> comprobarEstrategias());
        panelLateral.add(botones[2]);
        panelLateral.add(Box.createVerticalStrut(15));

        panelLateral.add(crearEtiquetaSeccion("Experimentación"));
        botones[3] = crearBoton("Comparar Todas", e -> compararTodas());
        botones[4] = crearBoton("Comparar 2 Estrategias", e -> dialogoComparar2());
        botones[5] = crearBoton("Uni vs Bidireccional", e -> compararUniVsBi());
        panelLateral.add(botones[3]);
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(botones[4]);
        panelLateral.add(Box.createVerticalStrut(5));
        panelLateral.add(botones[5]);

        panelLateral.add(Box.createVerticalGlue());

        // 2. Panel Central
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.65);

        panelGrafo = new PanelGrafo();

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(FUENTE_MONO);
        areaLog.setBackground(new Color(245, 245, 245));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(new TitledBorder("Resultados y Salida"));

        splitPane.setTopComponent(panelGrafo);
        splitPane.setBottomComponent(scrollLog);

        // 3. Barra Estado
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBorder(new BevelBorder(BevelBorder.LOWERED));
        barraEstado.setPreferredSize(new Dimension(0, 25));

        etiquetaEstado = new JLabel(" Listo. Cargue datos para empezar.");
        barraProgreso = new JProgressBar();
        barraProgreso.setVisible(false);

        barraEstado.add(etiquetaEstado, BorderLayout.CENTER);
        barraEstado.add(barraProgreso, BorderLayout.EAST);

        add(panelLateral, BorderLayout.WEST);
        add(splitPane, BorderLayout.CENTER);
        add(barraEstado, BorderLayout.SOUTH);
    }

    // --- Métodos de Lógica ---
    private void limpiarLienzo() {
        datosActuales.clear();
        rutaActual.clear();
        areaLog.setText("");
        panelGrafo.setDatos(datosActuales, rutaActual);
        etiquetaEstado.setText(" Lienzo limpio.");
    }

    private void crearTspAleatorio() {
        String s = JOptionPane.showInputDialog(this, "Puntos:", "50");
        if (s == null) {
            return;
        }
        try {
            int n = Integer.parseInt(s);
            datosActuales = generarPuntos(n);
            rutaActual.clear();
            panelGrafo.setDatos(datosActuales, rutaActual);
            log("\n> Generado dataset aleatorio (" + n + ").");

            if (JOptionPane.showConfirmDialog(this, "¿Guardar?", "Guardar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                // Carpeta ficheros en la raíz del proyecto
                File dirFicheros = new File(System.getProperty("user.dir"), "ficheros");
                if (!dirFicheros.exists()) {
                    dirFicheros.mkdirs(); // La crea si no existe
                }
                JFileChooser fc = new JFileChooser(dirFicheros);
                fc.setFileFilter(new FileNameExtensionFilter("Archivos TSP (*.tsp)", "tsp"));

                if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();

                    // Forzar extensión .tsp
                    if (!f.getName().toLowerCase().endsWith(".tsp")) {
                        f = new File(dirFicheros, f.getName() + ".tsp");
                    } else {
                        f = new File(dirFicheros, f.getName());
                    }

                    guardarTSP(f, datosActuales);
                    log("  Guardado en: ficheros/" + f.getName());
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número inválido.");
        }
    }

    private void cargarDataset() {
        File dir = new File(System.getProperty("user.dir"), "ficheros");
        if (!dir.exists()) {
            dir = new File(System.getProperty("user.dir"));
        }

        JFileChooser fc = new JFileChooser(dir);
        fc.setFileFilter(new FileNameExtensionFilter("Archivos TSP", "tsp"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            datosActuales = leerTSP(fc.getSelectedFile());
            rutaActual.clear();
            panelGrafo.setDatos(datosActuales, rutaActual);
            log("\n> Cargado: " + fc.getSelectedFile().getName());
        }
    }

    private void comprobarEstrategias() {
        if (datosActuales.isEmpty()) {
            return;
        }
        setProcesando(true, "Calculando...");
        log("\n=== Resultados Individuales ===");
        log(String.format("%-30s | %-12s | %-12s | %-12s", "Estrategia", "Solución", "Cálculos", "Tiempo(ms)"));

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                EstrategiaTSP[] strats = {new ExhaustivoUnidireccional(), new ExhaustivoBidireccional(), new PodaUnidireccional(), new PodaBidireccional()};
                for (EstrategiaTSP s : strats) {
                    Resultado r = s.resolver(datosActuales);
                    publish(String.format("%-30s | %-12.2f | %-12d | %-12.4f", s.getNombre(), r.getCoste(), r.getDistanciasCalculadas(), r.getTiempoNs() / 1e6));
                    if (s instanceof PodaBidireccional) {
                        rutaActual = r.getRuta();
                        panelGrafo.setDatos(datosActuales, rutaActual);
                    }
                }
                return null;
            }

            @Override
            protected void process(List<String> c) {
                for (String s : c) {
                    log(s);
                }
            }

            @Override
            protected void done() {
                setProcesando(false, "Fin.");
            }
        };
        worker.execute();
    }

    private void compararTodas() {
        setProcesando(true, "Experimentando...");
        log("\n=== Comparativa Global ===");
        log(String.format("%-8s | %-14s | %-14s | %-14s | %-14s", "Talla", "Uni Exh", "Bi Exh", "Uni Poda", "Bi Poda"));

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                int[] tallas = {50, 100, 200, 500};
                EstrategiaTSP[] algos = {new ExhaustivoUnidireccional(), new ExhaustivoBidireccional(), new PodaUnidireccional(), new PodaBidireccional()};
                for (int n : tallas) {
                    double[] tiempos = new double[4];
                    for (int k = 0; k < 10; k++) {
                        List<Punto> pts = generarPuntos(n);
                        for (int i = 0; i < 4; i++) {
                            tiempos[i] += algos[i].resolver(pts).getTiempoNs();
                        }
                    }
                    StringBuilder sb = new StringBuilder(String.format("%-8d | ", n));
                    for (double t : tiempos) {
                        sb.append(String.format("%-14.4f | ", (t / 10) / 1e6));
                    }
                    publish(sb.toString());
                }
                return null;
            }

            @Override
            protected void process(List<String> c) {
                for (String s : c) {
                    log(s);
                }
            }

            @Override
            protected void done() {
                setProcesando(false, "Fin experimentos.");
            }
        };
        worker.execute();
    }

    private void dialogoComparar2() {
        String[] nombres = {"Uni Exhaustivo", "Bi Exhaustivo", "Uni Poda", "Bi Poda"};
        JPanel p = new JPanel(new GridLayout(2, 2));
        JComboBox<String> c1 = new JComboBox<>(nombres);
        JComboBox<String> c2 = new JComboBox<>(nombres);
        p.add(new JLabel("Algoritmo 1:"));
        p.add(c1);
        p.add(new JLabel("Algoritmo 2:"));
        p.add(c2);

        if (JOptionPane.showConfirmDialog(this, p, "Comparar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            ejecutarComparacion2(getEstrategia(c1.getSelectedIndex()), getEstrategia(c2.getSelectedIndex()));
        }
    }

    private void ejecutarComparacion2(EstrategiaTSP s1, EstrategiaTSP s2) {
        setProcesando(true, "Comparando " + s1.getNombre() + " vs " + s2.getNombre());
        log("\n=== Vs: " + s1.getNombre() + " vs " + s2.getNombre() + " ===");
        log("Talla    | T1(ms)         | Calc1        | T2(ms)         | Calc2");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                for (int n : new int[]{50, 100, 200}) {
                    double t1 = 0, t2 = 0;
                    long c1 = 0, c2 = 0;
                    for (int k = 0; k < 10; k++) {
                        List<Punto> pts = generarPuntos(n);
                        Resultado r1 = s1.resolver(pts);
                        Resultado r2 = s2.resolver(pts);
                        t1 += r1.getTiempoNs();
                        c1 += r1.getDistanciasCalculadas();
                        t2 += r2.getTiempoNs();
                        c2 += r2.getDistanciasCalculadas();
                    }
                    publish(String.format("%-8d | %-14.4f | %-12d | %-14.4f | %-12d", n, (t1 / 10) / 1e6, c1 / 10, (t2 / 10) / 1e6, c2 / 10));
                }
                return null;
            }

            @Override
            protected void process(List<String> c) {
                for (String s : c) {
                    log(s);
                }
            }

            @Override
            protected void done() {
                setProcesando(false, "Fin comparación.");
            }
        };
        worker.execute();
    }

    private void compararUniVsBi() {
        setProcesando(true, "Uni vs Bi...");
        log("\n=== Uni (Poda) vs Bi (Poda) ===");
        log("Talla    | Gana Uni   | Gana Bi    | Empate     | T.Uni(ms)      | T.Bi(ms)");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                EstrategiaTSP u = new PodaUnidireccional();
                EstrategiaTSP b = new PodaBidireccional();
                for (int n : new int[]{50, 100, 200, 300}) {
                    int wU = 0, wB = 0, tie = 0;
                    double tU = 0, tB = 0;
                    for (int k = 0; k < 10; k++) {
                        List<Punto> pts = generarPuntos(n);
                        Resultado rU = u.resolver(pts);
                        Resultado rB = b.resolver(pts);
                        if (Math.abs(rU.getCoste() - rB.getCoste()) < 0.0001) {
                            tie++;
                        } else if (rU.getCoste() < rB.getCoste()) {
                            wU++;
                        } else {
                            wB++;
                        }
                        tU += rU.getTiempoNs();
                        tB += rB.getTiempoNs();
                    }
                    publish(String.format("%-8d | %-10d | %-10d | %-10d | %-14.4f | %-14.4f", n, wU, wB, tie, (tU / 10) / 1e6, (tB / 10) / 1e6));
                }
                return null;
            }

            @Override
            protected void process(List<String> c) {
                for (String s : c) {
                    log(s);
                }
            }

            @Override
            protected void done() {
                setProcesando(false, "Fin análisis.");
            }
        };
        worker.execute();
    }

    // --- Helpers ---
    private EstrategiaTSP getEstrategia(int idx) {
        switch (idx) {
            case 0:
                return new ExhaustivoUnidireccional();
            case 1:
                return new ExhaustivoBidireccional();
            case 2:
                return new PodaUnidireccional();
            case 3:
                return new PodaBidireccional();
            default:
                return new ExhaustivoUnidireccional();
        }
    }

    private List<Punto> generarPuntos(int n) {
        List<Punto> l = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            l.add(new Punto(i + 1, r.nextDouble() * 1000, r.nextDouble() * 800));
        }
        return l;
    }

    private void guardarTSP(File f, List<Punto> pts) {
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("NAME: " + f.getName());
            pw.println("TYPE: TSP");
            pw.println("DIMENSION: " + pts.size());
            pw.println("NODE_COORD_SECTION");
            for (Punto p : pts) {
                pw.println(p.getId() + " " + p.getX() + " " + p.getY());
            }
            pw.println("EOF");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Punto> leerTSP(File f) {
        List<Punto> l = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean sec = false;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("EOF")) {
                    break;
                }
                if (sec) {
                    String[] p = line.trim().split("\\s+");
                    if (p.length >= 3) {
                        l.add(new Punto(Integer.parseInt(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2])));
                    }
                }
                if (line.trim().equals("NODE_COORD_SECTION")) {
                    sec = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    private void log(String m) {
        areaLog.append(m + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void setProcesando(boolean b, String m) {
        for (JButton btn : botones) {
            if (btn != null) {
                btn.setEnabled(!b);
            }
        }
        barraProgreso.setVisible(b);
        barraProgreso.setIndeterminate(b);
        etiquetaEstado.setText(" " + m);
        if (!b) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private JButton crearBoton(String t, ActionListener l) {
        JButton b = new JButton(t);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(230, 30));
        b.addActionListener(l);
        return b;
    }

    private JLabel crearEtiquetaSeccion(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setBorder(new EmptyBorder(5, 0, 5, 0));
        return l;
    }
}
