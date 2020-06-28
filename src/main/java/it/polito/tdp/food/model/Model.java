package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Map<Integer, Food> food;
	public Graph<Food, DefaultWeightedEdge> grafo;
	private FoodDao dao;
	
	public Model() {
		this.dao = new FoodDao();
	}
	
	public void creaGrafo(int n) {
		this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		food = this.dao.getFoodByPortion(n);
		
		Graphs.addAllVertices(this.grafo, food.values());
		
		for(Adiacenza a : dao.getAdiacenze()) {
			if(food.containsKey(a.getF1().getFood_code()) && food.containsKey(a.getF2().getFood_code())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		System.out.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	 
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getMaxCalorieCongiunte(Food ciboScelto){
		List<Food> vicini = Graphs.neighborListOf(this.grafo, ciboScelto);
		List<Adiacenza> adiacenze = new LinkedList<Adiacenza>();
		for(Food f : vicini) {
			Adiacenza a = new Adiacenza(ciboScelto, f, this.grafo.getEdgeWeight(this.grafo.getEdge(ciboScelto, f)));
			adiacenze.add(a);
		}
		Collections.sort(adiacenze);
		return adiacenze;
	}
	
	public List<Food> getBestFive(Food ciboScelto){
		List<Adiacenza> adiacenze = this.getMaxCalorieCongiunte(ciboScelto);
		List<Food> res = new LinkedList<Food>();
		if(adiacenze.size()>5) {
			for(int i=1; i<=5; i++) {
				res.add(adiacenze.get(i).getF2());
			}
		} else {
			for(int i=1; i<=adiacenze.size(); i++) {
				res.add(adiacenze.get(i).getF2());
			}
		}
		return res;
	}
	
	public String simula(Food cibo, int k) {
		Simulatore sim = new Simulatore(k, this.grafo);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", sim.getNumCibi(), sim.getTempoTotale());
		return messaggio;
	}
}
