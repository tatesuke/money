package com.tatesuke.money;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.codeborne.selenide.WebDriverRunner;
import com.tatesuke.money.destination.GoogleFormDistination;
import com.tatesuke.money.destination.IDestination;
import com.tatesuke.money.destination.ZaimDistination;
import com.tatesuke.money.source.ISource;
import com.tatesuke.money.source.JibunSource;
import com.tatesuke.money.source.RakutenSource;
import com.tatesuke.money.source.UcSource;
import com.tatesuke.money.source.UfjSource;

public class App {

	public static void main(String[] args) {
		String profilePath = args[0];
		String idZaim = args[1];
		String passZaim = args[2];
		String idUfj = args[3];
		String passUfj = args[4];
		String ufjAccount1 = args[5];
		String ufjAccount2 = args[6];
		String ufjAccount3 = args[7];
		String idRakuten = args[8];
		String passRakuten = args[9];
		String idUc = args[10];
		String passUc = args[11];
		String urlGoogleForm = args[12];

		ISource[] sources = {
				new UcSource(idUc, passUc),
				new RakutenSource(idRakuten, passRakuten),
				new UfjSource(idUfj, passUfj, ufjAccount1),
				new UfjSource(idUfj, passUfj, ufjAccount2),
				new UfjSource(idUfj, passUfj, ufjAccount3),
				new JibunSource(idUfj, passUfj),
		};
		IDestination[] destinations = {
				new ZaimDistination(idZaim, passZaim),
				new GoogleFormDistination(urlGoogleForm)
		};

		init(profilePath);
		for (ISource source : sources) {
			source.loadBalance();
			System.out.println(source.getName() + "の残高:" + source.getBalance());
		}
		for (IDestination destination : destinations) {
			destination.write(sources);
		}
	}

	private static void init(String profilePath) {
		FirefoxProfile profile = new FirefoxProfile(
				new File(profilePath));
		WebDriverRunner.setWebDriver(new FirefoxDriver(profile));
	}

}
