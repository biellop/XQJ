import javax.xml.namespace.QName;
import javax.xml.xquery.*;
import net.xqj.exist.ExistXQDataSource;

import java.util.Scanner;

public class GestorDB {
    private XQConnection conn;

    public GestorDB() throws XQException {
        XQDataSource xqs = new ExistXQDataSource();
        xqs.setProperty("serverName", "localhost");
        xqs.setProperty("port", "8080");
        xqs.setProperty("user", "admin");
        xqs.setProperty("password", "");
        conn = xqs.getConnection();
    }

    public void close() throws XQException {
        conn.close();
    }

    // Consulta 1: Retorna tots els elements Estacio del document
    public void consulta1() throws XQException {
        String xquery = "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')//* return $x";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        XQResultSequence result = exp.executeQuery();
        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }

    // Consulta 2: Selecciona tots els elements del document
    public void consulta2() throws XQException {
        String xquery = "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')//Punt where contains($x/Tooltip, 'NITBUS') return $x";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        XQResultSequence result = exp.executeQuery();
        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }
    public void consulta3() throws XQException {
        String xquery = "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')//Punt where contains($x/Tooltip, 'M19') return $x";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        XQResultSequence result = exp.executeQuery();
        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }
    public void consulta4() throws XQException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduir ID a buscar:");
        String id = scanner.nextLine();

        // Consulta para buscar el Punt por ID
        String xquery = String.format(
                "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt " +
                        "where $x/ID = '%s' " +
                        "return $x",
                id
        );

        System.out.println("XQuery: " + xquery);  // Mensaje de depuración

        XQPreparedExpression exp = conn.prepareExpression(xquery);
        XQResultSequence result = exp.executeQuery();

