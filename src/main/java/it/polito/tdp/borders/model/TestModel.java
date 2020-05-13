package it.polito.tdp.borders.model;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		System.out.println("TestModel ");
		
	System.out.println("Creo il grafo relativo al 2000\n");
	model.creaGrafo(2000);
	System.out.println("nvertici: "+model.nVertici()+"\n"); //211
	System.out.println("nArchi: "+model.nArchi()+"\n"); //391 
	
		
//		List<Country> countries = model.getCountries();
//		System.out.format("Trovate %d nazioni\n", countries.size());

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));		
		
	}

}
