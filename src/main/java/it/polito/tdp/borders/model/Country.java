package it.polito.tdp.borders.model;

public class Country {
	private int ccode;
	private String stateAbb; 
	private String stateNme;

	
	public Country( int ccode, String stateAbb, String stateNme) {
		super();
		this.ccode = ccode;
		this.stateAbb = stateAbb;
		this.stateNme = stateNme;
	}

	public String getStateAbb() {
		return stateAbb;
	}

	public int getCcode() {
		return ccode;
	}


	public String getStateNme() {
		return stateNme;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ccode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (ccode != other.ccode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  ccode + " " + stateNme;
	}
	
	
	
	
}
