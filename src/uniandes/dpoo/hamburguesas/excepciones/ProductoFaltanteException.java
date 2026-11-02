package uniandes.dpoo.hamburguesas.excepciones;

@SuppressWarnings("serial")
public class ProductoFaltanteException extends HamburguesaException
{
    private String nombreProducto;

    public ProductoFaltanteException( String nombreProducto )
    {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String getMessage( )
    {
        return "El producto " + nombreProducto + " no aparece en la información del restaurante";
    }
}
