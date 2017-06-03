package name.peterbukhal.android.redmine.service.ntp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Ярус	    Адрес сервера	    IPv6
 *  2	    ntp2.stratum2.ru	Нет
 *  2	    ntp3.stratum2.ru	Нет
 *  2	    ntp4.stratum2.ru	Есть
 *  2	    ntp5.stratum2.ru	Нет
 *  1	    ntp1.stratum1.ru	Есть
 *  1	    ntp2.stratum1.ru	Есть
 *  1	    ntp3.stratum1.ru	Есть
 *  1 	    ntp4.stratum1.ru	Есть
 *  1	    ntp5.stratum1.ru	Есть
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 */
public final class NtpService extends IntentService {

    public static void sync(final Context context) {
        context.startService(new Intent(context, NtpService.class));
    }

    public static final String TAG = "NtpService";
    private static final String NTP_SERVER = "ntp5.stratum2.ru";

    public NtpService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(NTP_SERVER);
            byte[] buf = new NtpMessage(DateTime.now().getMillis()).toByteArray();
            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, 123);

            NtpMessage.encodeTimestamp(packet.getData(), 40,
                    (DateTime.now().getMillis() / 1000.0) + 2208988800.0);

            socket.send(packet);

            // Get response
            System.out.println("NTP request sent, waiting for response...\n");
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            socket.close();

            // Immediately record the incoming timestamp
            double destinationTimestamp =
                    (DateTime.now().getMillis() / 1000.0) + 2208988800.0;

            // Process response
            NtpMessage msg = new NtpMessage(packet.getData());

            // Corrected, according to RFC2030 errata
            double roundTripDelay = (destinationTimestamp - msg.originateTimestamp) -
                    (msg.transmitTimestamp - msg.receiveTimestamp);

            double localClockOffset =
                    ((msg.receiveTimestamp - msg.originateTimestamp) +
                            (msg.transmitTimestamp - destinationTimestamp)) / 2;

            Log.d(TAG, "NTP server: " + NTP_SERVER);
            Log.d(TAG, msg.toString());
            Log.d(TAG, "Dest. timestamp: " + NtpMessage.timestampToString(destinationTimestamp));
            Log.d(TAG, "Round-trip delay: " + new DecimalFormat("0.00").format(roundTripDelay*1000) + " ms");
            Log.d(TAG, "Local clock offset: " + new DecimalFormat("0.00").format(localClockOffset*1000) + " ms");
        } catch (Exception e) {
            try {
                TimeUnit.SECONDS.sleep(5);

                NtpService.sync(this);
            } catch (Exception e1) {
                //
            }
        }
    }

}
