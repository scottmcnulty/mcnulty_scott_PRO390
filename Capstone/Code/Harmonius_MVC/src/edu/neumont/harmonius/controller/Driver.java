package edu.neumont.harmonius.controller;

import edu.neumont.harmonius.model.Model;
import edu.neumont.harmonius.view.ApplicationView;


public class Driver {

	public Driver(){
		
	}
	
	
	AnalyzerController analyzer = new AnalyzerController();
	
    ApplicationView view = new ApplicationView(analyzer);
    
    Model model = new Model(analyzer);
    
    
	public static void main(String args[]){
		new Driver();
	}

}
