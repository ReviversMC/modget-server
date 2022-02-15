package com.github.reviversmc.modget.restservice.beans;

import com.github.reviversmc.modget.manifests.spec4.impl.data.manifest.version.BasicModVersionVariant;

/*
 * This class unifies a ModVersion and a ModVersionVariant.
 */
public class RestModVersion extends BasicModVersionVariant {
	private String version;

	public RestModVersion() {
		super(null);
	}


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
