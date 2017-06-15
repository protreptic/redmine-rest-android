package name.peterbukhal.android.redmine.util.spans;

import android.annotation.SuppressLint;
import android.view.View;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 15.06.2017.
 */
@SuppressLint("ParcelCreator")
public final class DateSpan extends RedmineUrlSpan {

    public DateSpan(String date) {
        super(date);
    }

    @Override
    public void onClick(View widget) {

    }

}
