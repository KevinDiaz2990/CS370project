/*******************************************************************************
 * Copyright (c) 2013 bon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     bon - initial API and implementation
 *     
 *******************************************************************************/
package org.openhealthtools.mdht.uml.cda.ccd.mytest;

/**
 * @author bon
 *
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.emf.common.util.Diagnostic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedCustodian;
import org.openhealthtools.mdht.uml.cda.AssignedEntity;
import org.openhealthtools.mdht.uml.cda.Author;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.Custodian;
import org.openhealthtools.mdht.uml.cda.CustodianOrganization;
import org.openhealthtools.mdht.uml.cda.DocumentationOf;
import org.openhealthtools.mdht.uml.cda.Entry;
import org.openhealthtools.mdht.uml.cda.EntryRelationship;
import org.openhealthtools.mdht.uml.cda.Informant12;
import org.openhealthtools.mdht.uml.cda.InfrastructureRootTypeId;
import org.openhealthtools.mdht.uml.cda.LegalAuthenticator;
import org.openhealthtools.mdht.uml.cda.Observation;
import org.openhealthtools.mdht.uml.cda.Organization;
import org.openhealthtools.mdht.uml.cda.Patient;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Performer1;
import org.openhealthtools.mdht.uml.cda.Person;
import org.openhealthtools.mdht.uml.cda.RecordTarget;
import org.openhealthtools.mdht.uml.cda.ServiceEvent;
import org.openhealthtools.mdht.uml.cda.ccd.CCDFactory;
import org.openhealthtools.mdht.uml.cda.ccd.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.ccd.ResultObservation;
import org.openhealthtools.mdht.uml.cda.ccd.VitalSignsOrganizer;
import org.openhealthtools.mdht.uml.cda.ccd.VitalSignsSection;
import org.openhealthtools.mdht.uml.cda.util.BasicValidationHandler;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVL_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.IVXB_TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.PQ;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
import org.openhealthtools.mdht.uml.hl7.vocab.ActClassObservation;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActClassDocumentEntryOrganizer;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActMoodDocumentObservation;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActRelationshipEntry;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActRelationshipEntryRelationship;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ServiceEventPerformer;

public class Main {

	private JSONObject obj;

	public Main(String file) throws Exception {
		JSONParser parser = new JSONParser();

		Object o = parser.parse(new FileReader(file)); // Parses JSON file
		obj = (JSONObject) o;
		init(); // Generates the Header

	}

	/**
	 * Generates the headers that was provided by Dr.Sy
	 * @throws Exception
	 */

	private void init() throws Exception {
		ContinuityOfCareDocument doc = CCDFactory.eINSTANCE.createContinuityOfCareDocument().init();

		InfrastructureRootTypeId myTypeId = CDAFactory.eINSTANCE.createInfrastructureRootTypeId();
		// myTypeId.setAssigningAuthorityName("aaa");
		myTypeId.setExtension("POCD_HD000040");
		myTypeId.setRoot("2.16.840.1.113883.1.3");
		doc.setTypeId(myTypeId);

		II one_id = DatatypesFactory.eINSTANCE.createII();
		one_id.setRoot("2.16.840.1.113883.19.5");
		one_id.setExtension("996-756-495");
		doc.setId(one_id);

		CE code = DatatypesFactory.eINSTANCE.createCE("34133-9", "2.16.840.1.113883.6.1", "LOINC", "Consultation note");
		doc.setCode(code);

		ST title = DatatypesFactory.eINSTANCE.createST("Health Insurance Claim Note");
		doc.setTitle(title);

		TS effectiveTime1 = DatatypesFactory.eINSTANCE.createTS("20000407130000+0500");
		doc.setEffectiveTime(effectiveTime1);

		CE confidentialityCode = DatatypesFactory.eINSTANCE.createCE("N", "2.16.840.1.113883.5.25");
		doc.setConfidentialityCode(confidentialityCode);

		CS lang_code = DatatypesFactory.eINSTANCE.createCS("en-US");
		doc.setLanguageCode(lang_code);

		RecordTarget RT = CDAFactory.eINSTANCE.createRecordTarget();

		II id = DatatypesFactory.eINSTANCE.createII();
		id.setExtension("996-756-495");
		id.setRoot("2.16.840.1.113883.19.5");

		PatientRole PR = CDAFactory.eINSTANCE.createPatientRole();
		PR.getIds().add(id);

		PN name = DatatypesFactory.eINSTANCE.createPN();
		name.addGiven("Kevin");
		name.addFamily("Diaz");

		Patient pt = CDAFactory.eINSTANCE.createPatient();
		pt.getNames().add(name);

		CE agc = DatatypesFactory.eINSTANCE.createCE("M", "2.16.840.1.113883.5.1");
		pt.setAdministrativeGenderCode(agc);
		TS birthtime = DatatypesFactory.eINSTANCE.createTS("19320924");
		pt.setBirthTime(birthtime);
		PR.setPatient(pt);

		Organization organization = CDAFactory.eINSTANCE.createOrganization();
		II id1 = DatatypesFactory.eINSTANCE.createII();
		id1.setRoot("2.16.840.1.113883.19.5");
		organization.getIds().add(id1);
		PR.setProviderOrganization(organization);

		RT.setPatientRole(PR);
		doc.getRecordTargets().add(RT);

		Author author = CDAFactory.eINSTANCE.createAuthor();
		TS effectiveTime2 = DatatypesFactory.eINSTANCE.createTS("20000407130000+0500");
		author.setTime(effectiveTime2);

		AssignedAuthor assignedAuthor = CDAFactory.eINSTANCE.createAssignedAuthor();
		II id2 = DatatypesFactory.eINSTANCE.createII();
		id2.setRoot("20cf14fb-b65c-4c8c-a54d-b0cca834c18c");
		assignedAuthor.getIds().add(id2);

		Person person = CDAFactory.eINSTANCE.createPerson();
		PN doctor = DatatypesFactory.eINSTANCE.createPN();
		doctor.addPrefix("Dr.");
		doctor.addGiven("Robert");
		doctor.addFamily("Dolin");
		person.getNames().add(doctor);
		assignedAuthor.setAssignedPerson(person);

		Organization organization2 = CDAFactory.eINSTANCE.createOrganization();
		II id3 = DatatypesFactory.eINSTANCE.createII();
		id3.setRoot("2.16.840.1.113883.19.5");
		organization2.getIds().add(id3);

		ON on = DatatypesFactory.eINSTANCE.createON();
		on.addText("Good Health Clinic");
		organization2.getNames().add(on);
		assignedAuthor.setRepresentedOrganization(organization2);

		author.setAssignedAuthor(assignedAuthor);
		doc.getAuthors().add(author);

		Informant12 informant = CDAFactory.eINSTANCE.createInformant12();
		AssignedEntity assignedEntity1 = CDAFactory.eINSTANCE.createAssignedEntity();

		informant.setAssignedEntity(assignedEntity1);
		NullFlavor nullFlavor = informant.getNullFlavor();
		nullFlavor.get("NI");
		II id4 = DatatypesFactory.eINSTANCE.createII();
		id4.setNullFlavor(nullFlavor);
		assignedEntity1.getIds().add(id4);
		assignedEntity1.getRepresentedOrganizations().add(organization);

		doc.getInformants().add(informant);

		Custodian custodian = CDAFactory.eINSTANCE.createCustodian();
		AssignedCustodian assignedCustodian = CDAFactory.eINSTANCE.createAssignedCustodian();
		CustodianOrganization organization3 = CDAFactory.eINSTANCE.createCustodianOrganization();
		II id5 = DatatypesFactory.eINSTANCE.createII();
		id5.setRoot("2.16.840.1.113883.19.5");
		organization3.getIds().add(id5);

		ON on1 = DatatypesFactory.eINSTANCE.createON();
		on1.addText("Good Health Clinic");
		organization3.setName(on1);
		assignedCustodian.setRepresentedCustodianOrganization(organization3);

		custodian.setAssignedCustodian(assignedCustodian);
		doc.setCustodian(custodian);

		LegalAuthenticator legalAuthenticator = CDAFactory.eINSTANCE.createLegalAuthenticator();
		TS effectiveTime3 = DatatypesFactory.eINSTANCE.createTS("20000407130000+0500");
		legalAuthenticator.setTime(effectiveTime3);
		CS signatureCode = DatatypesFactory.eINSTANCE.createCS("S");

		legalAuthenticator.setSignatureCode(signatureCode);
		AssignedEntity assignedEntity2 = CDAFactory.eINSTANCE.createAssignedEntity();

		NullFlavor nullFlavor1 = informant.getNullFlavor();
		nullFlavor1.get("NI");
		II id6 = DatatypesFactory.eINSTANCE.createII();
		id6.setNullFlavor(nullFlavor1);
		assignedEntity2.getIds().add(id6);

		Organization organization4 = CDAFactory.eINSTANCE.createOrganization();
		II id7 = DatatypesFactory.eINSTANCE.createII();
		id7.setRoot("2.16.840.1.113883.19.5");
		organization4.getIds().add(id7);

		ON on2 = DatatypesFactory.eINSTANCE.createON();
		on2.addText("Good Health Clinic");
		organization4.getNames().add(on2);
		assignedEntity2.getRepresentedOrganizations().add(organization4);

		legalAuthenticator.setAssignedEntity(assignedEntity2);
		doc.setLegalAuthenticator(legalAuthenticator);

		DocumentationOf documentationOf = CDAFactory.eINSTANCE.createDocumentationOf();
		ServiceEvent serviceEvent = CDAFactory.eINSTANCE.createServiceEvent();

		serviceEvent.setClassCode(serviceEvent.getClassCode().PCPR);
		IVL_TS effectiveTime4 = DatatypesFactory.eINSTANCE.createIVL_TS();
		IVXB_TS value1_IVXB_TS = DatatypesFactory.eINSTANCE.createIVXB_TS();
		value1_IVXB_TS.setValue("19320924");
		effectiveTime4.setLow(value1_IVXB_TS);
		IVXB_TS value2_IVXB_TS = DatatypesFactory.eINSTANCE.createIVXB_TS();
		value2_IVXB_TS.setValue("20000407");
		effectiveTime4.setHigh(value2_IVXB_TS);
		serviceEvent.setEffectiveTime(effectiveTime4);

		Performer1 performer1_value1 = CDAFactory.eINSTANCE.createPerformer1();
		x_ServiceEventPerformer x_ServiceEventPerfomer_value1 = null;
		performer1_value1.setTypeCode(x_ServiceEventPerfomer_value1); // Need to set to PRF

		CE functionCode1 = DatatypesFactory.eINSTANCE.createCE("PCP", "2.16.840.1.113883.5.88");
		performer1_value1.setFunctionCode(functionCode1);

		IVL_TS effectiveTime5 = DatatypesFactory.eINSTANCE.createIVL_TS();
		IVXB_TS value3_IVXB_TS = DatatypesFactory.eINSTANCE.createIVXB_TS();
		value3_IVXB_TS.setValue("1990");
		effectiveTime5.setLow(value3_IVXB_TS);
		IVXB_TS value4_IVXB_TS = DatatypesFactory.eINSTANCE.createIVXB_TS();
		value4_IVXB_TS.setValue("20000407");
		effectiveTime5.setHigh(value4_IVXB_TS);
		performer1_value1.setTime(effectiveTime5);

		AssignedEntity assignedEntity3 = CDAFactory.eINSTANCE.createAssignedEntity();
		II id8 = DatatypesFactory.eINSTANCE.createII();
		id8.setRoot("20cf14fb-b65c-4c8c-a54d-b0cca834c18c");
		assignedEntity3.getIds().add(id8);

		Person person2 = CDAFactory.eINSTANCE.createPerson();
		PN doctor2 = DatatypesFactory.eINSTANCE.createPN();
		doctor2.addPrefix("Dr.");
		doctor2.addGiven("Robert");
		doctor2.addFamily("Dolin");
		person2.getNames().add(doctor2);
		assignedEntity3.setAssignedPerson(person2);

		Organization organization5 = CDAFactory.eINSTANCE.createOrganization();
		II id9 = DatatypesFactory.eINSTANCE.createII();
		id9.setRoot("2.16.840.1.113883.19.5");
		organization5.getIds().add(id9);

		ON on3 = DatatypesFactory.eINSTANCE.createON();
		on3.addText("Good Health Clinic");
		organization5.getNames().add(on3);
		assignedEntity3.getRepresentedOrganizations().add(organization5);

		performer1_value1.setAssignedEntity(assignedEntity3);
		serviceEvent.getPerformers().add(performer1_value1);
		documentationOf.setServiceEvent(serviceEvent);

		doc.getDocumentationOfs().add(documentationOf);
		VitalSignsSection vsec;
		vsec = getVitalSec(); // generates the Vital Sign Section
		doc.addSection(vsec);

		CDAUtil.save(doc, new FileOutputStream(new File("samples/Output.xml")));
		System.out.println("\n\n***** Validate generated CCD *****");
		validate(doc);

	}

	/**
	 * Uses the data from the JSON to fill out the Vital Sign section
	 * 
	 * 
	 * @return
	 */

	private VitalSignsSection getVitalSec() {
		VitalSignsSection vsec = CCDFactory.eINSTANCE.createVitalSignsSection().init();

		ST title = DatatypesFactory.eINSTANCE.createST("Vital Signs");
		vsec.setTitle(title);
		vsec.createStrucDocText("text");
		JSONObject object = (JSONObject) obj.get("VitalSigns");
		JSONArray array1 = (JSONArray) object.get("data");
		for (Object o : array1) {

			JSONObject object1 = (JSONObject) o;

			Entry e = CDAFactory.eINSTANCE.createEntry();
			e.setTypeCode(x_ActRelationshipEntry.DRIV);
			vsec.getEntries().add(e);
			VitalSignsOrganizer vorg = CCDFactory.eINSTANCE.createVitalSignsOrganizer().init();
			e.setOrganizer(vorg);
			vorg.setClassCode(x_ActClassDocumentEntryOrganizer.CLUSTER);

			II id = DatatypesFactory.eINSTANCE.createII(UUID.randomUUID().toString());
			CD cd1 = DatatypesFactory.eINSTANCE.createCD(
				"46680005", "2.16.840.1.113883.6.96", "SNOMEDCT", "Vital Signs");
			JSONArray array2 = (JSONArray) object1.get("components");
			vorg.getIds().add(id);
			CS cs1 = DatatypesFactory.eINSTANCE.createCS("Completed");
			vorg.setStatusCode(cs1);
			vorg.setCode(cd1);
			Integer j = 300000000;
			HashMap<String, String> hm = new HashMap<String, String>(); // to assign unique values to new Observations that was not given in the
																		// original JSON

			for (Object l : array2) { // will loop through the JSONArray and fill XML with information contained the JSON Object

				JSONObject object2 = (JSONObject) l;
				ResultObservation ro = CCDFactory.eINSTANCE.createResultObservation().init();
				vorg.addObservation(ro);
				IVL_TS time = DatatypesFactory.eINSTANCE.createIVL_TS(object2.get("specificEntryDate").toString());
				CE ce2 = DatatypesFactory.eINSTANCE.createCE();
				ce2.setCodeSystem("2.16.840.1.113883.6.96");
				ce2.setDisplayName(object2.get("vitalName").toString());
				if (object2.get("vitalName").toString().equals("Plasma")) {
					ce2.setCode("p");
				} else if (object2.get("vitalName").toString().equals("Systolic blood pressure")) {
					ce2.setCode("271649006");
				} else if (object2.get("vitalName").toString().equals("Diastolic blood pressure"))
					ce2.setCode("271650006");
				else if (object2.get("vitalName").toString().equals("Body Weight"))
					ce2.setCode("271649006");
				else {
					String vital = (String) object2.get("vitalName");
					if (hm.containsKey(vital)) {
						ce2.setCode(hm.get(vital));
					} else if (hm.containsValue(j.toString())) {
						while (hm.containsValue(j.toString()))
							j++;
						hm.put(vital, j.toString());
						ce2.setCode(hm.get("vital"));
					} else {
						hm.put(vital, j.toString());
						ce2.setCode(hm.get(vital));
						j++;
					}
				}
				double unit = Double.parseDouble(object2.get("value").toString());
				CS cs2 = DatatypesFactory.eINSTANCE.createCS("completed");
				II ident1 = DatatypesFactory.eINSTANCE.createII(UUID.randomUUID().toString());
				PQ pq = DatatypesFactory.eINSTANCE.createPQ(unit, object2.get("unit").toString());
				IVL_TS time1 = DatatypesFactory.eINSTANCE.createIVL_TS(object2.get("specificEntryDate").toString());
				ro.setEffectiveTime(time1);
				ro.getValues().add(pq);
				ro.setCode(ce2);
				ro.getIds().add(ident1);
				ro.setStatusCode(cs2);
				EntryRelationship er = CDAFactory.eINSTANCE.createEntryRelationship();
				er.setTypeCode(x_ActRelationshipEntryRelationship.REFR);
				Observation o1 = CDAFactory.eINSTANCE.createObservation();
				o1.setClassCode(ActClassObservation.OBS);
				o1.setMoodCode(x_ActMoodDocumentObservation.EVN);
				CE ce3 = DatatypesFactory.eINSTANCE.createCE();
				ce3.setCodeSystem("2.16.840.1.113883.5.14");
				ce3.setDisplayName("Information source");
				ce3.setCode("48766-0");
				ce3.setCodeSystemName("LOINC");
				CS cs3 = DatatypesFactory.eINSTANCE.createCS("completed");
				o1.setStatusCode(cs3);
				o1.setCode(ce3);
				ST sf = DatatypesFactory.eINSTANCE.createST("Unknown");
				o1.getValues().add(sf);
				er.setObservation(o1);
				ro.getEntryRelationships().add(er);
				vorg.setEffectiveTime(time);

			}
		}

		return vsec;
	}

	/**
	 * @param doc
	 */
	private void validate(ContinuityOfCareDocument clinicalDocument) throws Exception {
		// TODO Auto-generated method stub
		boolean valid = CDAUtil.validate(clinicalDocument, new BasicValidationHandler() {
			@Override
			public void handleError(Diagnostic diagnostic) {
				System.out.println("ERROR: " + diagnostic.getMessage());
			}

			@Override
			public void handleWarning(Diagnostic diagnostic) {
				System.out.println("WARNING: " + diagnostic.getMessage());
			}
			// @Override
			// public void handleInfo(Diagnostic diagnostic) {
			// System.out.println("INFO: " + diagnostic.getMessage());
			// }
		});

		if (valid) {
			System.out.println("Document is valid");
		} else {
			System.out.println("Document is invalid");
		}

	}

	public static void main(String[] args) throws Exception {
		Main object = new Main(args[0]);

	}
}
