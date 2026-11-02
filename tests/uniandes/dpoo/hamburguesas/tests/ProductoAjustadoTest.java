package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente cebolla;

    @BeforeEach
    void setUp( )
    {
        base = new ProductoMenu( "corral", 14000 );
        ajustado = new ProductoAjustado( base );
        queso = new Ingrediente( "queso americano", 2500 );
        tocineta = new Ingrediente( "tocineta picada", 6000 );
        cebolla = new Ingrediente( "cebolla", 1000 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ), "El nombre debe ser el del producto base." );
    }

    @Test
    void testGetPrecioSinAjustes( )
    {
        assertEquals( 14000, ajustado.getPrecio( ), "Sin ajustes, el precio debe ser igual al del producto base." );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( tocineta );
        assertEquals( 14000 + 2500 + 6000, ajustado.getPrecio( ), "El precio debe sumar el costo de los ingredientes agregados." );
    }

    @Test
    void testGetPrecioIngredienteRepetido( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        assertEquals( 14000 + 2 * 2500, ajustado.getPrecio( ), "Si un ingrediente se agrega dos veces, se cobra dos veces." );
    }

    @Test
    void testGetPrecioEliminarNoDescuenta( )
    {
        ajustado.eliminarIngrediente( cebolla );
        assertEquals( 14000, ajustado.getPrecio( ), "Quitar un ingrediente no debe reducir el precio." );
    }

    @Test
    void testGetPrecioAgregadosYEliminados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( cebolla );
        assertEquals( 16500, ajustado.getPrecio( ), "Solo los ingredientes agregados deben afectar el precio." );
    }

    @Test
    void testGenerarTextoFacturaSinAjustes( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura sin ajustes no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( cebolla );

        String esperado = "corral\n" + "    +queso americano                2500\n" + "    -cebolla\n" + "            16500\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura con ajustes no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaUsaNombreDelProducto( )
    {
        String texto = ajustado.generarTextoFactura( );
        assertTrue( texto.startsWith( "corral" ), "La factura debe empezar con el nombre del producto base." );
        assertFalse( texto.contains( "ProductoMenu@" ), "La factura no debe mostrar la referencia del objeto (toString por defecto)." );
    }
}
