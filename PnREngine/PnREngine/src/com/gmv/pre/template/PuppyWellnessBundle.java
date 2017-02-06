package com.gmv.pre.template;

import com.gmv.pre.definitions.RoleDefinitions;
import com.gmv.pre.structs.ProcedureDoc;

public class PuppyWellnessBundle {
	
	public static void createAndPublish () {
		ProcedureDoc proc = new ProcedureDoc ();
		proc.setID("GMV_PROC_BW_101");
		proc.setName("Puppy Wellness Bundle");
		proc.setType("Wellness Bundle");
		proc.addApplicability("all", "", 0, 100, 0, 1000);
		proc.setVariantLevel(0);
		proc.setVariantsAvailable(false);
		proc.setCategory("Fill Payment Category Here");
		proc.setDescription("Puppy Wellness Bundle (0 -1 years)");
		proc.setReason("Well");
		proc.setProcedureTime(1);
		proc.setResultsTime(8760);
		proc.addAltCode("Provider 1", "FHO_P1");
		proc.addAltCode("Provider 2", "FHO_P2");
		proc.addAltCode("Provider 3", "FHO_P3");
		proc.addAltCode("Provider 4", "FHO_P4");
		proc.setWhoCanDo(RoleDefinitions.ROLE_007);

		proc.addInclusion ("GMV_PROC_AC_101", true, false, "1", -1, true);
		proc.addInclusion ("GMV_PROC_AV_801", true, false, "1", -1, true); 
		proc.addInclusion ("GMV_PROC_AV_807", true, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_809", false, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_808", false, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_806", false, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_810", true, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_811", true, false, "1",  -1, true);
		proc.addInclusion ("GMV_PROC_AC_101", true, false, "70",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_801", true, false, "70",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_806", false, false, "70", -1, true);
		proc.addInclusion ("GMV_PROC_AC_101", true, false, "90", -1, true);
		proc.addInclusion ("GMV_PROC_AV_801", true, false, "90", -1, true);
		proc.addInclusion ("GMV_PROC_AV_807", true, false, "90", -1, true);
		proc.addInclusion ("GMV_PROC_AC_101", true, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AV_801", true, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AV_802", true, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AV_803", false, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AV_804", false, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AV_805", false, false, "112", -1, true);
		proc.addInclusion ("GMV_PROC_AC_101", true, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AV_803", false, false, "140",  -1, true);
		proc.addInclusion ("GMV_PROC_AV_804", false, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AV_805", false, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AC_103", false, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AC_102", false, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AL_201", false, false, "140", -1, true);
		proc.addInclusion ("GMV_PROC_AC_101", true, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_801", true, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_802", true, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_810", true, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_811", true, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_803", false, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_804", false, false, "365", -1, true);
		proc.addInclusion ("GMV_PROC_AV_808", false, false, "365", -1, true);
		
		proc.write();
	}
}
