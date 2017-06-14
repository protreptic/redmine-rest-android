package name.peterbukhal.android.redmine.util.spans;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import name.peterbukhal.android.redmine.service.redmine.model.Author;

public class UserSpannable extends SpannableString {

    public UserSpannable(AppCompatActivity activity, Author author) {
        super(author.getName());

        setSpan(new UserSpan(activity.getSupportFragmentManager(), author), 0, length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSpan(new StyleSpan(Typeface.BOLD), 0, length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
