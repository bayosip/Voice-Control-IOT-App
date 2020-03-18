package inc.osips.iot_wireless_communication.wireless_comms_module.remote_comms;

import android.app.Activity;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import inc.osips.iot_wireless_communication.wireless_comms_module.interfaces.WirelessDeviceConnector;
import inc.osips.iot_wireless_communication.wireless_comms_module.remote_comms.ble_comms.BleConnection;
import inc.osips.iot_wireless_communication.wireless_comms_module.remote_comms.utilities.Constants;
import inc.osips.iot_wireless_communication.wireless_comms_module.remote_comms.p2p_comms.P2pConnection;

public class DeviceConnectionFactory {

    private Activity activity;
    public final static String FAILED_DEVICE_CONNECTION = "device_connection_failed";
    public final static String DEVICE_CONNECTION_SERVICE_STOPPED = "connection_service_stopped";

    public DeviceConnectionFactory(@NonNull Activity activity) {
        this.activity = activity;
    }

    public Builder establishConnectionWithDeviceOf(@NonNull String connectionType, @NonNull Parcelable device){

        if(TextUtils.isEmpty(connectionType))return null;

        else if (connectionType.equals(Constants.BLE)){
            return new BleDeviceConnectionBuilder(activity, device);
        }

        else if (connectionType.equals(Constants.P2P)){
            return new P2PDeviceConnectionBuilder(activity, device);
        }

        else return null;
    }

    abstract public static class Builder{
        Activity activity;
        Parcelable device;

        private Builder(Activity activity, Parcelable device) {

            this.activity = activity;
            this.device = device;
        }

        abstract public WirelessDeviceConnector build();
        abstract public DeviceConnectionFactory.Builder setDeviceUniqueID(String UUID_IP);
        abstract public String getDeviceType();
        abstract public Builder setConnectionTimeOut(int timeOut);
    }

    private class P2PDeviceConnectionBuilder extends Builder{

        String UUID_ServiceType;
        int PORT, timeOut;


        private P2PDeviceConnectionBuilder(Activity activity, Parcelable device) {
            super(activity, device);
        }

        @Override
        public WirelessDeviceConnector build() {
            return new P2pConnection(activity, device,  timeOut);
        }

        @Override
        public Builder setDeviceUniqueID(@Nullable String ip) {
            this.UUID_ServiceType = ip;
            return this;
        }

        @Override
        public String getDeviceType() {
            return Constants.P2P;
        }


        @Override
        public Builder setConnectionTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }
    }

    private class BleDeviceConnectionBuilder extends Builder{

        String UUID_IP;
        private BleDeviceConnectionBuilder(Activity activity, Parcelable device) {
            super(activity, device);
        }

        @Override
        public WirelessDeviceConnector build() {
            Log.w("Connection++", UUID_IP);
            if (!TextUtils.isEmpty(UUID_IP))
                return new BleConnection(activity, device, UUID_IP);
            else return null;
        }

        @Override
        public Builder setDeviceUniqueID(@NonNull String UUID) {
            this.UUID_IP = UUID_IP;
            return this;
        }

        @Override
        public String getDeviceType() {
            return Constants.BLE;
        }


        @Override
        public Builder setConnectionTimeOut(int timeOut) {
            return this;
        }
    }

    private class LocalWiFiServiceConnectionBuilder extends Builder{

        private LocalWiFiServiceConnectionBuilder(Activity activity, Parcelable device) {
            super(activity, device);
        }

        @Override
        public WirelessDeviceConnector build() {
            return null;
        }

        @Override
        public Builder setDeviceUniqueID(String UUID_IP) {
            return null;
        }

        @Override
        public String getDeviceType() {
            return null;
        }

        @Override
        public Builder setConnectionTimeOut(int timeOut) {
            return null;
        }
    }
}
