package com.tatesuke.money.destination;

import com.tatesuke.money.source.ISource;

public class ZaimDistination implements IDestination {

	private String idZaim;
	private String passZaim;

	public ZaimDistination(String idZaim, String passZaim) {
		this.idZaim = idZaim;
		this.passZaim = passZaim;
	}

	@Override
	public void write(ISource[] sources) {
		int balance = 0;
		for (ISource source : sources) {
			balance += source.getBalance();
		}
		System.out.println("総残高:" + balance);

		ZaimManager zm = new ZaimManager(idZaim, passZaim);
		int zaimBalance = zm.getBalance();
		System.out.println("ZAIM上の残高:" + zaimBalance);
		if (balance < zaimBalance) {
			int payment = zaimBalance - balance;
			System.out.println("ZAIMに支出を入力します:" + payment);
			zm.createPayment(payment);
		} else if (balance > zaimBalance) {
			int income = balance - zaimBalance;
			System.out.println("ZAIMに収入を入力します:" + income);
			zm.createIncome(income);
		}
		zm.logout();
	}

}
