package name.peterbukhal.android.redmine.util;

import com.squareup.otto.Bus;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 03.02.2017.
 */
public final class BusProvider {

    private BusProvider() {}

    private static final Bus BUS = new Bus();

    public static Bus getBus() {
        return BUS;
    }

}
