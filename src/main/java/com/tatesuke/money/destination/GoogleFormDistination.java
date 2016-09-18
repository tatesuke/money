package com.tatesuke.money.destination;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.tatesuke.money.source.ISource;

public class GoogleFormDistination implements IDestination {

	private String url;

	public GoogleFormDistination(String urlGoogleForm) {
		this.url = urlGoogleForm;
	}

	@Override
	public void write(ISource[] sources) {
		open(url);

		for (ISource source : sources) {
			String name = source.getName();
			SelenideElement inputEmenet = getInputElement(name);
			inputEmenet.val(Integer.toString(source.getBalance()));
		}

		getSubmitButton().click();

		$("body").shouldHave(Condition.text("回答を記録しました。"));
	}

	private SelenideElement getInputElement(String name) {
		return $("input[aria-label=\"" + name + "\"]");
	}

	private SelenideElement getSubmitButton() {
		ElementsCollection spans = $$("span");
		for (SelenideElement span : spans) {
			if (span.innerText().equals("送信")) {
				return span;
			}
		}
		throw new RuntimeException("送信ボタンが見つからない");
	}

}
