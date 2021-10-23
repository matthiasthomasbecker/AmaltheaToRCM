/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class Runtime extends BaseElement{
	
	/**
	 * Properties
	 */
	private String ctrlStack;
	private String timeBCET;
	private String timeWCET;
	
	public Runtime(String _name) {

		super(_name);
		ctrlStack = "0";
	}
	
	public void setTimeBCET(String timeBCET) {
		this.timeBCET = timeBCET;
	}
	
	public void setTimeWCET(String timeWCET) {
		this.timeWCET = timeWCET;
	}
	
	public void setCtrlStack(String ctrlStack) {
		this.ctrlStack = ctrlStack;
	}

	@Override
	public Element toXml() {
		
		Element runtime = new Element("Runtime");

		runtime.setAttribute("CtrlStack", ctrlStack);
		runtime.setAttribute("Name", super.getName());
		runtime.setAttribute("TimeBCET", timeBCET);
		runtime.setAttribute("TimeWCET", timeWCET);
		runtime.setAttribute("UUID", super.getId().toString());
		
		runtime.addContent(super.getNoteElement());
		
		return runtime;
	}
}
