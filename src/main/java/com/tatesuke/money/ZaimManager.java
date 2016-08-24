package com.tatesuke.money;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class ZaimManager {

	private static final String ACCOUNT_NAME = "主要金融機関";
	private static final DecimalFormat DF = new DecimalFormat("'¥'###,###");

	public ZaimManager(String idZaim, String passZaim) {
		open("https://auth.zaim.net/");
		$("#UserEmail").val(idZaim);
		$("#UserPassword").val(passZaim).pressEnter();
	}

	public void createPayment(int payment) {
		open("https://zaim.net/money/new");
		$("#receipt_items__name").val("自動入力");
		$("#receipt_items__payment_genre").sendKeys("1505"); //未分類のコード
		$("#receipt_items__payment_genre").pressTab();		// tabを押さないと内部的に反映されないらしい
		$("#receipt_items__amount").val(Integer.toString(payment));// sendKey + 次行のクリックでないと反映されない
		$(".calculator-ctrl.calculator-use").click();		// ポップアップで出てくる電卓の確定ボタンをクリックする
		$("#receipt_from_account_id").selectOption(ACCOUNT_NAME);
		$("#receipt_items__comment").val("自動入力").pressEnter();
		$("body").shouldHave(text("入力しました"));
	}

	public void createIncome(int income) {
		open("https://zaim.net/money/new");
		$(By.linkText("収入")).click();

		$("#income_amount").val(Integer.toString(income));
		$("#income_category_id").selectOption("その他");
		$("#income_to_account_id").selectOption(ACCOUNT_NAME);
		$("#income_comment").val("自動入力").pressEnter();
		$("body").shouldHave(text("入力しました"));
	}

	public int getBalance() {
		open("https://zaim.net/home");

		SelenideElement elem = findAccountElement();
		if (elem == null) {
			throw new RuntimeException("Zaimに口座が見つかりません:" + ACCOUNT_NAME);
		}

		String value = elem.find(".account-name .value").text();
		if (value.equals("-")) {
			return 0;
		}
		try {
			return DF.parse(value).intValue();
		} catch (ParseException e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

	private SelenideElement findAccountElement() {
		$("#list-accounts .account-name .name").shouldBe(appear);
		ElementsCollection ec = $$("#list-accounts .account-name .name");
		for (SelenideElement e : ec) {
			String accountName = e.text();
			if (ACCOUNT_NAME.equals(accountName)) {
				return e.parent().parent().parent();
			}
		}
		return null;
	}

	public void logout() {
		$(By.linkText("設定")).click();
		$(By.linkText("ログアウト")).click();
	}

}
