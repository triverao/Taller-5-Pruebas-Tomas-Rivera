package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private ArrayList<ProductoMenu> items;
    private Combo combo;

    @BeforeEach
    void setUp( )
    {
        items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 14000 ) );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );
        combo = new Combo( "combo corral", 0.1, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", combo.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecioConDescuento( )
    {
        assertEquals( 22050, combo.getPrecio( ), "El precio debe ser la suma de los productos menos el descuento." );
    }

    @Test
    void testGetPrecioMenorQueSumaDeProductos( )
    {
        assertTrue( combo.getPrecio( ) < 24500, "Un combo con descuento debe costar menos que sus productos por separado." );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "combo normal", 0, items );
        assertEquals( 24500, sinDescuento.getPrecio( ), "Con descuento 0, el precio debe ser la suma de los productos." );
    }

    @Test
    void testGetPrecioDescuentoDecimal( )
    {
        Combo decimal = new Combo( "combo especial", 0.095, items );
        assertEquals( 22172, decimal.getPrecio( ), "El precio con descuento decimal debe truncarse al entero." );
    }

    @Test
    void testGetPrecioDescuentoTotal( )
    {
        Combo gratis = new Combo( "combo gratis", 1, items );
        assertEquals( 0, gratis.getPrecio( ), "Con descuento del 100% el combo debe costar 0." );
    }

    @Test
    void testGetPrecioComboVacio( )
    {
        Combo vacio = new Combo( "combo vacio", 0.1, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ), "Un combo sin productos debe costar 0." );
    }

    @Test
    void testConstructorCopiaLaLista( )
    {
        items.add( new ProductoMenu( "papas grandes", 6900 ) );
        assertEquals( 22050, combo.getPrecio( ), "Modificar la lista original no debe afectar al combo ya creado." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
        assertEquals( esperado, combo.generarTextoFactura( ), "El texto de la factura del combo no es el esperado." );
    }
}
