package com.github.reviversmc.modget.restservice.api;

import com.github.reviversmc.modget.restservice.beans.RestModPackage;
import com.github.reviversmc.modget.restservice.service.ModService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1")
public class ModController {
	private final ModService modService;

	public ModController() {
		this.modService = new ModService();
	}


	@GetMapping("package/{packageId}")
	public RestModPackage getPackage(@PathVariable String packageId) {
		return modService.getPackage(packageId);
	}

}
