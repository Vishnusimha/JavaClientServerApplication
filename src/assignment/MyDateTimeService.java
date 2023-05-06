
package assignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateTimeService {
	private Calendar calendar;

	public String getDateAndTime() {
		this.calendar = Calendar.getInstance();
		Date d = this.calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(d);

		return dateStr;
	}
}