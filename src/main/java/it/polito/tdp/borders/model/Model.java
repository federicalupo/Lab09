package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;



import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
		private Map<Integer, Country> idMap; //tutti gli stati (non solo i vertici)
		private BordersDAO dao;
		private Graph<Country, DefaultEdge> grafo;
	

	public Model() {
		
		idMap = new HashMap<>();
		this.dao = new BordersDAO();
		
		dao.loadAllCountries(idMap);
		
	}

	public void creaGrafo(int anno) {
		
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(grafo, dao.trovaVertici(anno, idMap));
		
		/*Approccio 3*/
		
		for(Border b: this.dao.getCountryPairs(anno, idMap)) {
			
			if(! grafo.containsEdge(b.getState1(), b.getState2())) {
				
				grafo.addEdge(b.getState1(), b.getState2());
				
			}
			
			
		}
		
	
		/*Approccio 2 
		for(Country c1: grafo.vertexSet()) {
			for(Country c2: dao.adiacenti(c1, anno, idMap)) {
				if(!grafo.containsEdge(c1, c2)) {
					grafo.addEdge(c1, c2);
				}
	
			}		
		}*/
		
		
		/*Approccio 1
		for(Country c1: grafo.vertexSet()) {
			for(Country c2: grafo.vertexSet()) {
				if(! grafo.containsEdge(c1, c2)) {
					if(this.dao.esisteArco(c1, c2, anno)) {
					grafo.addEdge(c1, c2);		
				}
	
			}
		}
	}*/
		
}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
		
	}
	
	
	public Set<Country> vertici(){
		
		return this.grafo.vertexSet();
		
	}
	
	public int nConfinanti(Country c) {
		return this.grafo.degreeOf(c);
	}
	
	
	
	public int nComponentiConnesse(){
		
		ConnectivityInspector connectivityInspector = new ConnectivityInspector(this.grafo);
		
		
		return connectivityInspector.connectedSets().size();
		
		
	}
	
}
