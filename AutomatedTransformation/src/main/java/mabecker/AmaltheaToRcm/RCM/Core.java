package mabecker.AmaltheaToRcm.RCM;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class Core {

	private String name;
	
	private UUID id;
	private Notes notes;
	private ArrayList<Circuit> circuits;
	private ArrayList<TrigClockTT> trigClockTTs;
	private ArrayList<LinkTrig> linkTrigs;
	private ArrayList<TrigTerminator> trigTerminators;
	private ArrayList<LinkData> dataLinks;
	private ArrayList<ReactionDataStart> reactionStart;
	private ArrayList<ReactionDataEnd> reactionEnd;
	private ArrayList<AgeDataStart> ageStart;
	private ArrayList<AgeDataEnd> ageEnd;
	
	public Core(String _name) {
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

	public Element toXml(boolean _referenceUse) {
		
		Element core = new Element("Core");
		
		core.setAttribute("Name", name);
		core.setAttribute("UUID", id.toString());
		
		core.addContent(notes.toXml());
		
		
		for (Circuit circuit : circuits) {
			core.addContent(circuit.toXml());
		}
		
		for (TrigClockTT clock : trigClockTTs) {
			core.addContent(clock.getXml());
		}
		
		for (LinkTrig link : linkTrigs) {
			core.addContent(link.getXml());
		}
		
		for (TrigTerminator terminator : trigTerminators) {
			core.addContent(terminator.getXml());
		}
		
		for (LinkData link : dataLinks) {
			core.addContent(link.getXml());
		}
		
		for (AgeDataStart start : ageStart) {
			core.addContent(start.getXml());
		}
		
		for (AgeDataEnd end : ageEnd) {
			core.addContent(end.getXml());
		}
		
		for (ReactionDataStart start : reactionStart) {
			core.addContent(start.getXml());
		}
		
		for (ReactionDataEnd end : reactionEnd) {
			core.addContent(end.getXml());
		}
		
		return core;
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
					CrossRef ref = new CrossRef("ObjectSource");
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
