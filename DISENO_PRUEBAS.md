# Taller 5 – Pruebas automáticas (Hamburguesas)

## Errores encontrados por las pruebas y corregidos

| # | Clase / método | Error | Prueba que lo detecta | Corrección |
|---|---|---|---|---|
| 1 | `ProductoAjustado.getPrecio()` | Retornaba siempre `0` | `ProductoAjustadoTest.testGetPrecio*` | Precio base + costo de cada ingrediente agregado |
| 2 | `ProductoAjustado.generarTextoFactura()` | Imprimía la referencia del objeto (`ProductoMenu@1a2b…`) en vez del nombre, y no ponía saltos de línea entre ingredientes | `testGenerarTextoFactura*` | Usar `productoBase.getNombre()` y agregar `"\n"` en cada línea |
| 3 | `Combo.getPrecio()` | Multiplicaba por el descuento (`precio * 0.1`) en vez de restarlo | `ComboTest.testGetPrecio*`, `RestauranteTest.testCargarCombos` | `precio * (1 - descuento)` |
| 4 | `Restaurante.cerrarYGuardarPedido()` | No guardaba el pedido cerrado en el histórico (`pedidos`) | `RestauranteTest.testCerrarPedidoLoAgregaAlHistorico` | `pedidos.add(pedidoEnCurso)` |
| 5 | `Restaurante.cerrarYGuardarPedido()` | Fallaba con `FileNotFoundException` si la carpeta `./facturas/` no existía | `RestauranteTest.testCerrarYGuardarPedido` | Crear la carpeta antes de guardar |

Además, `ProductoAjustado` no tenía forma de agregar o quitar ingredientes (los atributos `agregados` y `eliminados` eran inaccesibles), lo que impedía cumplir el requerimiento "agregar un elemento a un pedido, incluyendo ajustes". Se añadieron `agregarIngrediente(Ingrediente)` y `eliminarIngrediente(Ingrediente)`.

## Escenarios de prueba

**ProductoMenuTest** – producto "corral" de 14000: nombre, precio, precio 0, texto exacto de factura.

**ProductoAjustadoTest** – base "corral" (14000), queso americano (2500), tocineta (6000), cebolla (1000):
sin ajustes; con agregados; ingrediente agregado dos veces (se cobra dos veces); eliminar no descuenta; agregados + eliminados; texto de factura con y sin ajustes.

**ComboTest** – corral + papas medianas + gaseosa = 24500:
descuento 10 % → 22050; 0 % → 24500; 9,5 % → 22172 (truncado); 100 % → 0; combo vacío; el constructor copia la lista; texto de factura.

**PedidoTest** – pedido vacío y pedido con producto de menú + combo + producto ajustado (neto 52550, IVA 9984, total 62534):
identificadores consecutivos y únicos; total vacío; IVA truncado (1001 → 190); texto exacto de factura (vacía y completa); `guardarFactura` crea el archivo con el texto exacto, sobrescribe un archivo previo y lanza `FileNotFoundException` con ruta inválida o directorio.

**RestauranteTest** – archivos en `data/pruebas/` y los reales de `data/`:
restaurante nuevo vacío; carga válida (ignora líneas vacías, precios de combos con descuento); archivos reales (15/22/4); `IngredienteRepetidoException`, `ProductoRepetidoException` (menú y combos), `ProductoFaltanteException`, `NumberFormatException`, `FileNotFoundException`; iniciar pedido; `YaHayUnPedidoEnCursoException`; `NoHayPedidoEnCursoException`; cerrar y guardar (archivo `facturas/factura_<id>.txt` con el texto exacto, histórico, nuevo pedido después de cerrar).

## Archivos de prueba (`data/pruebas/`)

| Archivo | Propósito |
|---|---|
| `ingredientes_validos.txt`, `menu_valido.txt`, `combos_validos.txt` | Escenario válido (incluye líneas vacías y un descuento decimal) |
| `ingredientes_repetidos.txt` | Ingrediente repetido |
| `ingredientes_costo_invalido.txt` | Costo no numérico |
| `menu_repetido.txt` | Producto repetido |
| `combos_repetidos.txt` | Combo repetido |
| `combos_producto_faltante.txt` | Combo con un producto que no está en el menú |

## Cobertura (JaCoCo)

57 pruebas, todas exitosas. Cobertura de instrucciones, líneas y ramas: **100 %** en `ProductoMenu`, `ProductoAjustado`, `Combo`, `Pedido`, `Restaurante` e `Ingrediente`.

> Las pruebas deben ejecutarse con la carpeta del proyecto como directorio de trabajo (es lo que hace Eclipse por defecto).
