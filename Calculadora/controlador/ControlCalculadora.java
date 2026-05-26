package Calculadora.controlador;

import Calculadora.backend.ConvertidorBase;
import Calculadora.frontend.VistaCalculadora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlCalculadora implements ActionListener {
    private ConvertidorBase modelo;
    private VistaCalculadora vista;

    public ControlCalculadora(ConvertidorBase modelo, VistaCalculadora vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializarControlador();
    }

    public void inicializarControlador() {
        this.vista.addListenerConvertidor(this);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        accionRealizada(evento);
    }

    public void accionRealizada(ActionEvent evento) {
        String numero = vista.getTextoEntrada();
        int baseOrigen = vista.getBaseOrigen();
        int baseDestino = vista.getBaseDestino();

        if (numero == null || numero.trim().isEmpty()) {
            vista.mostrarError("Debe ingresar un número");
            return;
        }

        try {
            modelo.setInsertarDato(numero.trim(), baseOrigen, baseDestino);
            String resultado = modelo.Conversion();
            vista.setTexto(resultado);
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }
}
