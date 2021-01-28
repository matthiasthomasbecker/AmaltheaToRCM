package mabecker.AmaltheaToRcm.transformation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import mabecker.AmaltheaToRcm.RCM.AgeDataEnd;
import mabecker.AmaltheaToRcm.RCM.AgeDataStart;
import mabecker.AmaltheaToRcm.RCM.Behaviour;
import mabecker.AmaltheaToRcm.RCM.Circuit;
import mabecker.AmaltheaToRcm.RCM.Core;
import mabecker.AmaltheaToRcm.RCM.CrossRef;
import mabecker.AmaltheaToRcm.RCM.Interface;
import mabecker.AmaltheaToRcm.RCM.LinkData;
import mabecker.AmaltheaToRcm.RCM.LinkTrig;
import mabecker.AmaltheaToRcm.RCM.Node;
import mabecker.AmaltheaToRcm.RCM.PortTrigIn;
import mabecker.AmaltheaToRcm.RCM.PortTrigOut;
import mabecker.AmaltheaToRcm.RCM.RCM;
import mabecker.AmaltheaToRcm.RCM.ReactionDataEnd;
import mabecker.AmaltheaToRcm.RCM.ReactionDataStart;
import mabecker.AmaltheaToRcm.RCM.ReferenceInst;
import mabecker.AmaltheaToRcm.RCM.Runtime;
import mabecker.AmaltheaToRcm.RCM.Time;
import mabecker.AmaltheaToRcm.RCM.TrigClockTT;
import mabecker.AmaltheaToRcm.RCM.TrigTerminator;
import mabecker.AmaltheaToRcm.RCM.OneAttributePerLineXmlProcessor;
import mabecker.AmaltheaToRcm.RCM.PortDataIn;
import mabecker.AmaltheaToRcm.RCM.PortDataOut;

