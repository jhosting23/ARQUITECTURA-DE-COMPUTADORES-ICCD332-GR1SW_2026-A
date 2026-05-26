package Calculadora.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaCalculadora extends JFrame {
    private JTextField TextoEntrada;
    private JTextField TextoSalida;
    private JComboBox<Integer> comboBaseOrigen;
    private JComboBox<Integer> comboBaseDestino;
    private JButton botonConvertir;

    public VistaCalculadora() {
        setTitle("Calculadora de Conversión de Bases");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Bases típicas: Binario(2), Octal(8), Decimal(10), Hexadecimal(16)
        Integer[] bases = { 2, 8, 10, 16 };

        add(new JLabel("  Base Origen:"));
        comboBaseOrigen = new JComboBox<>(bases);
        comboBaseOrigen.setSelectedItem(10);
        add(comboBaseOrigen);

        add(new JLabel("  Número a convertir:"));
        TextoEntrada = new JTextField();
        add(TextoEntrada);

        add(new JLabel("  Base Destino:"));
        comboBaseDestino = new JComboBox<>(bases);
        comboBaseDestino.setSelectedItem(2);
        add(comboBaseDestino);

        add(new JLabel("  Resultado:"));
        TextoSalida = new JTextField();
        TextoSalida.setEditable(false);
        add(TextoSalida);

        add(new JLabel("")); // Espaciador
        botonConvertir = new JButton("Convertir");
        add(botonConvertir);
    }

    public String getTextoEntrada() {
        return TextoEntrada.getText();
    }

    public int getBaseOrigen() {
        return (Integer) comboBaseOrigen.getSelectedItem();
    }

    public int getBaseDestino() {
        return (Integer) comboBaseDestino.getSelectedItem();
    }

    public void setTexto(String resultado) {
        TextoSalida.setText(resultado);
    }

    public void mostrarError(String mensajeError) {
        JOptionPane.showMessageDialog(this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addListenerConvertidor(ActionListener listener) {
        botonConvertir.addActionListener(listener);
    }
}
