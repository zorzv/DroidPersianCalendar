package com.byagowi.persiancalendar;

import static com.byagowi.persiancalendar.CalendarUtils.dayTitleSummary;
import static com.byagowi.persiancalendar.CalendarUtils.preferredDigits;
import static com.byagowi.persiancalendar.CalendarUtils.quickToast;

import java.util.Calendar;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;

/**
 * Days events listener
 * 
 * @author ebraminio
 * 
 */
class ClickDayListener implements View.OnClickListener, View.OnLongClickListener {
	private final String holidayTitle;
	private final PersianDate persianDate;
	private final CalendarActivity calendarAcitivity;

	ClickDayListener(String holidayTitle, PersianDate persianDate,
			CalendarActivity calendarAcitivity) {
		this.holidayTitle = holidayTitle;
		this.persianDate = persianDate;
		this.calendarAcitivity = calendarAcitivity;
	}

	@Override
	public void onClick(View v) {
		if (holidayTitle != null) { 
			quickToast(holidayTitle, v.getContext());
		}
		calendarAcitivity.setFocusedDay(persianDate);
	}

	@Override
	public boolean onLongClick(View v) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			try {
				addEventOnCalendar(preferredDigits(v.getContext()), v.getContext());
			} catch (Exception e) {
				// Ignore it for now
				// I guess it will occur on CyanogenMod phones
				// where Google extra things is not installed
			}
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void addEventOnCalendar(char[] digits, Context context) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(Events.CONTENT_URI);
		CivilDate civil = DateConverter.persianToCivil(persianDate);
		intent.putExtra(Events.DESCRIPTION,
				dayTitleSummary(civil, digits));
		Calendar time = Calendar.getInstance();
		time.set(civil.getYear(), civil.getMonth() - 1,
				civil.getDayOfMonth());
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				time.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
		context.startActivity(intent);
	}
}