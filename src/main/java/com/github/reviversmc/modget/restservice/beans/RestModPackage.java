package com.github.reviversmc.modget.restservice.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.reviversmc.modget.manifests.spec4.api.data.common.ModLoader;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersion;
import com.github.reviversmc.modget.manifests.spec4.impl.data.manifest.main.BasicModManifest;

public class RestModPackage extends BasicModManifest {
	private String packageId;
	private String publisher;
	private String modId;
	private List<ModLoader> loaders;

	public RestModPackage(String packageId) {
		super(null, null);
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
        if (packageId != null) {
            String[] packageIdParts = packageId.split("\\.");
            publisher = packageIdParts[0];
            modId = packageIdParts[1];
        }
	}


	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
        if (publisher != null && modId != null) {
		    packageId = String.format("%s.%s", publisher, modId);
        }
	}


	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
        if (publisher != null && modId != null) {
		    packageId = String.format("%s.%s", publisher, modId);
        }
	}


	public List<ModLoader> getLoaders() {
		return loaders;
	}

	public void setLoaders(List<ModLoader> loaders) {
        if (loaders == null) {
            this.loaders.clear();
            return;
        }
		this.loaders = loaders;
	}

	@Override
	@JsonIgnore
	public List<ModVersion> getVersions() {
		return super.getVersions();
	}
	
}
