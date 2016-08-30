package com.tatesuke.money;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.codeborne.selenide.WebDriverRunner;
import com.tatesuke.money.account.IAccount;
import com.tatesuke.money.account.JibunAccount;
import com.tatesuke.money.account.RakutenAccount;
import com.tatesuke.money.account.UcAccount;
import com.tatesuke.money.account.UfjAccount;

public class App {

	public static void main(String[] args) {
		String profilePath = args[0];
		String idZaim = args[1];
		String passZaim = args[2];
		String idUfj = args[3];
		String passUfj = args[4];
		String idRakuten = args[5];
		String passRakuten = args[6];
		String idUc = args[7];
		String passUc = args[8];

		init(profilePath);
		IAccount[] accounts = {
				new UcAccount(idUc, passUc),
				new RakutenAccount(idRakuten, passRakuten),
				new UfjAccount(idUfj, passUfj),
				new JibunAccount(idUfj, passUfj),
		};

		int balance = 0;
		for (IAccount account : accounts) {
			account.loadBalance();
			balance += account.getBalance();
			System.out.println(account.getName() + "の残高:" + account.getBalance());
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

	private static void init(String profilePath) {
		FirefoxProfile profile = new FirefoxProfile(
				new File(profilePath));
		WebDriverRunner.setWebDriver(new FirefoxDriver(profile));
	}

}
