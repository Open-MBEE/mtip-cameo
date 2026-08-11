/*
 * The Aerospace Corporation MTIP_Cameo Copyright 2022 The Aerospace Corporation
 * 
 * This product includes software developed at The Aerospace Corporation
 * (http://www.aerospace.org/).
 */
package org.aero.mtip.metamodel.sysml.activity;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aero.mtip.constants.XmlTagConstants;
import org.aero.mtip.data.DiagramData;
import org.aero.mtip.data.DiagramElementData;
import org.aero.mtip.data.ElementData;
import org.aero.mtip.io.Importer;
import org.aero.mtip.metamodel.core.AbstractDiagram;
import org.aero.mtip.util.Logger;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.PresentationElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.sysml.util.SysMLConstants;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.paths.PathElement;
import com.nomagic.magicdraw.uml.symbols.shapes.InterruptibleActivityRegionView;
import com.nomagic.magicdraw.uml.symbols.shapes.ShapeElement;
import com.nomagic.magicdraw.uml.symbols.shapes.SwimlaneHeaderView;
import com.nomagic.magicdraw.uml.symbols.shapes.SwimlaneView;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityParameterNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;

public class ActivityDiagram extends AbstractDiagram {
  protected Map<String, PresentationElement> presentationElementById = new HashMap<String, PresentationElement>();

  public ActivityDiagram(String name, String importId) {
    super(name, importId);

    metamodelConstant = SysMLConstants.SYSML_ACTIVITY_DIAGRAM;
    xmlConstant = XmlTagConstants.ACTIVITYDIAGRAM;
    cameoDiagramConstant = SysMLConstants.SYSML_ACTIVITY_DIAGRAM;
  }

  @Override
  public void writeDiagramElementRecursively(org.w3c.dom.Element elementListTag, org.w3c.dom.Element relationshipListTag,
      PresentationElement presentationElement, PresentationElement parentPresentationElement) {
    writeDiagramEntity(elementListTag, relationshipListTag, presentationElement, parentPresentationElement);

    for (PresentationElement subPresentationElement : presentationElement.getPresentationElements()) {
      writeDiagramElementRecursively(elementListTag, relationshipListTag, subPresentationElement, presentationElement);
    }
  }

  protected void writeDiagramEntity(org.w3c.dom.Element elementListTag, org.w3c.dom.Element relationshipListTag,
      PresentationElement presentationElement, PresentationElement parentPresentationElement) {
    if (presentationElement instanceof PathElement) {
      writeDiagramRelationship(relationshipListTag, presentationElement);
      return;
    }

    if (presentationElement instanceof SwimlaneView && !(presentationElement instanceof SwimlaneHeaderView)) {
      writeDiagramElementNoElement(elementListTag, presentationElement, parentPresentationElement, XmlTagConstants.SWIMLANE);
    }

    if (presentationElement instanceof InterruptibleActivityRegionView) {
      writeDiagramElementNoElement(elementListTag, presentationElement, parentPresentationElement,
          ActivityNode.XML_TAG_INTERRUPTIBLE_ACTIVITY_REGION);
    }

    writeDiagramElement(elementListTag, presentationElement, parentPresentationElement);
  }

  @Override
  public void createPresentationElements(Project project, Diagram diagram, ElementData elementData) {
    for (DiagramElementData diagramElementData : ((DiagramData) elementData).getDiagramElementDatas()) {
      if (diagramElementData.getType() == XmlTagConstants.ACTIVITY_PARTITION) {
        continue;
      }
      
      if (Importer.getInstance().getImportedElement(diagramElementData.getId()) instanceof ActivityParameterNode || Importer.getInstance().getImportedElement(diagramElementData.getId()) instanceof Parameter) {
	  	  // TODO Enable properly adding to diagram frame.
	  	  continue;
      }

      if (isOnDiagramFrame(diagramElementData)) {
        Logger.log(String.format("Adding presentation element to diagram frame [%s]: %s",
            getDiagramPresentationElement().getDiagramFrame().getBounds().toString(), diagramElementData.toString()));
        createPresentationElement(project, diagramElementData, getDiagramPresentationElement().getDiagramFrame());
        continue;
      }

      Logger.log(String.format("Adding presentation element: %s", diagramElementData.toString()));
      createPresentationElement(project, diagramElementData, getDiagramPresentationElement());
    }

    createSwimlanes(elementData);
  }

  @Override
  public void createPresentationElement(Project project, DiagramElementData diagramElementData,
      PresentationElement parentPresentationElement) {
    Element createdElement = Importer.getInstance().getImportedElement(diagramElementData.getId());

    try {
      // Adding Pins to the diagram re-adds their parent element duplicating elements on the diagram on
      // import. Filtering them out fixes this.
      if (createdElement instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin
          || createdElement instanceof com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.InputPin) {
        return;
      }

      if (createdElement instanceof com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition) {
        return;
      }
      
      if (diagramElementData.getBounds() == null) {
        PresentationElementsManager.getInstance().createShapeElement(createdElement, parentPresentationElement, true);
        return;
      }

      ShapeElement shape = PresentationElementsManager.getInstance().createShapeElement(createdElement, parentPresentationElement, true,
          new Point(diagramElementData.getBounds().x, diagramElementData.getBounds().y));
      
      if (shape == null) {
        Logger.log(String.format("Failed to create shape elemnt for %s", diagramElementData.toString()));
        shape = PresentationElementsManager.getInstance().createShapeElement(createdElement, getDiagramPresentationElement(), true,
            new Point(diagramElementData.getBounds().x, diagramElementData.getBounds().y));
        return;
      }

      PresentationElementsManager.getInstance().reshapeShapeElement(shape, diagramElementData.getBounds());
    } catch (NullPointerException npe) {
      Logger.logException(npe);
    } catch (IllegalArgumentException iae) {
      Logger.logException(iae);
    } catch (ClassCastException cce) {
      Logger.logException(cce);
    } catch (ReadOnlyElementException roee) {
      Logger.logException(roee);
    }
  }

  public void createSwimlanes(ElementData elementData) {
    List<? extends com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition> activityPartitionsVertical =
        ((DiagramData) elementData).getDiagramElementDatas().stream().filter(
            x -> x.getType() == XmlTagConstants.ACTIVITY_PARTITION && x.getOrientation() == XmlTagConstants.SWIMLANE_ORIENTATION_VERTICAL)
            .map(x -> (ActivityPartition) Importer.getInstance().getImportedElement(x.getId())).collect(Collectors.toList());

    List<? extends com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition> activityPartitionsHorizontal =
        ((DiagramData) elementData).getDiagramElementDatas().stream().filter(
            x -> x.getType() == XmlTagConstants.ACTIVITY_PARTITION && x.getOrientation() == XmlTagConstants.SWIMLANE_ORIENTATION_HORIZONTAL)
            .map(x -> (ActivityPartition) Importer.getInstance().getImportedElement(x.getId())).collect(Collectors.toList());

    try {
      PresentationElementsManager.getInstance().createSwimlane(activityPartitionsHorizontal, activityPartitionsVertical,
          getDiagramPresentationElement());
    } catch (NullPointerException npe) {
      Logger.logException(npe);
    } catch (IllegalArgumentException iae) {
      Logger.logException(iae);
    } catch (ClassCastException cce) {
      Logger.logException(cce);
    } catch (ReadOnlyElementException roee) {
      Logger.logException(roee);
    }
    return;
  }
}
