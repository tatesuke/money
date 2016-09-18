package com.tatesuke.money.destination;

import com.tatesuke.money.source.ISource;

public interface IDestination {

	void write(ISource[] sources);

}
