package com.gmv.pre.template;

import com.gmv.pre.definitions.ProcedurePrices;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class AtomicProcedures {
	
	public static void createAndPublish () {
		
		ProcedureDoc pd = new ProcedureDoc ();
		
		// Consultation
		pd.setID("GMV_PROC_AC_101");
		pd.setName("Consultation - General");
		pd.setDescription("General Consultation Procedure");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Consultation");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AC_101");
		pd.addAltCode("Insurer 2", "AC_101");
		pd.setRegionalPrice(ProcedurePrices.GEN_CONSULT);
		pd.setWhoCanDo(RoleDefinitions.ROLE_005);
		pd.write();
		
		pd.setID("GMV_PROC_AC_102");
		pd.setName("Consultation - Surgical");
		pd.setDescription("Surgical Consultation Procedure");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Consultation");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AC_102");
		pd.addAltCode("Insurer 2", "AC_102");
		pd.setRegionalPrice(ProcedurePrices.SURGERY_CONSULT);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd.setID("GMV_PROC_AC_103");
		pd.setName("Consultation - Urgent Care");
		pd.setDescription("Urgent Care Examination");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Consultation");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AC_103");
		pd.addAltCode("Insurer 2", "AC_103");
		pd.setRegionalPrice(ProcedurePrices.URGENT_CARE_CONSULT);
		pd.setWhoCanDo(RoleDefinitions.ROLE_006);
		pd.write();

		pd.setID("GMV_PROC_AC_104");
		pd.setName("Consultation - Oncology");
		pd.setDescription("Oncology Consultation Procedure");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Consultation");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(48);
		pd.addAltCode("Insurer 1", "AC_104");
		pd.addAltCode("Insurer 2", "AC_104");
		pd.setRegionalPrice(ProcedurePrices.ONCOLOGY_CONSULT);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		// Blood count
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AL_201");
		pd.setName("Complete Blood Count/Chemistry");
		pd.setDescription("Complete Blood Count and Chemistry - Describe what exactly is done here");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Labwork");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AL_201");
		pd.addAltCode("Insurer 2", "AL_201");
		pd.setWhoCanDo(RoleDefinitions.ROLE_011);
		pd.setRegionalPrice(ProcedurePrices.COMPLETE_BLOODWORK);
		pd.write();

		// Urinalysis
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AL_202");
		pd.setName("Urinalysis and UPC");
		pd.setDescription("Urinalysis and Urine Protein and Creatinine analysis");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Labwork");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AL_202");
		pd.addAltCode("Insurer 2", "AL_202");
		pd.setWhoCanDo(RoleDefinitions.ROLE_011);
		pd.setRegionalPrice(ProcedurePrices.URINALYSIS_AND_UPC);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AL_203");
		pd.setName("PT/PTT");
		pd.setDescription("The prothrombin time (PT) test measures the length of time it takes for a blood clot to form in a sample of blood. Partial Thromboplastin Time (PTT) test is ordered in case of unexplained bleeding or clotting.");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Labwork");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AL_203");
		pd.addAltCode("Insurer 2", "AL_203");
		pd.setWhoCanDo(RoleDefinitions.ROLE_012);
		pd.setRegionalPrice(ProcedurePrices.PT_PTT);
		pd.write();

		// Radiograph Sedation
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_301");
		pd.setName("Sedation for Radiographs");
		pd.setDescription("Sedation for radiographs");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(4);
		pd.addAltCode("Insurer 1", "AI_301");
		pd.addAltCode("Insurer 2", "AI_301");
		pd.setWhoCanDo(RoleDefinitions.ROLE_015);
		pd.setRegionalPrice(ProcedurePrices.SEDATION_FOR_RADIOGRAPH);
		pd.write();
	
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_302");
		pd.setName("Radiograph");
		pd.setDescription("Radiograph");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AI_302");
		pd.addAltCode("Insurer 2", "AI_302");
		pd.setWhoCanDo(RoleDefinitions.ROLE_013);
		pd.setRegionalPrice(ProcedurePrices.RADIOGRAPH);
		pd.write();
	
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_303");
		pd.setName("Thoracic Radiograph");
		pd.setDescription("Thoracic Radiograph");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AI_303");
		pd.addAltCode("Insurer 2", "AI_303");
		pd.setWhoCanDo(RoleDefinitions.ROLE_013);
		pd.setRegionalPrice(ProcedurePrices.THORACIC_RADIOGRAPH);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_304");
		pd.setName("Abdominal Radiograph");
		pd.setDescription("Abdominal Radiograph");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AI_304");
		pd.addAltCode("Insurer 2", "AI_304");
		pd.setWhoCanDo(RoleDefinitions.ROLE_013);
		pd.setRegionalPrice(ProcedurePrices.ABDOMINAL_RADIOGRAPH);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_305");
		pd.setName("Abdominal Ultrasound");
		pd.setDescription("Abdominal Ultrasound");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AI_305");
		pd.addAltCode("Insurer 2", "AI_305");
		pd.setWhoCanDo(RoleDefinitions.ROLE_013);
		pd.setRegionalPrice(ProcedurePrices.ABDOMINAL_ULTRASOUND);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AI_306");
		pd.setName("Abdominal and Thoracic CT with contrast");
		pd.setDescription("Abdominal and Thoracic CT with contrast");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Imaging");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(1);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AI_306");
		pd.addAltCode("Insurer 2", "AI_306");
		pd.setRegionalPrice(ProcedurePrices.ABDOMINAL_AND_THORACIC_CT);
		pd.setWhoCanDo(RoleDefinitions.ROLE_014);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_501");
		pd.setName("TPLO Surgery - Standard");
		pd.setDescription("TPLO Surgery for standard sized dog, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 40);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_501");
		pd.addAltCode("Insurer 2", "AS_501");
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.setRegionalPrice(ProcedurePrices.TPLO_STD);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_502");
		pd.setName("TPLO Surgery - Large");
		pd.setDescription("TPLO Surgery for large sized dog, includes medicines to go home");
		pd.addApplicability("large dogs", "", 0, 100, 40, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_502");
		pd.addAltCode("Insurer 2", "AS_502");
		pd.setRegionalPrice(ProcedurePrices.TPLO_LARGE);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_503");
		pd.setName("FHO Surgery - Standard");
		pd.setDescription("FHO Surgery for standard (<40 lb) sized dog, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 40);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_503");
		pd.addAltCode("Insurer 2", "AS_503");
		pd.setRegionalPrice(ProcedurePrices.FHO_STD);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_504");
		pd.setName("FHO Surgery - Large");
		pd.setDescription("FHO Surgery for large (>40lb) sized dog, includes medicines to go home");
		pd.addApplicability("large dogs", "", 0, 100, 40, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_504");
		pd.addAltCode("Insurer 2", "AS_504");
		pd.setRegionalPrice(ProcedurePrices.FHO_LARGE);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_505");
		pd.setName("MPL Surgery - Standard");
		pd.setDescription("MPL Surgery for standard (<40 lb) sized dog, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 40);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_505");
		pd.addAltCode("Insurer 2", "AS_505");
		pd.setRegionalPrice(ProcedurePrices.MPL_STD);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_506");
		pd.setName("MPL Surgery - Large");
		pd.setDescription("MPL Surgery for large (>40lb) sized dog, includes medicines to go home");
		pd.addApplicability("large dogs", "", 0, 100, 40, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_506");
		pd.addAltCode("Insurer 2", "AS_506");
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.setRegionalPrice(ProcedurePrices.MPL_LARGE);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_507");
		pd.setName("GDV Surgery");
		pd.setDescription("GDV Surgery, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_507");
		pd.addAltCode("Insurer 2", "AS_507");
		pd.setRegionalPrice(ProcedurePrices.GDV);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_508");
		pd.setName("Splenectomy Surgery - non-emergent");
		pd.setDescription("non-emergent Splenectomy Surgery, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_508");
		pd.addAltCode("Insurer 2", "AS_508");
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.setRegionalPrice(ProcedurePrices.SPLENECTOMY_NE);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AS_509");
		pd.setName("Splenectomy Surgery - emergent");
		pd.setDescription("Emergent Splenectomy Surgery, includes medicines to go home");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Surgery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AS_509");
		pd.addAltCode("Insurer 2", "AS_509");
		pd.setRegionalPrice(ProcedurePrices.SPLENECTOMY_E);
		pd.setWhoCanDo(RoleDefinitions.ROLE_007);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AR_601");
		pd.setName("Uncomplicated GDV Recovery");
		pd.setDescription("Uncomplicated GDV Recovery");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Post-operative Recovery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(0);
		pd.setResultsTime(24);
		pd.addAltCode("Insurer 1", "AR_601");
		pd.addAltCode("Insurer 2", "AR_601");
		pd.setRegionalPrice(ProcedurePrices.GDV_RECOVERY);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AR_602");
		pd.setName("Complicated GDV Recovery");
		pd.setDescription("Complicated GDV Recovery");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Post-operative Recovery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(0);
		pd.setResultsTime(48);
		pd.addAltCode("Insurer 1", "AR_602");
		pd.addAltCode("Insurer 2", "AR_602");
		pd.setRegionalPrice(ProcedurePrices.GDV_RECOVERY_C);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AR_603");
		pd.setName("Uncomplicated Splenectomy Recovery - non-Emergent");
		pd.setDescription("Uncomplicated Splenectomy Recovery - non-Emergent");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Post-operative Recovery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(0);
		pd.setResultsTime(24);
		pd.addAltCode("Insurer 1", "AR_603");
		pd.addAltCode("Insurer 2", "AR_603");
		pd.setRegionalPrice(ProcedurePrices.SPLENECTOMY_RECOVERY_NE);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AR_604");
		pd.setName("Uncomplicated Splenectomy Recovery - Emergent");
		pd.setDescription("Uncomplicated Splenectomy Recovery - Emergent");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Post-operative Recovery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(0);
		pd.setResultsTime(24);
		pd.addAltCode("Insurer 1", "AR_604");
		pd.addAltCode("Insurer 2", "AR_604");
		pd.setRegionalPrice(ProcedurePrices.SPLENECTOMY_NE);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AR_605");
		pd.setName("Complicated Splenectomy Recovery - Emergent");
		pd.setDescription("Complicated Splenectomy Recovery - Emergent");
		pd.addApplicability("dogs", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Post-operative Recovery");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(0);
		pd.setResultsTime(24);
		pd.addAltCode("Insurer 1", "AR_605");
		pd.addAltCode("Insurer 2", "AR_605");
		pd.setRegionalPrice(ProcedurePrices.SPLENECTOMY_RECOVERY_E_C);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AH_701");
		pd.setName("Rehabilitation Therapy");
		pd.setDescription("Rehabilitation therapy for post-operative recovery");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Rehabilitation");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Sick");
		pd.setProcedureTime(8);
		pd.setResultsTime(16);
		pd.addAltCode("Insurer 1", "AH_701");
		pd.addAltCode("Insurer 2", "AH_601");
		pd.setRegionalPrice(ProcedurePrices.REHABILITATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_801");
		pd.setName("DAP Vaccine");
		pd.setDescription("DAP Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_801");
		pd.addAltCode("Insurer 2", "AV_801");
		pd.setRegionalPrice(ProcedurePrices.DAP_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_802");
		pd.setName("Rabies Vaccine");
		pd.setDescription("Rabies Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_802");
		pd.addAltCode("Insurer 2", "AV_802");
		pd.setRegionalPrice(ProcedurePrices.RABIES_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_803");
		pd.setName("Leptospirosis Vaccine");
		pd.setDescription("Leptospirosis Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_803");
		pd.addAltCode("Insurer 2", "AV_803");
		pd.setRegionalPrice(ProcedurePrices.LEPTOSPIROSIS_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_804");
		pd.setName("Lyme Vaccine");
		pd.setDescription("Lyme Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_804");
		pd.addAltCode("Insurer 2", "AV_804");
		pd.setRegionalPrice(ProcedurePrices.LYME_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_805");
		pd.setName("K9 Influenza Vaccine");
		pd.setDescription("K9 Influenza Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_805");
		pd.addAltCode("Insurer 2", "AV_805");
		pd.setRegionalPrice(ProcedurePrices.K9INFLUENZA_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_806");
		pd.setName("Deworming");
		pd.setDescription("Deworming");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_806");
		pd.addAltCode("Insurer 2", "AV_806");
		pd.setRegionalPrice(ProcedurePrices.DEWORMING);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_807");
		pd.setName("Fecal");
		pd.setDescription("Fecal");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Other");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_807");
		pd.addAltCode("Insurer 2", "AV_807");
		pd.setRegionalPrice(ProcedurePrices.FECAL);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_808");
		pd.setName("Bordatella");
		pd.setDescription("Bordatella");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Other");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_808");
		pd.addAltCode("Insurer 2", "AV_808");
		pd.setRegionalPrice(ProcedurePrices.BORDETALLA);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);

		pd.write();
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_809");
		pd.setName("Nail Trimming");
		pd.setDescription("Nail Trimming");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Other");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_809");
		pd.addAltCode("Insurer 2", "AV_809");
		pd.setRegionalPrice(ProcedurePrices.NAIL_TRIMMING);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();
		
		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_810");
		pd.setName("HW");
		pd.setDescription("HW");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Other");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_810");
		pd.addAltCode("Insurer 2", "AV_810");
		pd.setRegionalPrice(ProcedurePrices.HW);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_811");
		pd.setName("Flea and Tick");
		pd.setDescription("Flea and Tick");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Other");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_811");
		pd.addAltCode("Insurer 2", "AV_811");
		pd.setRegionalPrice(ProcedurePrices.FLEA_AND_TICK);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_812");
		pd.setName("FVRCP Vaccine");
		pd.setDescription("FVRCP Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_812");
		pd.addAltCode("Insurer 2", "AV_812");
		pd.setRegionalPrice(ProcedurePrices.FVRCP_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_813");
		pd.setName("Lukemia Vaccine");
		pd.setDescription("Lukemia Vaccine");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_813");
		pd.addAltCode("Insurer 2", "AV_813");
		pd.setRegionalPrice(ProcedurePrices.LUKEMIA_VACCINATION);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();

		pd = new ProcedureDoc ();
		pd.setID("GMV_PROC_AV_814");
		pd.setName("FELV / FIV");
		pd.setDescription("FELV / FIV");
		pd.addApplicability("all", "", 0, 100, 0, 1000);
		pd.setVariantLevel(0);
		pd.setType("Vaccination / Preventive");
		pd.setCategory("Payment Category goes here");
		pd.setReason("Well");
		pd.setProcedureTime(0.25);
		pd.setResultsTime(0);
		pd.addAltCode("Insurer 1", "AV_813");
		pd.addAltCode("Insurer 2", "AV_813");
		pd.setRegionalPrice(ProcedurePrices.FELV_FIV);
		pd.setWhoCanDo(RoleDefinitions.ROLE_009);
		pd.write();
	}
}
