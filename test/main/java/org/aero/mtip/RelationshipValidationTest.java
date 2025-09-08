package org.aero.mtip;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.aero.mtip.metamodel.core.CommonRelationshipsFactory;
import org.aero.mtip.util.MtipUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.tests.MagicDrawApplication;
import com.nomagic.magicdraw.tests.MagicDrawTestCase;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;



@ExtendWith(MagicDrawApplication.class)
public class RelationshipValidationTest {
  public static Project sysmlTestingProject;
  public static CommonRelationshipsFactory crf = new CommonRelationshipsFactory();

  public static final Path AUTOMATED_TEST_MODELS_DIR = Paths.get("H:\\MTIP Project\\Testing Models\\automated");
  public static final Path SYSML_TESTING_MODEL = Paths.get(AUTOMATED_TEST_MODELS_DIR.toString(), "SysML MTIP Test Project.mdzip");
  
  @BeforeAll
  static void beforeTestsBegin() {
    sysmlTestingProject = MagicDrawTestCase.loadProject(SYSML_TESTING_MODEL.toAbsolutePath().toString());
  }
  
  
  @Test
  void relationshipCountMatchesExpected() {
    System.out.println(String.format("Searching %s for relationships.", sysmlTestingProject.getName()));
    List<Element> relationships = MtipUtils.getAllMtipRelationships(sysmlTestingProject.getPrimaryModel());
    
    System.out.println(String.format("Found %d relationships.", relationships.size()));

    assert(relationships.size() == 28);
  }
  
  @Test
  void clientSupplierMatchPoints() {    
//    List<Diagram> diagrams = MtipUtils.getAllDiagramsRecursively(sysmlTestingProject.getPrimaryModel());
//    
//    assert(diagrams.size() != 0);
//    
//    for (Diagram diagram : diagrams) {
//      DiagramPresentationElement dpe = sysmlTestingProject.getDiagram(diagram);
//      List<Element> relationshipsOnDiagram = MtipUtils.getElementsOnDiagram(sysmlTestingProject, diagram)
//          .stream()
//          .filter(item -> (item instanceof Relationship) 
//              || item instanceof ControlFlow
//              || item instanceof Message 
//              || item instanceof ObjectFlow)
//          .map(item -> (Element) item)
//          .collect(Collectors.toList());
//      
//      assert(relationshipsOnDiagram.size() != 0);
//      
//      for (Element relationship : relationshipsOnDiagram) {
//        CommonRelationship commonRelationship = crf.createElement(MtipUtils.getRelationshipType(relationship), 
//            CameoUtils.getElementName(relationship), 
//            MtipUtils.getId(relationship));
//        
//        Element supplier = commonRelationship.getSupplier();
//        Element client = commonRelationship.getClient();

//        PresentationElement presentationElement = dpe.findPresentationElement(relationship, PathElement.class);
//        
//        assert(presentationElement != null);
//        assert(presentationElement instanceof PathElement);
//        
//        PathElement pathElement = (PathElement)presentationElement;
//        
//        PresentationElement supplierPresentationElement = pathElement.getSupplier();
//        PresentationElement clientPresentationElement = pathElement.getClient();
//        
//        assert(supplierPresentationElement != null);
//        assert(clientPresentationElement != null);
//        
//        Element supplierFromPresentationElement = supplierPresentationElement.getElement();
//        Element clientFromPresentationElement = clientPresentationElement.getElement();
//        
//        assert(supplierFromPresentationElement != null);
//        assert(clientFromPresentationElement != null);
//        
//        System.out.println(String.format("Relationship type: %s", relationship.getHumanType()));
//        assert(supplier.equals(supplierFromPresentationElement));
//        assert(client.equals(clientFromPresentationElement));
//        
//      }
//    }
  }
}
