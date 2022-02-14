package com.github.reviversmc.modget.restservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import com.github.reviversmc.modget.library.util.ModSearcher;
import com.github.reviversmc.modget.manifests.spec4.api.data.ManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.common.NameUrlPair;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModManifest;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersion;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersionVariant;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.impl.data.BasicManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.impl.data.manifest.common.BasicNameUrlPair;
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

	public RestModPackage getMod(String id) {
		Pair<List<ModVersionVariant>, List<Exception>> versionVariantsFoundWithExceptions
                = ModSearcher.create().searchForCompatibleMods(Arrays.asList(repo), id, 9999, null, null);

        List<ModVersionVariant> versionVariantsFound = versionVariantsFoundWithExceptions.getLeft();


        for (ModVersionVariant modVersionVariant : versionVariantsFound) {
            ModVersion modVersion = modVersionVariant.getParentVersion();
            ModManifest modManifest = modVersion.getParentManifest();
            ModPackage modPackage = modManifest.getParentPackage();


            NameUrlPair downloadNameUrlPair = null;
            if (modVersionVariant.getDownloadPageUrls().getModrinth() != null) {
                downloadNameUrlPair = new BasicNameUrlPair("Modrinth", modVersionVariant.getDownloadPageUrls().getModrinth());
            } else if (modVersionVariant.getDownloadPageUrls().getCurseforge() != null) {
                downloadNameUrlPair = new BasicNameUrlPair("CurseForge", modVersionVariant.getDownloadPageUrls().getCurseforge());
            } else if (modVersionVariant.getDownloadPageUrls().getSourceControl() != null) {
                downloadNameUrlPair = new BasicNameUrlPair("Source Control", modVersionVariant.getDownloadPageUrls().getSourceControl());
            } else if (modVersionVariant.getDownloadPageUrls().getOther() != null) {
                for (NameUrlPair nameUrlPair : modVersionVariant.getDownloadPageUrls().getOther()) {
                    if (nameUrlPair.getUrl() != null) {
                        downloadNameUrlPair = new BasicNameUrlPair(nameUrlPair.getName(), nameUrlPair.getUrl());
                    }
                }
            }
			if (downloadNameUrlPair == null) {
				return null;
			}
            return new RestModPackage(modPackage.getPackageId()) {{
                try {
                    BeanUtils.copyProperties(this, modPackage);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }};
        }
		return null;
	}
}
