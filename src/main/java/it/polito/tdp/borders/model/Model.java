package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

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
		
		ConnectivityInspector<Country, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(this.grafo);
		
		return connectivityInspector.connectedSets().size();
		
	}

	public List<Country> trovaViciniAmpiezza(Country stato) {
		
		List<Country> visitaAmpiezza = new LinkedList<>();
		
		GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(this.grafo, stato );
		
		while(bfv.hasNext()) {
			visitaAmpiezza.add(bfv.next());
		}
		
		
		return visitaAmpiezza;
	
	}
	public List<Country> trovaViciniProfondita(Country stato) {
		
		List<Country> visitaProfondita = new LinkedList<>();
		
		GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<>(this.grafo, stato );
		
		while(dfv.hasNext()) {
			 visitaProfondita.add(dfv.next());
		}
		
		
		return  visitaProfondita;
	
	}
	/** RICORSIONE
	 * 
	 * livello = nodo
	 * 
	 * sol parziale = sequenza nodi
	 * 
	 * terminazione = non si può aggiungere altro -> lista(vicini - visitati).size() = 0 / se non ha vicini -> torna indietro 
	 * oppure quando entra nel for, se tutti i vicini sono stati messi, finisce il for e torna al livello prima 
	 * (=non serve terminazione)
	 * 
	 * soluzioni livello+1 => dato lo stato, trova vicini, per ogni vicino chiama ricorsione se il vicino non è stato aggiunto
	 * 
	 * salvo nella lista
	 * 
	 * @param stato
	 * @return  lista visita
	 */
	public List<Country> trovaViciniRicorsiva(Country stato){
		
		List<Country> visita = new LinkedList<>();
		
		visita.add(stato); //aggiungi il primo
		cerca(stato, visita);
		
		
		return visita;
	}
	
	
	private void cerca(Country stato, List<Country> parziale) {
		
		Set<Country> vicini = Graphs.neighborSetOf(this.grafo, stato);
	
		if(vicini.size()==0) {
			
			return;
				
		}
	
		for(Country c: vicini) {
			if(! parziale.contains(c)){ //se contenuto non lo aggiungere!!!!
					parziale.add(c);
					cerca(c, parziale);
				}
		}
		//quando esce dal for, torna ai livelli prima
	
	
	}
	
	
	public List<Country> trovaViciniIterativa(Country stato) {
		List<Country> visitati = new LinkedList<>();
		
		List<Country> daVisitare = new LinkedList<>();
			
		
		daVisitare.add(stato);
		
		while(daVisitare.size()>0) {
		
			Country corrente = daVisitare.get(0);
			
			List<Country> adiacenti = Graphs.neighborListOf(this.grafo, corrente); //adiacenti
			
			for(Country c: adiacenti) { //controllare non ci sia già stata => gli elementi in "daVisitare" non saranno in "visitati"
				if(!visitati.contains(c) && !daVisitare.contains(c)) {
					
					daVisitare.add(c); 		
					
					// se metto solo primo controllo, in "daVisitare" potrebbero esserci duplicati, nodi diversi hanno stesso adiacente che non è 
					//stato visitato ====> faccio controllo x evitare duplicati
				}
			}
			
			visitati.add(corrente);
			
			daVisitare.remove(0);
			
		}
		
		return visitati;
		
	}
	
	
	
	
	
}
