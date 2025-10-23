package org.aero.mtip.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CameoConstants {
  public static final String GLOSSARY_TABLE = "GlossaryTable";
  public static final String TERM = "Term";
  
  public static final String[] CAMEO_ELEMENT_VALUES = {TERM};
  public static final String[] CAMEO_RELATIONSHIP_VALUES = {};
  public static final String[] CAMEO_DIAGRAM_VALUES = {GLOSSARY_TABLE};
  
  public static Set<String> CAMEO_ELEMENTS =
      new HashSet<String>(Arrays.asList(CAMEO_ELEMENT_VALUES));
  public static Set<String> CAMEO_RELATIONSHIPS =
      new HashSet<String>(Arrays.asList(CAMEO_RELATIONSHIP_VALUES));
  public static Set<String> CAMEO_DIAGRAMS =
      new HashSet<String>(Arrays.asList(CAMEO_DIAGRAM_VALUES));
}
