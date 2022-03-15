package com.github.reviversmc.modget.restservice.beans;

import java.util.List;

public class SearchResponse {
	private List<RestModPackage> hits;
	private int offset;
	private int limit;
	private int totalHits;

	public SearchResponse(List<RestModPackage> hits, int offset, int limit, int totalHits) {
		this.hits = hits;
		this.offset = offset;
		this.limit = limit;
		this.totalHits = totalHits;
	}

	public List<RestModPackage> getHits() {
		return this.hits;
	}

	public void setHits(List<RestModPackage> hits) {
		this.hits = hits;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalHits() {
		return this.totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	
}
