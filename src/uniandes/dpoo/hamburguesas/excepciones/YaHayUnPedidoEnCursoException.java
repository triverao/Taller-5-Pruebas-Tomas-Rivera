package uniandes.dpoo.hamburguesas.excepciones;

@SuppressWarnings("serial")
public class YaHayUnPedidoEnCursoException extends HamburguesaException
{
    private String nombreCliente;

    private String nombreNuevoCliente;

    public YaHayUnPedidoEnCursoException( String nombreCliente, String nombreNuevoCliente )
    {
        this.nombreCliente = nombreCliente;
        this.nombreNuevoCliente = nombreNuevoCliente;
    }

    @Override
    public String getMessage( )
    {
        return "Ya existe un pedido en curso, para el cliente " + nombreCliente + " así que no se puede crear un pedido para " + nombreNuevoCliente;
    }
}
