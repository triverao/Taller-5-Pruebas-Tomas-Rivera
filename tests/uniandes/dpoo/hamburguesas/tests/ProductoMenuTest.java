package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu producto;

    @BeforeEach
    void setUp( )
    {
        producto = new ProductoMenu( "corral", 14000 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", producto.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, producto.getPrecio( ), "El precio del producto no es el esperado." );
    }

    @Test
    void testGetPrecioCero( )
    {
        ProductoMenu gratis = new ProductoMenu( "agua", 0 );
        assertEquals( 0, gratis.getPrecio( ), "Un producto con precio base 0 debe costar 0." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, producto.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaIncluyeNombreYPrecio( )
    {
        String texto = producto.generarTextoFactura( );
        assertTrue( texto.contains( "corral" ), "La factura debe incluir el nombre del producto." );
        assertTrue( texto.contains( "14000" ), "La factura debe incluir el precio del producto." );
    }
}
