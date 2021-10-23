/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;

import org.jdom2.Element;

public class Interface extends BaseElement{
	
	/* Trigger ports */
	private PortTrigIn portTrigIn;
	private PortTrigOut portTrigOut;

	/* Data ports */
	private ArrayList<PortDataIn> inData;
	private ArrayList<PortDataOut> outData;
	
	public Interface(String _name) {
		
		super(_name);
		
		inData = new ArrayList<PortDataIn>();
		outData = new ArrayList<PortDataOut>();

		portTrigIn = new PortTrigIn("IT");
		portTrigIn.setIndex(0);
		portTrigOut = new PortTrigOut("OT");
		portTrigOut.setIndex(0);
	}
	
	public void addInputData(PortDataIn _in) {
		inData.add(_in);
	}
	
	public void addOutputData(PortDataOut _out) {
		outData.add(_out);
	}
	
	public PortTrigIn getPortTrigIn() {
		return portTrigIn;
	}
	
	public PortTrigOut getPortTrigOut() {
		return portTrigOut;
	}

	public ArrayList<PortDataIn> getInData() {
		return inData;
	}
	
	public ArrayList<PortDataOut> getOutData() {
		return outData;
	}
	
	@Override
	public Element toXml() {
		
		Element iface = new Element("Interface");
		
		iface.setAttribute("Name", super.getName());
		iface.setAttribute("UUID", super.getId().toString());
		
		iface.addContent(super.getNoteElement());
		
		iface.addContent(portTrigIn.toXml());
		
		for (PortDataIn portDataIn : inData) {
			iface.addContent(portDataIn.toXml());
		}
		
		iface.addContent(portTrigOut.toXml());
		
		for (PortDataOut portDataout : outData) {
			iface.addContent(portDataout.toXml());
		}
		
		return iface;
	}
}
