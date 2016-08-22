package com.tatesuke;

import static com.codeborne.selenide.Selenide.*;

import java.text.DecimalFormat;
import java.text.ParseException;

public class RakutenAccount implements IAccount {

	private static final DecimalFormat DF = new DecimalFormat("###,###円");

	private String idRakuten;
	private String passRakuten;

	public RakutenAccount(String idRakuten, String passRakuten) {
		this.idRakuten = idRakuten;
		this.passRakuten = passRakuten;
	}

	@Override
	public String getName() {
		return "楽天カード";
	}

	@Override
	public int getBalance() {
		open("https://www.rakuten-card.co.jp/e-navi/");

		$("#u").val(idRakuten);
		$("#p").val(passRakuten).pressEnter();

		open("https://www.rakuten-card.co.jp/e-navi/members/credit-limit/balance/index.xhtml?l-id=enavi_mtop_usemenu_balanceinquiry_pc_b");

		String shoppingAmountStr = $("#wrap tfoot td[align=right]")
				.innerText();
		String cassingAmountStr = $$("#wrap tfoot td[align=right]").last()
				.innerHtml();

		int shoppingAmount = parse(shoppingAmountStr);
		int cassingAmount = parse(cassingAmountStr);

		return -(shoppingAmount + cassingAmount);
	}

	private int parse(String amountStr) {
		try {
			return DF.parse(amountStr).intValue();
		} catch (ParseException e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

}
