/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;

import mabecker.amaltheatorcm.transformation.AmaltheaToRCMTransform;
import mabecker.amaltheatorcm.transformation.BackAnnotation;

public class App {

	/**
	 * Indicate how often the transformation should be executed. 
	 * (Only important to collect statistics on the transformation times.)
	 */
	private final static int EXPERIMENT_ROUNDS = 1;
	
	/**
	 * Path to the AMALTHEA and RCM model
	 */
	private static String amaltheaPath = null;
	private static String rcmPath = null;

	/**
	 * Flags to indicate the the transformation direction 
	 */
	private static boolean transform = false;		//AMALTHEA -> RCM
	private static boolean backannotate = false;	//RCM->AMALTHEA (back annotate)
	
	
	/**
	 * Transform an AMALTHEA model into its RCM representation or back annotate the 
	 * AMALTHEA model after timing analysis was performed on the RCM model.
	 * 
	 * Argument string:
	 * -a / amalthea		Path to the Amalthea model of the application.
     * -r / rcm				RCM destination path.
     * -t / transform		Indicate that the Amalthea model shall be transformed to an RCM model.
     * -b / backannotate	Indicate that the Amalthea model shall be annotated with the timing analysis results obtained for the RCM model.
	 * @param args Argument string.
	 */
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	
    	// Parse the input arguments
    	parseArguments(args);
    	
    	Amalthea model = null;
		
    	// Variables to log the runtime of different parts of the model transformation
		double[] setupTime = new double[EXPERIMENT_ROUNDS];
		double[] transformationTime = new double[EXPERIMENT_ROUNDS];
		double[] exportTime = new double[EXPERIMENT_ROUNDS];
		double[] annotateTime = new double[EXPERIMENT_ROUNDS];
		double[] totalTime = new double[EXPERIMENT_ROUNDS];
			
		// Translate the model EXPERIMENT_ROUNDS times to get min/max/average values of the 
		// transformation times. For general use EXPERIMENT_ROUNDS should be 1
		for (int i = 0; i < EXPERIMENT_ROUNDS; i++) {
			
			double startTime = System.nanoTime();
			
	        File inputFile = new File(amaltheaPath);
	        model = null;
	        model = AmaltheaLoader.loadFromFile(inputFile);
	        
	        if (model == null) {
	        	System.err.println("Problem loading the Amalthea model!");
	    		System.exit(1);
	        }
		        
	        /**
	         * Transformation from Amalthea to RCM
	         */
	        if (transform) {
		        AmaltheaToRCMTransform transform = new AmaltheaToRCMTransform(model, getFileNameWithoutExtension(inputFile));
			      
		        double startTransformTime = System.nanoTime();
			        
		        transform.tranformModel();
			        
		        double startExportTime = System.nanoTime();
			        
		        transform.exportRCM(rcmPath);
		        
		        double endTime = System.nanoTime();
		        
			    setupTime[i] = startTransformTime - startTime;
			    transformationTime[i] = startExportTime - startTransformTime;
			    exportTime[i] = endTime - startExportTime;
			    totalTime[i] = endTime - startTime;
	        } 
	        /**
	         * Transformation from RCM to Amalthea (Back-Annotation)
	         */
	        else if (backannotate) {
	        	double startAnnotateTime = System.nanoTime();
	        	BackAnnotation back = new BackAnnotation(rcmPath, amaltheaPath, model, getFileNameWithoutExtension(inputFile));
	        	back.annotate();
	        	double endTime = System.nanoTime();
	        	
	        	setupTime[i] = startAnnotateTime - startTime;
	        	annotateTime[i] =  endTime - startAnnotateTime;
			    totalTime[i] = endTime - startTime;
	        }
		}	
		
