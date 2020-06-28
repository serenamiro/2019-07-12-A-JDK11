package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulatore {
	
	// input
	private int numStazioni;
	
	// output
	private int numCibi; // numero di cibi preparati
	private double tempoTotale; // tempo totale di preparazione in minuti
	
	// modello del mondo
	Graph<Food, DefaultWeightedEdge> grafo;
	List<Food> cibiDisponibili; 
	List<Stazione> stazioniDiLavoro; 
	
	// coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulatore (int k, Graph<Food, DefaultWeightedEdge> grafo) {
		this.numStazioni = k;
		this.grafo = grafo;
	}
	
	public void init(Food ciboScelto) {
		cibiDisponibili = new ArrayList<Food>(this.grafo.vertexSet());
		
		for(Food f : cibiDisponibili) {
			f.setPreparazione(StatoPreparazione.DA_PREPARARE);
		}
		
		this.stazioniDiLavoro = new ArrayList<>();
		for(int i = 0; i<this.numStazioni; i++) {
			stazioniDiLavoro.add(new Stazione(true, null));
		}
		
		this.numCibi = 0;
		this.tempoTotale = 0.0;
		
		this.queue = new PriorityQueue<Event>();
		
		List<FoodCalories> vicini = getMaxCalorieCongiunte(ciboScelto);
		
		for(int i = 0; i<this.numStazioni && i<vicini.size(); i++) {
			this.stazioniDiLavoro.get(i).setLibero(false);
			this.stazioniDiLavoro.get(i).setFood(vicini.get(i).getFood());
			vicini.get(i).getFood().setPreparazione(StatoPreparazione.IN_CORSO);
			
			Event e = new Event(vicini.get(i).getCalories(), EventType.FINE_PREPARAZIONE,
					this.stazioniDiLavoro.get(i), vicini.get(i).getFood());
			
			queue.add(e);
		}
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
			
		case INIZIO_PREPARAZIONE:
			List<FoodCalories> vicini = getMaxCalorieCongiunte(e.getFood());
			FoodCalories prox = null;
			for(FoodCalories vicina : vicini) {
				if(vicina.getFood().getPreparazione()==StatoPreparazione.DA_PREPARARE) {
					prox = vicina;
					break;
				}
			}
			if(prox != null) {
				prox.getFood().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibero(false);
				e.getStazione().setFood(prox.getFood());
				
				Event e2 = new Event(e.getTempo()+prox.getCalories(), 
						EventType.FINE_PREPARAZIONE,
						e.getStazione(), 
						prox.getFood());
				
				this.queue.add(e2);
				break;
			}
			
		case FINE_PREPARAZIONE: 
			this.numCibi++;
			this.tempoTotale = e.getTempo();
			
			e.getStazione().setLibero(true);
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO);
			
			Event e2 = new Event(e.getTempo(), EventType.INIZIO_PREPARAZIONE, e.getStazione(), e.getFood());
			
			this.queue.add(e2);
			break;
		}
			
	}

	public List<FoodCalories> getMaxCalorieCongiunte(Food ciboScelto){
		List<Food> vicini = Graphs.neighborListOf(this.grafo, ciboScelto);
		List<FoodCalories> adiacenze = new LinkedList<FoodCalories>();
		for(Food f : vicini) {
			Double calorie = this.grafo.getEdgeWeight(this.grafo.getEdge(ciboScelto, f));
			adiacenze.add(new FoodCalories(f, calorie));
		}
		Collections.sort(adiacenze);
		return adiacenze;
	}

	public int getNumStazioni() {
		return numStazioni;
	}

	public int getNumCibi() {
		return numCibi;
	}

	public double getTempoTotale() {
		return tempoTotale;
	}


	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Food> getCibiDisponibili() {
		return cibiDisponibili;
	}

	public List<Stazione> getStazioniDiLavoro() {
		return stazioniDiLavoro;
	}

	public PriorityQueue<Event> getQueue() {
		return queue;
	}
	
	
}
