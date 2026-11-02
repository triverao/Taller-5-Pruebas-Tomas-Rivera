package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private static final String CARPETA_PRUEBAS = "./data/pruebas/";
    private static final String CARPETA_FACTURAS = "./facturas/";

    private static final File INGREDIENTES_VALIDOS = new File( CARPETA_PRUEBAS + "ingredientes_validos.txt" );
    private static final File MENU_VALIDO = new File( CARPETA_PRUEBAS + "menu_valido.txt" );
    private static final File COMBOS_VALIDOS = new File( CARPETA_PRUEBAS + "combos_validos.txt" );

    private Restaurante restaurante;

    private ArrayList<File> facturasGeneradas;

    @BeforeEach
    void setUp( )
    {
        restaurante = new Restaurante( );
        facturasGeneradas = new ArrayList<File>( );
    }

    @AfterEach
    void tearDown( )
    {
        for( File f : facturasGeneradas )
        {
            f.delete( );
        }
    }

    private File archivoFactura( Pedido pedido )
    {
        File f = new File( CARPETA_FACTURAS + "factura_" + pedido.getIdPedido( ) + ".txt" );
        facturasGeneradas.add( f );
        return f;
    }

    private void cargarEscenarioValido( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( INGREDIENTES_VALIDOS, MENU_VALIDO, COMBOS_VALIDOS );
    }

    @Test
    void testRestauranteNuevoVacio( )
    {
        assertTrue( restaurante.getPedidos( ).isEmpty( ), "Un restaurante nuevo no debe tener pedidos." );
        assertTrue( restaurante.getIngredientes( ).isEmpty( ), "Un restaurante nuevo no debe tener ingredientes." );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ), "Un restaurante nuevo no debe tener productos en el menú." );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ), "Un restaurante nuevo no debe tener combos." );
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debe tener un pedido en curso." );
    }

    @Test
    void testCargarIngredientes( ) throws Exception
    {
        cargarEscenarioValido( );
        ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes( );
        assertEquals( 3, ingredientes.size( ), "Deben cargarse 3 ingredientes (la línea vacía se ignora)." );
        assertEquals( "lechuga", ingredientes.get( 0 ).getNombre( ) );
        assertEquals( 1000, ingredientes.get( 0 ).getCostoAdicional( ) );
        assertEquals( "queso americano", ingredientes.get( 2 ).getNombre( ) );
        assertEquals( 2500, ingredientes.get( 2 ).getCostoAdicional( ) );
    }

    @Test
    void testCargarMenu( ) throws Exception
    {
        cargarEscenarioValido( );
        ArrayList<ProductoMenu> menu = restaurante.getMenuBase( );
        assertEquals( 4, menu.size( ), "Deben cargarse 4 productos (la línea vacía se ignora)." );
        assertEquals( "corral", menu.get( 0 ).getNombre( ) );
        assertEquals( 14000, menu.get( 0 ).getPrecio( ) );
        assertEquals( "especial", menu.get( 3 ).getNombre( ) );
        assertEquals( 24000, menu.get( 3 ).getPrecio( ) );
    }

    @Test
    void testCargarCombos( ) throws Exception
    {
        cargarEscenarioValido( );
        ArrayList<Combo> combos = restaurante.getMenuCombos( );
        assertEquals( 2, combos.size( ), "Deben cargarse 2 combos (la línea vacía se ignora)." );

        Combo corral = combos.get( 0 );
        assertEquals( "combo corral", corral.getNombre( ) );

        assertEquals( 22050, corral.getPrecio( ), "El precio del combo cargado debe aplicar el 10% de descuento." );

        Combo especial = combos.get( 1 );
        assertEquals( "combo especial", especial.getNombre( ) );

        assertEquals( 31222, especial.getPrecio( ), "El precio del combo cargado debe aplicar el 9.5% de descuento." );
    }

    @Test
    void testCargarArchivosReales( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( "./data/ingredientes.txt" ), new File( "./data/menu.txt" ), new File( "./data/combos.txt" ) );
        assertEquals( 15, restaurante.getIngredientes( ).size( ), "El archivo real tiene 15 ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "El archivo real tiene 22 productos." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "El archivo real tiene 4 combos." );
    }

    @Test
    void testCargarIngredienteRepetido( )
    {
        IngredienteRepetidoException e = assertThrows( IngredienteRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( CARPETA_PRUEBAS + "ingredientes_repetidos.txt" ), MENU_VALIDO, COMBOS_VALIDOS ) );
        assertEquals( "El ingrediente lechuga está repetido", e.getMessage( ) );
    }

    @Test
    void testCargarIngredienteCostoInvalido( )
    {
        assertThrows( NumberFormatException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( CARPETA_PRUEBAS + "ingredientes_costo_invalido.txt" ), MENU_VALIDO, COMBOS_VALIDOS ) );
    }

    @Test
    void testCargarProductoRepetido( )
    {
        ProductoRepetidoException e = assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( INGREDIENTES_VALIDOS, new File( CARPETA_PRUEBAS + "menu_repetido.txt" ), COMBOS_VALIDOS ) );
        assertEquals( "El producto corral está repetido", e.getMessage( ) );
    }

    @Test
    void testCargarComboRepetido( )
    {
        ProductoRepetidoException e = assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( INGREDIENTES_VALIDOS, MENU_VALIDO, new File( CARPETA_PRUEBAS + "combos_repetidos.txt" ) ) );
        assertEquals( "El producto combo corral está repetido", e.getMessage( ) );
    }

    @Test
    void testCargarComboProductoFaltante( )
    {
        ProductoFaltanteException e = assertThrows( ProductoFaltanteException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( INGREDIENTES_VALIDOS, MENU_VALIDO, new File( CARPETA_PRUEBAS + "combos_producto_faltante.txt" ) ) );
        assertEquals( "El producto malteada no aparece en la información del restaurante", e.getMessage( ) );
    }

    @Test
    void testCargarArchivoInexistente( )
    {
        assertThrows( FileNotFoundException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( CARPETA_PRUEBAS + "no_existe.txt" ), MENU_VALIDO, COMBOS_VALIDOS ) );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Tomás", "Calle 19 # 1-10" );
        Pedido enCurso = restaurante.getPedidoEnCurso( );
        assertNotNull( enCurso, "Después de iniciar un pedido debe haber un pedido en curso." );
        assertEquals( "Tomás", enCurso.getNombreCliente( ) );
        assertEquals( 0, enCurso.getPrecioTotalPedido( ), "Un pedido recién iniciado no tiene productos." );
    }

    @Test
    void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Tomás", "Calle 19 # 1-10" );
        YaHayUnPedidoEnCursoException e = assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Ana", "Carrera 7" ) );
        assertEquals( "Ya existe un pedido en curso, para el cliente Tomás así que no se puede crear un pedido para Ana", e.getMessage( ) );
        assertEquals( "Tomás", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso no debe cambiar." );
    }

    @Test
    void testCerrarSinPedidoEnCurso( )
    {
        NoHayPedidoEnCursoException e = assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ) );
        assertEquals( "Actualmente no hay un pedido en curso", e.getMessage( ) );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        cargarEscenarioValido( );
        restaurante.iniciarPedido( "Tomás", "Calle 19 # 1-10" );
        Pedido pedido = restaurante.getPedidoEnCurso( );

        pedido.agregarProducto( restaurante.getMenuBase( ).get( 0 ) );
        pedido.agregarProducto( restaurante.getMenuCombos( ).get( 0 ) );
        ProductoAjustado ajustado = new ProductoAjustado( restaurante.getMenuBase( ).get( 0 ) );
        ajustado.agregarIngrediente( restaurante.getIngredientes( ).get( 2 ) );
        ajustado.eliminarIngrediente( restaurante.getIngredientes( ).get( 1 ) );
        pedido.agregarProducto( ajustado );

        String facturaEsperada = pedido.generarTextoFactura( );
        File archivo = archivoFactura( pedido );

        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar no debe haber pedido en curso." );
        assertTrue( archivo.exists( ), "Debe existir el archivo " + archivo.getPath( ) );
        String contenido = new String( Files.readAllBytes( archivo.toPath( ) ), Charset.defaultCharset( ) );
        assertEquals( facturaEsperada, contenido, "El archivo debe contener la factura del pedido." );

        assertEquals( 62534, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testCerrarPedidoLoAgregaAlHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Tomás", "Calle 19 # 1-10" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        archivoFactura( pedido );

        restaurante.cerrarYGuardarPedido( );

        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en la lista de pedidos." );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ), "El pedido en el histórico debe ser el que se cerró." );
    }

    @Test
    void testIniciarNuevoPedidoDespuesDeCerrar( ) throws Exception
    {
        restaurante.iniciarPedido( "Tomás", "Calle 19 # 1-10" );
        Pedido primero = restaurante.getPedidoEnCurso( );
        archivoFactura( primero );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Ana", "Carrera 7" );
        Pedido segundo = restaurante.getPedidoEnCurso( );
        archivoFactura( segundo );
        assertEquals( "Ana", segundo.getNombreCliente( ) );
        assertNotEquals( primero.getIdPedido( ), segundo.getIdPedido( ), "Cada pedido debe tener un identificador distinto." );

        restaurante.cerrarYGuardarPedido( );
        assertEquals( 2, restaurante.getPedidos( ).size( ), "Los dos pedidos cerrados deben estar en el histórico." );
    }
}
