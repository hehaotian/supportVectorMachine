// Support Vector Machine Classifier
// author: Haotian He
// created by 22:20:05, March 11, 2014
// LING 572 HW 8

import java.io.*;
import java.util.*;

public class SupportVectorMachine {
    
	private Map<Double, Integer> model = new HashMap<Double, Integer>();

	public SupportVectorMachine(String model_path) throws IOException {
		this.model = build_model(model_path);
	}

	private Map<Double, Integer> build_model(String model_path) throws IOException {
		BufferedReader model_file = new BufferedReader(new FileReader(model_path));
		List<String> exptInfo = new ArrayList<String>();
		String kernel = "";
		String line = "";
		while ((line = model_file.readLine()) != "SV") {
			exptInfo.add(line);
			System.out.println(line);
		}
		while ((line = model_file.readLine()) == "SV") {
			while ((line = model_file.readLine()) != null) {
				break;
			}
		}
		return model;
	}

	public void predict(String test_path, PrintStream sys_output) throws IOException {
		System.out.println("HERE!");
	}
}