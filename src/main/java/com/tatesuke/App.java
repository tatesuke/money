package com.tatesuke;

import java.awt.AWTException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.codeborne.selenide.WebDriverRunner;

public class App {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");

	public static void main(String[] args) throws AWTException {
		String profilePath = args[0];
		String idUfj = args[1];
		String passUfj = args[2];
		String idRakuten = args[3];
		String passRakuten = args[4];
		String idUc = args[5];
		String passUc = args[6];

		FirefoxProfile profile = new FirefoxProfile(
				new File(profilePath));
		WebDriverRunner.setWebDriver(new FirefoxDriver(profile));

		IAccount[] accounts = {
				new UcAccount(idUc, passUc),
				new RakutenAccount(idRakuten, passRakuten),
				new UfuAccount(idUfj, passUfj),
				new JibunAccount(idUfj, passUfj),
		};

		int sum = 0;
		for (IAccount account : accounts) {
			int balance = account.getBalance();
			sum += balance;
//
//			System.out.print(account.getName() + "\t");
//			System.out.println(balance);
		}

		System.out.println(SDF.format(new Date()) + ", " + sum);
	}

}
