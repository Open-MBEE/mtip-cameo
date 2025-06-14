/* The Aerospace Corporation Huddle_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.menu.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import org.aero.mtip.util.CameoUtils;
import org.aero.mtip.util.MtipUtils;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.shapes.ImageShapeView;
import com.nomagic.magicdraw.uml.symbols.shapes.ImageView;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

@SuppressWarnings("serial")
public class InspectDiagramElementNestedAction extends MDAction {
	DiagramPresentationElement diagramPresentationElement;
	PresentationElement[] selectedPresentationElements;
	PresentationElement requestorPresentationElement;
	
	public InspectDiagramElementNestedAction(String id, String name, DiagramPresentationElement diagramPresentationElement, PresentationElement[] selectedPresentationElements, PresentationElement requestorPresentationElement)	{
		super(id, name, null, null);
		this.diagramPresentationElement = diagramPresentationElement;
		this.selectedPresentationElements = selectedPresentationElements;
		this.requestorPresentationElement = requestorPresentationElement;
	}
	
	public void actionPerformed(ActionEvent e) {
		Element diagramElement = diagramPresentationElement.getElement();
		CameoUtils.logGui("Diagram element has id:" + MtipUtils.getId(diagramElement));
		CameoUtils.logGui(Integer.toString(selectedPresentationElements.length) + " elements selected on diagram.");
		
		int allElementCount = findNestedPresentationElements(diagramPresentationElement.getPresentationElements());
		CameoUtils.logGui(Integer.toString(allElementCount) + " total elements on diagram found via api.");
		
		for (int i = 0; i < selectedPresentationElements.length; i++) {
			PresentationElement displayedPresentationElement = selectedPresentationElements[i];
			Element displayedElement = displayedPresentationElement.getElement();
			if(displayedElement != null) {
				CameoUtils.logGui("Displayed Element has id " + MtipUtils.getId(displayedElement) + " and displays as an object of type " + displayedPresentationElement.getClass().toString());
				CameoUtils.logGui("......with size: " + displayedPresentationElement.getBounds().toString());
			} else {
				CameoUtils.logGui("Presentation Element with id " + displayedPresentationElement.getID() + " has no element and is an object of type " + displayedPresentationElement.getClass().toString());
				CameoUtils.logGui("......with size: " + displayedPresentationElement.getBounds().toString());
			}
			
		}
	}
	
	public int findNestedPresentationElements(List<PresentationElement> presentationElements) {
		int count = 0;
		
		List<ImageView> imageViews = new ArrayList<ImageView>();
		List<ImageShapeView> imageShapeViews = new ArrayList<ImageShapeView>();
		
		for(int i = 0; i < presentationElements.size(); i++) {
			PresentationElement presentationElement = presentationElements.get(i);
			CameoUtils.logGui("Presentation Element from api is an object of type " + presentationElement.getClass().toString() + " with size" + presentationElement.getBounds().toString());
			List<PresentationElement> nestedPresentationElements = presentationElement.getPresentationElements();
			
			if(!nestedPresentationElements.isEmpty()) {
				count += findNestedPresentationElements(nestedPresentationElements);
			} else {
				count += 1;
			}
			
			if (presentationElement instanceof ImageView) {
			  imageViews.add((ImageView) presentationElement);
			}
			
			if (presentationElement instanceof ImageShapeView) {
			  imageShapeViews.add((ImageShapeView)presentationElement);
			}
		}
		
		CameoUtils.logGui(String.format("%d image views on diagram.", imageViews.size()));
		CameoUtils.logGui(String.format("%d image shape views on diagram.", imageShapeViews.size()));
		
		return count;
	}
}