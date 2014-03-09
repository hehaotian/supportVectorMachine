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

public class svm_classify {
      
   public static void main(String[] args) throws IOException {
      String test_path = args[0];
      String model_path = args[1];
      PrintStream sys_output = new PrintStream(args[1]);

      SupportVectorMachine svm = new SupportVectorMachine(model_path);
      svm.predict(test_path, sys_output);
   }

}