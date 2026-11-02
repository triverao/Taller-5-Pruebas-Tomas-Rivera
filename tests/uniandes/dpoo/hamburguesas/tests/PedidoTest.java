package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    @TempDir
    Path carpetaTemporal;

    private Pedido pedidoVacio;
    private Pedido pedidoCompleto;
    private ProductoMenu corral;
    private Combo comboCorral;
    private ProductoAjustado corralAjustado;

    @BeforeEach
    void setUp( )
    {
        pedidoVacio = new Pedido( "Tomás", "Calle 19 # 1-10" );

        corral = new ProductoMenu( "corral", 14000 );

        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 14000 ) );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );
        comboCorral = new Combo( "combo corral", 0.1, items );

        corralAjustado = new ProductoAjustado( new ProductoMenu( "corral", 14000 ) );
        corralAjustado.agregarIngrediente( new Ingrediente( "queso americano", 2500 ) );
        corralAjustado.eliminarIngrediente( new Ingrediente( "cebolla", 1000 ) );

        pedidoCompleto = new Pedido( "Ana", "Carrera 7 # 40-62" );
        pedidoCompleto.agregarProducto( corral );
        pedidoCompleto.agregarProducto( comboCorral );
        pedidoCompleto.agregarProducto( corralAjustado );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Tomás", pedidoVacio.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdentificadoresConsecutivos( )
    {
        Pedido p1 = new Pedido( "Cliente 1", "Dir 1" );
        Pedido p2 = new Pedido( "Cliente 2", "Dir 2" );
        assertEquals( p1.getIdPedido( ) + 1, p2.getIdPedido( ), "Cada pedido nuevo debe tener el identificador siguiente." );
    }

    @Test
    void testIdentificadoresUnicos( )
    {
        assertNotEquals( pedidoVacio.getIdPedido( ), pedidoCompleto.getIdPedido( ), "Dos pedidos no pueden tener el mismo identificador." );
    }

    @Test
    void testPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedidoVacio.getPrecioTotalPedido( ), "Un pedido sin productos debe costar 0." );
    }

    @Test
    void testPrecioTotalUnProducto( )
    {
        pedidoVacio.agregarProducto( corral );

        assertEquals( 16660, pedidoVacio.getPrecioTotalPedido( ), "El total debe ser el neto más el 19% de IVA." );
    }

    @Test
    void testPrecioTotalPedidoCompleto( )
    {
        assertEquals( 62534, pedidoCompleto.getPrecioTotalPedido( ), "El precio total del pedido con varios tipos de producto no es el esperado." );
    }

    @Test
    void testIVATruncado( )
    {
        pedidoVacio.agregarProducto( new ProductoMenu( "dulce", 1001 ) );

        assertEquals( 1191, pedidoVacio.getPrecioTotalPedido( ), "El IVA debe calcularse como el 19% del neto truncado a entero." );
    }

    @Test
    void testAgregarProductoActualizaPrecio( )
    {
        int antes = pedidoVacio.getPrecioTotalPedido( );
        pedidoVacio.agregarProducto( new ProductoMenu( "gaseosa", 5000 ) );
        assertEquals( antes + 5950, pedidoVacio.getPrecioTotalPedido( ), "Agregar un producto de 5000 debe aumentar el total en 5950 (con IVA)." );
    }

    @Test
    void testGenerarTextoFacturaPedidoVacio( )
    {
        String esperado = "Cliente: Tomás\n" + "Dirección: Calle 19 # 1-10\n" + "----------------\n" + "----------------\n" + "Precio Neto:  0\n" + "IVA:          0\n"
                + "Precio Total: 0\n";
        assertEquals( esperado, pedidoVacio.generarTextoFactura( ), "La factura de un pedido vacío no es la esperada." );
    }

    @Test
    void testGenerarTextoFacturaPedidoCompleto( )
    {
        String esperado = "Cliente: Ana\n" + "Dirección: Carrera 7 # 40-62\n" + "----------------\n"

                + "corral\n" + "            14000\n"

                + "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n"

                + "corral\n" + "    +queso americano                2500\n" + "    -cebolla\n" + "            16500\n"

                + "----------------\n" + "Precio Neto:  52550\n" + "IVA:          9984\n" + "Precio Total: 62534\n";

        assertEquals( esperado, pedidoCompleto.generarTextoFactura( ), "La factura del pedido completo no es la esperada." );
    }

    @Test
    void testGenerarTextoFacturaContieneTextoDeCadaProducto( )
    {
        String factura = pedidoCompleto.generarTextoFactura( );
        assertTrue( factura.contains( corral.generarTextoFactura( ) ), "La factura debe incluir el texto del producto del menú." );
        assertTrue( factura.contains( comboCorral.generarTextoFactura( ) ), "La factura debe incluir el texto del combo." );
        assertTrue( factura.contains( corralAjustado.generarTextoFactura( ) ), "La factura debe incluir el texto del producto ajustado." );
    }

    @Test
    void testGuardarFactura( ) throws IOException
    {
        File archivo = carpetaTemporal.resolve( "factura_prueba.txt" ).toFile( );
        pedidoCompleto.guardarFactura( archivo );

        assertTrue( archivo.exists( ), "El archivo de la factura debería haberse creado." );
        String contenido = new String( Files.readAllBytes( archivo.toPath( ) ), Charset.defaultCharset( ) );
        assertEquals( pedidoCompleto.generarTextoFactura( ), contenido, "El contenido del archivo debe ser exactamente el texto de la factura." );
    }

    @Test
    void testGuardarFacturaSobrescribeArchivo( ) throws IOException
    {
        File archivo = carpetaTemporal.resolve( "factura_prueba.txt" ).toFile( );
        Files.write( archivo.toPath( ), "contenido viejo que no debe quedar".getBytes( ) );

        pedidoVacio.guardarFactura( archivo );

        String contenido = new String( Files.readAllBytes( archivo.toPath( ) ), Charset.defaultCharset( ) );
        assertEquals( pedidoVacio.generarTextoFactura( ), contenido, "Guardar la factura debe reemplazar el contenido anterior del archivo." );
    }

    @Test
    void testGuardarFacturaRutaInvalida( )
    {
        File archivo = carpetaTemporal.resolve( "carpeta_inexistente" ).resolve( "factura.txt" ).toFile( );
        assertThrows( FileNotFoundException.class, ( ) -> pedidoCompleto.guardarFactura( archivo ),
                "Debe lanzarse FileNotFoundException si no se puede crear el archivo." );
    }

    @Test
    void testGuardarFacturaEnUnDirectorio( )
    {
        File directorio = carpetaTemporal.toFile( );
        assertThrows( FileNotFoundException.class, ( ) -> pedidoCompleto.guardarFactura( directorio ),
                "Debe lanzarse FileNotFoundException si la ruta es un directorio." );
    }
}
