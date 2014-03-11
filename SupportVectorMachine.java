// Support Vector Machine Classifier
// author: Haotian He (haotianh) and Yaohua Zhuo (yz48)
// created by 22:20:05, March 11, 2014
// LING 572 HW 8

import java.io.*;
import java.util.*;

public class SupportVectorMachine {
    
	private Map<Double, Map<Integer, Integer>> model = new HashMap<Double, Map<Integer, Integer>>();

	private String kernel_type;
	private double gamma;
	private double coef0;
	private double degree;
	private double rho;

	public SupportVectorMachine(String model_path) throws IOException {
		this.model = build_model(model_path);
	}

	private Map<Double, Map<Integer, Integer>> build_model(String model_path) throws IOException {
		BufferedReader model_file = new BufferedReader(new FileReader(model_path));
		List<String> exptInfo = new ArrayList<String>();
		String line = "";
		while ((line = model_file.readLine()) != null) {
			if (isKernel(line)) {
				exptInfo.add(line);
			} else {
				String[] tokens = line.split(" ");
				double weight = Double.parseDouble(tokens[0]);
				// System.out.println(weight * 2.0 + " " + weight + " " + weight * 20000 + " " + weight * 10000);
				model.put(weight, new HashMap<Integer, Integer>());
				for (int i = 1; i < tokens.length; i ++) {
					int feat = Integer.parseInt(tokens[i].replaceAll(":1", ""));
					int val = Integer.parseInt(tokens[i].replaceAll("[\\d]+:", ""));
					model.get(weight).put(feat, val);
				}
			}
		}
		getKernelInfo(exptInfo);
		return model;
	}

	public void predict(String test_path, PrintStream sys) throws IOException {
		List<String> results = new ArrayList<String>();
		List<String> compare = new ArrayList<String>();
		
		BufferedReader test_file = new BufferedReader(new FileReader(test_path));
		String line = "";
		while ((line = test_file.readLine()) != null) {
			String[] tokens = line.split(" ");

			// trueLabel: test file true class label
			String trueLabel = tokens[0];
			String predLabel = "";

			// instance_vector: the vector of the test instance with feat:val (feat=val)
			Map<Integer, Integer> instance_vector = new HashMap<Integer, Integer>();
			for (int i = 1; i < tokens.length; i ++) {
				int feat = Integer.parseInt(tokens[i].replaceAll(":1", ""));
				int val = Integer.parseInt(tokens[i].replaceAll("[\\d]+:", ""));
				instance_vector.put(feat, val);
			}

			double sum = 0.0;
			for (double weight : model.keySet()) {
				Map<Integer, Integer> support_vector = model.get(weight);
				double kernel = kernelFunction(support_vector, instance_vector);
				sum += weight * kernel;
			}
			sum -= rho;

			if (sum >= 0) {
				predLabel = "0";
			} else {
				predLabel = "1";
			}

			compare.add(trueLabel + " " + predLabel);
			results.add(trueLabel + " " + predLabel + " " + sum); 
		}
		printResults(results, sys);
		getAccuracy(compare);
	}

	private void getKernelInfo(List<String> exptInfo) {
		for (int i = 0; i < exptInfo.size(); i ++) {
			String[] tokens = exptInfo.get(i).split(" ");
			if (tokens[0].equals("kernel_type")) kernel_type = tokens[1];
			if (tokens[0].equals("rho")) rho = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("gamma")) gamma = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("coef0")) coef0 = Double.parseDouble(tokens[1]);
			if (tokens[0].equals("degree")) degree = Double.parseDouble(tokens[1]);
		}
		// DEBUG:
		System.out.println(kernel_type);
		System.out.println(rho);
		System.out.println(gamma);
		System.out.println(coef0);
		System.out.println(degree);
		//
	}

	private void printResults(List<String> results, PrintStream sys) {
		for (int i = 0; i < results.size(); i ++) {
			sys.println(results.get(i));
		}
	}

	private void getAccuracy(List<String> compare) {
		int totalCount = compare.size();
		int rightCount = totalCount;
		for (int i = 0; i < totalCount; i ++) {
			String[] tokens = compare.get(i).split(" ");
			if (!tokens[0].equals(tokens[1])) rightCount --;
		}
		double accuracy = rightCount * 1.0 / totalCount;
		System.out.println(accuracy);
	}

	private boolean isKernel(String line) {
		boolean isKernel;
		return isKernel = line.matches(".*[a-zA-Z]+[a-zA-Z]+.*");
	}

	private double kernelFunction(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double kernel = 0.0;
		if (kernel_type.equals("linear")) {
			kernel = linearKernel(support_vector, instance_vector);
		} 
		if (kernel_type.equals("polynomial")) {
			kernel = polynomialKernel(support_vector, instance_vector);
		}
		if (kernel_type.equals("rbf")) {
			kernel = rbfKernel(support_vector, instance_vector);
		}
		if (kernel_type.equals("sigmoid")) {
			kernel = sigmoidKernel(support_vector, instance_vector);
		} 
		return kernel;
	}

	private double linearKernel(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double sum = dotProduct(support_vector, instance_vector);
		return sum;
	}

	private double polynomialKernel(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double sum = dotProduct(support_vector, instance_vector);
		sum = gamma * sum + coef0;		
		double res = Math.pow(sum, degree);
		return res;
	}

	private double rbfKernel(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double sum = 0.0;
		Set<Integer> common = intersection(support_vector.keySet(), instance_vector.keySet());
		Set<Integer> checked = new HashSet<Integer>();
		for (int a : common) {
			sum += Math.pow((support_vector.get(a) - instance_vector.get(a)), 2);
			checked.add(a);
		}
		for (int b : instance_vector.keySet()) {
			if (!checked.contains(b)) {
				sum += Math.pow(instance_vector.get(b), 2);
				checked.add(b);
			}
		}
		for (int c : support_vector.keySet()) {
			if (!checked.contains(c)) {
				sum += Math.pow(support_vector.get(c), 2);
			}
		}
		sum = gamma * sum * (-1.0);
		return Math.exp(sum);
	}

	private double sigmoidKernel(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double sum = dotProduct(support_vector, instance_vector);
		sum = gamma * sum + coef0;
		double numerator = Math.exp(sum) - Math.exp(-sum);
		double donominator = Math.exp(sum) + Math.exp(-sum);
		return numerator / donominator;
	}

	private double dotProduct(Map<Integer, Integer> support_vector, Map<Integer, Integer> instance_vector) {
		double sum = 0.0;
		Set<Integer> common = intersection(support_vector.keySet(), instance_vector.keySet());
		for (int e : common) {
			sum += support_vector.get(e) * instance_vector.get(e);
		}
		return sum;
	}

	private Set<Integer> intersection(Set<Integer> set1, Set<Integer> set2) {
		Set<Integer> res = new HashSet<Integer>();
		Set<Integer> a = new HashSet<Integer>();
		Set<Integer> b = new HashSet<Integer>();
		if (set1.size() <= set2.size()) {
			a = set1;
			b = set2;
		} else {
			a = set2;
			b = set1;
		}
		for (int e : a) {
			if (b.contains(e)) {
				res.add(e);
			}
		}
		return res;
	}

}