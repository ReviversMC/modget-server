package com.github.reviversmc.modget.restservice.beans;

import com.github.reviversmc.modget.manifests.spec4.api.data.lookuptable.LookupTableEntry;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.impl.data.manifest.main.BasicModManifest;

public class RestModManifest extends BasicModManifest {


	public RestModManifest(ModPackage parentPackage, LookupTableEntry parentLookupTableEntry) {
		super(parentPackage, parentLookupTableEntry);
	}
	
}
