package name.peterbukhal.android.redmine.util.spans;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import name.peterbukhal.android.redmine.service.redmine.model.Attachment;

/**
 * Created by petronic on 16.06.17.
 */
@SuppressLint("ParcelCreator")
public final class AttachmentSpan extends RedmineUrlSpan {

    private Context context;
    private Attachment attachment;

    public AttachmentSpan(Context context, Attachment attachment) {
        super(attachment.getContentUrl());

        this.context = context;
        this.attachment = attachment;
    }

    @Override
    public void onClick(View widget) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Uri.parse(attachment.getContentUrl()), attachment.getContentType());

            if (context.getPackageManager().resolveActivity(intent, 0) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Данный тип файлов не поддерживается", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //
        }
    }

}
