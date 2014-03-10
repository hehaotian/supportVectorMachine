// Support Vector Machine Classifier
// author: Haotian He
// created by 22:20:05, March 11, 2014
// LING 572 HW 8

import java.io.*;
import java.util.*;

public class SupportVectorMachine {
    
	private Map<Double, Integer> model = new HashMap<Double, Integer>();
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

	private Map<Double, Integer> build_model(String model_path) throws IOException {
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
					model.put(key, value);
				}
			}
		}
		getKernelInfo(exptInfo);
		return model;
	}

	public void predict(String test_path, PrintStream sys_output) throws IOException {
		System.out.println("HERE!");
	}

	public void getKernelInfo(List<String> exptInfo) {
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
		System.out.println(gamma);
		System.out.println(coef0);
		System.out.println(degree);
	}

	public boolean kernel(String line) {
		boolean kernel;
		return kernel = line.matches(".*[a-zA-Z]+[a-zA-Z]+.*");
	}
}