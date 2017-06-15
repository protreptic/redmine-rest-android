package name.peterbukhal.android.redmine.util.spans;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Years;

public class DateSpannable extends SpannableString {

    private static String buildYearsString(int years) {
        final int divisor = years % 10;

        if (years >= 5 && years <= 20) {
            return years + " лет";
        } else {
            switch (divisor) {
                case 1:
                    return years + " год";
                case 2:
                case 3:
                case 4:
                    return years + " года";
                default:
                    return years + " лет";
            }
        }
    }

    private static String buildMonthsString(int months) {
        final int divisor = months % 10;

        if (months >= 5 && months <= 20) {
            return months + " месяцев";
        } else {
            switch (divisor) {
                case 1:
                    return months + " месяц";
                case 2:
                case 3:
                case 4:
                    return months + " месяца";
                default:
                    return months + " месяцев";
            }
        }
    }

    private static String buildDaysString(int days) {
        final int divisor = days % 10;

        if (days >= 5 && days <= 20) {
            return days + " дней";
        } else {
            switch (divisor) {
                case 1:
                    return days + " день";
                case 2:
                case 3:
                case 4:
                    return days + " дня";
                default:
                    return days + " дней";
            }
        }
    }

    private static String buildHoursString(int hours) {
        final int divisor = hours % 10;

        if (hours >= 5 && hours <= 20) {
            return hours + " часов";
        } else {
            switch (divisor) {
                case 1:
                    return hours + " час";
                case 2:
                case 3:
                case 4:
                    return hours + " часа";
                default:
                    return hours + " часов";
            }
        }
    }

    private static String buildMinutesString(int minutes) {
        final int divisor = minutes % 10;

        if (minutes >= 5 && minutes <= 20) {
            return minutes + " минут";
        } else {
            switch (divisor) {
                case 1:
                    return minutes + " минуту";
                case 2:
                case 3:
                case 4:
                    return minutes + " минуты";
                default:
                    return minutes + " минут";
            }
        }
    }

    private static String process(String dateString) {
        final DateTime now = DateTime.now();
        final DateTime date = DateTime.parse(dateString);

        final int years = Years.yearsBetween(date, now).getYears();

        if (years > 0) {
            return buildYearsString(years);
        }

        final int months = Months.monthsBetween(date, now).getMonths();

        if (months > 0) {
            return buildMonthsString(months);
        }

        final int days = Days.daysBetween(date, now).getDays();

        if (days > 0) {
            return buildDaysString(days);
        }

        final int hours = Hours.hoursBetween(date, now).getHours();

        if (hours > 0) {
            return buildHoursString(hours);
        }

        final int minutes = Minutes.minutesBetween(date, now).getMinutes();

        if (minutes > 0) {
            return buildMinutesString(minutes);
        }

        return "меньше минуты";
    }

    public DateSpannable(String date) {
        super(process(date) + " назад");

        setSpan(new DateSpan(""), 0, length() - 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSpan(new StyleSpan(Typeface.BOLD), 0, length() - 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
