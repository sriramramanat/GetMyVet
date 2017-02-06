package com.gmv.pre.template;

import com.gmv.pre.definitions.ProcedurePrices;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class FHOBundle {

	static public void createAndPublish (int variantLevel, boolean largeAnimal) {
		ProcedureDoc pd = create (variantLevel, largeAnimal);
		if (pd != null) {
			pd.write();
		}
	}
	
	static public ProcedureDoc create (int variantLevel, boolean largeAnimal) {
		String tier = "";
		String id = "";
		String name = "FHO - ";
		String size = "Standard - ";
		double totalPrice = 0;

		int rehabCount = 0;
		
		ProcedureDoc proc = new ProcedureDoc ();

		if (!largeAnimal) {
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_107";
				totalPrice = ProcedurePrices.B_FHO_STD_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_108";
				rehabCount = 4;
				totalPrice = ProcedurePrices.B_FHO_STD_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_109";
				rehabCount = 8;
				totalPrice = ProcedurePrices.B_FHO_STD_GOLD;
			}
			proc.addVariant(0, "GMV_PROC_BS_107");
			proc.addVariant(1, "GMV_PROC_BS_108");
			proc.addVariant(2, "GMV_PROC_BS_109");
		} else {
			size = "Large - ";
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_110";
				totalPrice = ProcedurePrices.B_FHO_LARGE_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_111";
				rehabCount = 4;
				totalPrice = ProcedurePrices.B_FHO_LARGE_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_112";
				rehabCount = 8;
				totalPrice = ProcedurePrices.B_FHO_LARGE_GOLD;
			}
			proc.addVariant(0, "GMV_PROC_BS_110");
			proc.addVariant(1, "GMV_PROC_BS_111");
			proc.addVariant(2, "GMV_PROC_BS_112");
		}
		name = name + size + tier;
		
		proc.setID (id);
		proc.setName (name);
		proc.setType("Surgical Bundle");
		proc.addApplicability("all", "", 0, 100, 0, 1000);
		proc.setVariantLevel(variantLevel);
		proc.setVariantsAvailable(true);
		proc.setCategory("Fill Payment Category Here");
		proc.setDescription("FHO, or femoral head ostectomy is a surgical procedure that removes the head and neck from the femur. FHO surgery is performed to alleviate pain. It is a salvage procedure, reserved for condition where pain can not be alleviated in any other way.");
		proc.setReason("Sick");
		proc.setProcedureTime(24);
		proc.setResultsTime(1680);
		proc.addAltCode("Provider 1", "FHO_P1");
		proc.addAltCode("Provider 2", "FHO_P2");
		proc.addAltCode("Provider 3", "FHO_P3");
		proc.addAltCode("Provider 4", "FHO_P4");
		proc.setWhoCanDo(RoleDefinitions.ROLE_007);
		
		int stepNumber = 1;

		proc.addInclusion("GMV_PROC_AC_102", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);

		proc.addInclusion("GMV_PROC_AL_201", true, true, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.COMPLETE_BLOODWORK/totalPrice);
		
		proc.addInclusion("GMV_PROC_AL_202", true, true, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.URINALYSIS_AND_UPC/totalPrice);
		
		proc.addInclusion("GMV_PROC_AI_301", true, true, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SEDATION_FOR_RADIOGRAPH/totalPrice);
		
		proc.addInclusion("GMV_PROC_AI_302", true, true, "1", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.RADIOGRAPH/totalPrice);

		if (!largeAnimal) {
			proc.addInclusion("GMV_PROC_AS_503", true, true, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.TPLO_STD/totalPrice);
			proc.setCore("GMV_PROC_AS_503");
		} else {
			proc.addInclusion("GMV_PROC_AS_504", true, true, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.TPLO_LARGE/totalPrice);
			proc.setCore("GMV_PROC_AS_504");
		}

		proc.addInclusion("GMV_PROC_AC_102", true, false, "x + 42", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);

		proc.addInclusion("GMV_PROC_AC_102", true, false, "x + 70", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);
		
		proc.addInclusion("GMV_PROC_AH_701", true, false, "TBD", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.REHABILITATION/totalPrice);

		for (int i = 0; i < rehabCount; i++) {
			proc.addInclusion("GMV_PROC_AH_701", false, false, "TBD", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.REHABILITATION/totalPrice);
		}

		return proc;
	}
}
