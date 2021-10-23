/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigPriority extends BaseElement{

	/**
	 * Properties
	 */
	private int ctrlPriority;
	
	/**
	 * Elements
	 */
	private PortTrigIn in;
	private PortTrigOut out;
	
	public TrigPriority(String _name, int _priority) {
		
		super(_name);
		
		ctrlPriority = _priority;
		
		in = new PortTrigIn("TI1");
		in.setIndex(0);
		
		out = new PortTrigOut("TO1");
		out.setIndex(0);
	}
	
	public int getCtrlPriority() {
		return ctrlPriority;
	}
	
	public PortTrigIn getTrigInPort() {
		return in;
	}
	
	public PortTrigOut getGetOutPort() {
		return out;
	}
	
	@Override
	public Element toXml() {
		Element prio = new Element("TrigPriority");

		prio.setAttribute("CtrlPriority", Integer.toString(ctrlPriority));
		prio.setAttribute("Name", super.getName());
		prio.setAttribute("UUID", super.getId().toString());
		
		prio.addContent(super.getNoteElement());
		
		prio.addContent(in.toXml());
		prio.addContent(out.toXml());
		
		return prio;
	}
}
