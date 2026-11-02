package uniandes.dpoo.hamburguesas.excepciones;

@SuppressWarnings("serial")
public class ProductoRepetidoException extends HamburguesaException
{
    private String nombreProducto;

    public ProductoRepetidoException( String nombreProducto )
    {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String getMessage( )
    {
        return "El producto " + nombreProducto + " está repetido";
    }
}
