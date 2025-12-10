package org.aero.mtip.profiles;

import javax.annotation.CheckForNull;
import org.aero.mtip.util.Logger;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

public class UAF {
	static UAF instance = null;
	
	static final String NAME = "UAF";
	static final String PROJECT_NAME = "UAF Profile";
	static final String UPDM_NAME = "UPDM Customization";
	
	public static final String DEFINITION_NAME = "Definition";
	
	Project project;
	Profile uafProfile;
	Profile updmProfile;

	UAF() {
		project = Application.getInstance().getProject();
		uafProfile = StereotypesHelper.getProfile(project, NAME);
		updmProfile = StereotypesHelper.getProfile(project, UPDM_NAME);
	}
	
	Project getProject() {
		return project;
	}
	
	Profile getUafProfile() {
		return uafProfile;
	}
	
	Profile getUpdmProfile() {
		return updmProfile;
	}
	
	public static void clearProfile() {
	  instance = null;
	}
	
	public static UAF getInstance() {
		if (instance == null) {
			instance = new UAF();
		}
		
		return instance;
	}
	
	boolean hasStereotype(Element element, String stereotypeName) {
	    if (uafProfile == null) {
	      Logger.log(String.format("Profile not initialized when looking for stereotype name %s",
	          stereotypeName));
	      return false;
	    }
	    
	    if (element == null) {
	      return false;
	    }

	    Stereotype stereotype = StereotypesHelper.getStereotype(project, stereotypeName, uafProfile);

	    if (stereotype == null) {
	      Logger.log(String.format("Stereotype %s not found in profile %s", stereotypeName,
	          uafProfile.getHumanName()));
	      return false;
	    }

	    if (!StereotypesHelper.hasStereotype(element, stereotype)) {
	      return false;
	    }

	    return true;
	  }
	
	@CheckForNull
	public static Stereotype getStereotype(String stereotypeName) {
		Stereotype stereotype = StereotypesHelper.getStereotype(getInstance().getProject(), stereotypeName, getInstance().getUafProfile());
		
		if (stereotype != null) {
			return stereotype;
		}
		
		return StereotypesHelper.getStereotype(getInstance().getProject(), stereotypeName, getInstance().getUpdmProfile());
	}
	
	public static boolean isUafProfile(Package profile) {
		if (profile.getName().contentEquals(NAME) 
				|| profile.getName().contentEquals(UPDM_NAME)) {
			return true;
		}
		
		return false;
	}
	
	
	
	public static boolean isDefinition(Element element) {
	  return getInstance().hasStereotype(element, DEFINITION_NAME);
	}
	
	public static String getProfileModelName() {
	  return PROJECT_NAME;
	}
}