		/**
		 * The remainder is used only to compute min/max/average times based on the recorded time
		 * values and print them on the console. 
		 */
		if (transform) {
			if (EXPERIMENT_ROUNDS > 1) {
				double[] resultSetupTime = new double[4];
				double[] resultTransformationTime = new double[4];
				double[] resultExportTime = new double[4];
				double[] resultTotalTime = new double[4];
					
					
				/* Calculate minimum values*/
				resultSetupTime[0] = calculateMin(setupTime);
				resultTransformationTime[0] = calculateMin(transformationTime);
				resultExportTime[0] = calculateMin(exportTime);
				resultTotalTime[0] = calculateMin(totalTime);
					
				/* Calculate maximum values*/
				resultSetupTime[1] = calculateMax(setupTime);
				resultTransformationTime[1] = calculateMax(transformationTime);
				resultExportTime[1] = calculateMax(exportTime);
				resultTotalTime[1] = calculateMax(totalTime);
					
				/* Calculate average values*/
				resultSetupTime[2] = calculateAvrg(setupTime);
				resultTransformationTime[2] = calculateAvrg(transformationTime);
				resultExportTime[2] = calculateAvrg(exportTime);
				resultTotalTime[2] = calculateAvrg(totalTime);
					
				/* Calculate standard deviation values*/
				resultSetupTime[3] = calculateSD(setupTime);
				resultTransformationTime[3] = calculateSD(transformationTime);
				resultExportTime[3] = calculateSD(exportTime);
				resultTotalTime[3] = calculateSD(totalTime);
					
				System.out.println("================================");
				System.out.println("= Measurements [" + EXPERIMENT_ROUNDS + " Experiments]");
				System.out.println("================================");
				
				System.out.println("Setup Time:\t\t" + getResultString(resultSetupTime));
			    System.out.println("Transformation Time:\t" + getResultString(resultTransformationTime)); 
			    System.out.println("Export Time:\t\t" + getResultString(resultExportTime));
				System.out.println("Total Time:\t\t" + getResultString(resultTotalTime));
					
			} else {
				System.out.println("================================");
				System.out.println("= Measurements                 =");
				System.out.println("================================");
				System.out.println("Setup Time:\t\t" + setupTime[0]/1000000 + " ms");
				System.out.println("Transformation Time:\t" + transformationTime[0]/1000000 + " ms"); 
				System.out.println("Export Time:\t\t" + exportTime[0]/1000000 + " ms");
				System.out.println("Total Time:\t\t" + totalTime[0]/1000000 + " ms");
			}
		}else if (backannotate) {
			if (EXPERIMENT_ROUNDS > 1) {
				double[] resultSetupTime = new double[4];
				double[] resultAnnotationTime = new double[4];
				double[] resultTotalTime = new double[4];
				
				/* Calculate minimum values*/
				resultSetupTime[0] = calculateMin(setupTime);
				resultAnnotationTime[0] = calculateMin(annotateTime);
				resultTotalTime[0] = calculateMin(totalTime);
					
				/* Calculate maximum values*/
				resultSetupTime[1] = calculateMax(setupTime);
				resultAnnotationTime[1] = calculateMax(annotateTime);
				resultTotalTime[1] = calculateMax(totalTime);
					
				/* Calculate average values*/
				resultSetupTime[2] = calculateAvrg(setupTime);
				resultAnnotationTime[2] = calculateAvrg(annotateTime);
				resultTotalTime[2] = calculateAvrg(totalTime);
					
				/* Calculate standard deviation values*/
				resultSetupTime[3] = calculateSD(setupTime);
				resultAnnotationTime[3] = calculateSD(annotateTime);
				resultTotalTime[3] = calculateSD(totalTime);
				
				System.out.println("================================");
				System.out.println("= Measurements [" + EXPERIMENT_ROUNDS + " Experiments]");
				System.out.println("================================");
				
				System.out.println("Setup Time:\t\t" + getResultString(resultSetupTime));
			    System.out.println("Annotation Time:\t" + getResultString(resultAnnotationTime)); 
				System.out.println("Total Time:\t\t" + getResultString(resultTotalTime));
			} else {
				System.out.println("================================");
				System.out.println("= Measurements                 =");
				System.out.println("================================");
				System.out.println("Setup Time:\t\t" + setupTime[0]/1000000 + " ms");
				System.out.println("Annotation Time:\t" + annotateTime[0]/1000000 + " ms"); 
				System.out.println("Total Time:\t\t" + totalTime[0]/1000000 + " ms");
			}
		}
			
    }
    /**
     * -a / amalthea		Path to the Amalthea model of the application.
     * -r / rcm				RCM destination path.
     * -t / transform		Indicate that the Amalthea model shall be transformed to an RCM model.
     * -b / backannotate	Indicate that the Amalthea model shall be annotated with the timing analysis results obtained for the RCM model.
     * @param args Argument string.
     */
    private static void parseArguments(String[] args) {
    	Options options = new Options();
    	options.addOption("a", "amalthea", true, "Path to the Amalthea model of the application.");
    	options.addOption("r", "rcm", true, "RCM destination path.");
    	options.addOption("t", "transform", false, "Indicate that the Amalthea model shall be transformed to an RCM model.");
    	options.addOption("b", "backannotate", false, "Indicate that the Amalthea model shall be annotated with the timing analysis results obtained for the RCM model.");
    	
    	CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			amaltheaPath = cmd.getOptionValue("a");
	    	if (amaltheaPath == null) {
	    		System.err.println("Expect argument -a");
				System.exit(1);
	    	}
			System.out.println("Model: " + amaltheaPath);
			
	        
			rcmPath = cmd.getOptionValue("r");
			if (rcmPath == null) {
	    		System.err.println("Expect argument -r");
				System.exit(1);
	    	}
			System.out.println("RCM Path: " + rcmPath);
			
			if (cmd.hasOption("t") && cmd.hasOption("b")) {
				System.err.println("Cannot specify transformation and back-annotation at the same time!");
				System.exit(1);
			}
			
			if (cmd.hasOption("t")) {
				transform = true;
				System.out.println("Amalthea -> RCM");
			} else if (cmd.hasOption("b")) {
				backannotate = true;
				System.out.println("RCM -> Amalthea");
			} else {
				System.err.println("Direction not specified! -transform or -backannotate");
				System.exit(1);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    public static String getResultString(double result[]) {
    	String retval = "";
    	
    	String minValue = result[0] / 1000000 + " ms";
    	String maxValue = result[1] / 1000000 + " ms";
    	String averValue = result[2] / 1000000 + " ms";
    	String sdValue = result[3] / 1000000 + " ms";
    	
    	retval = minValue + " " + maxValue + " " + averValue + " " + sdValue;
    	
    	return retval;
    }
    
    public static double calculateMin(double numArray[]) {
    	double min = Double.MAX_VALUE;
    	
    	for (double d : numArray) {
			if (d < min) min = d;
		}
    	
    	return min;
    }
    
    public static double calculateMax(double numArray[]) {
    	double max = 0;
    	
    	for (double d : numArray) {
			if (d > max) max = d;
		}
    	
    	return max;
    }

    public static double calculateAvrg(double numArray[]) {
    	double avrg = 0;
    	
    	for (double d : numArray) {
			avrg += d;
		}
    	
    	avrg = avrg / numArray.length;
    	
    	return avrg;
    }

    public static double calculateSD(double numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }
    
    /**
	 * This method returns the filename of a file without path or extension 
	 * Note: from here https://www.technicalkeeda.com/java-tutorials/get-filename-without-extension-using-java
	 * @param file
	 * @return Filename without extension as string.
	 */
	protected static String getFileNameWithoutExtension(File file) {
        String fileName = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }
 
        return fileName;
 
    }
}
