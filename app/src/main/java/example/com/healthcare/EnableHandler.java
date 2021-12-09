package example.com.healthcare;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;

public class EnableHandler extends Handler
{
    AlertDialog dialog;

    public EnableHandler(AlertDialog dialog)
    {
        this.dialog = dialog;
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        switch(msg.what)
        {
            case 0:
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false); // OK 버튼 비활성화
                break;
            case 1:
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true); // OK 버튼 활성화
                break;
            default:
        }
    }
}
