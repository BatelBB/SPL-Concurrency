package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.passiveObjects.Attack;

public class Input {
	private Attack[] attacks;
	Long R2D2;
	Long Lando;
	int Ewoks;

	public int getEwoks() {

		return Ewoks;
	}

	public void setEwoks(int ewoks) {

		Ewoks = ewoks;
	}

	public long getLando() {
		return Lando;
	}

	public void setLando(Long lando) {
		Lando = lando;
	}

	public Long getR2D2() {
		return R2D2;
	}

	public void setR2D2(Long r2d2) {
		R2D2 = r2d2;
	}

	public Attack[] getAttacks() {
		return attacks;
	}

	public void setAttacks(Attack[] attacks) {
		this.attacks = attacks;
	}
}
