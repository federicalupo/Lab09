package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Country c = new Country (rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				
				if(!idMap.containsKey(c.getCcode())) {
					idMap.put(c.getCcode(), c);
				}
				
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	/**
	 * 
	 * @param anno
	 * @param idMap
	 * @return lista vertici = stati per cui esiste confine rispettando anno
	 */
	public List<Country> trovaVertici(int anno, Map<Integer, Country> idMap) {

		String sql = "SELECT state1no, state2no " + 
				"FROM contiguity " + 
				"WHERE  contiguity.`year`<=? ";
		
		List<Country> vertici = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
		

			while (rs.next()) {
				
				
				Country c1= idMap.get(rs.getInt("state1no"));
				Country c2= idMap.get(rs.getInt("state2no"));
				
				if(c1!=null && c2!=null) {
					
					if(!vertici.contains(c1)) {
						vertici.add(c1);
					}
					
					if(!vertici.contains(c2)) {
						vertici.add(c2);
					}
				
				}
				
			}
			
			conn.close();
			return vertici;
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
		
		
	}

	
	public boolean esisteArco(Country c1, Country c2, int anno) {
		
		String sql = "SELECT COUNT(*) as tot " + 
				"FROM contiguity " + 
				"WHERE (contiguity.`state1no`=? and contiguity.`state2no`=?)  and " + 
				"contiguity.`year`<=? and  " + 
				"contiguity.`conttype`=1";
		//posso controllare solo un verso
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, c1.getCcode());
			st.setInt(2, c2.getCcode());
			st.setInt(3, anno);
			
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				int cont = rs.getInt("tot");
				
				if(cont>0)
				{
					conn.close();
					return true;
				}
			}
			
			conn.close();
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	public List<Country> adiacenti(Country c1, int anno, Map<Integer, Country> idMap) {
		
		String sql = "SELECT state2no " + 
				"FROM contiguity " + 
				"WHERE contiguity.`state1no`=? and contiguity.`year`<=? and " + 
				"contiguity.`conttype`=1";
		
		List<Country> vicini = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, c1.getCcode());
			st.setInt(2, anno);
			
			ResultSet rs = st.executeQuery();

			while(rs.next()) { //è while non if
				Country c = idMap.get(rs.getInt("state2no"));
				
				if(c!=null){
					
					vicini.add(c);
					
				}
				
			}
			
			conn.close();
			return vicini;
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
		
	}

	
	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMap) {

		String sql = "SELECT state1no, state2no " + 
				"FROM contiguity " + 
				"WHERE  contiguity.`year`<=? and " + 
				"contiguity.`conttype`=1 " + 
				"GROUP BY contiguity.`state1no`, contiguity.`state2no` ";
		
		
		List<Border> confine = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
		
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery();

			while(rs.next()){
				
				Country c1 =idMap.get(rs.getInt("state1no"));
				Country c2 = idMap.get(rs.getInt("state2no"));
				
				if(c1!=null && c2!=null) {
					Border b = new Border(c1, c2);
					confine.add(b);
				}
				
			}
			
			conn.close();
			return confine;
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	
	
}
