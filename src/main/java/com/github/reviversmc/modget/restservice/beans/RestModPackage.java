package com.github.reviversmc.modget.restservice.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.reviversmc.modget.manifests.spec4.api.data.common.ModLoader;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersion;
import com.github.reviversmc.modget.manifests.spec4.impl.data.manifest.main.BasicModManifest;

/*
 * This class unifies a ModPackage and a ModManifest.
 * The only reason why they're separated in the Manifest API is because
 * each Package could contain multiple Manifests (from different repos),
 * but here we only have one repo anyway, so it makes no sense to differentiate them.
 * Also includes data from the parent LookupTableEntry.
 */
public class RestModPackage extends BasicModManifest {
	private String packageId;
	private String publisher;
	private String modId;
	private List<ModLoader> loaders;
	private List<String> alternativeNames;
	private List<String> tags;
	private List<RestModVersion> featuredVersions;

	public RestModPackage(String packageId) {
		super(null, null);
		
		loaders = new ArrayList<>(2);
		alternativeNames = new ArrayList<>(4);
		tags = new ArrayList<>(5);
		featuredVersions = new ArrayList<>(4);
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


	public List<String> getAlternativeNames() {
		return alternativeNames;
	}

	public void setAlternativeNames(List<String> alternativeNames) {
        if (alternativeNames == null) {
            this.alternativeNames.clear();
            return;
        }
		this.alternativeNames = alternativeNames;
	}


	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
        if (tags == null) {
            this.tags.clear();
            return;
        }
		this.tags = tags;
	}

	public List<RestModVersion> getFeaturedVersions() {
		return featuredVersions;
	}

	public void setFeaturedVersions(List<RestModVersion> featuredVersions) {
		if (featuredVersions == null) {
			this.featuredVersions.clear();
			return;
		}
		this.featuredVersions = featuredVersions;
	}
	
}
