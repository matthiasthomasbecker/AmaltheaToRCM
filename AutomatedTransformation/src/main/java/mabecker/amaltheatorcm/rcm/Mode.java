package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class Mode extends BaseElement{

	/* Trigger for the mode itself */
	private PortTrigIn trigIn;
	private PortTrigOut trigOut;
	
	/* SWCs that are executed by this mode */
	private ArrayList<Circuit> circuits;
	private ArrayList<TrigClockTT> trigClockTTs;
	private ArrayList<LinkTrig> linkTrigs;
	private ArrayList<TrigTerminator> trigTerminators;
	private ArrayList<LinkData> dataLinks;
	private ArrayList<ReactionDataStart> reactionStart;
	private ArrayList<ReactionDataEnd> reactionEnd;
	private ArrayList<AgeDataStart> ageStart;
	private ArrayList<AgeDataEnd> ageEnd;
	private ArrayList<TrigPriority> priorities;
	
	public Mode(String _name) {
		
		super(_name);
		
		circuits = new ArrayList<Circuit>();
		trigClockTTs = new ArrayList<TrigClockTT>();
		linkTrigs = new ArrayList<LinkTrig>();
		trigTerminators = new ArrayList<TrigTerminator>();
		dataLinks = new ArrayList<LinkData>();
		reactionStart = new ArrayList<ReactionDataStart>();
		reactionEnd = new ArrayList<ReactionDataEnd>();
		ageStart = new ArrayList<AgeDataStart>();
		ageEnd = new ArrayList<AgeDataEnd>();
		priorities = new ArrayList<TrigPriority>();
		
		trigIn = new PortTrigIn("IT1");
		trigIn.setIndex(0);
		trigOut = new PortTrigOut("OT1");
		trigOut.setIndex(0);
	}
	
	public void addTrigPriority(TrigPriority _priority) {
		priorities.add(_priority);
	}
	
	public void addReactionStart(ReactionDataStart _start) {
		reactionStart.add(_start);
	}
	
	public void addReactionEnd(ReactionDataEnd _end) {
		reactionEnd.add(_end);
	}
	
	public void addAgeStart(AgeDataStart _start) {
		ageStart.add(_start);
	}
	
	public void addAgeEnd(AgeDataEnd _end) {
		ageEnd.add(_end);
	}
	
	public void addCircuit(Circuit _circuit) {
		circuits.add(_circuit);
	}
	
	public ArrayList<Circuit> getCircuits() {
		return circuits;
	}
	
	public void addTrigClockTT(TrigClockTT _trigClockTT) {
		trigClockTTs.add(_trigClockTT);
	}
	
	public void addLinkTrigs(LinkTrig _linkTrig) {
		linkTrigs.add(_linkTrig);
	}
	
	public void addDataLink(LinkData _dataLink) {
		dataLinks.add(_dataLink);
	}

	public void addTrigTerminator(TrigTerminator _terminator) {
		trigTerminators.add(_terminator);
	}
	
	public String getModeInputRef() {
		return super.getName() + "\\" + trigIn.getName();
	}
	
	public String getModeOutputRef() {
		return super.getName() + "\\" + trigOut.getName();
	}
	
	@Override
	public Element toXml() {
		
		Element mode = new Element("Mode");
		
		mode.setAttribute("Name", super.getName());
		mode.setAttribute("UUID", super.getId().toString());
		
		mode.addContent(super.getNoteElement());
		
		mode.addContent(trigIn.toXml());
		mode.addContent(trigOut.toXml());
		
		for (Circuit circuit : circuits) {
			mode.addContent(circuit.toXml());
		}
		
		for (TrigClockTT clock : trigClockTTs) {
			mode.addContent(clock.toXml());
		}
		
		for (LinkTrig link : linkTrigs) {
			mode.addContent(link.toXml());
		}
		
		for (TrigTerminator terminator : trigTerminators) {
			mode.addContent(terminator.toXml());
		}
		
		for (LinkData link : dataLinks) {
			mode.addContent(link.toXml());
		}
		
		for (AgeDataStart start : ageStart) {
			mode.addContent(start.toXml());
		}
		
		for (AgeDataEnd end : ageEnd) {
			mode.addContent(end.toXml());
		}
		
		for (ReactionDataStart start : reactionStart) {
			mode.addContent(start.toXml());
		}
		
		for (ReactionDataEnd end : reactionEnd) {
			mode.addContent(end.toXml());
		}
		
		for (TrigPriority prio : priorities) {
			mode.addContent(prio.toXml());
		}
		
		return mode;
	}
	
	public void generateDataLinks() {
		for (Circuit swc : circuits) {
			
			/* First create one linkData for each output label. Here we have the assumption that each label is written by only one SWC */
			for (PortDataOut outPort : swc.getInterface().getOutData()) {
				LinkData dataLink = new LinkData(outPort.getLabel());
				
				CrossRef ref = new CrossRef("ObjectSource");
				ref.setReference(swc.getName() + "\\" + swc.getInterface().getName() + "\\" + outPort.getName());
				dataLink.addCrossRef(ref);
				
				addDataLink(dataLink);
			}
		}
		
		for (Circuit swc : circuits) {
			for (PortDataIn inPort : swc.getInterface().getInData()) {
				LinkData dataLink = getDataLink(inPort.getLabel());
				
				if (dataLink != null) {
					
					if (hasDestination(dataLink.getLabel())) {
						System.err.println("Label " + inPort.getLabel().getName() + " on core " + getName() + " has already a destination...");
					} else {
						//create the destination for the data link
						CrossRef ref = new CrossRef("ObjectDest");
						ref.setReference(swc.getName() + "\\" + swc.getInterface().getName() + "\\" + inPort.getName());
						dataLink.addCrossRef(ref);
					}
				} else {
					System.err.println("No writing SWC for Label " + inPort.getLabel().getName() + " on core " + getName());
					//System.exit(1);
				}
			}
		}
		
		ArrayList<LinkData> tmp = new ArrayList<LinkData>();
		for (LinkData link : dataLinks) {
			if (link.getCrossrefs().size() == 1) {
				tmp.add(link);
			}
		}
		dataLinks.removeAll(tmp);
	}
	
	public ArrayList<LinkData> getDataLinks() {
		return dataLinks;
	}

	private LinkData getDataLink(Label label) {
		
		for (LinkData link : dataLinks) {
			if (link.getLabel().equals(label)) {
				return link;
			}
		}
		
		return null;
	}
	
	private boolean hasDestination(Label label) {
		
		for (LinkData link : dataLinks) {
			if (link.getLabel().equals(label)) {
				for (CrossRef ref : link.getCrossrefs()) {
					if (ref.getName().equals("ObjectDest")) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
