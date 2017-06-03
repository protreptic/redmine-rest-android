package name.peterbukhal.android.redmine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import name.peterbukhal.android.redmine.R;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 21.11.2016.
 */
public final class ConfirmationDialog extends Dialog {

    public interface ConfirmationDialogListener {

        void onConfirmed();
        void onCancel();

    }

    private ConfirmationDialogListener mConfirmationDialogListener;

    public ConfirmationDialog(@NonNull final Context context) {
        super(context, R.style.AlertDialog);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.d_confirmation);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        TextView tvMessage = (TextView) findViewById(R.id.message);
        tvMessage.setText(R.string.message_000002);

        Button btConfirm = (Button) findViewById(R.id.confirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyConfirmed();
                dismiss();
            }

        });

        Button btCancel = (Button) findViewById(R.id.cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyCancel();
                dismiss();
            }

        });
    }

    public void setConfirmationDialogListener(@NonNull final ConfirmationDialogListener listener) {
        mConfirmationDialogListener = listener;
    }

    private void notifyConfirmed() {
        if (mConfirmationDialogListener != null) {
            mConfirmationDialogListener.onConfirmed();
        }
    }

    private void notifyCancel() {
        if (mConfirmationDialogListener != null) {
            mConfirmationDialogListener.onCancel();
        }
    }

}
