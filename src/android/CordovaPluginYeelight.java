package com.perales.yeelight;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mollin.yapi.enumeration.*;
import com.mollin.yapi.*;

/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaPluginYeelight extends CordovaPlugin {

    private static YeelightDevice device = null;
    private static YeelightMusicServer server = null;
    private static int brightnessFlag = 0;
    private static String brightnessFlagResult = "cambio";
    private static YeelightEffect effectType = YeelightEffect.SMOOTH;
    private static int port = 55443;
    private static int effectDuration = 1000;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String ip = args.getString(0);
            String rgbString = args.getString(1);
            String[] rgb = rgbString.split(",");
            int brightness = args.getInt(2);
            String result = "";
            try{
                if(device == null){
                    device = new YeelightDevice(ip, port, effectType, effectDuration);
                    server = device.enableMusicMode();
                    server.setColorTemperature(5500);
                    server.setPower(true);
                }
                server.setRGB( Integer.parseInt( rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                if(brightnessFlag != brightness){
                    server.setBrightness(brightness);
                    brightnessFlag = brightness;
                    brightnessFlagResult = " cambio";
                }else{
                    brightnessFlagResult = " igual";
                }
                result = "Result: " + rgbString + ":" +  brightnessFlagResult;
            }catch (Exception e){
                result = e.getMessage();
            }

            callbackContext.success(result);

            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
