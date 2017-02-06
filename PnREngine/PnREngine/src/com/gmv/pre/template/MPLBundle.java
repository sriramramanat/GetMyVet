package com.gmv.pre.template;

import com.gmv.pre.definitions.ProcedurePrices;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class MPLBundle {
	static public void createAndPublish (int variantLevel, boolean largeAnimal) {
		ProcedureDoc pd = create (variantLevel, largeAnimal);
		if (pd != null) {
			pd.write();
		}
	}
	
	static public ProcedureDoc create (int variantLevel, boolean largeAnimal) {
		
		String tier = "";
		String id = "";
		String name = "MPL - ";
		String size = "Standard - ";
		double totalPrice = 0;

		int rehabCount = 0;
		
		ProcedureDoc proc = new ProcedureDoc ();

		if (!largeAnimal) {
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_113";
				totalPrice = ProcedurePrices.B_MPL_STD_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_114";
				rehabCount = 4;
				totalPrice = ProcedurePrices.B_MPL_STD_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_115";
				rehabCount = 8;
				totalPrice = ProcedurePrices.B_MPL_STD_GOLD;
			}
			proc.addVariant(0, "GMV_PROC_BS_113");
			proc.addVariant(1, "GMV_PROC_BS_114");
			proc.addVariant(2, "GMV_PROC_BS_115");
		} else {
			size = "Large - ";
			if (variantLevel == 0) {
				tier = "Bronze";
				id = "GMV_PROC_BS_116";
				totalPrice = ProcedurePrices.B_MPL_LARGE_BRONZE;
			} else if (variantLevel == 1) {
				tier = "Silver";
				id = "GMV_PROC_BS_117";
				rehabCount = 4;
				totalPrice = ProcedurePrices.B_MPL_LARGE_SILVER;
			} else if (variantLevel == 2) {
				tier = "Gold";
				id = "GMV_PROC_BS_118";
				rehabCount = 8;
				totalPrice = ProcedurePrices.B_MPL_LARGE_GOLD;
			}
			proc.addVariant(0, "GMV_PROC_BS_116");
			proc.addVariant(1, "GMV_PROC_BS_117");
			proc.addVariant(2, "GMV_PROC_BS_118");
		}
		name = name + size + tier;
		
		proc.setID (id);
		proc.setName (name);
		proc.setType("Surgical Bundle");
		proc.addApplicability("all", "", 0, 100, 0, 1000);
		proc.setVariantLevel(variantLevel);
		proc.setVariantsAvailable(true);
		proc.setCategory("Fill Payment Category Here");
		proc.setDescription("MPL, Medial Patella Luxation is a condition in which the patella (knee-cap) no longer glides within its natural groove (sulcus) in the femur, the upper bone of the knee joint.");
		proc.setReason("Sick");
		proc.setProcedureTime(24);
		proc.setResultsTime(1680);
		proc.addAltCode("Provider 1", "MPL_P1");
		proc.addAltCode("Provider 2", "MPL_P2");
		proc.addAltCode("Provider 3", "MPL_P3");
		proc.addAltCode("Provider 4", "MPL_P4");
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
		
		proc.addInclusion("GMV_PROC_AI_302", true, true, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.RADIOGRAPH/totalPrice);

		if (!largeAnimal) {
			proc.addInclusion("GMV_PROC_AS_505", true, true, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.MPL_STD/totalPrice);
			proc.setCore("GMV_PROC_AS_505");
		} else {
			proc.addInclusion("GMV_PROC_AS_506", true, true, "x", -1, true);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.MPL_LARGE/totalPrice);
			proc.setCore("GMV_PROC_AS_506");
		}

		proc.addInclusion("GMV_PROC_AC_102", true, true, "x + 42", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);

		proc.addInclusion("GMV_PROC_AI_301", true, true, "x + 42", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SEDATION_FOR_RADIOGRAPH/totalPrice);

		proc.addInclusion("GMV_PROC_AI_302", true, true, "x + 42", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.RADIOGRAPH/totalPrice);

		proc.addInclusion("GMV_PROC_AC_102", true, true, "x + 70", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SURGERY_CONSULT/totalPrice);

		proc.addInclusion("GMV_PROC_AI_301", true, true, "x + 70", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SEDATION_FOR_RADIOGRAPH/totalPrice);

		proc.addInclusion("GMV_PROC_AI_302", true, true, "x + 70", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.RADIOGRAPH/totalPrice);

		proc.addInclusion("GMV_PROC_AH_701", true, false, "TBD", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.REHABILITATION/totalPrice);

		for (int i = 0; i < rehabCount; i++) {
			proc.addInclusion("GMV_PROC_AH_701", false, false, "TBD", -1, false);
			proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.REHABILITATION/totalPrice);
		}
		
		return proc;
	}
}