import org.eclipse.app4mc.amalthea.model.ActivityGraphItem;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.ConstraintsModel;
import org.eclipse.app4mc.amalthea.model.EventChain;
import org.eclipse.app4mc.amalthea.model.EventChainContainer;
import org.eclipse.app4mc.amalthea.model.EventChainItem;
import org.eclipse.app4mc.amalthea.model.EventChainItemType;
import org.eclipse.app4mc.amalthea.model.EventChainLatencyConstraint;
import org.eclipse.app4mc.amalthea.model.FrequencyDomain;
import org.eclipse.app4mc.amalthea.model.Group;
import org.eclipse.app4mc.amalthea.model.HWModel;
import org.eclipse.app4mc.amalthea.model.HwModule;
import org.eclipse.app4mc.amalthea.model.HwStructure;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.LabelAccess;
import org.eclipse.app4mc.amalthea.model.LabelAccessEnum;
import org.eclipse.app4mc.amalthea.model.LatencyType;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.RunnableCall;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.StructureType;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.TaskAllocation;
import org.eclipse.app4mc.amalthea.model.Ticks;
import org.eclipse.app4mc.amalthea.model.TimingConstraint;
import org.eclipse.app4mc.amalthea.model.impl.EventChainContainerImpl;
import org.eclipse.app4mc.amalthea.model.impl.EventChainLatencyConstraintImpl;
import org.eclipse.app4mc.amalthea.model.impl.PeriodicStimulusImpl;
import org.eclipse.app4mc.amalthea.model.impl.ProcessEventImpl;
import org.eclipse.app4mc.amalthea.model.impl.RelativePeriodicStimulusImpl;
import org.eclipse.app4mc.amalthea.model.impl.RunnableEventImpl;
import org.eclipse.emf.common.util.EList;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class AmaltheaToRCM {

	private Amalthea  	amaltheaModel;
	private RCM			rcmModel;
	private String 		name;
	
	private ArrayList<Circuit> allCircuits;
	
	public AmaltheaToRCM(Amalthea _model, String _name) {
		if (_model == null) {
			System.err.println("AmaltheaToRCM: model is null");
			System.exit(1);
		}
		amaltheaModel = _model;
		name = _name;
		
		allCircuits = new ArrayList<Circuit>();
	}
	
	public void tranformModel() {
		
		rcmModel = new RCM(name);	//create the root of the model, including the system

		/**
		 * Load  the CPU frequency for the Time class
		 */
		HWModel hwModel = amaltheaModel.getHwModel();
		if (hwModel.getDomains().size() > 0) {
			FrequencyDomain domain = (FrequencyDomain)hwModel.getDomains().get(0);
			String freqUnit = domain.getDefaultValue().getUnit().toString();
			double freq = domain.getDefaultValue().getValue();
			Time.setClockSpeed(new BigDecimal(freq).toPlainString() + " " + freqUnit);
		}
		
		/**
		 * Create the hardware model
		 */
        for (HwStructure hw : amaltheaModel.getHwModel().getStructures()) {
        	addNode(hw);
        }
		
        /**
         * Parse all runnables, each of which will be represented as SWC
         */
     /*   for (Runnable runnable : amaltheaModel.getSwModel().getRunnables()) {
        	Circuit swc = runnableToCircuit(runnable, null);
        	System.out.println("RCM SWC:" + swc.getName());
        	allCircuits.add(swc);
        }*/
        
        /**
         * Create the RCM representation of each task. 
         */
        for (TaskAllocation alloc : amaltheaModel.getMappingModel().getTaskAllocation()) {
        	Core core = getDestinationCore(alloc);
        	
        	transformTask(alloc.getTask(), core);
        }
        
        /**
         * Create the data links based on the labels of the Amalthea model
         */
        for (Node node : rcmModel.getSystem().getNodes()) {
        	for (Core core : node.getCores()) {
        		core.getPartition().getApplication().getMode().generateDataLinks();		
        	}
        }
        
        /**
         * Add the end-to-end delay constraints to the RCM model
         */
        transformEtoE();
        
        for (Node node : rcmModel.getSystem().getNodes()) {
        	for (Core core : node.getCores()) {
		        System.out.println("\nData Links Core: " + core.getName());
				System.out.println("------------------------------------------------------------");
				for (LinkData dataLink : core.getPartition().getApplication().getMode().getDataLinks()) {
					System.out.println(dataLink.toString() + "\n");
				}
        	}
        }
	}
	
	/**
	 * Transform the end-to-end delay constraints specified in the Amalthea model to RCM
	 */
	private void transformEtoE() {
		
		System.out.println("Data Chains:");
		System.out.println("------------------------------------------------------------");
		ConstraintsModel constraints = amaltheaModel.getConstraintsModel();
		
		if (constraints != null) {
			for (EventChain eventChain : constraints.getEventChains()) {
								
				String chainStartName = null;			//start of the chain
				String chainEndName = null;				//end of the chain
				boolean taskLevelConstraint = true;
				
				/* Extract the chain start and end from the Amalthea model */
				for (EventChainItem item : eventChain.getItems()) {
					EventChainContainerImpl container = (EventChainContainerImpl)item;
					
					String stimuliName = "";
					String responseName = "";
					
					if (container.getEventChain().getStimulus() instanceof ProcessEventImpl) {
						stimuliName = ((ProcessEventImpl)container.getEventChain().getStimulus()).getEntity().getName();
						responseName = ((ProcessEventImpl)container.getEventChain().getResponse()).getEntity().getName();
					} else if (container.getEventChain().getStimulus() instanceof RunnableEventImpl) {
						taskLevelConstraint = false;
						stimuliName = ((RunnableEventImpl)container.getEventChain().getStimulus()).getEntity().getName();
						responseName = ((RunnableEventImpl)container.getEventChain().getResponse()).getEntity().getName();
					}
					
					if (stimuliName != null && responseName != null) {
						if (chainStartName == null){
							chainStartName = stimuliName;
						} else {
							chainEndName = responseName;
						}
					}
				}

				Circuit chainStart = null;
				Circuit chainEnd = null;
				
				if (chainStartName != null && chainEndName != null) {
					chainStart = getChainStart(chainStartName, taskLevelConstraint);
					chainEnd = getChainEnd(chainEndName, taskLevelConstraint);
					
					if (chainStart == null) {
						System.err.println("No SWC found for ChainStart! " + chainStartName);
						System.exit(1);
					}
					if (chainEnd == null) {
						System.err.println("No SWC found for ChainEnd! " + chainEndName);
						System.exit(1);
					}
				} else {
					System.err.println("Chain start or end undefined! " + eventChain.toString());
					System.exit(1);
				}
				
				System.out.println("Data Chain: " + chainStart.getName() + " => " + chainEnd.getName());
				
				/* Get all timing constraints for this chain */
				for (TimingConstraint constr : constraints.getTimingConstraints()) {
					if (constr instanceof EventChainLatencyConstraint) {
						
						EventChainLatencyConstraintImpl constraint = (EventChainLatencyConstraintImpl) constr;
						String chainName = constraint.getScope().getName();
						
						if (chainName.equals(eventChain.getName())) {
							/* This is a end-to-end delay constraint for this chain */
							
							Time maxDelay = new Time(constraint.getMaximum().getValue().toString() + " " + constraint.getMaximum().getUnit().toString());
							
							if (LatencyType.AGE == constraint.getType()) {
								addEndToEndConstraint(chainStart, chainEnd, maxDelay, constraint.getType());
							} else if (LatencyType.REACTION == constraint.getType()) {
								addEndToEndConstraint(chainStart, chainEnd, maxDelay, constraint.getType());
							} else {
								System.err.println("Unsupported end-to-end delay constraint type! " + constraint.getType());
								System.exit(1);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Add the model elements for the reaction constraint to the RCM model.
	 * @param chainStart SWC at the beginning of the data chain.
	 * @param chainEnd SWC at the end of the data chain.
	 * @param maxDelay Deadline of the data chain. 
	 */
	private void addEndToEndConstraint(Circuit _chainStart, Circuit _chainEnd, Time _maxDelay, LatencyType _type) {
		Core chainStartCore = null;
		Core chainEndCore = null;
		
		/* Get the cores the start and end SWC are allocated to */
		for (Node node : rcmModel.getSystem().getNodes()) {
			for (Core core : node.getCores()) {
				for (Circuit swc : core.getPartition().getApplication().getMode().getCircuits()) {
					if (swc.equals(_chainStart)) {
						chainStartCore = core;
					} else if (swc.equals(_chainEnd)) {
						chainEndCore = core;
					}
				}
			}
		}
		
		/* Currently chain start and end need to be on the same core */
		if (!chainStartCore.equals(chainEndCore)) {
			System.err.println("Start \\(" + _chainStart.toString() + "\\) and End \\(" + chainEndCore.toString() + "\\)of the data chain are not on the same core!");
			System.exit(1);
		}
		
		/* Add a data port to the start SWC */
		int dataInCount = _chainStart.getInterface().getInData().size() + 1;
		PortDataIn dataIn = new PortDataIn("ID_" + Integer.toString(dataInCount), null);
		dataIn.setIndex(dataInCount - 1);
		dataIn.setDataPassingMethod("Automatic");
		
		ReferenceInst ref = new ReferenceInst("Reference");
		ref.setPath("RCM_Datatypes.rubusModel");
		ref.setReference("uint16_t");
		
		dataIn.setReferenceInstance(ref);
		_chainStart.getInterface().addInputData(dataIn);
		
		/* Add a data port to the end SWC */
		int dataOutCount = _chainEnd.getInterface().getOutData().size() + 1;
		PortDataOut dataOut = new PortDataOut("OD_" + Integer.toString(dataOutCount), null);
		dataOut.setIndex(dataInCount - 1);
		dataOut.setDataPassingMethod("Automatic");
		
		ReferenceInst refOut = new ReferenceInst("Reference");
		refOut.setPath("RCM_Datatypes.rubusModel");
		refOut.setReference("uint16_t");
		
		dataOut.setReferenceInstance(refOut);
		_chainEnd.getInterface().addOutputData(dataOut);
		
		if (LatencyType.REACTION == _type) {
			/* Create a reaction delay start element and connect it to the port of the start SWC created above */
			ReactionDataStart reactionStart = new ReactionDataStart();
			chainStartCore.getPartition().getApplication().getMode().addReactionStart(reactionStart);
			LinkData startLink = new LinkData(null);
			
			CrossRef startSource = new CrossRef("ObjectSource");
			startSource.setReference(reactionStart.getName() + "\\OD");
			
			CrossRef startDest = new CrossRef("ObjectDest");
			startDest.setReference(_chainStart.getName() + "\\Unnamed_Interface\\" + dataIn.getName());
			
			startLink.addCrossRef(startSource);
			startLink.addCrossRef(startDest);
			
			chainEndCore.getPartition().getApplication().getMode().addDataLink(startLink);
			
			/* Create a reaction delay end element and connect it to the port of the end SWC created above */
			ReactionDataEnd reactionEnd = new ReactionDataEnd(_maxDelay);
			chainEndCore.getPartition().getApplication().getMode().addReactionEnd(reactionEnd);
			LinkData endLink = new LinkData(null);
			
			CrossRef endSource = new CrossRef("ObjectSource");
			endSource.setReference(_chainStart.getName() + "\\Unnamed_Interface\\" + dataOut.getName());
			
			CrossRef endDest = new CrossRef("ObjectDest");
			endDest.setReference(reactionStart.getName() + "\\ID");
			
			endLink.addCrossRef(endSource);
			endLink.addCrossRef(endDest);
			
			chainEndCore.getPartition().getApplication().getMode().addDataLink(endLink);
		} else if (LatencyType.AGE == _type) {
			/* Create a age delay start element and connect it to the port of the start SWC created above */
			AgeDataStart ageStart = new AgeDataStart();
			chainStartCore.getPartition().getApplication().getMode().addAgeStart(ageStart);
			LinkData startLink = new LinkData(null);
			
			CrossRef startSource = new CrossRef("ObjectSource");
			startSource.setReference(ageStart.getName() + "\\OD");
			
			CrossRef startDest = new CrossRef("ObjectDest");
			startDest.setReference(_chainStart.getName() + "\\Unnamed_Interface\\" + dataIn.getName());
			
			startLink.addCrossRef(startSource);
			startLink.addCrossRef(startDest);
			
			chainEndCore.getPartition().getApplication().getMode().addDataLink(startLink);
			
			/* Create a age delay end element and connect it to the port of the end SWC created above */
			AgeDataEnd ageEnd = new AgeDataEnd(_maxDelay);
			chainEndCore.getPartition().getApplication().getMode().addAgeEnd(ageEnd);
			LinkData endLink = new LinkData(null);
			
			CrossRef endSource = new CrossRef("ObjectSource");
			endSource.setReference(_chainStart.getName() + "\\Unnamed_Interface\\" + dataOut.getName());
			
			CrossRef endDest = new CrossRef("ObjectDest");
			endDest.setReference(ageEnd.getName() + "\\ID");
			
			endLink.addCrossRef(endSource);
			endLink.addCrossRef(endDest);
			
			chainEndCore.getPartition().getApplication().getMode().addDataLink(endLink);
		}
	}

	/**
	 * Get the SWC that represents the end of the task chain. 
	 * @param _chainEndName Name of the Amalthea task that represents the chain's end.
	 * @param _taskLevelConstraint 
	 * @return Respective SWC.
	 */
	private Circuit getChainEnd(String _chainEndName, boolean _taskLevelConstraint) {
		
		/* Find the task allocation of the task at the chain's end */
		for (TaskAllocation alloc : amaltheaModel.getMappingModel().getTaskAllocation()) {
			Task t = alloc.getTask();
			
			String swcName = "";
			
			if (_taskLevelConstraint) {
	        	if (alloc.getTask().getName().equals(_chainEndName)) {
	        		
	        		for (ActivityGraphItem item : t.getActivityGraph().getItems()) {
	        			if (item instanceof Group) {
	        				Group g = (Group) item;
	        				
	        				if(g.isOrdered()) {
	        					for (ActivityGraphItem taskItem : g.getItems()) {
	        						if (taskItem instanceof RunnableCall) {
	        							Runnable r = (Runnable) ((RunnableCall)taskItem).getRunnable();
	        							swcName = t.getName() + "_" + r.getName();
	        						}
	        					}
	        				}
	        			}
	        		}
	        		Core core = getDestinationCore(alloc);
	        	
	        		for (Circuit swc : core.getPartition().getApplication().getMode().getCircuits()) {
	        			if (swc.getName().equals(swcName)) {
	        				return swc;
	        			}
	        		}
	        	}
			} else {
        		for (ActivityGraphItem item : t.getActivityGraph().getItems()) {
        			if (item instanceof Group) {
        				Group g = (Group) item;
        				
        				if(g.isOrdered()) {
        					for (ActivityGraphItem taskItem : g.getItems()) {
        						if (taskItem instanceof RunnableCall) {
        							Runnable r = (Runnable) ((RunnableCall)taskItem).getRunnable();
        							if (r.getName().equals(_chainEndName)) {
        								swcName = t.getName() + "_" + r.getName();
	        							Core core = getDestinationCore(alloc);
	        				        	
	        			        		for (Circuit swc : core.getPartition().getApplication().getMode().getCircuits()) {
	        			        			if (swc.getName().equals(swcName)) {
	        			        				return swc;
	        			        			}
	        			        		}
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        }
		
		return null;
	}

	/**
	 * Get the SWC that represents the start of the task chain. 
	 * @param _chainStartName Name of the Amalthea task that represents the chain's start.
	 * @param taskLevelConstraint 
	 * @return Respective SWC.
	 */
	private Circuit getChainStart(String _chainStartName, boolean _taskLevelConstraint) {
		/* Find the task allocation of the task at the chain's start */
		for (TaskAllocation alloc : amaltheaModel.getMappingModel().getTaskAllocation()) {
			Task t = alloc.getTask();
			String swcName = "";
			
			if (_taskLevelConstraint) {
				if (alloc.getTask().getName().equals(_chainStartName)) {
        		
	        		for (ActivityGraphItem item : t.getActivityGraph().getItems()) {
	        			if (item instanceof Group) {
	        				Group g = (Group) item;
	        				
	        				if(g.isOrdered()) {
	        					for (ActivityGraphItem taskItem : g.getItems()) {
	        						if (taskItem instanceof RunnableCall) {
	        							
	        							/* For the chain's start we only need to get the first runnable call */
	        							Runnable r = (Runnable) ((RunnableCall)taskItem).getRunnable();
	        							swcName = t.getName() + "_" + r.getName();
	        							Core core = getDestinationCore(alloc);
	        				        	
	        			        		for (Circuit swc : core.getPartition().getApplication().getMode().getCircuits()) {
	        			        			if (swc.getName().equals(swcName)) {
	        			        				return swc;
	        			        			}
	        			        		}
	        						}
	        					}
	        				}
	        			}
	        		}
        		}
        	} else {
        		for (ActivityGraphItem item : t.getActivityGraph().getItems()) {
        			if (item instanceof Group) {
        				Group g = (Group) item;
        				
        				if(g.isOrdered()) {
        					for (ActivityGraphItem taskItem : g.getItems()) {
        						if (taskItem instanceof RunnableCall) {
        							Runnable r = (Runnable) ((RunnableCall)taskItem).getRunnable();
        							if (r.getName().equals(_chainStartName)) {
        								swcName = t.getName() + "_" + r.getName();
	        							Core core = getDestinationCore(alloc);
	        				        	
	        			        		for (Circuit swc : core.getPartition().getApplication().getMode().getCircuits()) {
	        			        			if (swc.getName().equals(swcName)) {
	        			        				return swc;
	        			        			}
	        			        		}
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        }
		
		return null;
	}

	/**
	 * Gets the RCM destination core for a task allocation
	 * @param _alloc
	 * @return
	 */
	private Core getDestinationCore(TaskAllocation _alloc) {
		String cpuName = null;
		
		//get the amalthea scheduler the task is allocated to and the name of the CPU the scheduler is allocated to
		for (SchedulerAllocation scheduAlloc : amaltheaModel.getMappingModel().getSchedulerAllocation()) {
			if (_alloc.getScheduler().equals(scheduAlloc.getScheduler())) {
				if (scheduAlloc.getExecutingPU().getName() == null) {
					System.out.println("now");
				}
				cpuName = scheduAlloc.getExecutingPU().getName();
			}
		}
		
		if (cpuName == null) {
			System.err.println("Could not find the CPU the task " + _alloc.getTask().getName() + " is allocated to...");
			System.exit(1);
		}
		
		//get the RCM core with the same name and return it
		for (Node node : rcmModel.getSystem().getNodes()) {
			for (Core core : node.getCores()) {
				if (core.getName().equals(cpuName)) {
				return core;
				}
			}
		}
		return null;
	}
	
	/**
	 * Creates the RCM representation of the task.
	 * One circuit for each runnable that is called.
	 * The runnables that are called in sequence by the task are mapped to a trigger chain in RCM.
	 * The first RCM circuit is activated by a periodic clock with same activation frequency as the Amalthea task.
	 * The output of the last circuit in the trigger chain is terminated by a trigger termination.
	 * @param t
	 * @param core
	 */
	private void transformTask(Task t, Core core) {
		ArrayList<Circuit> tmpCircuits = new ArrayList<Circuit>();
		
		System.out.println("Amalthea Task: " + t.getName() + " [Core: " + core.getName() + "]");
		System.out.println("------------------------------------------------------------");
		for (ActivityGraphItem item : t.getActivityGraph().getItems()) {
			if (item instanceof Group) {
				Group g = (Group) item;
				
				if(g.isOrdered()) {
					for (ActivityGraphItem taskItem : g.getItems()) {
						if (taskItem instanceof RunnableCall) {
							Runnable r = (Runnable) ((RunnableCall)taskItem).getRunnable();
							
							Circuit circuit = runnableToCircuit(r, t.getName());
							System.out.println("RCM SWC:" + circuit.getName());
							core.getPartition().getApplication().getMode().addCircuit(circuit);
							tmpCircuits.add(circuit);
						}
					}
				}
			}
		}
		
		/**
		 * Get the activation period of the task
		 */
		List<Stimulus> stimuli = t.getStimuli();
		Time period = null;
		for( Stimulus stimulus : stimuli) {
			if (stimulus instanceof PeriodicStimulusImpl) {
				PeriodicStimulusImpl p = (PeriodicStimulusImpl)stimulus;
				
				BigInteger v = p.getRecurrence().getValue();
				String u = p.getRecurrence().getUnit().toString();
				
				String timeString = v.toString() + " " + u;
				period = new Time(timeString);
			} else if (stimulus instanceof RelativePeriodicStimulusImpl) {
				RelativePeriodicStimulusImpl p = (RelativePeriodicStimulusImpl)stimulus;
				
				BigInteger v = p.getNextOccurrence().getLowerBound().getValue();
				String u = p.getNextOccurrence().getLowerBound().getUnit().toString();
				
				String timeString = v.toString() + " " + u;
				period = new Time(timeString);
			} else {
				System.err.println("Stimuli type not supported! " + stimulus.toString());
				System.exit(1);
			}
		}
		
		/**
		 * Create the periodic clock
		 */
		TrigClockTT periodicTrigger = new TrigClockTT("Clock_" + t.getName());
		periodicTrigger.setTimePeriod(period.toStringShort());
		PortTrigOut outPort = new PortTrigOut("TO1");
		outPort.setIndex(0);
		periodicTrigger.setPortTrigOut(outPort);
		core.getPartition().getApplication().getMode().addTrigClockTT(periodicTrigger);
		System.out.println("RCM Clock: " + periodicTrigger.getName());
		
		/**
		 * Link all parts together to form the trigger chain
		 */
		for (int i = 0; i < tmpCircuits.size(); i++) {
			if (i == 0) {
				/**
				 * The first task is connected to the periodic clock
				 */
				LinkTrig triggerLink = connect(periodicTrigger.getName() + "\\T01", tmpCircuits.get(i).getName() + "\\Unnamed_Interface\\IT");
				System.out.println("RCM Link: " + periodicTrigger.getName() + "\\T01 ==> " + tmpCircuits.get(i).getName() + "\\Unnamed_Interface\\IT");
				core.getPartition().getApplication().getMode().addLinkTrigs(triggerLink);
			} else {
				/**
				 * All remaining circuits are triggered in a trigger chain
				 */
				Circuit srcCircuit = tmpCircuits.get(i-1);
				Circuit dstCircuit = tmpCircuits.get(0);
				LinkTrig triggerLink = connect(srcCircuit.getName() + "\\Unnamed_Interface\\OT", dstCircuit.getName() + "\\Unnamed_Interface\\IT");
				System.out.println("RCM Link: " + srcCircuit.getName() + "\\Unnamed_Interface\\OT" + "\\T01 ==> " + dstCircuit.getName() + "\\Unnamed_Interface\\IT");
				core.getPartition().getApplication().getMode().addLinkTrigs(triggerLink);
			}
		}
		
		/**
		 * Add a trigger terminator at the end
		 */
		TrigTerminator terminator = new TrigTerminator();
		System.out.println("RCM Terminator:" + terminator.getName());
		PortTrigIn inTrig = new PortTrigIn("IT");
		inTrig.setIndex(0);
		terminator.setPortTrigIn(inTrig);
		LinkTrig triggerLink = connect(tmpCircuits.get(tmpCircuits.size()-1).getName() + "\\Unnamed_Interface\\OT", terminator.getName() + "\\IT");
		core.getPartition().getApplication().getMode().addLinkTrigs(triggerLink);
		core.getPartition().getApplication().getMode().addTrigTerminator(terminator);
		System.out.println("RCM Link: " + tmpCircuits.get(tmpCircuits.size()-1).getName() + "\\Unnamed_Interface\\OT" + "\\T01 ==> " + terminator.getName() + "\\IT");
		
		System.out.println();
	}

	/**
	 * Create a trigger link that connects the source to the destination
	 * @param _src
	 * @param _dst
	 * @return
	 */
	private LinkTrig connect(String _src, String _dst) {
		LinkTrig link = new LinkTrig();
		link.setIndexLink(0);
		
		CrossRef refSrc = new CrossRef("ObjectSource");
		refSrc.setReference(_src);
		
		CrossRef refDest = new CrossRef("ObjectDest");
		refDest.setReference(_dst);
		
		link.addCrossRef(refSrc);
		link.addCrossRef(refDest);
		
		return link;
	}

	/**
	 * Creates an RCM Circuit that corresponds to the runnable
	 * @param r
	 * @param _taskName 
	 * @return
	 */
	private Circuit runnableToCircuit(Runnable r, String _taskName) {
		
		Circuit circuit;
		int dataInCount = 1;
		int dataOutCount = 1;
		
		if (_taskName != null) {
			circuit = new Circuit(_taskName + "_" + r.getName());
		} else {
			circuit = new Circuit(r.getName());
		}
		Behaviour behaviour = new Behaviour(r.getName() + "_Entry");
		Runtime runtime = new Runtime("DEFAULT");
		
		Interface iface = new Interface("Unnamed_Interface");
		PortTrigIn inTrig = new PortTrigIn("IT");
		inTrig.setIndex(0);
		PortTrigOut outTrig = new PortTrigOut("OT");
		outTrig.setIndex(0);
		iface.setPortTrigIn(inTrig);
		iface.setPortTrigOut(outTrig);
		
		behaviour.setRuntime(runtime);
		circuit.setBehaviour(behaviour);
		circuit.setIface(iface);
		
		for (ActivityGraphItem item : r.getActivityGraph().getItems()) {
			if (item instanceof Ticks) {
				Ticks ticks = (Ticks)item;
				Time WCET = new Time(ticks.getDefault().getUpperBound());
				Time BCET = new Time(ticks.getDefault().getLowerBound());
				
				runtime.setTimeBCET(BCET.toStringShort());
				runtime.setTimeWCET(WCET.toStringShort());
			} else if (item instanceof LabelAccess) {
				LabelAccess access = (LabelAccess) item;
				
				Label label = access.getData();
				long size = label.getSize().getNumberBits();
				
				String dataType = "";
				if (Math.ceil((double)size / 8) == 1) {
					dataType = "uint8_t";
				} else if (Math.ceil((double)size / 8) == 2) {
					dataType = "uint16_t";
				} else if (Math.ceil((double)size / 8) == 4) {
					dataType = "uint32_t";
				} else {
					System.out.println("Unsupported data type! Size: " + size + " trying to specify as pointer.");
					
					dataType = "uint8_t*";
					//System.exit(1);
				}
				
				if (access.getAccess() == LabelAccessEnum.READ) {
					
					PortDataIn dataIn = new PortDataIn("ID_" + Integer.toString(dataInCount), label);
					dataIn.setIndex(dataInCount - 1);
					dataIn.setDataPassingMethod("Automatic");
					
					ReferenceInst ref = new ReferenceInst("Reference");
					ref.setPath("RCM_Datatypes.rubusModel");
					ref.setReference(dataType);
					
					dataIn.setReferenceInstance(ref);
					
					circuit.addInputData(dataIn);
				} else if (access.getAccess() == LabelAccessEnum.WRITE) {
					PortDataOut dataOut = new PortDataOut("OD_" + Integer.toString(dataOutCount), label);
					dataOut.setIndex(dataInCount - 1);
					dataOut.setDataPassingMethod("Automatic");
					
					ReferenceInst ref = new ReferenceInst("Reference");
					ref.setPath("RCM_Datatypes.rubusModel");
					ref.setReference(dataType);
					
					dataOut.setReferenceInstance(ref);
					
					circuit.addOutputData(dataOut);
				}
			}
		}
			
		return circuit;
	}

	/**
	 * Adds the node and cores to the RCM model.
	 * @param structure
	 */
	private void addNode(HwStructure structure) {
		
		if (structure.getStructureType() == StructureType.MICROCONTROLLER) {
			Node tmpNode = new Node(structure.getName());
    		
    		for (HwModule m : structure.getModules()) {
    			if (m instanceof ProcessingUnit) {
    				ProcessingUnit p = (ProcessingUnit)m;
    				Core c = new Core(p.getName());
    				tmpNode.addCore(c);
    			}
    		}
    		rcmModel.getSystem().addNode(tmpNode);
    	} else {
		
    		for (HwStructure hw : structure.getStructures()) {

        		addNode(hw);
        	
        	}
    	}
	}
	
	public void exportRCM(String _rcmPath) {

		String outputBase = makeDirectory(_rcmPath);//setupFolderStructure(false);
		
		try {	
			Document doc = new Document();
			
			Comment comment = new Comment("\n" + 
					"  Copyright Arcticus Systems AB, All rights reserved.\n" + 
					"  The format and content in the File is protected by copyright.\n" + 
					"  The File is furnished under a licence and may only be used in\n" + 
					"  accordance with the terms of this licence.\n");
			doc.addContent(comment);
			
			Element root = rcmModel.toXml();
			doc.setRootElement(root);
			
			XMLOutputter outter=new XMLOutputter();
			outter.setXMLOutputProcessor(new OneAttributePerLineXmlProcessor());
			outter.setFormat(Format.getPrettyFormat());
		
			outter.output(doc, new FileWriter(new File(outputBase + "/" + name + ".rubusModel")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
	private String setupFolderStructure(boolean _extended) {
		
		String path = "output/RCM_Project";
		
		makeDirectory(path);
		
		if (_extended) {
			// Common folder
			makeDirectory(path + "/common");
			
			// Project name folder
			makeDirectory(path + "/" + rcmModel.getSystem().getName());
		}
		
		return path;
	}
	
	/**
	 * Create the directory if it does not exist yet
	 * @param _dirName
	 */
	private static String makeDirectory(String _dirName) {
		File directory = new File(_dirName);
	    if (! directory.exists()){
	        directory.mkdirs();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
	    
	    String path = "";
		try {
			path = directory.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return path;
	}
}
