package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;

import org.jdom2.Element;

public class Node extends BaseElement{

	/**
	 * Elements
	 */
	private ArrayList<Core>	cores;
	
	public Node(String _name) {

		super(_name);
		
		cores = new ArrayList<Core>();
	}
	
	public void addCore(Core _core) {
		cores.add(_core);
	}
	
	public ArrayList<Core> getCores() {
		return cores;
	}

	@Override
	public Element toXml() {
		
		Element node = new Element("Node"); 
		
		node.setAttribute("Name", super.getName());
		node.setAttribute("UUID", super.getId().toString());
		
		node.addContent(super.getNoteElement());
		
		for (Core core : cores) {
			node.addContent(core.toXml());
		}
		return node;
	}
}
