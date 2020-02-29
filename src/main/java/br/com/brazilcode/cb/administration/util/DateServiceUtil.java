package br.com.brazilcode.cb.administration.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

/**
 * Classe utilitária para datas.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 29 de fev de 2020 18:05:52
 * @version 1.0
 */
@Service
public class DateServiceUtil {

	/**
	 * Método responsável por incrementar a data e hora atual.
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
