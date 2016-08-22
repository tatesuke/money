package com.tatesuke;

import static com.codeborne.selenide.Selenide.*;

import java.text.DecimalFormat;
import java.text.ParseException;

import com.codeborne.selenide.Condition;

public class UcAccount implements IAccount {

	private static final DecimalFormat DF_MAX = new DecimalFormat("###,### 万円");
	private static final DecimalFormat DF_AMMOUNT = new DecimalFormat("###,### 円");

	private String idUc;
	private String passUc;

	public UcAccount(String idUc, String passUc) {
		this.idUc = idUc;
		this.passUc = passUc;
	}

	@Override
	public String getName() {
		return "UCカード";
	}

	@Override
	public int getBalance() {
		open("https://api.saisoncard.co.jp/auth/screen/atu/authorize?response_type=code&client_id=ZC002&state=&scope=openid");

		if ($("#capy-captcha").exists()) {
			$("#login_id").val(idUc);
			$("#password").val(passUc);

			$("#capy-captcha").waitWhile(Condition.exist, 60 * 1000);
		} else {
			$("#password").val(passUc).pressEnter();
		}

		if ($("#fade-box").is(Condition.visible)) {
			$("input[value=閉じる]").click();
		}

		$("[alt=ご利用可能額照会]").click();
		String shoppingMaxStr = $$(".section01 td").get(4).text();
		String shoppingAmountStr = $$(".section01 td").get(5).text();
		int shoppingMax = parseMax(shoppingMaxStr);
		int shoppingAmmount = parseAmount(shoppingAmountStr);

		$("[name=forward_installment]").click();
		String cashMaxStr = $$(".section01 td").get(4).text();
		String cashAmountStr = $$(".section01 td").get(5).text();
		int cashMax = parseMax(cashMaxStr);
		int cashAmount  = parseAmount(cashAmountStr);


		return -(shoppingMax - shoppingAmmount) - (cashMax - cashAmount) ;
	}

	private int parseMax(String amountStr) {
		try {
			return DF_MAX.parse(amountStr).intValue() * 10000;
		} catch (ParseException e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

	private int parseAmount(String amountStr) {
		try {
			return DF_AMMOUNT.parse(amountStr).intValue();
		} catch (ParseException e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

}
