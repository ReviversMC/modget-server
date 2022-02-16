package com.github.reviversmc.modget.restservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.reviversmc.modget.library.exception.NoCompatibleVersionException;
import com.github.reviversmc.modget.library.util.ModVersionVariantUtils;
import com.github.reviversmc.modget.library.util.search.ModSearcher;
import com.github.reviversmc.modget.library.util.search.SearchMode;
import com.github.reviversmc.modget.library.util.search.SearchModeBuilder;
import com.github.reviversmc.modget.manifests.spec4.api.data.ManifestRepository;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.main.ModManifest;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersion;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersionVariant;
import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ReleaseChannel;
import com.github.reviversmc.modget.manifests.spec4.api.data.mod.ModPackage;
import com.github.reviversmc.modget.manifests.spec4.impl.data.BasicManifestRepository;
import com.github.reviversmc.modget.restservice.beans.RestModPackage;
import com.github.reviversmc.modget.restservice.beans.RestModVersion;

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
    

	public List<ModVersionVariant> getModVersionVariantsForPackage(String packageId) {
        SearchMode searchMode = new SearchModeBuilder()
                .enablePackageIdSearch(true)
                .build();
		Pair<List<ModVersionVariant>, List<Exception>> versionVariantsAndExceptions
                = ModSearcher.create().searchForMods(
                        Arrays.asList(repo),
                        packageId,
                        searchMode,
                        Optional.empty(),
                        Optional.empty());

        List<ModVersionVariant> modVersionVariants = new ArrayList<>();
        // We can do the following since we only have only one repo, so all versions / version variants
        // link back to the same parent objects anyway
        for (ModVersionVariant variant : extractModVersionVariants(versionVariantsAndExceptions)) {
            for (ModVersion version : variant.getParentVersion().getParentManifest().getVersions()) {
                try {
                    modVersionVariants.add(ModVersionVariantUtils.create().getLatestCompatibleVersionVariant(
                            version.getVariants(), Optional.empty(), Optional.empty()));
                } catch (NoCompatibleVersionException ignored) {}
            }
        }
        return modVersionVariants;
	}


	public RestModPackage getPackage(String packageId) {
        List<ModVersionVariant> modVersionVariants = getModVersionVariantsForPackage(packageId);
        ModManifest modManifest = modVersionVariants.get(0).getParentVersion().getParentManifest();
        ModPackage modPackage = modManifest.getParentPackage();

        return new RestModPackage(modPackage.getPackageId()) {{
            // Copy package, lookup table and manifest info
            try {
                BeanUtils.copyProperties(this, modPackage);
                BeanUtils.copyProperties(this, modManifest.getParentLookupTableEntry());
                BeanUtils.copyProperties(this, modPackage.getManifests().get(0));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            // -------- Generate featured versions --------
            List<ModVersionVariant> featuredVersions = new ArrayList<>(4);
            List<String> availableMinecraftVersions = new ArrayList<>(4);

            // Get all supported Minecraft versions, add one version variant
            // to the featured list already
            for (ModVersionVariant variant : modVersionVariants) {
                for (String minecraftVersion : variant.getMinecraftVersions()) {
                    if (!availableMinecraftVersions.contains(minecraftVersion)) {
                        availableMinecraftVersions.add(minecraftVersion);
                    }
                }
            }
            // Get the latest (preferably non-alpha) version variant for each MC version.
            // Versions are listed newest-to-oldest, so we can just take the first ones!
            for (String minecraftVersion : availableMinecraftVersions) {
                ModVersionVariant latestAlpha = null;
                boolean nonAlphaFound = false;

                for (ModVersionVariant variant : modVersionVariants) {
                    if (!variant.getMinecraftVersions().contains(minecraftVersion)) {
                        continue;
                    }

                    if (variant.getChannel().equals(ReleaseChannel.ALPHA)) {
                        if (latestAlpha == null) {
                            latestAlpha = variant;
                        }
                    } else {
                        featuredVersions.add(variant);
                        nonAlphaFound = true;
                        break;
                    }
                }
                // No beta or full release has been found, so we have to add an alpha
                if (nonAlphaFound == false) {
                    featuredVersions.add(latestAlpha);
                }
            }
            // Remove duplicates
            List<RestModVersion> deduplicatedFeaturedVersions = new ArrayList<>();
            List<ModVersionVariant> alreadyAddedModVersionVariants = new ArrayList<>();
            for (String minecraftVersion : availableMinecraftVersions) {
                for (ModVersionVariant variant : featuredVersions) {
                    if (!alreadyAddedModVersionVariants.contains(variant)) {
                        deduplicatedFeaturedVersions.add(new RestModVersion() {{
                            setVersion(variant.getParentVersion().getVersion());
                            try {
                                BeanUtils.copyProperties(this, variant);
                            } catch (Exception ignored) {}
                        }});
                        alreadyAddedModVersionVariants.add(variant);
                    }
                }
            }
            this.setFeaturedVersions(deduplicatedFeaturedVersions);
        }};
    }


    private List<ModVersionVariant> extractModVersionVariants(
        Pair<List<ModVersionVariant>, List<Exception>> versionVariantsAndExceptions
    ) {
        List<ModVersionVariant> versionVariants = versionVariantsAndExceptions.getLeft();
        versionVariants.removeIf(variant -> variant == null);
        return versionVariants;
    }
}
