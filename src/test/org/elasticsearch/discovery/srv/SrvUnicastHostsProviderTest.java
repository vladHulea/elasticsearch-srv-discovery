package org.elasticsearch.discovery.srv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.junit.Test;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class SrvUnicastHostsProviderTest {

    @Test
    public void local_address_returns_true() {
        SrvUnicastHostsProvider provider = new SrvUnicastHostsProvider(ImmutableSettings.EMPTY, null, null);
        boolean isLocal = provider.isLocalIP("127.0.0.1");
        assertTrue(isLocal);
    }

    @Test
    public void non_local_address_returns_false() {
        SrvUnicastHostsProvider provider = new SrvUnicastHostsProvider(ImmutableSettings.EMPTY, null, null);
        boolean isLocal = provider.isLocalIP("255.255.255.255");
        assertFalse(isLocal);
    }

    @Test
    public void non_local_address_returns_fals123e() {
        SrvUnicastHostsProvider provider = new SrvUnicastHostsProvider(ImmutableSettings.EMPTY, null, null);
        Record[] records = new Record[] { getRecord("www.somewhere.com."), getRecord("localhost.") };
        List<Record> filteredRecords = provider.filterOutOwnRecord(records);
        assertEquals(filteredRecords.size(), 1);
    }

    private Record getRecord(String name) {
        try {
            Name hostname = new Name(name);

            Name target = new Name(name);

            return new SRVRecord(hostname, Type.SRV, DClass.ANY, 30, 1, 80, target);
        } catch (TextParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Test
    public void isTCP() {
        SrvUnicastHostsProvider provider = new SrvUnicastHostsProvider(ImmutableSettings.EMPTY, null, null);
        boolean isTCP = provider.isTCP("tcp");
        assertTrue(isTCP);

        boolean isUDP = provider.isTCP("udp");
        assertFalse(isUDP);
    }

}