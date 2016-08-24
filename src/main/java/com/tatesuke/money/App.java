package com.tatesuke.money;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.codeborne.selenide.WebDriverRunner;
import com.tatesuke.money.account.IAccount;
import com.tatesuke.money.account.JibunAccount;
import com.tatesuke.money.account.RakutenAccount;
import com.tatesuke.money.account.UcAccount;
import com.tatesuke.money.account.UfuAccount;

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
				new UfuAccount(idUfj, passUfj),
				new JibunAccount(idUfj, passUfj),
		};

		for (IAccount account : accounts) {
			account.loadBalance();
		}

		ZaimManager zm = new ZaimManager(idZaim, passZaim);
		for (IAccount account : accounts) {
			int zaimBalance = zm.getBalance(account);
			System.out.println(zaimBalance);
			if (account.getBalance() < zaimBalance) {
				int payment = zaimBalance - account.getBalance();
				zm.createPayment(account, payment);
			} else if (account.getBalance() > zaimBalance) {
				int income = account.getBalance() - zaimBalance;
				zm.createIncome(account, income);
			}
		}
	}

	private static void init(String profilePath) {
		FirefoxProfile profile = new FirefoxProfile(
				new File(profilePath));
		WebDriverRunner.setWebDriver(new FirefoxDriver(profile));
	}

}
