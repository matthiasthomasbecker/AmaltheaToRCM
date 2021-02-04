package mabecker.amaltheatorcm.transformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.AmaltheaFactory;
import org.eclipse.app4mc.amalthea.model.ConstraintsModel;
import org.eclipse.app4mc.amalthea.model.EventChainLatencyConstraint;
import org.eclipse.app4mc.amalthea.model.EventChainMeasurement;
import org.eclipse.app4mc.amalthea.model.LatencyType;
import org.eclipse.app4mc.amalthea.model.Measurement;
import org.eclipse.app4mc.amalthea.model.MeasurementModel;
import org.eclipse.app4mc.amalthea.model.StringObject;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.TimingConstraint;
import org.eclipse.app4mc.amalthea.model.Value;
import org.eclipse.app4mc.amalthea.model.impl.EventChainLatencyConstraintImpl;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaWriter;
import org.eclipse.emf.common.util.EMap;

public class BackAnnotation {

	private String rcmFolder;
	private String amaltheaPath;
	private Amalthea amalthea;
	
	public BackAnnotation(String _rcmFolder, String _amaltheaPath, Amalthea _amalthea) {
		rcmFolder = _rcmFolder;
		amaltheaPath = _amaltheaPath;
		amalthea = _amalthea;
	}
	
	/**
	 * Method to back-annotate the Amalthea model with timing analysis results performed on the RCM model.
	 */
	public void annotate() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(rcmFolder + "/Report/Network Analyzer.txt"));
			
			String line = reader.readLine();
			boolean start = false;
			
			while (line != null) {
				
				if (start) {
					if (line.equals("")) start = false;
					else {
						//evaluateResult(line);
						
						String[] parts = line.split("\\s+");
						String name = parts[0];
						long value = Long.parseLong(parts[3]);
						String unit = parts[4];
						annotateAmaltheaModel(name, value, unit);
					}
				}
				
				String[] parts = line.split(" ");
				if (parts[0].equals("Constraint") ) {
					start = true;
				}
				
				line = reader.readLine();
			}
			
			reader.close();
			
			File outputFile = new File(amaltheaPath);
			AmaltheaWriter.writeToFile(amalthea, outputFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Add the timing results to the Amalthea model
	 * @param _constraintName Name of the timing constraint
	 * @param _value	Bound calculated by the RCM timing analysis
	 */
	private void annotateAmaltheaModel(String _constraintName, long _value, String _unit) {
		final AmaltheaFactory fac = AmaltheaFactory.eINSTANCE;
			
		/**
		 * First we get the Amalthea measurement model.
		 * If the model does not yet exist we add it. 
		 */
		MeasurementModel measurements = amalthea.getMeasurementModel();
		
		if (measurements == null) {							/* If no measurement model exists yet add it */
			measurements = fac.createMeasurementModel();	/* Create the measurement model */
			amalthea.setMeasurementModel(measurements);		/* Add the new measurement model to the amalthea model */
		}

		/**
		 * Find the constraint we want to annotate in the Amalthea model
		 */
		ConstraintsModel constraints = amalthea.getConstraintsModel();
		TimingConstraint constraint = null;
		
		for (TimingConstraint constr : constraints.getTimingConstraints()) {
			
			if (constr.getName().equals(_constraintName)) {
				/* Found the constraint in the Amalthea model */
				constraint = constr;
				break;
			}
		}
		
		/**
		 * Add the respective timing analysis result to the measurement model.
		 */
		if (constraint != null) {
			
			/**
			 * Handle Event-Chain Latency Constraints
			 */
			if (constraint instanceof EventChainLatencyConstraint) {
				EventChainLatencyConstraintImpl evtChainConstr = (EventChainLatencyConstraintImpl) constraint;
				
				deletePreviousMeasurement(measurements, evtChainConstr.getScope().getName());	/* If an earlier measurement of the same constraint exists delete it */
				
				EventChainMeasurement measurement = fac.createEventChainMeasurement();	/* Create a new measurement */
				measurement.setEventChain(evtChainConstr.getScope());					/* Link to the event chain the measurement refers to */
				EMap<String, Value> prop = measurement.getCustomProperties();			/* The remaining values are added as custom properties */
				
				/* Get the constraint type */
				String constraintType = "";
				if (LatencyType.AGE == evtChainConstr.getType()) {
					constraintType = "Worst-Case Data Age";
				} else if (LatencyType.REACTION == evtChainConstr.getType()) {
					constraintType = "Worst-Case Reaction Time";
				}
				
				/* Add the result of the timing analysis */
				org.eclipse.app4mc.amalthea.model.Time timeBound = fac.createTime();
				
				timeBound.setUnit(parseUnit(_unit));
				timeBound.setValue(BigInteger.valueOf(_value));
				prop.put(constraintType, timeBound);
				
				
				/* Add a note that this was computed by the RCM analysis */
				StringObject creator = fac.createStringObject();
				creator.setValue("Rubus-ICE");
				prop.put("Creator", creator);
				
				measurements.getMeasurements().add(measurement);
			}
		} else {
			System.err.println("Error: Constraint " + _constraintName + " is not part of the Amalthea model!");
			System.exit(1);
		}
	}

	/**
	 * Check if an earlier measurement of the same constraint exists. 
	 * If this is the case, delete it (since we add a new one).
	 * @param _measurements 
	 * @param _constraintName Name of the timing constraint
	 */
	private void deletePreviousMeasurement(MeasurementModel _measurements, String _constraintName) {
		for (Measurement tmpMeasurement : _measurements.getMeasurements()) {
			if (tmpMeasurement instanceof EventChainMeasurement) {
				EventChainMeasurement tmpEvtMeasurement = (EventChainMeasurement) tmpMeasurement;
				String ecName = tmpEvtMeasurement.getEventChain().getName();
				if (ecName.equals(_constraintName)) {
					//System.out.println("Measurement for constraint " + _constraintName + " exists -> Deleting it");
					_measurements.getMeasurements().remove(tmpMeasurement);
					return;
				}
			}
		}
		
	}

	/**
	 * Convert a unit string to Amalthea time unit
	 * @param _unit Unit as String.
	 * @return Unit as Amalthea Time Unit Type
	 */
	private TimeUnit parseUnit(String _unit) {
		
		if (_unit.equals("s")) {
			return TimeUnit.S;
		} else if (_unit.equals("ms")) {
			return TimeUnit.MS;
		} else if (_unit.equals("us")) {
			return TimeUnit.US;
		} else if (_unit.equals("ns")) {
			return TimeUnit.NS;
		} else if (_unit.equals("ps")) {
			return TimeUnit.PS;
		}
		return null;
	}
}
