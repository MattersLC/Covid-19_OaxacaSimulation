package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SEIR extends JFrame implements ActionListener {

    int totalDias = 200; // Días a simular
    int N = 4132000; // Población
    // Variables modelo SEIR
    int I0 = 1; // Infectados iniciales
    int E0 = 1; // Expuestos iniciales
    double beta = 0.5; // Tasa de infección
    double beta2 = 0.8; // Tasa de infección
    double beta3 = 2.3; // Tasa de infección
    double gamma = 1.0/10.0; // Tiempo de recuperación
    double lambda = 100; // Tasa de natalidad
    double a = 1.0/2.0; // Periodo de incubación
    double mi = 0.18; // Tasa de defunción
    double ipsilon = 0.567; // Población vacunada
    double gamma2 = 0;
    double sigma = 2.33; // Tasa de contagio (tasa variable a simular)
    // Simulación de las variables SEIR durante el tiempo
    List<Double> S = new ArrayList<>();
    List<Double> E = new ArrayList<>();
    List<Double> I = new ArrayList<>();
    List<Double> R = new ArrayList<>();
    // Gráfica de infectados
    List<Double> I1 = new ArrayList<>();
    List<Double> I2 = new ArrayList<>();
    List<Double> I3 = new ArrayList<>();
    List<Double> S2 = new ArrayList<>();
    List<Double> E2 = new ArrayList<>();
    List<Double> R2 = new ArrayList<>();
    List<Double> S3 = new ArrayList<>();
    List<Double> E3 = new ArrayList<>();
    List<Double> R3 = new ArrayList<>();

    // Variables modelo Verhulst
    double K = 0.999; // Número máximo de casos acumulados
    double gamma3 = 0.1822; // Tasa intrínseca de crecimiento per cápita
    double tau = 33.9911;
    double epsilon = 0;
    double L = 0.0000005;
    double euler = 2.718;
    double exponent = -0.238;
    double potencia=0;
    int t = 100; // Días a simular
    // Simulación de la función C(t) durante el tiempo
    List<Double> C = new ArrayList<>();

    // Variables modelo Gompertz
    double aG = 2000000;
    double b = 6.77;
    double c = 0.05;
    double potencia1 = 0;
    double potencia2 = 0;
    int tTiempo = 100;
    List<Double> Ft = new ArrayList<>();

    // Variables para la interfaz gráfica
    public JFrame simulador = new JFrame("Simulador"); // Frame principal
    public JPanel panelTitulo = new JPanel();
    public JPanel panelGraph = new JPanel();
    public JPanel panelSEIR = new JPanel();
    public ChartPanel panelSEIRG1; // Primera gráfica SEIR
    public ChartPanel panelSEIRG2; // Segunda gráfica SEIR
    public JPanel panelVerhulst = new JPanel();
    public ChartPanel panelVerhulstG1; // Gráfica Verhulst
    public JPanel panelGompertz = new JPanel();
    public ChartPanel panelGompertzG1; // Gráfica Gompertz
    public JPanel panelButtons = new JPanel();
    public JButton bSEIR = new JButton("Modelo SEIR");
    public JButton bVerhulst = new JButton("Modelo Verhulst");
    public JButton bGompertz = new JButton("Modelo Gompertz");
    // En el sig. panel se agregará toda la información que se mostrará en la interfaz
    public JPanel panelInfo = new JPanel(new BorderLayout());
    JLabel titleV = new JLabel("Modelo Verhulst");
    JLabel titleG = new JLabel("Modelo Gompertz");
    JLabel titleS = new JLabel("Modelo SEIR");
    JLabel info = new JLabel();

    private void iniciarSimulacionSEIR() {
        // Creamos las líneas que serán graficadas según el modelo
        XYSeries lineaS = new XYSeries("(S)usceptibles");
        XYSeries lineaE = new XYSeries("(E)xpuestos");
        XYSeries lineaI = new XYSeries("(I)nfectados");
        XYSeries lineaR = new XYSeries("(R)ecuperados");
        final int R0 = 0; // Recuperados (variación en el tiempo)
        // Población susceptible (población total - menos infectados - Recuperados)
        final int S0 = N - E0 - I0 - R0;
        // Agregamos el valor inicial del que partiremos para graficar
        S.add((double) S0);
        E.add((double) E0);
        I.add((double) I0);
        R.add((double) R0);
        // Se calcula el valor en cada día de las variables
        for (int dia=1; dia<totalDias+1; dia++) {
            double[] ecuaciones = calculoSEIRG1(dia);
            // Se guardan los valores de newS, newE, newI y newR en los arreglos S, E, I y R
            S.add(ecuaciones[0]);
            E.add(ecuaciones[1]);
            I.add(ecuaciones[2]);
            R.add(ecuaciones[3]);
            // Agregamos los valores nuevos de los arreglos S, E, I y R a las líneas, para poder graficarlas
            lineaS.add(dia, S.get(dia));
            lineaE.add(dia, E.get(dia));
            lineaI.add(dia, I.get(dia));
            lineaR.add(dia, R.get(dia));
        }

        // Creamos un "arreglo" de líneas para poder visualizar las 4 al mismo tiempo
        XYSeriesCollection datacol = new XYSeriesCollection();
        datacol.addSeries(lineaS);
        datacol.addSeries(lineaE);
        datacol.addSeries(lineaI);
        datacol.addSeries(lineaR);

        // Creamos la gráfica
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "", // Titulo
                "Días", // Etiqueta de datos
                "Población", // Etiqueta de valores
                datacol, // Datos
                PlotOrientation.VERTICAL, // orientacion
                true, // Incluye leyenda
                true, // Incluye tooltips
                false // urls
        );

        XYPlot plot = xyLineChart.getXYPlot();
        // Le asignamos un color y ancho personalizado a cada linea
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesPaint(3, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));
        renderer.setSeriesStroke(3, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        // Agregamos la gráfica a nuestro panel
        panelSEIRG1 = new ChartPanel(xyLineChart);
        // Establecemos el layout del panel principal de nuestras dos gráficas SEIR
        panelSEIR.setLayout(new BorderLayout());
        panelSEIR.add(panelSEIRG1, BorderLayout.WEST); // Asignamos la primer gráfica creada en el lado izquierdo
    }

    private void iniciarSimulacionSEIR2() {
        XYSeries lineaI1 = new XYSeries("Tasa de contagio: 0.5");
        XYSeries lineaI2 = new XYSeries("Tasa de contagio: 0.8");
        XYSeries lineaI3 = new XYSeries("Tasa de contagio: 2.3");

        final int R0 = 0; // Recuperados (variación en el tiempo)
        final int S0 = N - E0 - I0 - R0; // Población susceptible (población total - menos infectados - Recuperados)

        S2.add((double) S0);
        E2.add((double) E0);
        R2.add((double) R0);

        I1.add((double) I0);
        I2.add((double) I0);
        I3.add((double) I0);

        S3.add((double) S0);
        E3.add((double) E0);
        R3.add((double) R0);

        for (int dia=1; dia<totalDias+1; dia++) {
            double[] ecuaciones = calculoSEIRG2(dia);
            S2.add(ecuaciones[0]);
            E2.add(ecuaciones[1]);
            I2.add(ecuaciones[2]);
            R2.add(ecuaciones[3]);
            S3.add(ecuaciones[4]);
            E3.add(ecuaciones[5]);
            I3.add(ecuaciones[6]);
            R3.add(ecuaciones[7]);
            I1.add(ecuaciones[8]);
            lineaI1.add(dia, I1.get(dia));
            lineaI2.add(dia, I2.get(dia));
            lineaI3.add(dia, I3.get(dia));
        }

        XYSeriesCollection datacol = new XYSeriesCollection();
        datacol.addSeries(lineaI1);
        datacol.addSeries(lineaI2);
        datacol.addSeries(lineaI3);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "", // Titulo
                "Días", // Etiqueta de datos
                "Población", // Etiqueta de valores
                datacol, // Datos
                PlotOrientation.VERTICAL, // orientacion
                true, // Incluye leyenda
                true, // Incluye tooltips
                false // urls
        );

        XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.YELLOW);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        panelSEIRG2 = new ChartPanel(xyLineChart);

        panelSEIR.add(panelSEIRG2, BorderLayout.EAST);
    }

    private void iniciarSimulacionVerhulst() {
        XYSeries lineaC = new XYSeries("Contagios");

        C.add(0.0);

        for (int dia=1; dia<t+1; dia++) {
            double[] ecuacion = calculoVerhulst(dia);
            C.add(ecuacion[0]);
            lineaC.add(dia, C.get(dia));
        }

        XYSeriesCollection datacol = new XYSeriesCollection();
        datacol.addSeries(lineaC);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "", // Titulo
                "Días", // Etiqueta de datos
                "Infectados", // Etiqueta de valores
                datacol, // Datos
                PlotOrientation.VERTICAL, // orientacion
                true, // Incluye leyenda
                true, // Incluye tooltips
                false // urls
        );

        XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.ORANGE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        panelVerhulstG1 = new ChartPanel(xyLineChart);

        panelVerhulst.setLayout(new BorderLayout());
        panelVerhulst.add(panelVerhulstG1);
    }

    private void iniciarSimulacionGomertz() {
        XYSeries lineaFt = new XYSeries("Contagios");

        Ft.add(0.0);

        for (int dia=1; dia<tTiempo+1; dia++) {
            double[] ecuacion = calculoGompertz(dia);
            Ft.add(ecuacion[0]);
            lineaFt.add(dia, Ft.get(dia));
        }

        XYSeriesCollection datacol = new XYSeriesCollection();
        datacol.addSeries(lineaFt);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "", // Titulo
                "Días", // Etiqueta de datos
                "Infectados", // Etiqueta de valores
                datacol, // Datos
                PlotOrientation.VERTICAL, // orientacion
                true, // Incluye leyenda
                true, // Incluye tooltips
                false // urls
        );

        XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        panelGompertzG1 = new ChartPanel(xyLineChart);

        panelGompertz.setLayout(new BorderLayout());
        panelGompertz.add(panelGompertzG1);
    }

    // Agregamos el panel con la gráfica del modelo SEIR
    public void Graph1_SEIR() {
        panelGraph.removeAll();
        JPanel ws = new JPanel(new BorderLayout());
        ws.setBackground(Color.WHITE);
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(Color.WHITE);
        JPanel es = new JPanel(new BorderLayout());
        es.setBackground(Color.WHITE);
        JLabel info2 = new JLabel();
        JLabel cen = new JLabel("                       ");
        labelTexto(titleS, 32);
        info.setText("<html><body>Este modelo nos ofrece una estimación de 1,700,174 casos acumulados en el día<br>" +
                                "74, siendo este el pico máximo esperado. A partir de este día se espera que los<br>" +
                                "casos comiencen a descender.</body></html>");
        info2.setText("<html><body>Se realizó una simulación variando la velocidad de la tasa de transmisión<br>" +
                                "en 3 valores distintos para poder aproximar diferentes picos de contagios<br>" +
                                "en el estado, basado en esto, los resultados concluyen que con una tasa de<br>" +
                                "contagio de 0.8 el pico máximo se alcanza el día 52 con 2,191,583 infectados,<br>" +
                                "y con una tasa de 2.3 el pico llegaría el día 28 con 2,831,577 infectados.</body></html>");
        labelTexto(info, 20);
        labelTexto(info2, 20);
        ws.add(titleS, BorderLayout.NORTH);
        ws.add(info, BorderLayout.CENTER);
        c.add(cen);
        es.add(info2);
        panelInfo.add(ws, BorderLayout.WEST);
        panelInfo.add(c, BorderLayout.CENTER);
        panelInfo.add(es, BorderLayout.EAST);
        panelGraph.add(panelSEIR, BorderLayout.CENTER);
        panelGraph.add(panelInfo, BorderLayout.SOUTH);
    }

    // Agregamos el panel con la gráfica del modelo Verhulst
    private void Graph2_Verhulst() {
        panelGraph.removeAll();
        labelTexto(titleV, 32);
        info.setText( "<html><body>Este modelo nos ofrece una estimación de 1,999,765 casos<br>" +
                                    "acumulados en el día 100.<br>" +
                                    "Asimismo, el avance en el incremento de casos es similar<br>" +
                                    "con respecto al resto de modelos, en este, obtenemos la<br>" +
                                    "aproximación de 1,892,131 casos en el día 74, cifra que<br>" +
                                    "se corresponde con los 1.6M y 1.7M de casos que estiman<br>" +
                                    "los modelos SEIR y Gompertz (respectivamente) para el<br>" +
                                    "mismo día<br><br></body></html>");
        labelTexto(info, 26);
        panelInfo.setBackground(Color.WHITE);
        panelInfo.add(titleV, BorderLayout.NORTH);
        panelInfo.add(info, BorderLayout.CENTER);
        panelGraph.add(panelVerhulst, BorderLayout.CENTER);
        panelGraph.add(panelInfo, BorderLayout.EAST);
    }

    // Agregamos el panel con la gráfica del modelo Gompertz
    private void Graph3_Gompertz() {
        panelGraph.removeAll();
        labelTexto(titleG, 32);
        info.setText( "<html><body>Este modelo nos ofrece una estimación de 1,906,316 casos<br>" +
                                    "acumulados en el día 100.<br>" +
                                    "Asimismo, el avance en el incremento de casos es similar<br>" +
                                    "con respecto al resto de modelos, en este, obtenemos la<br>" +
                                    "aproximación de 1,677,222 casos en el día 74, cifra que<br>" +
                                    "se corresponde con los 1.7M y 1.8M de casos que estiman<br>" +
                                    "los modelos SEIR y Verhulst (respectivamente) para el<br>" +
                                    "mismo día<br><br></body></html>");
        labelTexto(info, 26);
        panelInfo.setBackground(Color.WHITE);
        panelInfo.add(titleG, BorderLayout.NORTH);
        panelInfo.add(info, BorderLayout.CENTER);
        panelGraph.add(panelGompertz, BorderLayout.CENTER);
        panelGraph.add(panelInfo, BorderLayout.EAST);
    }

    public void crearUI() {
        simulador.setVisible(true);
        simulador.setSize(1400, 685);
        simulador.setLocationRelativeTo(null);
        simulador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelTitulo.setLayout(new BorderLayout());
        panelTitulo.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("                                            Simulación de la variable ómicron en Oaxaca");
        titulo.setFont(new Font("Serif", Font.PLAIN, 35));
        panelTitulo.add(titulo, BorderLayout.CENTER);

        panelButtons.setLayout(new FlowLayout());
        panelButtons.setBackground(Color.WHITE);
        bSEIR.setActionCommand("bSEIR");
        bSEIR.addActionListener(this);
        bSEIR.setBackground(Color.WHITE);
        bSEIR.setForeground(Color.BLACK);
        bSEIR.setFocusPainted(false);
        panelButtons.add(bSEIR);

        bVerhulst.setActionCommand("bVerhulst");
        bVerhulst.addActionListener(this);
        bVerhulst.setBackground(Color.WHITE);
        bVerhulst.setForeground(Color.BLACK);
        bVerhulst.setFocusPainted(false);
        panelButtons.add(bVerhulst);

        bGompertz.setActionCommand("bGompertz");
        bGompertz.addActionListener(this);
        bGompertz.setBackground(Color.WHITE);
        bGompertz.setForeground(Color.BLACK);
        bGompertz.setFocusPainted(false);
        panelButtons.add(bGompertz);

        panelGraph.setBackground(Color.WHITE);
        JLabel presentacion = new JLabel("<html><body><br><br><br><br><br><br><br><font color='blue'>Presiona cualquier botón para iniciar con la simulación</html></body>");
        labelTexto(presentacion, 28);
        panelGraph.add(presentacion, BorderLayout.CENTER);
        simulador.setLayout(new BorderLayout());
        simulador.add(panelTitulo, BorderLayout.NORTH);
        simulador.add(panelGraph, BorderLayout.CENTER);
        simulador.add(panelButtons, BorderLayout.SOUTH);
    }

    // Predicción de la variable ómicron usando el modelo SEIR (Gráfica general, líneas S, E, I y R)
    private double[] calculoSEIRG1(int dia) {
        dia = dia - 1;
        double dS = (beta * S.get(dia) * I.get(dia)) / N;
        double newS = S.get(dia) - (dS);
        double newE = E.get(dia) + (dS - (a * E.get(dia)));
        double newI = I.get(dia) + ((a * E.get(dia)) - (gamma * I.get(dia)));
        double newR = R.get(dia) + (gamma * I.get(dia));
        return new double[] {newS, newE, newI, newR};
    }

    // Predicción de la variable ómicron usando el modelo SEIR
    // (Gráfica sólo infectados, variaciones de la línea (I)nfectador variando la tasa de contagio)
    private double[] calculoSEIRG2(int dia) {
        dia = dia - 1;
        double newI = I.get(dia) + ((a * E.get(dia)) - (gamma * I.get(dia)));

        double dS2 = (beta2 * S2.get(dia) * I2.get(dia)) / N;
        double newS2 = S2.get(dia) - (dS2);
        double newE2 = E2.get(dia) + (dS2 - (a * E2.get(dia)));
        double newI2 = I2.get(dia) + ((a * E2.get(dia)) - (gamma * I2.get(dia)));
        double newR2 = R2.get(dia) + (gamma * I2.get(dia));

        double dS3 = (beta3 * S3.get(dia) * I3.get(dia)) / N;
        double newS3 = S3.get(dia) - (dS3);
        double newE3 = E3.get(dia) + (dS3 - (a * E3.get(dia)));
        double newI3 = I3.get(dia) + ((a * E3.get(dia)) - (gamma * I3.get(dia)));
        double newR3 = R3.get(dia) + (gamma * I3.get(dia));
        return new double[] {newS2, newE2, newI2, newR2, newS3, newE3, newI3, newR3, newI};
    }

    // Predicción de la variable ómicron usando el modelo Verhulst
    private double[] calculoVerhulst(int dia) {
        dia = dia -1;
        potencia = Math.pow(euler, (exponent*dia));
        double newC = 1 / (L + (K*potencia));
        return new double[] {newC};
    }

    // Predicción de la variable ómicron usando el modelo Gompertz
    private double[] calculoGompertz(int dia) {
        dia = dia -1;
        potencia1 = Math.pow(euler, (-c*dia));
        potencia2 = Math.pow(euler, (-b*potencia1));
        double ft = aG*potencia2;
        return new double[] {ft};
    }

    public SEIR() {
        iniciarSimulacionSEIR();
        iniciarSimulacionSEIR2();
        iniciarSimulacionVerhulst();
        iniciarSimulacionGomertz();
        crearUI();
    }

    public static void main(String[] args) {
        new SEIR();
    }

    public void btnClicked (JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
    }

    public void btnExit (JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
    }

    public void labelTexto (JLabel jl, int fuente) {
        jl.setFont(new Font("Serif", Font.PLAIN, fuente));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();

        switch (button) {
            case "bSEIR":
                panelInfo.removeAll();
                Graph1_SEIR();
                panelGraph.updateUI();
                btnClicked(bSEIR);
                btnExit(bVerhulst);
                btnExit(bGompertz);
                break;
            case "bVerhulst":
                panelInfo.removeAll();
                Graph2_Verhulst();
                panelGraph.updateUI();
                btnClicked(bVerhulst);
                btnExit(bSEIR);
                btnExit(bGompertz);
                break;
            case "bGompertz":
                panelInfo.removeAll();
                Graph3_Gompertz();
                panelGraph.updateUI();
                btnClicked(bGompertz);
                btnExit(bSEIR);
                btnExit(bVerhulst);
                break;
            default: JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado");
        }
    }
}
