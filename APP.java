import Calculadora.backend.ConvertidorBase;
import Calculadora.controlador.ControlCalculadora;
import Calculadora.frontend.VistaCalculadora;

public class APP {
    public static void main(String[] args) {
        ConvertidorBase modelo = new ConvertidorBase();
        VistaCalculadora vista = new VistaCalculadora();
        ControlCalculadora control = new ControlCalculadora(modelo, vista);
        
        vista.setVisible(true);
    }
}
