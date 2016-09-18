package com.tatesuke.money.source;

import static com.codeborne.selenide.Selenide.*;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;

public class JibunSource implements ISource {

	private static final DecimalFormat DF = new DecimalFormat("###,###円");

	private int balance;
	private String idUfj;
	private String passUfj;

	public JibunSource(String idUfj, String passUfj) {
		this.idUfj = idUfj;
		this.passUfj = passUfj;
	}

	@Override
	public String getName() {
		return "じぶん銀行";
	}

	@Override
	public int loadBalance() {
		open("http://www.bk.mufg.jp/");
		$("#k-login").find("a").click();
		WebDriver webDriver = WebDriverRunner.getWebDriver();

		String parentHander = webDriver.getWindowHandle();
		for (String windowhandle : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(windowhandle);
		}

		$("#account_id").waitUntil(Condition.appear, 30000); //画面表示に時間がかかることがあるので、しばらく待つ
		$("#account_id").val(idUfj);
		$("#ib_password").val(passUfj).pressEnter();

		while ($("name[informationShousaiActionForm]").exists()) {
			$("[name=hyouzi]").click();
			$("[name=top]").click();
		}

		$("#list img").click();
		$(By.linkText("じぶん銀行口座残高照会")).click();
		String amountStr = $$("td.number").last().text();
		balance = parse(amountStr);

		$(By.linkText("ログアウト")).click();
		$(By.linkText("閉じる")).click();

		webDriver.switchTo().window(parentHander);

		return balance;
	}

	private int parse(String amountStr) {
		try {
			return DF.parse(amountStr).intValue();
		} catch (ParseException e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

	@Override
	public int getBalance() {
		return balance;
	}

}
