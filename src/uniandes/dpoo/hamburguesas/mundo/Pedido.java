package uniandes.dpoo.hamburguesas.mundo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Pedido
{
    private static final double IVA = 0.19;

    private static int numeroPedidos;

    private int idPedido;

    private ArrayList<Producto> productos;

    private String nombreCliente;

    private String direccionCliente;

    public Pedido( String nombreCliente, String direccionCliente )
    {
        this.idPedido = numeroPedidos++;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        productos = new ArrayList<Producto>( );
    }

    public int getIdPedido( )
    {
        return idPedido;
    }

    public String getNombreCliente( )
    {
        return nombreCliente;
    }

    public void agregarProducto( Producto nuevoProducto )
    {
        productos.add( nuevoProducto );
    }

    public int getPrecioTotalPedido( )
    {
        return getPrecioNetoPedido( ) + getPrecioIVAPedido( );
    }

    private int getPrecioNetoPedido( )
    {
        int valor = 0;
        for( Producto item : productos )
        {
            valor += item.getPrecio( );
        }
        return valor;
    }

    private int getPrecioIVAPedido( )
    {
        return ( int ) ( getPrecioNetoPedido( ) * IVA );
    }

    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );

        sb.append( "Cliente: " + nombreCliente + "\n" );
        sb.append( "Dirección: " + direccionCliente + "\n" );
        sb.append( "----------------\n" );

        for( Producto item : productos )
        {
            sb.append( item.generarTextoFactura( ) );
        }

        sb.append( "----------------\n" );
        sb.append( "Precio Neto:  " + getPrecioNetoPedido( ) + "\n" );
        sb.append( "IVA:          " + getPrecioIVAPedido( ) + "\n" );
        sb.append( "Precio Total: " + getPrecioTotalPedido( ) + "\n" );

        return sb.toString( );
    }

    public void guardarFactura( File archivo ) throws FileNotFoundException
    {
        String factura = generarTextoFactura( );

        PrintWriter out;
        out = new PrintWriter( archivo );
        out.print( factura );
        out.close( );
    }
}
