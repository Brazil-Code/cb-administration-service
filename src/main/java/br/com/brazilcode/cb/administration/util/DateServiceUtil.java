package br.com.brazilcode.cb.administration.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

/**
 * Utility class for data conversion.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since Apr 26, 2020 2:36:58 PM
 * @version 1.0
 */
@Service
public class DateServiceUtil {

	/**
	 * Method responsible for incrementing current timestamp.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param data
	 * @param field
	 * @param amount
	 * @return
	 */
	public Calendar getAndIncrementCalendar(Date data, int field, int amount) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		calendar.add(field, amount);
		return calendar;
	}

}
