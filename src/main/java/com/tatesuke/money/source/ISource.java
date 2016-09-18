package com.tatesuke.money.source;

public interface ISource {

	String getName();

	int loadBalance();
	int getBalance();
}
