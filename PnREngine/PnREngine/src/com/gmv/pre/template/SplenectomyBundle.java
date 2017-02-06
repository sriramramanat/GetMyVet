package com.gmv.pre.template;

import com.gmv.pre.definitions.ProcedurePrices;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class SplenectomyBundle {

	static public void createAndPublish (int variantLevel, boolean emergent) {
		ProcedureDoc pd = create (variantLevel, emergent);
		if (pd != null) {
			pd.write();
		}
	}
	
	static public ProcedureDoc create (int variantLevel, boolean emergent) {
		
		String tier = "";
		String id = "";
		String name = "Splenectomy - ";
		double totalPrice = 0;

		ProcedureDoc proc = new ProcedureDoc ();

		if (!emergent) {
			name = name + "Non Emergent - ";
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_119";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_NE_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_120";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_NE_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_121";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_NE_GOLD;
				proc.addVariant(0, "GMV_PROC_BS_119");
				proc.addVariant(1, "GMV_PROC_BS_120");
				proc.addVariant(2, "GMV_PROC_BS_121");
			}
		} else {
			name = name + "Emergent - ";
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_122";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_E_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_123";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_E_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_124";
				totalPrice = ProcedurePrices.B_SPLENECTOMY_E_GOLD;
			}
			proc.addVariant(0, "GMV_PROC_BS_122");
			proc.addVariant(1, "GMV_PROC_BS_123");
			proc.addVariant(2, "GMV_PROC_BS_124");
		}
		name = name + tier;
		
		proc.setID (id);
		proc.setName (name);
		proc.setType("Surgical Bundle");
		proc.addApplicability("all", "", 0, 100, 0, 1000);
		proc.setVariantLevel(variantLevel);
		proc.setVariantsAvailable(true);
		proc.setCategory("Fill Payment Category Here");
		proc.setDescription("Surgical bundle to remove spleen");
		proc.setReason("Sick");
		proc.setProcedureTime(24);
		proc.setResultsTime(48);
		proc.addAltCode("Provider 1", "SPLENECTOMY_P1");
		proc.addAltCode("Provider 2", "SPLENECTOMY_P2");
		proc.addAltCode("Provider 3", "SPLENECTOMY_P3");
		proc.addAltCode("Provider 4", "SPLENECTOMY_P4");
		proc.setWhoCanDo(RoleDefinitions.ROLE_007);
		
		int stepNumber = 1;
		
		proc.addInclusion ("GMV_PROC_AC_102", false, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);

		proc.addInclusion ("GMV_PROC_AC_103", false, false, "1", -1, false);
		stepNumber++;

		proc.addInclusion ("GMV_PROC_AL_201", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.COMPLETE_BLOODWORK/totalPrice);

		proc.addInclusion ("GMV_PROC_AL_202", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.URINALYSIS_AND_UPC/totalPrice);

		proc.addInclusion ("GMV_PROC_AL_203", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.PT_PTT/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_301", false, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SEDATION_FOR_RADIOGRAPH/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_303", false, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.THORACIC_RADIOGRAPH/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_305", false, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.ABDOMINAL_ULTRASOUND/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_306", false, false, "1", -1, false);
		stepNumber++;

		if (!emergent) {
			proc.addInclusion ("GMV_PROC_AS_508", true, false, "x", -1, false);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SPLENECTOMY_NE/totalPrice);

			proc.setCore("GMV_PROC_AS_508");
		} else {
			proc.addInclusion ("GMV_PROC_AS_509", true, false, "x", -1, false);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SPLENECTOMY_E/totalPrice);

			proc.setCore("GMV_PROC_AS_509");
		}

		if (!emergent) {
			proc.addInclusion ("GMV_PROC_AR_603", true, false, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SPLENECTOMY_RECOVERY_NE/totalPrice);
		} else {
			proc.addInclusion ("GMV_PROC_AR_604", true, false, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SPLENECTOMY_RECOVERY_E/totalPrice);

			proc.addInclusion ("GMV_PROC_AR_605", false, false, "x", -1, true);
		}
		
		proc.addInclusion ("GMV_PROC_AC_104", false, false, "x+1", -1, true);

		return proc;
	}
}
