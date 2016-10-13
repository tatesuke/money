package com.tatesuke.money.source;

import static com.codeborne.selenide.Selenide.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

public class UfjSource implements ISource {

	private static final DecimalFormat DF = new DecimalFormat("###,###円");

	private int balance;
	private String idUfj;
	private String passUfj;
	private String name;

	public UfjSource(String idUfj, String passUfj, String name) {
		this.idUfj = idUfj;
		this.passUfj = passUfj;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
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

		while ($("[name=informationShousaiActionForm]").exists()) {
			$("[name=hyouzi]").click();
			$("[name=top]").click();
		}

		$("#list img").click();

		String amountStr = getAmountElement().innerText();
		balance = parse(amountStr);

		$(By.linkText("ログアウト")).click();
		$(By.linkText("閉じる")).click();

		webDriver.switchTo().window(parentHander);

		return balance;
	}

	private SelenideElement getAmountElement() {
		Iterator<SelenideElement> it = $$("span.unit").iterator();
		while (it.hasNext()) {
			SelenideElement nameElem = it.next();
			if (name.equals(nameElem.innerText())) {
				return nameElem.parent().find(".unit");
			}
		}
		return null;
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
