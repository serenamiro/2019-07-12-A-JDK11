package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM `portion`" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public Map<Integer, Food> getFoodByPortion(int n){
		String sql = "SELECT f.food_code, f.display_name, COUNT(*) " + 
				"FROM food f, `portion` p " + 
				"WHERE f.food_code = p.food_code " + 
				"GROUP BY f.food_code, f.display_name " + 
				"HAVING COUNT(*) = ?";
		
		Map<Integer, Food> food = new HashMap<Integer, Food>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, n);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f = new Food(res.getInt("food_code"), res.getString("display_name"));
				food.put(f.getFood_code(), f);
			}
			
			conn.close();
			return food;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getAdiacenze(){
		String sql = "SELECT f1.food_code, f1.display_name, f2.food_code, f2.display_name, AVG(c1.condiment_calories+c2.condiment_calories) AS peso " + 
				"FROM food f1, food f2, food_condiment fc1, food_condiment fc2, condiment c1, condiment c2 " + 
				"WHERE f1.food_code>f2.food_code AND " + 
				"		f1.food_code = fc1.food_code and " + 
				"		f2.food_code = fc2.food_code and " + 
				"		fc1.condiment_code = fc2.condiment_code and " + 
				"		fc1.condiment_code = c1.condiment_code and " + 
				"		fc2.condiment_code = c2.condiment_code " + 
				"GROUP BY f1.food_code, f1.display_name, f2.food_code, f2.display_name";
		
		List<Adiacenza> adj = new ArrayList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
		
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f1 = new Food(res.getInt("f1.food_code"), res.getString("f1.display_name"));
				Food f2 = new Food(res.getInt("f2.food_code"), res.getString("f2.display_name"));
				Adiacenza a = new Adiacenza(f1, f2, res.getDouble("peso"));
				adj.add(a);
			}
			
			conn.close();
			return adj;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
