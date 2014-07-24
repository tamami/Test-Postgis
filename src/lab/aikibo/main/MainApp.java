package lab.aikibo.main;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.postgis.Geometry;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;


public class MainApp {
	
	public static void main(String args[]) {
		
	
	java.sql.Connection conn;
	
	try {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://192.168.2.27:5432/pbbmap";
		conn = DriverManager.getConnection(url, "pbb", "rahasiapbb");
		
		((org.postgresql.PGConnection) conn).addDataType("geometry", "org.postgis.PGgeometry");
		((org.postgresql.PGConnection) conn).addDataType("box3d", "org.postgis.PGbox3d");
		
		Statement s = conn.createStatement();
		//ResultSet r = s.executeQuery("select ST_AsText(geom) as geom, id from t3329");
		ResultSet r = s.executeQuery("select geom, id from t3329");
		while(r.next()) {
			PGgeometry geom = (PGgeometry)r.getObject(1);
			int id = r.getInt(2);
			System.out.println("Row " + id + ":");
			System.out.println(geom.toString());
			
			System.out.println("Data Start from here:");
			if(geom.getGeoType() == Geometry.POLYGON){
				Polygon pl = (Polygon) geom.getGeometry();
				for(int re = 0; re < pl.numRings(); re++) {
					LinearRing rng = pl.getRing(re);
					System.out.println("Ring : " + re);
					for(int p=0; p<rng.numPoints(); p++) {
						Point pt = rng.getPoint(p);
						System.out.println("Point : " + p);
						System.out.println(pt.toString());
					}
				}
			}
		}
		s.close();
		conn.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
	}
}