        if (result.next()) {
            System.out.println("Resultat de la cerca: " + result.getItemAsString(null));
        } else {
            System.out.println("No s'ha trobat cap Punt amb l'ID indicat.");
        }
    }


    public void inserir() throws XQException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduir ID:");
        String id = scanner.nextLine();

        // Consulta para verificar si el ID ya existe
        String xqueryCheckId = String.format(
                "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt " +
                        "where $x/ID = '%s' " +
                        "return $x",
                id
        );

        XQPreparedExpression expCheckId = conn.prepareExpression(xqueryCheckId);
        XQResultSequence resultCheckId = expCheckId.executeQuery();

        if (resultCheckId.next()) {
            System.out.println("El ID ya está en uso. Inserción cancelada.");
            return;
        }

        // Solicitar el resto de datos
        System.out.println("Introduir ED50_COORD_X:");
        String ed50CoordX = scanner.nextLine();
        System.out.println("Introduir ED50_COORD_Y:");
        String ed50CoordY = scanner.nextLine();
        System.out.println("Introduir ETRS89_COORD_X:");
        String etrs89CoordX = scanner.nextLine();
        System.out.println("Introduir ETRS89_COORD_Y:");
        String etrs89CoordY = scanner.nextLine();
        System.out.println("Introduir Longitud:");
        String longitud = scanner.nextLine();
        System.out.println("Introduir Latitud:");
        String latitud = scanner.nextLine();
        System.out.println("Introduir Icon URL:");
        String icon = scanner.nextLine();
        System.out.println("Introduir Tooltip:");
        String tooltip = scanner.nextLine();
        System.out.println("Introduir URL:");
        String url = scanner.nextLine();

        String xmlData = String.format(
                "<Punt>" +
                        "<ID>%s</ID>" +
                        "<Coord>" +
                        "<ED50_COORD_X>%s</ED50_COORD_X>" +
                        "<ED50_COORD_Y>%s</ED50_COORD_Y>" +
                        "<ETRS89_COORD_X>%s</ETRS89_COORD_X>" +
                        "<ETRS89_COORD_Y>%s</ETRS89_COORD_Y>" +
                        "<Longitud>%s</Longitud>" +
                        "<Latitud>%s</Latitud>" +
                        "</Coord>" +
                        "<Icon>%s</Icon>" +
                        "<Tooltip>%s</Tooltip>" +
                        "<URL>%s</URL>" +
                        "</Punt>",
                id, ed50CoordX, ed50CoordY, etrs89CoordX, etrs89CoordY, longitud, latitud, icon, tooltip, url
        );

        String xqueryInsert = String.format(
                "update insert %s into doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange",
                xmlData
        );

        System.out.println("XQuery Insert: " + xqueryInsert);  // Mensaje de depuración

        try {
            XQExpression expr = conn.createExpression();
            expr.executeCommand(xqueryInsert);
            System.out.println("Nou Punt inserit correctament.");
        } catch (XQException e) {
            System.out.println("Error en la inserción: " + e.getMessage());
            return;
        }

        // Consulta para verificar la inserción
        String xqueryVerify = String.format(
                "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt " +
                        "where $x/ID = '%s' " +
                        "return $x",
                id
        );

        System.out.println("XQuery Verify: " + xqueryVerify);  // Mensaje de depuración

        try {
            XQPreparedExpression expVerify = conn.prepareExpression(xqueryVerify);
            XQResultSequence resultVerify = expVerify.executeQuery();

            if (resultVerify.next()) {
                System.out.println("Verificación de la inserción: " + resultVerify.getItemAsString(null));
            } else {
                System.out.println("No se encontró el nuevo Punt insertado.");
            }
        } catch (XQException e) {
            System.out.println("Error en la verificación: " + e.getMessage());
        }
    }

    public void modificar() throws XQException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduir ID del Punt a modificar:");
        String id = scanner.nextLine();

        // Verificar si el ID existe
        String xqueryCheckId = String.format(
                "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt " +
                        "where $x/ID = '%s' " +
                        "return $x",
                id
        );

        XQPreparedExpression expCheckId = conn.prepareExpression(xqueryCheckId);
        XQResultSequence resultCheckId = expCheckId.executeQuery();

        if (!resultCheckId.next()) {
            System.out.println("El ID no existe. Modificación cancelada.");
            return;
        }

        // Solicitar nuevos datos
        System.out.println("Introduir ED50_COORD_X:");
        String ed50CoordX = scanner.nextLine();
        System.out.println("Introduir ED50_COORD_Y:");
        String ed50CoordY = scanner.nextLine();
        System.out.println("Introduir ETRS89_COORD_X:");
        String etrs89CoordX = scanner.nextLine();
        System.out.println("Introduir ETRS89_COORD_Y:");
        String etrs89CoordY = scanner.nextLine();
        System.out.println("Introduir Longitud:");
        String longitud = scanner.nextLine();
        System.out.println("Introduir Latitud:");
        String latitud = scanner.nextLine();
        System.out.println("Introduir Icon URL:");
        String icon = scanner.nextLine();
        System.out.println("Introduir Tooltip:");
        String tooltip = scanner.nextLine();
        System.out.println("Introduir URL:");
        String url = scanner.nextLine();

        String xmlData = String.format(
                "<Punt>" +
                        "<ID>%s</ID>" +
                        "<Coord>" +
                        "<ED50_COORD_X>%s</ED50_COORD_X>" +
                        "<ED50_COORD_Y>%s</ED50_COORD_Y>" +
                        "<ETRS89_COORD_X>%s</ETRS89_COORD_X>" +
                        "<ETRS89_COORD_Y>%s</ETRS89_COORD_Y>" +
                        "<Longitud>%s</Longitud>" +
                        "<Latitud>%s</Latitud>" +
                        "</Coord>" +
                        "<Icon>%s</Icon>" +
                        "<Tooltip>%s</Tooltip>" +
                        "<URL>%s</URL>" +
                        "</Punt>",
                id, ed50CoordX, ed50CoordY, etrs89CoordX, etrs89CoordY, longitud, latitud, icon, tooltip, url
        );

        String xqueryUpdate = String.format(
                "update replace doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt[ID='%s'] with %s",
                id, xmlData
        );

        System.out.println("XQuery Update: " + xqueryUpdate);  // Mensaje de depuración

        try {
            XQExpression expr = conn.createExpression();
            expr.executeCommand(xqueryUpdate);
            System.out.println("Punt modificat correctament.");
        } catch (XQException e) {
            System.out.println("Error en la modificación: " + e.getMessage());
        }
    }

    public void eliminar() throws XQException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduir ID del Punt a eliminar:");
        String id = scanner.nextLine();

        // Verificar si el ID existe
        String xqueryCheckId = String.format(
                "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt " +
                        "where $x/ID = '%s' " +
                        "return $x",
                id
        );

        XQPreparedExpression expCheckId = conn.prepareExpression(xqueryCheckId);
        XQResultSequence resultCheckId = expCheckId.executeQuery();

        if (!resultCheckId.next()) {
            System.out.println("El ID no existe. Eliminación cancelada.");
            return;
        }

        String xqueryDelete = String.format(
                "update delete doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/Guiamap_Xchange/Punt[ID='%s']",
                id
        );

        System.out.println("XQuery Delete: " + xqueryDelete);  // Mensaje de depuración

        try {
            XQExpression expr = conn.createExpression();
            expr.executeCommand(xqueryDelete);
            System.out.println("Punt eliminat correctament.");
        } catch (XQException e) {
            System.out.println("Error en la eliminación: " + e.getMessage());
        }
    }
}
