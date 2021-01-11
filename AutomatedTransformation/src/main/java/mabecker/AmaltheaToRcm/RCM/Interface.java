package mabecker.AmaltheaToRcm.RCM;

import java.util.ArrayList;
import java.util.UUID;

import org.jdom2.Element;

public class Interface {

	private String name;
	private UUID id;
	private Notes notes;
	
	/* Trigger ports */
	private PortTrigIn portTrigIn;
	private PortTrigOut PortTrigOut;

	/* Data ports */
	private ArrayList<PortDataIn> inData;
	private ArrayList<PortDataOut> outData;
	
	public Interface(String _name) {
		name = _name;
		id = UUID.randomUUID();
		notes = new Notes("");
		inData = new ArrayList<PortDataIn>();
		outData = new ArrayList<PortDataOut>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addInputData(PortDataIn _in) {
		inData.add(_in);
	}
	
	public void addOutputData(PortDataOut _out) {
		outData.add(_out);
	}
	
	
	public void setPortTrigIn(PortTrigIn portTrigIn) {
		this.portTrigIn = portTrigIn;
	}
	
	public void setPortTrigOut(PortTrigOut portTrigOut) {
		PortTrigOut = portTrigOut;
	}

	public ArrayList<PortDataIn> getInData() {
		return inData;
	}
	
	public ArrayList<PortDataOut> getOutData() {
		return outData;
	}
	
	public Element toXml() {
		
		Element iface = new Element("Interface");
		
		iface.setAttribute("Name", name);
		iface.setAttribute("UUID", id.toString());
		
		iface.addContent(notes.toXml());
		
		iface.addContent(portTrigIn.toXml());
		
		for (PortDataIn portDataIn : inData) {
			iface.addContent(portDataIn.toXml());
		}
		
		iface.addContent(PortTrigOut.toXml());
		
		for (PortDataOut portDataout : outData) {
			iface.addContent(portDataout.toXml());
		}
		
		return iface;
	}
}
