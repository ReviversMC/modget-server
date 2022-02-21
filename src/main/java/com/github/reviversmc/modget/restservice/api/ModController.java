package com.github.reviversmc.modget.restservice.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.github.reviversmc.modget.manifests.spec4.api.data.manifest.version.ModVersionVariant;
import com.github.reviversmc.modget.restservice.beans.RestModPackage;
import com.github.reviversmc.modget.restservice.beans.RestModVersion;
import com.github.reviversmc.modget.restservice.service.ModService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1")
public class ModController {
	private final ModService modService;

	public ModController(ModService modService) {
		this.modService = modService;
	}

	@GetMapping("package/{packageId}")
	public RestModPackage getPackage(@PathVariable String packageId) {
		return modService.getPackage(packageId);
	}

	@GetMapping("package/{packageId}/versions")
	public List<RestModVersion> getVersions(@PathVariable String packageId) throws IllegalAccessException, InvocationTargetException {
		List<RestModVersion> restModVersions = new ArrayList<>();
		for (ModVersionVariant variant : modService.getModVersionVariantsForPackage(packageId)) {
			restModVersions.add(new RestModVersion() {{
				BeanUtils.copyProperties(this, variant);
				this.setVersion(variant.getParentVersion().getVersion());
			}});
		}
		return restModVersions;
	}

}
