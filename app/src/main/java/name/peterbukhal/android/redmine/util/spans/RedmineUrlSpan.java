package name.peterbukhal.android.redmine.util.spans;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;

@SuppressLint("ParcelCreator")
public class RedmineUrlSpan extends URLSpan {

    public RedmineUrlSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLUE);
        ds.setUnderlineText(false);
    }

}
