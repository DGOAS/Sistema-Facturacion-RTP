package com.facturacion.backend.servicio;

import com.facturacion.backend.entidad.DetallePreventa;
import com.facturacion.backend.entidad.Preventa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfPreventaService {

    
    // CONFIGURACIÓN
    

    private static final float MARGEN_IZQ = 35;
    private static final float MARGEN_DER = 35;

    private static final float ANCHO = PDRectangle.A4.getWidth();
    private static final float ALTO = PDRectangle.A4.getHeight();

    // Colores RTP
    private static final Color AZUL_RTP =
            new Color(18, 63, 110);

    private static final Color AZUL_CLARO =
            new Color(232, 240, 250);

    private static final Color GRIS_CLARO =
            new Color(238, 241, 245);

    private static final Color GRIS_BORDE =
            new Color(190, 198, 208);

    private static final Color GRIS_TEXTO =
            new Color(80, 90, 105);

    private final PDType1Font NORMAL =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    private final PDType1Font BOLD =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );


    
    // GENERAR PDF
    

    public byte[] generarPdf(Preventa preventa) {

        try {

            PDDocument documento = new PDDocument();

            PDPage pagina = new PDPage(PDRectangle.A4);

            documento.addPage(pagina);

            PDPageContentStream contenido =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );

            float y = ALTO - 35;


            
            // ENCABEZADO
            

            contenido.setNonStrokingColor(AZUL_RTP);

            contenido.beginText();
            contenido.setFont(BOLD, 20);

            contenido.newLineAtOffset(
                    MARGEN_IZQ,
                    y
            );

            contenido.showText("RTP");
            contenido.endText();


            contenido.beginText();
            contenido.setFont(BOLD, 9);

            contenido.newLineAtOffset(
                    MARGEN_IZQ,
                    y - 15
            );

            contenido.showText("REPUESTOS");
            contenido.endText();


            // Línea decorativa
            contenido.setStrokingColor(AZUL_RTP);
            contenido.setLineWidth(2);

            contenido.moveTo(
                    MARGEN_IZQ,
                    y - 25
            );

            contenido.lineTo(
                    ANCHO - MARGEN_DER,
                    y - 25
            );

            contenido.stroke();


            // Título
            contenido.setNonStrokingColor(Color.BLACK);

            contenido.beginText();
            contenido.setFont(BOLD, 16);

            contenido.newLineAtOffset(
                    330,
                    y
            );

            contenido.showText(
                    "COMPROMISO DE VENTA"
            );

            contenido.endText();


            // Número
            contenido.beginText();
            contenido.setFont(BOLD, 11);
            contenido.setNonStrokingColor(AZUL_RTP);

            contenido.newLineAtOffset(
                    330,
                    y - 18
            );

            contenido.showText(
                "N.° de preventa: " +
                valorSeguro(preventa.getNumero())
        );

            contenido.endText();


            y -= 45;


            
            // DATOS DE LA OPERACIÓN
            

            dibujarTituloSeccion(
                    contenido,
                    "DATOS DE LA OPERACION",
                    MARGEN_IZQ,
                    y,
                    ANCHO - MARGEN_IZQ - MARGEN_DER
            );

            y -= 27;


            float anchoCampo =
                    (
                            ANCHO
                                    - MARGEN_IZQ
                                    - MARGEN_DER
                                    - 20
                    ) / 3;


            dibujarCampo(
                    contenido,
                    "Fecha",
                    valorSeguro(preventa.getFecha()),
                    MARGEN_IZQ,
                    y,
                    anchoCampo
            );


            dibujarCampo(
                    contenido,
                    "Tipo de venta",
                    valorSeguro(preventa.getTipoVenta()),
                    MARGEN_IZQ + anchoCampo + 10,
                    y,
                    anchoCampo
            );


            dibujarCampo(
                    contenido,
                    "Condicion de pago",
                    valorSeguro(preventa.getCondicionPago()),
                    MARGEN_IZQ + (anchoCampo + 10) * 2,
                    y,
                    anchoCampo
            );


            y -= 45;


            
            // DATOS DEL CLIENTE
            

            dibujarTituloSeccion(
                    contenido,
                    "DATOS DEL CLIENTE",
                    MARGEN_IZQ,
                    y,
                    ANCHO - MARGEN_IZQ - MARGEN_DER
            );

            y -= 27;


            dibujarCampo(
                    contenido,
                    "Razon Social",
                    valorSeguro(preventa.getRazonSocial()),
                    MARGEN_IZQ,
                    y,
                    260
            );


            dibujarCampo(
                    contenido,
                    "R.U.C.",
                    valorSeguro(preventa.getRuc()),
                    310,
                    y,
                    115
            );


            dibujarCampo(
                    contenido,
                    "Telefono",
                    valorSeguro(preventa.getTelefono()),
                    435,
                    y,
                    122
            );


            y -= 43;


            dibujarCampo(
                    contenido,
                    "Direccion",
                    valorSeguro(preventa.getDireccion()),
                    MARGEN_IZQ,
                    y,
                    260
            );


            dibujarCampo(
                    contenido,
                    "Urbanizacion",
                    valorSeguro(preventa.getUrbanizacion()),
                    310,
                    y,
                    115
            );


            dibujarCampo(
                    contenido,
                    "Distrito",
                    valorSeguro(preventa.getDistrito()),
                    435,
                    y,
                    122
            );


            y -= 48;


            
            // DETALLE DE PRODUCTOS
            

            dibujarTituloSeccion(
                    contenido,
                    "DETALLE DE PRODUCTOS",
                    MARGEN_IZQ,
                    y,
                    ANCHO - MARGEN_IZQ - MARGEN_DER
            );

            y -= 25;


            float tablaX = MARGEN_IZQ;

            float tablaAncho =
                    ANCHO
                            - MARGEN_IZQ
                            - MARGEN_DER;

            float altoCabecera = 24;


            
            float[] columnas = {
                60,   // código
                35,   // cantidad
                220,  // detalle
                65,   // precio
                45,   // descuento
                100   // total
        };


            
            // CABECERA DE TABLA
            

            contenido.setNonStrokingColor(AZUL_RTP);

            contenido.addRect(
                    tablaX,
                    y - altoCabecera,
                    tablaAncho,
                    altoCabecera
            );

            contenido.fill();


            contenido.setNonStrokingColor(Color.WHITE);


            String[] cabeceras = {
                    "CODIGO",
                    "CANT.",
                    "DETALLE",
                    "P. UNIT.",
                    "DCTO.",
                    "TOTAL"
            };


            float x = tablaX;


            for (int i = 0; i < cabeceras.length; i++) {

                contenido.beginText();

                contenido.setFont(
                        BOLD,
                        7
                );

                contenido.newLineAtOffset(
                        x + 4,
                        y - 16
                );

                contenido.showText(
                        cabeceras[i]
                );

                contenido.endText();

                x += columnas[i];
            }


            y -= altoCabecera;

            contenido.setNonStrokingColor(Color.BLACK);


            
            // PRODUCTOS
            

            List<DetallePreventa> detalles =
                    preventa.getDetalles();


            if (detalles != null) {

                for (DetallePreventa detalle : detalles) {

                    float altoFila = 24;


                    // Borde de fila
                    contenido.setStrokingColor(
                            GRIS_BORDE
                    );

                    contenido.setLineWidth(
                            0.5f
                    );

                    contenido.addRect(
                            tablaX,
                            y - altoFila,
                            tablaAncho,
                            altoFila
                    );

                    contenido.stroke();


                    String codigo =
                            detalle.getProducto() != null
                                    ? valorSeguro(
                                            detalle
                                                    .getProducto()
                                                    .getCodigo()
                                    )
                                    : "-";


                    String producto =
                            detalle.getProducto() != null
                                    ? valorSeguro(
                                            detalle
                                                    .getProducto()
                                                    .getNombre()
                                    )
                                    : "-";


                    String cantidad =
                            String.valueOf(
                                    detalle.getCantidad()
                            );


                    String precio =
                            String.format(
                                    "US$ %.2f",
                                    detalle.getPrecioUnitarioUsd()
                            );


                    String total =
                            String.format(
                                    "US$ %.2f",
                                    detalle.getTotalUsd()
                            );


                    String descuento =
                            String.format(
                                    "%.2f%%",
                                    preventa.getDescuentoPorcentaje()
                            );


                    contenido.setNonStrokingColor(
                            Color.BLACK
                    );


                    // Código
                    escribirCelda(
                            contenido,
                            codigo,
                            tablaX + 4,
                            y - 16,
                            7,
                            false
                    );


                    // Cantidad
                    escribirCelda(
                            contenido,
                            cantidad,
                            tablaX
                                    + columnas[0]
                                    + 4,
                            y - 16,
                            7,
                            false
                    );


                    // Producto
                    escribirCelda(
                            contenido,
                            producto,
                            tablaX
                                    + columnas[0]
                                    + columnas[1]
                                    + 4,
                            y - 16,
                            7,
                            false
                    );


                    // Precio
                    escribirCelda(
                            contenido,
                            precio,
                            tablaX
                                    + columnas[0]
                                    + columnas[1]
                                    + columnas[2]
                                    + 4,
                            y - 16,
                            7,
                            false
                    );


                    // Descuento
                    escribirCelda(
                            contenido,
                            descuento,
                            tablaX
                                    + columnas[0]
                                    + columnas[1]
                                    + columnas[2]
                                    + columnas[3]
                                    + 4,
                            y - 16,
                            7,
                            false
                    );


                    // Total
                    escribirCelda(
                            contenido,
                            total,
                            tablaX
                                    + columnas[0]
                                    + columnas[1]
                                    + columnas[2]
                                    + columnas[3]
                                    + columnas[4]
                                    + 4,
                            y - 16,
                            7,
                            false
                    );


                    y -= altoFila;
                }
            }


            
            // SUBTOTAL
            

            y -= 12;


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    9
            );

            contenido.setNonStrokingColor(
                    GRIS_TEXTO
            );

            contenido.newLineAtOffset(
                    365,
                    y
            );

            contenido.showText(
                    "Subtotal de productos:"
            );

            contenido.endText();


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    10
            );

            contenido.setNonStrokingColor(
                    AZUL_RTP
            );

            contenido.newLineAtOffset(
                    480,
                    y
            );

            contenido.showText(
                    String.format(
                            "US$ %.2f",
                            preventa.getSubtotalUsd()
                    )
            );

            contenido.endText();


            y -= 35;


            
            // INFORMACIÓN ADICIONAL
            

            dibujarTituloSeccion(
                    contenido,
                    "INFORMACION ADICIONAL",
                    MARGEN_IZQ,
                    y,
                    340
            );


            y -= 27;


            dibujarCampo(
                    contenido,
                    "Transporte",
                    valorSeguro(
                            preventa.getTransporte()
                    ),
                    MARGEN_IZQ,
                    y,
                    155
            );


            dibujarCampo(
                    contenido,
                    "Plazo de pago",
                    preventa.getPlazoPago() != null
                            ? preventa.getPlazoPago() + " dias"
                            : "-",
                    MARGEN_IZQ + 165,
                    y,
                    155
            );


            y -= 43;


            dibujarCampo(
                    contenido,
                    "Agencia de transporte",
                    valorSeguro(
                            preventa.getAgenciaTransporte()
                    ),
                    MARGEN_IZQ,
                    y,
                    155
            );


            dibujarCampo(
                    contenido,
                    "Representante del cliente",
                    valorSeguro(
                            preventa.getRepresentanteCliente()
                    ),
                    MARGEN_IZQ + 165,
                    y,
                    155
            );


            
            // RESUMEN FINANCIERO
            

            float resumenX = 405;

            float resumenY = y + 35;

            float resumenAncho = 152;

            float resumenAlto = 145;


            contenido.setNonStrokingColor(
                    AZUL_CLARO
            );

            contenido.addRect(
                    resumenX,
                    resumenY - resumenAlto,
                    resumenAncho,
                    resumenAlto
            );

            contenido.fill();


            contenido.setStrokingColor(
                    GRIS_BORDE
            );

            contenido.setLineWidth(
                    0.8f
            );

            contenido.addRect(
                    resumenX,
                    resumenY - resumenAlto,
                    resumenAncho,
                    resumenAlto
            );

            contenido.stroke();


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    11
            );

            contenido.setNonStrokingColor(
                    AZUL_RTP
            );

            contenido.newLineAtOffset(
                    resumenX + 10,
                    resumenY - 18
            );

            contenido.showText(
                    "RESUMEN FINANCIERO"
            );

            contenido.endText();


            float ry = resumenY - 40;


            ry = escribirResumen(
                    contenido,
                    "Subtotal",
                    preventa.getSubtotalUsd(),
                    resumenX,
                    ry,
                    resumenAncho
            );


            ry = escribirResumen(
                    contenido,
                    "Descuento",
                    preventa.getDescuentoUsd(),
                    resumenX,
                    ry,
                    resumenAncho
            );


            ry = escribirResumen(
                    contenido,
                    "Neto",
                    preventa.getNetoUsd(),
                    resumenX,
                    ry,
                    resumenAncho
            );


            ry = escribirResumen(
                    contenido,
                    "IGV (18%)",
                    preventa.getIgvUsd(),
                    resumenX,
                    ry,
                    resumenAncho
            );


            
            // TOTAL
            

            contenido.setNonStrokingColor(
                    AZUL_RTP
            );

            contenido.addRect(
                    resumenX + 7,
                    ry - 23,
                    resumenAncho - 14,
                    25
            );

            contenido.fill();


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    9
            );

            contenido.setNonStrokingColor(
                    Color.WHITE
            );

            contenido.newLineAtOffset(
                    resumenX + 13,
                    ry - 15
            );

            contenido.showText(
                    "TOTAL"
            );

            contenido.endText();


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    10
            );

            contenido.newLineAtOffset(
                    resumenX + 80,
                    ry - 15
            );

            contenido.showText(
                    String.format(
                            "US$ %.2f",
                            preventa.getTotalUsd()
                    )
            );

            contenido.endText();


            
            // PIE DEL DOCUMENTO
            

            float pieY = 62;


            contenido.setStrokingColor(
                    GRIS_BORDE
            );

            contenido.setLineWidth(
                    0.7f
            );

            contenido.moveTo(
                    MARGEN_IZQ,
                    pieY + 28
            );

            contenido.lineTo(
                    ANCHO - MARGEN_DER,
                    pieY + 28
            );

            contenido.stroke();


            contenido.setNonStrokingColor(
                    GRIS_TEXTO
            );


            contenido.beginText();

            contenido.setFont(
                    NORMAL,
                    7
            );

            contenido.newLineAtOffset(
                    MARGEN_IZQ,
                    pieY + 12
            );

            contenido.showText(
                    "Documento generado automaticamente por el sistema RTP Repuestos."
            );

            contenido.endText();


            contenido.beginText();

            contenido.setFont(
                    BOLD,
                    8
            );

            contenido.newLineAtOffset(
                    430,
                    pieY + 12
            );

            contenido.showText(
                    "ORIGINAL - OFICINA"
            );

            contenido.endText();


            
            // FIRMAS
           

            contenido.setStrokingColor(
                    Color.DARK_GRAY
            );


            contenido.moveTo(
                    60,
                    pieY - 12
            );

            contenido.lineTo(
                    220,
                    pieY - 12
            );

            contenido.stroke();


            contenido.moveTo(
                    330,
                    pieY - 12
            );

            contenido.lineTo(
                    490,
                    pieY - 12
            );

            contenido.stroke();


            contenido.beginText();

            contenido.setFont(
                    NORMAL,
                    7
            );

            contenido.setNonStrokingColor(
                    GRIS_TEXTO
            );

            contenido.newLineAtOffset(
                    100,
                    pieY - 24
            );

            contenido.showText(
                    "Firma Vendedor"
            );

            contenido.endText();


            contenido.beginText();

            contenido.setFont(
                    NORMAL,
                    7
            );

            contenido.newLineAtOffset(
                    370,
                    pieY - 24
            );

            contenido.showText(
                    "Firma Cliente"
            );

            contenido.endText();


            
            // CERRAR
            

            contenido.close();


            ByteArrayOutputStream salida =
                    new ByteArrayOutputStream();


            documento.save(salida);

            documento.close();


            return salida.toByteArray();


        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al generar PDF de preventa",
                    e
            );
        }
    }


    
    // MÉTODOS AUXILIARES
    

    private void dibujarTituloSeccion(
            PDPageContentStream contenido,
            String titulo,
            float x,
            float y,
            float ancho
    ) throws Exception {

        contenido.setNonStrokingColor(
                GRIS_CLARO
        );

        contenido.addRect(
                x,
                y - 20,
                ancho,
                20
        );

        contenido.fill();


        contenido.setNonStrokingColor(
                AZUL_RTP
        );


        contenido.beginText();

        contenido.setFont(
                BOLD,
                9
        );

        contenido.newLineAtOffset(
                x + 7,
                y - 14
        );

        contenido.showText(
                titulo
        );

        contenido.endText();
    }


    private void dibujarCampo(
            PDPageContentStream contenido,
            String etiqueta,
            String valor,
            float x,
            float y,
            float ancho
    ) throws Exception {

        contenido.setStrokingColor(
                GRIS_BORDE
        );

        contenido.setLineWidth(
                0.6f
        );


        contenido.addRect(
                x,
                y - 30,
                ancho,
                30
        );

        contenido.stroke();


        // Etiqueta
        contenido.beginText();

        contenido.setFont(
                NORMAL,
                6.5f
        );

        contenido.setNonStrokingColor(
                GRIS_TEXTO
        );

        contenido.newLineAtOffset(
                x + 6,
                y - 10
        );

        contenido.showText(
                etiqueta
        );

        contenido.endText();


        // Valor
        contenido.beginText();

        contenido.setFont(
                BOLD,
                8
        );

        contenido.setNonStrokingColor(
                Color.BLACK
        );

        contenido.newLineAtOffset(
                x + 6,
                y - 22
        );

        contenido.showText(
                recortar(
                        valorSeguro(valor),
                        ancho - 12
                )
        );

        contenido.endText();
    }


    private void escribirCelda(
            PDPageContentStream contenido,
            String texto,
            float x,
            float y,
            float tamanio,
            boolean negrita
    ) throws Exception {

        contenido.beginText();

        contenido.setFont(
                negrita
                        ? BOLD
                        : NORMAL,
                tamanio
        );

        contenido.setNonStrokingColor(
                Color.BLACK
        );

        contenido.newLineAtOffset(
                x,
                y
        );

        contenido.showText(
                recortar(
                        texto,
                        150
                )
        );

        contenido.endText();
    }


    private float escribirResumen(
            PDPageContentStream contenido,
            String etiqueta,
            double valor,
            float x,
            float y,
            float ancho
    ) throws Exception {

        // Etiqueta
        contenido.beginText();

        contenido.setFont(
                NORMAL,
                8
        );

        contenido.setNonStrokingColor(
                GRIS_TEXTO
        );

        contenido.newLineAtOffset(
                x + 10,
                y
        );

        contenido.showText(
                etiqueta
        );

        contenido.endText();


        // Importe
        String importe =
                String.format(
                        "US$ %.2f",
                        valor
                );


        contenido.beginText();

        contenido.setFont(
                BOLD,
                8
        );

        contenido.setNonStrokingColor(
                Color.BLACK
        );

        contenido.newLineAtOffset(
                x + ancho - 62,
                y
        );

        contenido.showText(
                importe
        );

        contenido.endText();


        // Línea
        contenido.setStrokingColor(
                GRIS_BORDE
        );

        contenido.setLineWidth(
                0.4f
        );

        contenido.moveTo(
                x + 8,
                y - 7
        );

        contenido.lineTo(
                x + ancho - 8,
                y - 7
        );

        contenido.stroke();


        return y - 22;
    }


    private String valorSeguro(Object valor) {

        if (valor == null) {
            return "-";
        }

        String texto = valor.toString();

        if (texto.isBlank()) {
            return "-";
        }

        return texto;
    }


    private String recortar(
                String texto,
                float anchoMaximo
        ) {

        if (texto == null) {
                return "-";
        }

        if (texto.length() <= 42) {
                return texto;
        }

        return texto.substring(
                0,
                39
        ) + "...";
        }
}