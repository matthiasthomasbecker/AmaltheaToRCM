package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Circuit extends BaseElement{

	/**
	 * Properties
	 */
	private String activeBehaviour;
	
	/**
	 * Elements
	 */
	private Behaviour behaviour;
	private Interface iface;
	
	public Circuit(String _name) {
		
		super(_name);
		activeBehaviour = "";
	}
	
	public void addInputData(PortDataIn _in) {
		iface.addInputData(_in);
	}
	
	public void addOutputData(PortDataOut _out) {
		iface.addOutputData(_out);
	}
	
	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	public Behaviour getBehaviour() {
		return behaviour;
	}
	
	public void setIface(Interface iface) {
		this.iface = iface;
	}
	
	public Interface getInterface() {
		return iface;
	}

	@Override
	public Element toXml() {
		Element circuit = new Element("Circuit");
		
		circuit.setAttribute("ActiveBehaviour", activeBehaviour);
		circuit.setAttribute("Name", super.getName());
		circuit.setAttribute("UUID", super.getId().toString());
		
		circuit.addContent(super.getNoteElement());
		
		circuit.addContent(behaviour.toXml());
		
		circuit.addContent(iface.toXml());
		
		return circuit;
	}
}
