/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.jdom2.Element;

public class TrigClockTT extends BaseElement{

	/**
	 * Properties
	 */
	private String timePeriod;
	
	/**
	 * elements
	 */
	private PortTrigOut portTrigOut;
	
	public TrigClockTT(String _name) {
		super(_name);
		
		portTrigOut = new PortTrigOut("TO1");
		portTrigOut.setIndex(0);
	}
	
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	@Override
	public Element toXml() {
		
		Element trigClockTT = new Element("TrigClockTT");
		
		trigClockTT.setAttribute("Name", super.getName());
		trigClockTT.setAttribute("TimePeriod", timePeriod);
		trigClockTT.setAttribute("UUID", super.getId().toString());
		
		trigClockTT.addContent(super.getNoteElement());
		
		trigClockTT.addContent(portTrigOut.toXml());
		
		return trigClockTT;
	}
}
