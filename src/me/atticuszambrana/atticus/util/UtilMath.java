package me.atticuszambrana.atticus.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UtilMath {
	
	/*
	 * Taken from Mineplex Source code from 2015
	 */
	
	public static double trim(int degree, double d) 
	{
		String format = "#.#";
		
		for (int i=1 ; i<degree ; i++)
			format += "#";

		DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
		DecimalFormat twoDForm = new DecimalFormat(format, symb);
		return Double.valueOf(twoDForm.format(d));
	}
}
