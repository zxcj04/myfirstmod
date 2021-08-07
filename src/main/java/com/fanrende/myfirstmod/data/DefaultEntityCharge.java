package com.fanrende.myfirstmod.data;

public class DefaultEntityCharge implements IEntityCharge
{
	private int charge;

	@Override
	public void setCharge(int charge)
	{
		this.charge = charge;
	}

	@Override
	public int getCharge()
	{
		return charge;
	}
}
