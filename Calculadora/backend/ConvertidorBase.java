package Calculadora.backend;

public class ConvertidorBase {
    private String Numero;
    private int BaseOrigen;
    private int BaseDestino;

    public void setInsertarDato(String Numero, int from, int to) {
        this.Numero = Numero;
        this.BaseOrigen = from;
        this.BaseDestino = to;
    }

    private int ConvertiraDecimal() {
        return Integer.parseInt(this.Numero, this.BaseOrigen);
    }

    private String ConvertirdeDecimal(int ValorDecimal) {
        if (this.BaseDestino == 10) return String.valueOf(ValorDecimal);
        return Integer.toString(ValorDecimal, this.BaseDestino).toUpperCase();
    }

    public String Conversion() {
        try {
            int decimal = ConvertiraDecimal();
            return ConvertirdeDecimal(decimal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido para la base de origen seleccionada");
        }
    }
}
