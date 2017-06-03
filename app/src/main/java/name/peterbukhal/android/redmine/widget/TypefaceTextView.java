package name.peterbukhal.android.redmine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import name.peterbukhal.android.redmine.R;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 20.12.2016.
 */
public class TypefaceTextView extends AppCompatTextView {

    public TypefaceTextView(Context context) {
        super(context);

        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        mTypeface = attributes.getString(R.styleable.TypefaceTextView_typeface);

        attributes.recycle();

        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        mTypeface = attributes.getString(R.styleable.TypefaceTextView_typeface);

        attributes.recycle();

        init();
    }

    private String mTypeface = "Roboto-Regular";

    private void init() {
        try {
            if (!TextUtils.isEmpty(mTypeface)) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto/" + mTypeface + ".ttf"));
            }
        } catch (Exception e) {
            Log.e("TypefaceTextView", "Typeface not found: " + mTypeface);
        }
    }

}
