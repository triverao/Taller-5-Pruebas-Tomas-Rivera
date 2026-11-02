package uniandes.dpoo.hamburguesas.excepciones;

@SuppressWarnings("serial")
public class NoHayPedidoEnCursoException extends HamburguesaException
{
    @Override
    public String getMessage( )
    {
        return "Actualmente no hay un pedido en curso";
    }
}
