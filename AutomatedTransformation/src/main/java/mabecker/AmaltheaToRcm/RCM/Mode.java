package mabecker.AmaltheaToRcm.RCM;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class Mode {

	private String name;
	private UUID id;
	private Notes notes;

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
		name = _name;
		
		id = UUID.randomUUID();
		
		notes = new Notes("");
		
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
	
	public String getName() {
		return name;
	}

	public void addTrigTerminator(TrigTerminator _terminator) {
		trigTerminators.add(_terminator);
	}
	
	public String getModeInputRef() {
		return name + "\\" + trigIn.getName();
	}
	
	public String getModeOutputRef() {
		return name + "\\" + trigOut.getName();
	}
	
	public Element toXml() {
		
		Element mode = new Element("Mode");
		
		mode.setAttribute("Name", name);
		mode.setAttribute("UUID", id.toString());
		
		mode.addContent(notes.toXml());
		
		mode.addContent(trigIn.toXml());
		mode.addContent(trigOut.toXml());
		
		for (Circuit circuit : circuits) {
			mode.addContent(circuit.toXml());
		}
		
		for (TrigClockTT clock : trigClockTTs) {
			mode.addContent(clock.getXml());
		}
		
		for (LinkTrig link : linkTrigs) {
			mode.addContent(link.getXml());
		}
		
		for (TrigTerminator terminator : trigTerminators) {
			mode.addContent(terminator.getXml());
		}
		
		for (LinkData link : dataLinks) {
			mode.addContent(link.getXml());
		}
		
		for (AgeDataStart start : ageStart) {
			mode.addContent(start.getXml());
		}
		
		for (AgeDataEnd end : ageEnd) {
			mode.addContent(end.getXml());
		}
		
		for (ReactionDataStart start : reactionStart) {
			mode.addContent(start.getXml());
		}
		
		for (ReactionDataEnd end : reactionEnd) {
			mode.addContent(end.getXml());
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
					CrossRef ref = new CrossRef("ObjectDest");
					ref.setReference(swc.getName() + "\\" + swc.getInterface().getName() + "\\" + inPort.getName());
					dataLink.addCrossRef(ref);
				} else {
					System.err.println("No writing SWC for Label " + inPort.getLabel().getName() + " on core " + getName());
					//System.exit(1);
				}
			}
		}
		
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
}
