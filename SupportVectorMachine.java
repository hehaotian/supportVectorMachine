// Support Vector Machine Classifier
// author: Haotian He
// created by 22:20:05, March 11, 2014
// LING 572 HW 8

import java.io.*;
import java.util.*;

public class SupportVectorMachine {
    
	private Map<Double, List<Integer>> model = new HashMap<Double, List<Integer>>();
	private Map<Integer, Map<Integer, List<Integer>>> test = new HashMap<Integer, Map<Integer, List<Integer>>>();
	private String kernel_type;
	private double gamma;
	private double coef0;
	private double degree;
	private int total_sv;
	private int nr_class;
	private double rho;
	private List<Integer> nr_sv = new ArrayList<Integer>();
	private List<String> label = new ArrayList<String>();

	public SupportVectorMachine(String model_path) throws IOException {
		this.model = build_model(model_path);
	}

	private Map<Double, List<Integer>> build_model(String model_path) throws IOException {
		BufferedReader model_file = new BufferedReader(new FileReader(model_path));
		List<String> exptInfo = new ArrayList<String>();
		String line = "";
		while ((line = model_file.readLine()) != null) {
			if (kernel(line)) {
				exptInfo.add(line);
			} else {
				String[] tokens = line.split(" ");
				double key = Double.parseDouble(tokens[0]);
				for (int i = 1; i < tokens.length; i ++) {
					int value = Integer.parseInt(tokens[i].replaceAll(":1", ""));
					if (!model.containsKey(key)) model.put(key, new ArrayList<Integer>());
					List<Integer> temp = model.get(key);
					temp.add(value);
					model.put(key, temp);
				}
			}
		}
		getKernelInfo(exptInfo);
		// debugModel(model);
		return model;
	}

	public void predict(String test_path, PrintStream sys) throws IOException {
		BufferedReader test_file = new BufferedReader(new FileReader(test_path));
		String line = "";
		int instance_num = 0;
		while ((line = test_file.readLine()) != null) {
			test.put(instance_num, new HashMap<Integer, List<Integer>>());
			String[] tokens = line.split(" ");
			int trueLabel = Integer.parseInt(tokens[0]);
			List<Integer> vector = new ArrayList<Integer>();
			for (int i = 1; i < tokens.length; i ++) {
				int value = Integer.parseInt(tokens[i].replaceAll(":1", ""));
				vector.add(value);
			}
			test.get(instance_num).put(trueLabel, vector);
			instance_num ++;
		}
		// debugTest(test);
		sys.println("HERE!");
	}

	private void getKernelInfo(List<String> exptInfo) {
		for (int i = 0; i < exptInfo.size(); i ++) {
			String[] tokens = exptInfo.get(i).split(" ");
			if (tokens[0].equals("kernel_type")) kernel_type = tokens[1];
			if (tokens[0].equals("nr_class")) nr_class = Integer.parseInt(tokens[1]);
			if (tokens[0].equals("total_sv")) total_sv = Integer.parseInt(tokens[1]);
			if (tokens[0].equals("rho")) rho = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("label")) {
				for (int j = 1; j < tokens.length; j ++) {
					label.add(tokens[j]);
				}
			}
			if (tokens[0].equals("nr_sv")) {
				for (int k = 1; k < tokens.length; k ++) {
					nr_sv.add(Integer.parseInt(tokens[k]));
				}
			}
			if (tokens[0].equals("gamma")) gamma = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("coef0")) coef0 = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("degree")) degree = Double.parseDouble(tokens[1]);
		}
	}

	private boolean kernel(String line) {
		boolean kernel;
		return kernel = line.matches(".*[a-zA-Z]+[a-zA-Z]+.*");
	}

	private void debugModel(Map<Double, List<Integer>> model) {
		for (double d : model.keySet()) {
			System.out.println(d + " with its vector info: " + model.get(d));
		}
	}

	private void debugTest(Map<Integer, Map<Integer, List<Integer>>> test) {
		for (int instance_num : test.keySet()) {
			System.out.println(instance_num + " with its vector info: " + test.get(instance_num));
		}
	}
}