import Calculadora.backend.ConvertidorBase;
import Calculadora.frontend.VistaCalculadora;
import Calculadora.controlador.ControlCalculadora;

public class APP {
    public static void main(String[] args) {
        ConvertidorBase modelo = new ConvertidorBase();
        VistaCalculadora vista = new VistaCalculadora();
        ControlCalculadora control = new ControlCalculadora(modelo, vista);
        
        vista.setVisible(true);
    }
}
