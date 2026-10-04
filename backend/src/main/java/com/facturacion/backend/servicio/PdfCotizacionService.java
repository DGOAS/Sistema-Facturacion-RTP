package com.facturacion.backend.servicio;

import com.facturacion.backend.entidad.Cotizacion;
import com.facturacion.backend.entidad.DetalleCotizacion;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfCotizacionService {

    
    // CONFIGURACION
    

    private static final float MARGEN_IZQ = 50;
    private static final float ANCHO = 495;

    // Colores RGB normales 0 - 255
    private static final int AZUL_R = 31;
    private static final int AZUL_G = 75;
    private static final int AZUL_B = 115;

    private static final int AZUL_CLARO_R = 235;
    private static final int AZUL_CLARO_G = 242;
    private static final int AZUL_CLARO_B = 250;

    private static final int GRIS_R = 100;
    private static final int GRIS_G = 100;
    private static final int GRIS_B = 100;

    private static final int GRIS_CLARO_R = 245;
    private static final int GRIS_CLARO_G = 247;
    private static final int GRIS_CLARO_B = 249;

    private static final int NEGRO_R = 30;
    private static final int NEGRO_G = 30;
    private static final int NEGRO_B = 30;

    
    // GENERAR PDF
    

    public byte[] generarPdf(Cotizacion cotizacion) {

        try {

            PDDocument documento = new PDDocument();

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDPageContentStream contenido =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );

            float y = 800;

            
            // ENCABEZADO
            

            contenido.setNonStrokingColor(
                    AZUL_R / 255f,
                    AZUL_G / 255f,
                    AZUL_B / 255f
            );

            contenido.addRect(
                    MARGEN_IZQ,
                    y - 95,
                    ANCHO,
                    95
            );

            contenido.fill();

            // Empresa

            escribirTexto(
                    contenido,
                    "RTP REPUESTOS",
                    MARGEN_IZQ + 18,
                    y - 30,
                    16,
                    true,
                    255,
                    255,
                    255
            );

            escribirTexto(
                    contenido,
                    "SOLUCIONES PARA EL SECTOR AUTOMOTRIZ",
                    MARGEN_IZQ + 18,
                    y - 48,
                    7,
                    false,
                    230,
                    240,
                    250
            );

            // Título

            escribirTexto(
                    contenido,
                    "COTIZACION",
                    MARGEN_IZQ + 18,
                    y - 76,
                    21,
                    true,
                    255,
                    255,
                    255
            );

            // Número

            String numeroCotizacion =
                    cotizacion.getNumero() != null
                            ? cotizacion.getNumero()
                            : "";

            escribirTexto(
                    contenido,
                    "N. de cotizacion: " + numeroCotizacion,
                    MARGEN_IZQ + 285,
                    y - 30,
                    10,
                    false,
                    255,
                    255,
                    255
            );

            // Fecha

            String fecha =
                    cotizacion.getFecha() != null
                            ? cotizacion.getFecha().toString()
                            : "";

            escribirTexto(
                    contenido,
                    "Fecha: " + fecha,
                    MARGEN_IZQ + 285,
                    y - 50,
                    10,
                    false,
                    255,
                    255,
                    255
            );

            y -= 125;

            
            // ESTIMADO CLIENTE
            

            escribirTexto(
                    contenido,
                    "ESTIMADO CLIENTE",
                    MARGEN_IZQ,
                    y,
                    13,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            y -= 25;

            String razonSocial = "";
            String ruc = "";
            String telefono = "";
            String direccion = "";
            String representante = "";
            String tipoVenta = "";
            String transporte = "";

            if (cotizacion.getPreventa() != null) {

                razonSocial = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getRazonSocial()
                );

                ruc = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getRuc()
                );

                telefono = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getTelefono()
                );

                direccion = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getDireccion()
                );

                representante = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getRepresentanteCliente()
                );

                tipoVenta = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getTipoVenta()
                );

                transporte = textoSeguro(
                        cotizacion
                                .getPreventa()
                                .getTransporte()
                );
            }

            // Columna izquierda

            escribirTexto(
                    contenido,
                    "Razon Social: " + razonSocial,
                    MARGEN_IZQ,
                    y,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            escribirTexto(
                    contenido,
                    "Telefono: " + telefono,
                    MARGEN_IZQ,
                    y - 20,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            escribirTexto(
                    contenido,
                    "Direccion: " + direccion,
                    MARGEN_IZQ,
                    y - 40,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            escribirTexto(
                    contenido,
                    "Representante: " + representante,
                    MARGEN_IZQ,
                    y - 60,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            // Columna derecha

            escribirTexto(
                    contenido,
                    "RUC: " + ruc,
                    MARGEN_IZQ + 280,
                    y,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            escribirTexto(
                    contenido,
                    "Tipo de venta: " + tipoVenta,
                    MARGEN_IZQ + 280,
                    y - 20,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            y -= 95;

            
            // MENSAJE DE PRESENTACION
            

            contenido.setNonStrokingColor(
                    GRIS_CLARO_R / 255f,
                    GRIS_CLARO_G / 255f,
                    GRIS_CLARO_B / 255f
            );

            contenido.addRect(
                    MARGEN_IZQ,
                    y - 35,
                    ANCHO,
                    35
            );

            contenido.fill();

            escribirTexto(
                    contenido,
                    "Le hacemos llegar nuestra cotizacion por los productos solicitados.",
                    MARGEN_IZQ + 15,
                    y - 22,
                    10,
                    false,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            y -= 60;

            
            // DETALLE DE PRODUCTOS
            

            escribirTexto(
                    contenido,
                    "DETALLE DE PRODUCTOS",
                    MARGEN_IZQ,
                    y,
                    13,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            y -= 20;

            // Encabezado de tabla

            contenido.setNonStrokingColor(
                    AZUL_R / 255f,
                    AZUL_G / 255f,
                    AZUL_B / 255f
            );

            contenido.addRect(
                    MARGEN_IZQ,
                    y - 22,
                    ANCHO,
                    22
            );

            contenido.fill();

            escribirTexto(
                    contenido,
                    "CODIGO",
                    MARGEN_IZQ + 7,
                    y - 15,
                    8,
                    true,
                    255,
                    255,
                    255
            );

            escribirTexto(
                    contenido,
                    "PRODUCTO",
                    MARGEN_IZQ + 75,
                    y - 15,
                    8,
                    true,
                    255,
                    255,
                    255
            );

            escribirTexto(
                    contenido,
                    "CANT.",
                    MARGEN_IZQ + 340,
                    y - 15,
                    8,
                    true,
                    255,
                    255,
                    255
            );

            escribirTexto(
                    contenido,
                    "P. UNIT.",
                    MARGEN_IZQ + 385,
                    y - 15,
                    8,
                    true,
                    255,
                    255,
                    255
            );

            escribirTexto(
                    contenido,
                    "TOTAL",
                    MARGEN_IZQ + 445,
                    y - 15,
                    8,
                    true,
                    255,
                    255,
                    255
            );

            y -= 22;

            
            // PRODUCTOS
            

            List<DetalleCotizacion> detalles =
                    cotizacion.getDetalles();

            if (detalles != null) {

                for (DetalleCotizacion detalle : detalles) {

                    // Si queda poco espacio, nueva página

                    if (y < 170) {

                        contenido.close();

                        pagina = new PDPage(
                                PDRectangle.A4
                        );

                        documento.addPage(pagina);

                        contenido =
                                new PDPageContentStream(
                                        documento,
                                        pagina
                                );

                        y = 800;

                        escribirTexto(
                                contenido,
                                "RTP REPUESTOS - COTIZACION",
                                MARGEN_IZQ,
                                y,
                                14,
                                true,
                                AZUL_R,
                                AZUL_G,
                                AZUL_B
                        );

                        y -= 35;
                    }

                    String codigo = "";
                    String producto = "";

                    if (detalle.getProducto() != null) {

                        codigo = textoSeguro(
                                detalle
                                        .getProducto()
                                        .getCodigo()
                        );

                        producto = textoSeguro(
                                detalle
                                        .getProducto()
                                        .getNombre()
                        );
                    }

                    String cantidad =
                            String.valueOf(
                                    detalle.getCantidad()
                            );

                    String precioUnitario =
                            String.format(
                                    "%.2f",
                                    detalle.getPrecioUnitarioUsd()
                            );

                    String total =
                            String.format(
                                    "%.2f",
                                    detalle.getTotalUsd()
                            );

                    // Fondo de fila

                    contenido.setNonStrokingColor(
                            250 / 255f,
                            250 / 255f,
                            250 / 255f
                    );

                    contenido.addRect(
                            MARGEN_IZQ,
                            y - 25,
                            ANCHO,
                            25
                    );

                    contenido.fill();

                    escribirTexto(
                            contenido,
                            codigo,
                            MARGEN_IZQ + 7,
                            y - 16,
                            8,
                            false,
                            NEGRO_R,
                            NEGRO_G,
                            NEGRO_B
                    );

                    escribirTexto(
                            contenido,
                            limitarTexto(producto, 40),
                            MARGEN_IZQ + 75,
                            y - 16,
                            8,
                            false,
                            NEGRO_R,
                            NEGRO_G,
                            NEGRO_B
                    );

                    escribirTexto(
                            contenido,
                            cantidad,
                            MARGEN_IZQ + 345,
                            y - 16,
                            8,
                            false,
                            NEGRO_R,
                            NEGRO_G,
                            NEGRO_B
                    );

                    escribirTexto(
                            contenido,
                            "US$ " + precioUnitario,
                            MARGEN_IZQ + 380,
                            y - 16,
                            8,
                            false,
                            NEGRO_R,
                            NEGRO_G,
                            NEGRO_B
                    );

                    escribirTexto(
                            contenido,
                            "US$ " + total,
                            MARGEN_IZQ + 440,
                            y - 16,
                            8,
                            true,
                            NEGRO_R,
                            NEGRO_G,
                            NEGRO_B
                    );

                    // Línea

                    contenido.setStrokingColor(
                            220 / 255f,
                            225 / 255f,
                            230 / 255f
                    );

                    contenido.moveTo(
                            MARGEN_IZQ,
                            y - 25
                    );

                    contenido.lineTo(
                            MARGEN_IZQ + ANCHO,
                            y - 25
                    );

                    contenido.stroke();

                    y -= 25;
                }
            }

            y -= 30;

            
            // CONDICIONES
            

            escribirTexto(
                    contenido,
                    "CONDICIONES DE LA COTIZACION",
                    MARGEN_IZQ,
                    y,
                    13,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            y -= 22;

            String formaPago =
                    cotizacion.getFormaPago() != null
                            ? textoSeguro(
                                    cotizacion.getFormaPago()
                            )
                            : "No registrada";

            // Fondo

            contenido.setNonStrokingColor(
                    AZUL_CLARO_R / 255f,
                    AZUL_CLARO_G / 255f,
                    AZUL_CLARO_B / 255f
            );

            contenido.addRect(
                    MARGEN_IZQ,
                    y - 45,
                    ANCHO,
                    45
            );

            contenido.fill();

            escribirTexto(
                    contenido,
                    "Forma de pago:",
                    MARGEN_IZQ + 12,
                    y - 17,
                    8,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            escribirTexto(
                    contenido,
                    formaPago,
                    MARGEN_IZQ + 95,
                    y - 17,
                    9,
                    true,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            escribirTexto(
                    contenido,
                    "Transporte:",
                    MARGEN_IZQ + 270,
                    y - 17,
                    8,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            escribirTexto(
                    contenido,
                    transporte,
                    MARGEN_IZQ + 335,
                    y - 17,
                    9,
                    true,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            y -= 75;

            
            // RESUMEN FINANCIERO
            

            float resumenX = 350;
            float resumenAncho = 195;

            escribirTexto(
                    contenido,
                    "RESUMEN FINANCIERO",
                    resumenX,
                    y,
                    12,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            y -= 25;

            // Subtotal

            escribirTexto(
                    contenido,
                    "Subtotal:",
                    resumenX,
                    y,
                    9,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            escribirTexto(
                    contenido,
                    String.format(
                            "US$ %.2f",
                            cotizacion.getSubtotalUsd()
                    ),
                    resumenX + 110,
                    y,
                    9,
                    true,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            y -= 18;

            // IGV

            escribirTexto(
                    contenido,
                    "IGV (18%):",
                    resumenX,
                    y,
                    9,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            escribirTexto(
                    contenido,
                    String.format(
                            "US$ %.2f",
                            cotizacion.getIgvUsd()
                    ),
                    resumenX + 110,
                    y,
                    9,
                    true,
                    NEGRO_R,
                    NEGRO_G,
                    NEGRO_B
            );

            y -= 22;

            // TOTAL

            contenido.setNonStrokingColor(
                    AZUL_CLARO_R / 255f,
                    AZUL_CLARO_G / 255f,
                    AZUL_CLARO_B / 255f
            );

            contenido.addRect(
                    resumenX - 10,
                    y - 30,
                    resumenAncho,
                    30
            );

            contenido.fill();

            escribirTexto(
                    contenido,
                    "TOTAL",
                    resumenX,
                    y - 20,
                    11,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            escribirTexto(
                    contenido,
                    String.format(
                            "US$ %.2f",
                            cotizacion.getTotalUsd()
                    ),
                    resumenX + 105,
                    y - 20,
                    12,
                    true,
                    AZUL_R,
                    AZUL_G,
                    AZUL_B
            );

            
            // PIE DE PAGINA
            

            escribirTexto(
                    contenido,
                    "RTP REPUESTOS - Cotizacion comercial",
                    MARGEN_IZQ,
                    45,
                    8,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            escribirTexto(
                    contenido,
                    "Documento generado por SistemaFacturacion",
                    MARGEN_IZQ + 300,
                    45,
                    7,
                    false,
                    GRIS_R,
                    GRIS_G,
                    GRIS_B
            );

            
            // GUARDAR
            

            contenido.close();

            ByteArrayOutputStream salida =
                    new ByteArrayOutputStream();

            documento.save(salida);

            documento.close();

            return salida.toByteArray();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error al generar PDF de cotizacion",
                    e
            );
        }
    }

    
    // ESCRIBIR TEXTO
    

    private void escribirTexto(
            PDPageContentStream contenido,
            String texto,
            float x,
            float y,
            float tamano,
            boolean negrita,
            int r,
            int g,
            int b
    ) throws Exception {

        contenido.beginText();

        if (negrita) {

            contenido.setFont(
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    ),
                    tamano
            );

        } else {

            contenido.setFont(
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA
                    ),
                    tamano
            );
        }

        contenido.setNonStrokingColor(
                r / 255f,
                g / 255f,
                b / 255f
        );

        contenido.newLineAtOffset(x, y);

        contenido.showText(
                textoSeguro(texto)
        );

        contenido.endText();
    }

    
    // TEXTO SEGURO
    

    private String textoSeguro(String valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("ñ", "n")
                .replace("Ñ", "N")
                .replace("°", " ")
                .replace("–", "-")
                .replace("—", "-");
    }

    
    // LIMITAR TEXTO
    

    private String limitarTexto(
            String texto,
            int maximo
    ) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= maximo) {
            return texto;
        }

        return texto.substring(
                0,
                maximo - 3
        ) + "...";
    }
}