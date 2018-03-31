package org.stth.siak.util;


import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;


@SuppressWarnings("serial")
public class StringToDouble implements Converter<String, Double> {
	@Override
	public Result<Double> convertToModel(String value, ValueContext context) {
		if (!value.isEmpty()) {
			
			try {
				Double d = Double.valueOf(value);
				return Result.ok(d);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return Result.error("Masukkan Angka");
			}
		}
		return Result.ok(null);
	}

	@Override
	public String convertToPresentation(Double value, ValueContext context) {
		return value==null?"":String.valueOf(value);
	}

}
