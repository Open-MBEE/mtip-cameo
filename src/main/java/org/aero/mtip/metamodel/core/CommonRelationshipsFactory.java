/* The Aerospace Corporation MTIP_Cameo
Copyright 2022 The Aerospace Corporation

This product includes software developed at
The Aerospace Corporation (http://www.aerospace.org/). */

package org.aero.mtip.metamodel.core;

import org.aero.mtip.constants.SysmlConstants;
import org.aero.mtip.constants.UAFConstants;
import org.aero.mtip.metamodel.core.general.Abstraction;
import org.aero.mtip.metamodel.core.general.Aggregation;
import org.aero.mtip.metamodel.core.general.Allocate;
import org.aero.mtip.metamodel.core.general.Association;
import org.aero.mtip.metamodel.core.general.Composition;
import org.aero.mtip.metamodel.core.general.Dependency;
import org.aero.mtip.metamodel.core.general.Generalization;
import org.aero.mtip.metamodel.core.general.Realization;
import org.aero.mtip.metamodel.core.general.Usage;
import org.aero.mtip.metamodel.sysml.activity.ControlFlow;
import org.aero.mtip.metamodel.sysml.activity.ObjectFlow;
import org.aero.mtip.metamodel.sysml.block.InterfaceRealization;
import org.aero.mtip.metamodel.sysml.block.PackageImport;
import org.aero.mtip.metamodel.sysml.internalblock.BindingConnector;
import org.aero.mtip.metamodel.sysml.internalblock.Connector;
import org.aero.mtip.metamodel.sysml.internalblock.InformationFlow;
import org.aero.mtip.metamodel.sysml.internalblock.ItemFlow;
import org.aero.mtip.metamodel.sysml.profile.Extension;
import org.aero.mtip.metamodel.sysml.requirements.Copy;
import org.aero.mtip.metamodel.sysml.requirements.DeriveRequirement;
import org.aero.mtip.metamodel.sysml.requirements.Refine;
import org.aero.mtip.metamodel.sysml.requirements.Satisfy;
import org.aero.mtip.metamodel.sysml.requirements.Trace;
import org.aero.mtip.metamodel.sysml.requirements.Verify;
import org.aero.mtip.metamodel.sysml.sequence.Message;
import org.aero.mtip.metamodel.sysml.statemachine.Transition;
import org.aero.mtip.metamodel.sysml.usecase.Extend;
import org.aero.mtip.metamodel.sysml.usecase.Include;
import org.aero.mtip.metamodel.uaf.IsCapableToPerform;
import org.aero.mtip.metamodel.uaf.Dictionary.SameAs;
import org.aero.mtip.metamodel.uaf.Metadata.Implements;
import org.aero.mtip.metamodel.uaf.Metadata.PerformsInContext;
import org.aero.mtip.metamodel.uaf.Operational.ArbitraryConnector;
import org.aero.mtip.metamodel.uaf.Operational.OperationalAssociation;
import org.aero.mtip.metamodel.uaf.Operational.OperationalConnector;
import org.aero.mtip.metamodel.uaf.Operational.OperationalControlFlow;
import org.aero.mtip.metamodel.uaf.Operational.OperationalExchange;
import org.aero.mtip.metamodel.uaf.Operational.OperationalObjectFlow;
import org.aero.mtip.metamodel.uaf.Projects.MilestoneDependency;
import org.aero.mtip.metamodel.uaf.Projects.ProjectSequence;
import org.aero.mtip.metamodel.uaf.Resources.Forecast;
import org.aero.mtip.metamodel.uaf.Resources.FunctionControlFlow;
import org.aero.mtip.metamodel.uaf.Resources.FunctionObjectFlow;
import org.aero.mtip.metamodel.uaf.Resources.ResourceConnector;
import org.aero.mtip.metamodel.uaf.Resources.ResourceExchange;
import org.aero.mtip.metamodel.uaf.Resources.VersionSuccession;
import org.aero.mtip.metamodel.uaf.Strategic.AchievedEffect;
import org.aero.mtip.metamodel.uaf.Strategic.CapabilityForTask;
import org.aero.mtip.metamodel.uaf.Strategic.DesiredEffect;
import org.aero.mtip.metamodel.uaf.Strategic.Exhibits;
import org.aero.mtip.metamodel.uaf.Strategic.MapsToCapability;
import org.aero.mtip.metamodel.uaf.Strategic.OrganizationInEnterprise;
import org.aero.mtip.metamodel.uaf.actualresources.ActualResourceRelationship;
import org.aero.mtip.metamodel.uaf.actualresources.FillsPost;
import org.aero.mtip.metamodel.uaf.actualresources.OwnsProcess;
import org.aero.mtip.metamodel.uaf.actualresources.ProvidesCompetence;
import org.aero.mtip.metamodel.uaf.personnel.Command;
import org.aero.mtip.metamodel.uaf.personnel.CompetenceForRole;
import org.aero.mtip.metamodel.uaf.personnel.CompetenceToConduct;
import org.aero.mtip.metamodel.uaf.personnel.Control;
import org.aero.mtip.metamodel.uaf.personnel.RequiresCompetence;
import org.aero.mtip.metamodel.uaf.security.Affects;
import org.aero.mtip.metamodel.uaf.security.AffectsInContext;
import org.aero.mtip.metamodel.uaf.security.Enhances;
import org.aero.mtip.metamodel.uaf.security.Mitigates;
import org.aero.mtip.metamodel.uaf.security.OwnsRisk;
import org.aero.mtip.metamodel.uaf.security.OwnsRiskInContext;
import org.aero.mtip.metamodel.uaf.security.Protects;
import org.aero.mtip.metamodel.uaf.security.ProtectsInContext;
import org.aero.mtip.metamodel.uaf.services.Consumes;
import org.aero.mtip.metamodel.uaf.services.ServiceConnector;
import org.aero.mtip.metamodel.uaf.services.ServiceMessage;

