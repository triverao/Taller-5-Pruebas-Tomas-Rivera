package uniandes.dpoo.hamburguesas.excepciones;

@SuppressWarnings("serial")
public class IngredienteRepetidoException extends HamburguesaException
{
    private String nombreIngrediente;

    public IngredienteRepetidoException( String nombreIngrediente )
    {
        this.nombreIngrediente = nombreIngrediente;
    }

    @Override
    public String getMessage( )
    {
        return "El ingrediente " + nombreIngrediente + " está repetido";
    }
}
