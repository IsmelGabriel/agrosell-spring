package com.agrosellnova.Agrosellnova.servicio;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente SOAP para consumir los servicios web de SIPSA del DANE
 * Realiza peticiones SOAP 1.2 al endpoint configurado
 */
@Service
public class SipsaSoapClient {

    private static final String SOAP_ENDPOINT = "https://appweb.dane.gov.co/sipsaWS/SrvSipsaUpraBeanService";
    private static final String NAMESPACE_URI = "http://servicios.sipsa.co.gov.dane/";

    /**
     * Consulta precios promedio por ciudad
     * Método: promediosSipsaCiudad
     * @return Lista de mapas con los datos de precios por ciudad
     */
    public List<Map<String, Object>> consultarPreciosCiudad() {
        try {
            // Crear la petición SOAP
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Crear el mensaje SOAP
            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();

            // Configurar namespace
            envelope.addNamespaceDeclaration("tns", NAMESPACE_URI);

            // Crear el body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement operacion = soapBody.addChildElement("promediosSipsaCiudad", "tns");

            soapMessage.saveChanges();

            // Enviar la petición
            SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);

            // Parsear la respuesta
            List<Map<String, Object>> resultados = parsearRespuestaCiudad(soapResponse);

            soapConnection.close();

            return resultados;

        } catch (Exception e) {
            System.err.println("Error consultando API SIPSA: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Consulta cantidades por mes para mayoristas
     * Método: promediosSipsaMesMadr
     * @return Lista de mapas con datos mensuales
     */
    public List<Map<String, Object>> consultarDatosMensuales() {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("tns", NAMESPACE_URI);

            SOAPBody soapBody = envelope.getBody();
            SOAPElement operacion = soapBody.addChildElement("promediosSipsaMesMadr", "tns");

            soapMessage.saveChanges();

            SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);
            List<Map<String, Object>> resultados = parsearRespuestaMensual(soapResponse);

            soapConnection.close();

            return resultados;

        } catch (Exception e) {
            System.err.println("Error consultando datos mensuales: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Consulta datos semanales
     * Método: promediosSipsaSemanaMadr
     * @return Lista de mapas con datos semanales
     */
    public List<Map<String, Object>> consultarDatosSemanales() {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("tns", NAMESPACE_URI);

            SOAPBody soapBody = envelope.getBody();
            SOAPElement operacion = soapBody.addChildElement("promediosSipsaSemanaMadr", "tns");

            soapMessage.saveChanges();

            SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);
            List<Map<String, Object>> resultados = parsearRespuestaSemanal(soapResponse);

            soapConnection.close();

            return resultados;

        } catch (Exception e) {
            System.err.println("Error consultando datos semanales: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Consulta datos de abastecimiento
     * Método: promedioAbasSipsaMesMadr
     * @return Lista de mapas con datos de abastecimiento
     */
    public List<Map<String, Object>> consultarAbastecimiento() {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("tns", NAMESPACE_URI);

            SOAPBody soapBody = envelope.getBody();
            SOAPElement operacion = soapBody.addChildElement("promedioAbasSipsaMesMadr", "tns");

            soapMessage.saveChanges();

            SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);
            List<Map<String, Object>> resultados = parsearRespuestaAbastecimiento(soapResponse);

            soapConnection.close();

            return resultados;

        } catch (Exception e) {
            System.err.println("Error consultando abastecimiento: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Parsea la respuesta del servicio promediosSipsaCiudad
     */
    private List<Map<String, Object>> parsearRespuestaCiudad(SOAPMessage response) throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(
                response.getSOAPBody().getTextContent().getBytes()
        ));

        NodeList returnNodes = response.getSOAPBody().getElementsByTagName("return");

        for (int i = 0; i < returnNodes.getLength(); i++) {
            Element returnElement = (Element) returnNodes.item(i);
            Map<String, Object> registro = new HashMap<>();

            registro.put("ciudad", getElementValue(returnElement, "ciudad"));
            registro.put("producto", getElementValue(returnElement, "producto"));
            registro.put("codProducto", getElementValueInt(returnElement, "codProducto"));
            registro.put("precioPromedio", getElementValueDouble(returnElement, "precioPromedio"));
            registro.put("fechaCaptura", getElementValue(returnElement, "fechaCaptura"));
            registro.put("fechaCreacion", getElementValue(returnElement, "fechaCreacion"));

            resultados.add(registro);
        }

        return resultados;
    }

    /**
     * Parsea la respuesta del servicio promediosSipsaMesMadr
     */
    private List<Map<String, Object>> parsearRespuestaMensual(SOAPMessage response) throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();

        NodeList returnNodes = response.getSOAPBody().getElementsByTagName("return");

        for (int i = 0; i < returnNodes.getLength(); i++) {
            Element returnElement = (Element) returnNodes.item(i);
            Map<String, Object> registro = new HashMap<>();

            registro.put("artiId", getElementValueInt(returnElement, "artiId"));
            registro.put("artiNombre", getElementValue(returnElement, "artiNombre"));
            registro.put("fuenNombre", getElementValue(returnElement, "fuenNombre"));
            registro.put("promedioKg", getElementValueDouble(returnElement, "promedioKg"));
            registro.put("minimoKg", getElementValueDouble(returnElement, "minimoKg"));
            registro.put("maximoKg", getElementValueDouble(returnElement, "maximoKg"));
            registro.put("fechaMesIni", getElementValue(returnElement, "fechaMesIni"));

            resultados.add(registro);
        }

        return resultados;
    }

    /**
     * Parsea la respuesta del servicio promediosSipsaSemanaMadr
     */
    private List<Map<String, Object>> parsearRespuestaSemanal(SOAPMessage response) throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();

        NodeList returnNodes = response.getSOAPBody().getElementsByTagName("return");

        for (int i = 0; i < returnNodes.getLength(); i++) {
            Element returnElement = (Element) returnNodes.item(i);
            Map<String, Object> registro = new HashMap<>();

            registro.put("artiId", getElementValueInt(returnElement, "artiId"));
            registro.put("artiNombre", getElementValue(returnElement, "artiNombre"));
            registro.put("fuenNombre", getElementValue(returnElement, "fuenNombre"));
            registro.put("promedioKg", getElementValueDouble(returnElement, "promedioKg"));
            registro.put("minimoKg", getElementValueDouble(returnElement, "minimoKg"));
            registro.put("maximoKg", getElementValueDouble(returnElement, "maximoKg"));
            registro.put("fechaIni", getElementValue(returnElement, "fechaIni"));

            resultados.add(registro);
        }

        return resultados;
    }

    /**
     * Parsea la respuesta del servicio promedioAbasSipsaMesMadr (abastecimiento)
     */
    private List<Map<String, Object>> parsearRespuestaAbastecimiento(SOAPMessage response) throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();

        NodeList returnNodes = response.getSOAPBody().getElementsByTagName("return");

        for (int i = 0; i < returnNodes.getLength(); i++) {
            Element returnElement = (Element) returnNodes.item(i);
            Map<String, Object> registro = new HashMap<>();

            registro.put("artiId", getElementValueInt(returnElement, "artiId"));
            registro.put("artiNombre", getElementValue(returnElement, "artiNombre"));
            registro.put("fuenNombre", getElementValue(returnElement, "fuenNombre"));
            registro.put("cantidadTon", getElementValueDouble(returnElement, "cantidadTon"));
            registro.put("fechaMesIni", getElementValue(returnElement, "fechaMesIni"));

            resultados.add(registro);
        }

        return resultados;
    }

    // Métodos auxiliares para extraer valores
    private String getElementValue(Element parent, String tagName) {
        try {
            NodeList nodeList = parent.getElementsByTagName(tagName);
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            }
        } catch (Exception e) {
            // Ignorar
        }
        return "";
    }

    private Integer getElementValueInt(Element parent, String tagName) {
        try {
            String value = getElementValue(parent, tagName);
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private Double getElementValueDouble(Element parent, String tagName) {
        try {
            String value = getElementValue(parent, tagName);
            return value.isEmpty() ? 0.0 : Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
