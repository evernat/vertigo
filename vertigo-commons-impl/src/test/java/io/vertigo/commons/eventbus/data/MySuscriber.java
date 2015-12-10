package io.vertigo.commons.eventbus.data;

import io.vertigo.commons.eventbus.EventSuscriber;

public class MySuscriber {
	private int count = 0;
	private int redCount = 0;
	private int blueCount = 0;

	@EventSuscriber
	public void onAllColor(final ColorEvent colorEvent) {
		count++;
	}

	@EventSuscriber
	public void onRedColor(final RedColorEvent colorEvent) {
		redCount++;
	}

	@EventSuscriber
	public void onBlueColor(final BlueColorEvent colorEvent) {
		blueCount++;
	}

	public int getCount() {
		return count;
	}

	public int getRedCount() {
		return redCount;
	}

	public int getBlueCount() {
		return blueCount;
	}

}