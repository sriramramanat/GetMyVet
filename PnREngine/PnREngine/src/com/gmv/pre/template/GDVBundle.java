package com.gmv.pre.template;

import com.gmv.pre.definitions.ProcedurePrices;
import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class GDVBundle {

	static public void createAndPublish (int variantLevel) {
		ProcedureDoc pd = create (variantLevel);
		if (pd != null) {
			pd.write();
		}
	}
	
	static public ProcedureDoc create (int variantLevel) {
		
		String tier = "";
		String id = "";
		String name = "GDV - ";
		double totalPrice = 0;

		ProcedureDoc proc = new ProcedureDoc ();

		if (variantLevel == 0) {
			tier = "Bronze";
			id = "GMV_PROC_BS_125";
			totalPrice = ProcedurePrices.B_GDV_BRONZE;
		} else if (variantLevel == 1) {
			tier = "Silver";
			id = "GMV_PROC_BS_126";
			totalPrice = ProcedurePrices.B_GDV_SILVER;
		} else if (variantLevel == 2) {
			tier = "Gold";
			id = "GMV_PROC_BS_127";
			totalPrice = ProcedurePrices.B_GDV_GOLD;
		}
		
		proc.addVariant(0, "GMV_PROC_BS_125");
		proc.addVariant(1, "GMV_PROC_BS_126");
		proc.addVariant(2, "GMV_PROC_BS_127");

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
		proc.addAltCode("Provider 1", "GDV_P1");
		proc.addAltCode("Provider 2", "GDV_P2");
		proc.addAltCode("Provider 3", "GDV_P3");
		proc.addAltCode("Provider 4", "GDV_P4");
		proc.setWhoCanDo(RoleDefinitions.ROLE_007);
		
		int stepNumber = 1;
		
		proc.addInclusion ("GMV_PROC_AC_103", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.URGENT_CARE_CONSULT/totalPrice);

		proc.addInclusion ("GMV_PROC_AL_201", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.COMPLETE_BLOODWORK/totalPrice);

		proc.addInclusion ("GMV_PROC_AL_202", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.URINALYSIS_AND_UPC/totalPrice);

		proc.addInclusion ("GMV_PROC_AL_203", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.PT_PTT/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_301", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.SEDATION_FOR_RADIOGRAPH/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_303", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.THORACIC_RADIOGRAPH/totalPrice);

		proc.addInclusion ("GMV_PROC_AI_304", true, false, "1", -1, false);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.ABDOMINAL_RADIOGRAPH/totalPrice);

		proc.addInclusion ("GMV_PROC_AS_507", true, false, "x", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.GDV/totalPrice);

		proc.addInclusion ("GMV_PROC_AR_601", false, false, "x", -1, true);
		proc.setPercentOfTotalPrice(stepNumber++, ProcedurePrices.GDV_RECOVERY/totalPrice);

		proc.addInclusion ("GMV_PROC_AR_602", false, false, "x", -1, false);

		return proc;
	}
}
