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

public class LinkTrig extends BaseElement{

	private static int count = 0;	//count the number of created links
	
	/**
	 * Properties
	 */
	private int indexLink;
	
	/**
	 * Elements
	 */
	private ArrayList<CrossRef> crossrefs;
	
	public LinkTrig() {
		
		super("link_trig_" + count);
		count++;
		
		crossrefs = new ArrayList<CrossRef>();
	}
	
	public void setIndexLink(int indexLink) {
		this.indexLink = indexLink;
	}
	
	public void addCrossRef(CrossRef _crossRef) {
		crossrefs.add(_crossRef);
	}

	@Override
	public Element toXml() {
		
		Element link = new Element("LinkTrig");
		
		link.setAttribute("IndexLink", Integer.toString(indexLink));
		link.setAttribute("Name", super.getName());
		link.setAttribute("UUID", super.getName().toString());
		
		link.addContent(super.getNoteElement());
		
		for (CrossRef crossRef : crossrefs) {
			link.addContent(crossRef.toXml());
		}
		return link;
	}
}
