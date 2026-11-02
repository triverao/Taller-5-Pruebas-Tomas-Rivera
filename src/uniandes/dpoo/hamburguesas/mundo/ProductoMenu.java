package uniandes.dpoo.hamburguesas.mundo;

public class ProductoMenu implements Producto
{
    private String nombre;

    private int precioBase;

    public ProductoMenu( String nombre, int precioBase )
    {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    @Override
    public String getNombre( )
    {
        return nombre;
    }

    @Override
    public int getPrecio( )
    {
        return precioBase;
    }

    @Override
    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );
        sb.append( nombre + "\n" );
        sb.append( "            " + getPrecio( ) + "\n" );

        return sb.toString( );
    }
}
