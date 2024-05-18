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
        String xquery = "for $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')//Punt where contains($x/Tooltip, 'NITBUS') return $x";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        XQResultSequence result = exp.executeQuery();
        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }

    public void inserir() throws XQException {
        Scanner scanner = new Scanner(System.in);

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

        String xmlData = "<Punt>"
                + "<Coord>"
                + "<ED50_COORD_X>" + ed50CoordX + "</ED50_COORD_X>"
                + "<ED50_COORD_Y>" + ed50CoordY + "</ED50_COORD_Y>"
                + "<ETRS89_COORD_X>" + etrs89CoordX + "</ETRS89_COORD_X>"
                + "<ETRS89_COORD_Y>" + etrs89CoordY + "</ETRS89_COORD_Y>"
                + "<Longitud>" + longitud + "</Longitud>"
                + "<Latitud>" + latitud + "</Latitud>"
                + "</Coord>"
                + "<Icon>" + icon + "</Icon>"
                + "<Tooltip>" + tooltip + "</Tooltip>"
                + "<URL>" + url + "</URL>"
                + "</Punt>";

        String xquery = "update insert " + xmlData + " into doc('/db/Aparcaments/ESTACIONS_BUS_GEOXML.xml')/root";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        exp.executeQuery();
    }

    public void modificar(String xmlData) throws XQException {
        String xquery = "update replace $x in doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/root with " + xmlData;
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        exp.executeQuery();
    }

    public void eliminar(String condition) throws XQException {
        String xquery = "update delete doc('/db/bus/ESTACIONS_BUS_GEOXML.xml')/root/*[condition]";
        XQPreparedExpression exp = conn.prepareExpression(xquery);
        exp.executeQuery();
    }
}