public class CommonRelationshipsFactory {
	public CommonRelationship createElement(String type, String name, String importId) {
		CommonRelationship relationship = null;
		switch(type) {
			case SysmlConstants.ABSTRACTION:
				relationship = new Abstraction(name, importId);
				break;
			case SysmlConstants.ALLOCATE:
			    relationship = new Allocate(name, importId);
			    break;
			case SysmlConstants.AGGREGATION:
				relationship = new Aggregation(name, importId);
				break;
			case SysmlConstants.ASSOCIATION:
				relationship = new Association(name, importId);
				break;
			case SysmlConstants.BINDING_CONNECTOR:
				relationship = new BindingConnector(name, importId);
				break;
			case SysmlConstants.COMPOSITION:
				relationship = new Composition(name, importId);
				break;
			case SysmlConstants.COPY:
				relationship = new Copy(name, importId);
				break;
			case SysmlConstants.CONNECTOR:
				relationship = new Connector(name, importId);
				break;
			case SysmlConstants.CONTROL_FLOW:
				relationship = new ControlFlow(name, importId);
				break;
			case SysmlConstants.DERIVE_REQUIREMENT:
				relationship = new DeriveRequirement(name, importId);
				break;
			case SysmlConstants.DEPENDENCY:
				relationship = new Dependency(name, importId);
				break;
			case SysmlConstants.EXTEND:
				relationship = new Extend(name, importId);
				break;
			case SysmlConstants.EXTENSION:
				relationship = new Extension(name, importId);
				break;
			case SysmlConstants.GENERALIZATION:
				relationship = new Generalization(name, importId);
				break;
			case SysmlConstants.INCLUDE:
				relationship = new Include(name, importId);
				break;
			case SysmlConstants.INFORMATION_FLOW:
				relationship = new InformationFlow(name, importId);
				break;
			case SysmlConstants.INTERFACE_REALIZATION:
				relationship = new InterfaceRealization(name, importId);
				break;
			case SysmlConstants.ITEM_FLOW:
				relationship = new ItemFlow(name, importId);
				break;
			case SysmlConstants.MESSAGE:
				relationship = new Message(name, importId);
				break;
			case SysmlConstants.OBJECT_FLOW:
				relationship = new ObjectFlow(name, importId);
				break;
			case SysmlConstants.PACKAGE_IMPORT:
				relationship = new PackageImport(name, importId);
				break;
			case SysmlConstants.REALIZATION:
			    relationship = new Realization(name, importId);
			    break;
			case SysmlConstants.REFINE:
				relationship = new Refine(name, importId);
				break;
			case SysmlConstants.SATISFY:
				relationship = new Satisfy(name, importId);
				break;
			case SysmlConstants.TRACE:
				relationship = new Trace(name, importId);
				break;
			case SysmlConstants.TRANSITION:
				relationship = new Transition(name, importId);
				break;
			case SysmlConstants.USAGE:
				relationship = new Usage(name, importId);
				break;
			case SysmlConstants.VERIFY:
				relationship = new Verify(name, importId);
				break;
				
				
			//UAF
			case UAFConstants.ACHIEVED_EFFECT:
				relationship = new AchievedEffect(name, importId);
				break;
			case UAFConstants.CAPABILITY_FOR_TASK:
				relationship = new CapabilityForTask(name, importId);
				break;
			case UAFConstants.DESIRED_EFFECT:
				relationship = new DesiredEffect(name, importId);
				break;

			case UAFConstants.EXHIBITS:
				relationship = new Exhibits(name, importId);
				break;
			case UAFConstants.MAPS_TO_CAPABILITY:
				relationship = new MapsToCapability(name, importId);
				break;
			case UAFConstants.ORGANIZATION_IN_ENTERPRISE:
				relationship = new OrganizationInEnterprise(name, importId);
				break;
			//Operational
			/*
			case UAFConstants.OPERATIONAL_CONNECTOR:
				relationship = new OperationalConnector(name, importId);
				break;*/
		    /*case UAFConstants.INFORMATION_FLOW:
				relationship = new uaf.Operational.InformationFlow(name, importId);
				break;
			case UAFConstants.OBJECT_FLOW:
				relationship = new uaf.Operational.ObjectFlow(name, importId);
				break;*/
			/*case UAFConstants.OPERATIONAL_MESSAGE:
				relationship = new OperationalMessage(name, importId);
				break;*/
			case UAFConstants.OPERATIONAL_EXCHANGE:
				relationship = new OperationalExchange(name, importId);
				break;
			case UAFConstants.OPERATIONAL_CONNECTOR:
				relationship = new OperationalConnector(name, importId);
				break;
			case UAFConstants.OPERATIONAL_OBJECT_FLOW:
				relationship = new OperationalObjectFlow(name, importId);
				break;
			case UAFConstants.OPERATIONAL_CONTROL_FLOW:
				relationship = new OperationalControlFlow(name, importId);
				break;
			case UAFConstants.OPERATIONAL_ASSOCIATION:
				relationship = new OperationalAssociation(name, importId);
				break;
			case UAFConstants.ARBITRARY_CONNECTOR:
				relationship = new ArbitraryConnector(name, importId);
				break;
			case UAFConstants.RESOURCE_CONNECTOR:
				relationship = new ResourceConnector(name, importId);
				break;
			case UAFConstants.RESOURCE_EXCHANGE:
				relationship = new ResourceExchange(name, importId);
				break;
			case UAFConstants.FUNCTION_CONTROL_FLOW:
				relationship = new FunctionControlFlow(name, importId);
				break;
			case UAFConstants.FUNCTION_OBJECT_FLOW:
				relationship = new FunctionObjectFlow(name, importId);
				break;
			/*case UAFConstants.RESOURCE_MESSAGE:
				relationship = new ResourceMessage(name, importId);
				break;*/
			case UAFConstants.FORECAST:
				relationship = new Forecast(name, importId);
				break;
			case UAFConstants.VERSION_SUCCESSION:
				relationship = new VersionSuccession(name, importId);
				break;
			case UAFConstants.MILESTONE_DEPENDENCY:
				relationship = new MilestoneDependency(name, importId);
				break;
			case UAFConstants.PROJECT_SEQUENCE:
				relationship = new ProjectSequence(name, importId);
				break;
			case UAFConstants.SAME_AS:
				relationship = new SameAs(name, importId);
				break;
				
			// Actual Resources
			case UAFConstants.ACTUAL_RESOURCE_RELATIONSHIP:
				relationship = new ActualResourceRelationship(name, importId);
				break;
			case UAFConstants.FILLS_POST:
				relationship = new FillsPost(name, importId);
				break;
			case UAFConstants.OWNS_PROCESS:
				relationship = new OwnsProcess(name, importId);
				break;
			case UAFConstants.PROVIDES_COMPETENCE:
				relationship = new ProvidesCompetence(name, importId);
				break;
			
			// Personnel
			case UAFConstants.COMMAND:
				relationship = new Command(name, importId);
				break;
			case UAFConstants.COMPETENCE_FOR_ROLE:
				relationship = new CompetenceForRole(name, importId);
				break;
			case UAFConstants.COMPETENCE_TO_CONDUCT:
				relationship = new CompetenceToConduct(name, importId);
				break;
			case UAFConstants.CONTROL:
				relationship = new Control(name, importId);
				break;
			case UAFConstants.REQUIRES_COMPETENCE:
				relationship = new RequiresCompetence(name, importId);
				break;
				
			// Security
			case UAFConstants.AFFECTS:
				relationship = new Affects(name, importId);
				break;
			case UAFConstants.AFFECTS_IN_CONTEXT:
				relationship = new AffectsInContext(name, importId);
				break;
			case UAFConstants.ENHANCES:
				relationship = new Enhances(name, importId);
				break;
			case UAFConstants.MITIGATES:
				relationship = new Mitigates(name, importId);
				break;
			case UAFConstants.OWNS_RISK:
				relationship = new OwnsRisk(name, importId);
				break;
			case UAFConstants.OWNS_RISK_IN_CONTEXT:
				relationship = new OwnsRiskInContext(name, importId);
				break;
			case UAFConstants.PROTECTS:
				relationship = new Protects(name, importId);
				break;
			case UAFConstants.PROTECTS_IN_CONTEXT:
				relationship = new ProtectsInContext(name, importId);
				break;
				
			// Services
			case UAFConstants.CONSUMES:
				relationship = new Consumes(name, importId);
				break;
			case UAFConstants.SERVICE_CONNECTOR:
				relationship = new ServiceConnector(name, importId);
				break;
			case UAFConstants.SERVICE_MESSAGE:
				relationship = new ServiceMessage(name, importId);
				break;
			//MetaData
			case UAFConstants.IMPLEMENTS:
				relationship = new Implements(name, importId);
				break;
			case UAFConstants.PERFORMS_IN_CONTEXT:
				relationship = new PerformsInContext(name, importId);
				break;
			case UAFConstants.IS_CAPABLE_TO_PERFORM:
				relationship = new IsCapableToPerform(name, importId);
				break;				
			default:
				break;
		}
		return relationship;
	}
}
