//package org.aero.mtip;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.platform.suite.api.SelectPackages;
//import org.junit.platform.suite.api.Suite;
//import org.junit.platform.suite.api.SuiteDisplayName;
//import com.nomagic.magicdraw.core.Project;
//import com.nomagic.magicdraw.tests.MagicDrawTestCase;
//
//@Suite
//@SuiteDisplayName("MTIP Test Suite")
//@SelectPackages({
// "org.aero.mtip"
//})
//public class MtipTestSuite {
//  public static Project sysmlTestingProject;
////  public static Project husvProject;
//  
////  public static final Path INSTALL_DIR = Paths.get("C:\\Users\\tas30828\\Desktop\\CEA-2022x-R2 HF1");
////  public static final Path HUSV_MODEL = Paths.get(INSTALL_DIR.toString(), "samples\\SysML", "hybrid sport utility vehicle.mdzip");
//  
//  public static final Path AUTOMATED_TEST_MODELS_DIR = Paths.get("H:\\MTIP Project\\Testing Models\\automated");
//  public static final Path SYSML_TESTING_MODEL = Paths.get(AUTOMATED_TEST_MODELS_DIR.toString(), "SysML MTIP Test Project.mdzip");
//  
//  @BeforeAll
//  static void beforeTestsBegin() {
////    husvProject = MagicDrawTestCase.loadProject(HUSV_MODEL.toAbsolutePath().toString());
//    sysmlTestingProject = MagicDrawTestCase.loadProject(SYSML_TESTING_MODEL.toAbsolutePath().toString());
//  }
//  
//  @AfterAll
//  void afterTestDone() {
//    System.out.println(String.format("Tests finished. Cleaning up..."));
//  }
//}
