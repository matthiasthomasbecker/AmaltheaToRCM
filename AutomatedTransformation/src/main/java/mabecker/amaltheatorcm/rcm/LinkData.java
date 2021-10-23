package mabecker.amaltheatorcm.rcm;

import java.util.ArrayList;

import org.eclipse.app4mc.amalthea.model.Label;
import org.jdom2.Element;

public class LinkData extends BaseElement{
	private static int count = 0;	//count the number of created links
	
	/**
	 * Properties
	 */
	private int indexLink;
	
	/**
	 * Elements
	 */
	private ArrayList<CrossRef> crossrefs;
	
	/**
	 * Amalthea label that is associated with this data link
	 */
	private Label label;
	
	public LinkData(Label _label) {
		
		super("link_data_" + count);
		indexLink = count;
		count++;
		
		crossrefs = new ArrayList<CrossRef>();
		
		label = _label;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void setIndexLink(int indexLink) {
		this.indexLink = indexLink;
	}
	
	public void addCrossRef(CrossRef _crossRef) {
		crossrefs.add(_crossRef);
	}

	public ArrayList<CrossRef> getCrossrefs() {
		return crossrefs;
	}
	
	@Override
	public String toString() {
		String retval = "RCM LinkData: ";
		
		for (int i = 0; i < crossrefs.size(); i++) {
			if (i == 0) retval = retval + crossrefs.get(i).getReference();
			else {
				retval = retval + "\n              " + crossrefs.get(i).getReference();
			}
		}
		return retval;
	}
	
	@Override
	public Element toXml() {
		
		Element link = new Element("LinkData");
		
		link.setAttribute("IndexLink", Integer.toString(indexLink));
		link.setAttribute("Name", super.getName());
		link.setAttribute("UUID", super.getId().toString());
		
		link.addContent(super.getNoteElement());
		
		for (CrossRef crossRef : crossrefs) {
			link.addContent(crossRef.toXml());
		}
		return link;
	}
}
