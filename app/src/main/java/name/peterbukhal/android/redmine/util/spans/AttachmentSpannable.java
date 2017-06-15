package name.peterbukhal.android.redmine.util.spans;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import name.peterbukhal.android.redmine.service.redmine.model.Attachment;

/**
 * Created by petronic on 16.06.17.
 */
public class AttachmentSpannable extends SpannableString {

    public AttachmentSpannable(Context context, Attachment attachment) {
        super(attachment.getName());

        setSpan(new AttachmentSpan(context, attachment), 0, length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSpan(new StyleSpan(Typeface.BOLD), 0, length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
