package cordova-plugin-dial;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.content.pm.PackageManager;
/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaDial extends CordovaPlugin {
 public static final int CALL_REQ_CODE = 0;
  public static final int PERMISSION_DENIED_ERROR = 20;
  public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;

  private CallbackContext callbackContext;        // The callback context from which we were invoked.
  private JSONArray executeArgs;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
            this.executeArgs = args;
        if (action.equals("dial")) {
            if (cordova.hasPermission(CALL_PHONE)) {
                   callPhone(executeArgs);
                 } else {
                   getCallPermission(CALL_REQ_CODE);
                 }
        }else{
        return false;
        }
        return true;
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                            int[] grantResults) throws JSONException {
        for (int r : grantResults) {
          if (r == PackageManager.PERMISSION_DENIED) {
            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, PERMISSION_DENIED_ERROR));
            return;
          }
        }
        switch (requestCode) {
          case CALL_REQ_CODE:
            callPhone(executeArgs);
            break;
        }
      }

    private void dial(JSONArray args) {
        String number = args.getString(0);
            number = number.replaceAll("#", "%23");

            if (!number.startsWith("tel:")) {
              number = String.format("tel:%s", number);
            }
            try {
              Intent intent = new Intent(isTelephonyEnabled() ? Intent.ACTION_DIAL : Intent.ACTION_VIEW);
              intent.setData(Uri.parse(number));

              cordova.getActivity().startActivity(intent);
              callbackContext.success();
            } catch (Exception e) {
              callbackContext.error("CouldNotCallPhoneNumber");
            }
    }

     protected void getCallPermission(int requestCode) {
        cordova.requestPermission(this, requestCode, CALL_PHONE);
      }

    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
      }
}
