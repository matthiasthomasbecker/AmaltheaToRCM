/*
 * Copyright (C) Matthias Becker Royal Institute of Technology, Sweden, Alessio Bucaioni Malardalen University, Sweden.
 * All rights reserved.
 * See LICENSE file for copyright and license details.
 * 
 * This file is part of the artifact of the publication https://doi.org/10.1016/j.jss.2021.111106
 */
package mabecker.amaltheatorcm.rcm;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class PortDataIn extends BaseElement{

	/**
	 * Properties
	 */
	private String 			dataPassingMethod;
	private String			dimension;
	private int				index;
	private	String			valueInitial;
	
	/**
	 * Elements
	 */
	private ReferenceInst 	referenceInstance;
	
	/**
	 * Amalthea label associated with the data port
	 */
	private Label 			label;
	
	public PortDataIn(String _name, Label _label) {
		
		super(_name);
		
		dimension = "";
		index = 0;
		valueInitial = "";
		referenceInstance = null;
		label = _label;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setDataPassingMethod(String dataPassingMethod) {
		this.dataPassingMethod = dataPassingMethod;
	}
	
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setReferenceInstance(ReferenceInst referenceInstance) {
		this.referenceInstance = referenceInstance;
	}
	
	public void setValueInitial(String valueInitial) {
		this.valueInitial = valueInitial;
	}
	
	public String getDataPassingMethod() {
		return dataPassingMethod;
	}
	
	public String getDimension() {
		return dimension;
	}
	
	public int getIndex() {
		return index;
	}
	
	public ReferenceInst getReferenceInstance() {
		return referenceInstance;
	}
	
	public String getValueInitial() {
		return valueInitial;
	}
	
	@Override
	public Element toXml() {
		
		Element portDataIn = new Element("PortDataIn");
		
		if (dataPassingMethod != null) portDataIn.setAttribute("DataPassingMethod", dataPassingMethod);
		if (dimension != null) portDataIn.setAttribute("Dimension", dimension);
		portDataIn.setAttribute("Index", Integer.toString(index));
		portDataIn.setAttribute("Name", super.getName());
		portDataIn.setAttribute("UUID", super.getId().toString());
		if (valueInitial != null) portDataIn.setAttribute("ValueInitial", valueInitial);
		
		portDataIn.addContent(super.getNoteElement());
		if (referenceInstance != null) portDataIn.addContent(referenceInstance.toXml());
		
		return portDataIn;
	}
}
