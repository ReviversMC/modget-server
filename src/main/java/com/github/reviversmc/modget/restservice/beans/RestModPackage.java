package com.github.reviversmc.modget.restservice.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.reviversmc.modget.manifests.spec4.impl.data.mod.BasicModPackage;

public class RestModPackage extends BasicModPackage {

	public RestModPackage(String packageId) {
		super(packageId);
	}

	@Override
	@JsonIgnore
	public String getVersion() {
		return super.getVersion();
	}
	
}
