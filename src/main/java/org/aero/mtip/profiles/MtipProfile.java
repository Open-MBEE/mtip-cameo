package org.aero.mtip.profiles;

import org.aero.mtip.util.CameoUtils;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.helpers.CoreHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class MtipProfile {
  private static MtipProfile instance = null;

  private Project project = null;
  private Profile profile = null;
  
  public static final String NAME = "MTIP";
  public static final String MTIP_IMPORT_NAME = "MtipImport";
  public static final String MTIP_IMPORT_PROPERTY_IMPORT_ID_NAME = "importId";
  
  private static Stereotype mtipImport = null;

  public static MtipProfile getInstance() {
    if (instance == null) {
      instance = new MtipProfile();
    }
    
    return instance;
  }
  
  public static void clearProfile() {
    instance = null;
  }
  
  public static Stereotype getMtipImportStereotype() {
    return getInstance().getStereotype(MTIP_IMPORT_NAME);
  }
  
  public static Property getMtipImportPropertyImportId() {
    return getInstance().getStereotypeProperty(getMtipImportStereotype(), MTIP_IMPORT_PROPERTY_IMPORT_ID_NAME);
  }

  public MtipProfile() {
    project = Application.getInstance().getProject();
    profile = StereotypesHelper.getProfile(project, NAME);
    
    if (profile == null) {
      createProfile();
    }
  }
  
  private Stereotype getStereotype(String stereotypeName) {
    return StereotypesHelper.getStereotype(project, stereotypeName, profile);
  }
  
  private Property getStereotypeProperty(Stereotype stereotype, String propertyName) {
    return StereotypesHelper.getPropertyByName(stereotype, MTIP_IMPORT_PROPERTY_IMPORT_ID_NAME);
  }
  
  private void createProfile() {
    boolean inSessionPrior = SessionManager.getInstance().isSessionCreated(project);
    
    if (!inSessionPrior) {
      CameoUtils.createSession(project, "create-mtip-profile");
    }
      
    profile = project.getElementsFactory().createProfileInstance();
    profile.setOwner(project.getPrimaryModel());
    profile.setName(NAME);
    
    createStereotypes();
    CameoUtils.closeSession(project);
    
    CameoUtils.createSession(project, "Create mtip import properties.");
    createProperties();
    CameoUtils.closeSession(project);
    
    if (!inSessionPrior) {
      CameoUtils.closeSession(project);
    }
  }
  
  private void createStereotypes() {
    Stereotype mtipImport = project.getElementsFactory().createStereotypeInstance();
    
    mtipImport.setOwner(profile);
    mtipImport.setName(MTIP_IMPORT_NAME);    
  }
  
  private void createProperties() {
	Property property = project.getElementsFactory().createPropertyInstance();
	
	property.setOwner(mtipImport);
	property.setName(MTIP_IMPORT_PROPERTY_IMPORT_ID_NAME);
    property.setDatatype((DataType) project.getElementByID("_9_0_2_91a0295_1110274713995_297054_0"));
    CoreHelper.setMultiplicity(1, 1, property);
	    
  }
}
