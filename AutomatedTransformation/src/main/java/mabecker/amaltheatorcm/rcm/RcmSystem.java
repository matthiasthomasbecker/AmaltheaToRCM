package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;
import org.jdom2.Element;

public class RcmSystem extends BaseElement{

	/**
	 * Elements
	 */
	private ArrayList<Node> 	nodes;
	
	public RcmSystem(String _name) {
		
		super(_name);
		nodes = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	@Override
	public Element toXml() {
		
		Element system = new Element("System");
		system.setAttribute("Name", super.getName());
		system.setAttribute("UUID", super.getId().toString());
		
		system.addContent(super.getNoteElement());
		
		for (Node node : nodes) {
			system.addContent(node.toXml());
		}
		return system;
	}
}
