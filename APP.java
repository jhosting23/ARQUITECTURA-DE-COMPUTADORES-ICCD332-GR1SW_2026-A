import Calculadora.conversion.bases.ControlCalculadora;
import Calculadora.conversion.bases.ConvertidorBase;
import Calculadora.conversion.bases.VistaCalculadora;

public class APP {
    public static void main(String[] args) {
        ConvertidorBase modelo = new ConvertidorBase();
        VistaCalculadora vista = new VistaCalculadora();
        ControlCalculadora control = new ControlCalculadora(modelo, vista);
        
        vista.setVisible(true);
    }
}
