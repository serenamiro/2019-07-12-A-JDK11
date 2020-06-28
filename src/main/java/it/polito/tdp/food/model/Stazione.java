package it.polito.tdp.food.model;

public class Stazione {
	
	Boolean libero;
	Food food;
	
	public Stazione(Boolean libero, Food food) {
		super();
		this.libero = libero;
		this.food = food;
	}

	public Boolean getLibero() {
		return libero;
	}

	public void setLibero(Boolean libero) {
		this.libero = libero;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	

}
