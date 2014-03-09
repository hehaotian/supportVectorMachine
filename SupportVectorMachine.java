// Support Vector Machine Classifier
// author: Haotian He
// created by 22:20:05, March 11, 2014
// LING 572 HW 8

import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.math.*;

public class SupportVectorMachine {
    
	private Map<String, Map<String, Double>> model = new HashMap<String, Map<String, Double>>;

	public SupportVectorMachine(String model_path) {
		this.model = build_model(model_path);
	}

	private Map<String, Map<String, Double>> build_model(String model_path) throws IOException {
		BufferedReader model_file = new BufferedReader(new FileReader(model_path));
		
	}

}