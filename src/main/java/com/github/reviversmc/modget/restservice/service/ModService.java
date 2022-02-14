package com.github.reviversmc.modget.restservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.reviversmc.modget.library.util.search.ModSearcher;
import com.github.reviversmc.modget.library.util.search.SearchMode;
import com.github.reviversmc.modget.library.util.search.SearchModeBuilder;
import com.github.reviversmc.modget.manifests.spec4.api.data.ManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModManifest;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersion;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersionVariant;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.impl.data.BasicManifestRepository;
import com.github.reviversmc.modget.restservice.beans.RestModPackage;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
public class ModService {
	ManifestRepository repo = new BasicManifestRepository(0, "https://raw.githubusercontent.com/ReviversMC/modget-manifests");

	public ModService() {
		try {
			repo.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

	public RestModPackage getPackage(String packageId) {
        SearchMode searchMode = new SearchModeBuilder()
                .enablePackageIdSearch(true)
                .build();
		Pair<List<ModVersionVariant>, List<Exception>> versionVariantsFoundWithExceptions
                = ModSearcher.create().searchForMods(
                        Arrays.asList(repo),
                        packageId,
                        searchMode,
                        Optional.empty(),
                        Optional.empty());

        ModVersion modVersion = getFirstModVersionVariant(versionVariantsFoundWithExceptions).getParentVersion();
        ModManifest modManifest = modVersion.getParentManifest();
        ModPackage modPackage = modManifest.getParentPackage();

        return new RestModPackage(modPackage.getPackageId()) {{
            try {
                BeanUtils.copyProperties(this, modPackage);
                BeanUtils.copyProperties(this, modPackage.getManifests().get(0));
                
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }};
	}


    private ModVersionVariant getFirstModVersionVariant(
        Pair<List<ModVersionVariant>, List<Exception>> versionVariantsFoundWithExceptions
    ) {
        List<ModVersionVariant> versionVariantsFound = versionVariantsFoundWithExceptions.getLeft();

        for (ModVersionVariant modVersionVariant : versionVariantsFound) {
            return modVersionVariant;
        }
        return null;
    }
}
