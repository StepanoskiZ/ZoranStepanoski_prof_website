package com.paradox.zswebsite.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IpGeolocationService {

    private static final Logger log = LoggerFactory.getLogger(IpGeolocationService.class);
    private final DatabaseReader databaseReader;

    public IpGeolocationService() throws IOException {
        // This looks for the file in 'src/main/resources/geolite/GeoLite2-City.mmdb'
        InputStream dbStream = getClass().getClassLoader().getResourceAsStream("geolite/GeoLite2-City.mmdb");

        if (dbStream == null) {
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.error("!!! GeoLite2-City.mmdb database not found in resources/geolite folder! !!!");
            log.error("!!! Please download it from MaxMind and place it there.                !!!");
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new IOException("GeoLite2 database not found. Cannot start IpGeolocationService.");
        }

        databaseReader = new DatabaseReader.Builder(dbStream).build();
        log.info("GeoLite2 Database loaded successfully.");
    }

    /**
     * Get geolocation data for a given IP address.
     * @param ip The IP address string.
     * @return An Optional containing the CityResponse, or empty if not found or on error.
     */
    public Optional<CityResponse> getLocation(String ip) {
        // Skip lookup for local/invalid addresses
        if (ip == null || ip.isBlank() || ip.equals("127.0.0.1") || ip.equals("0:0:0.0:0:0:0:1")) {
            return Optional.empty();
        }

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            return Optional.of(databaseReader.city(ipAddress));
        } catch (IOException | GeoIp2Exception e) {
            // This is a 'warn' because it's common for private/internal IPs to not be in the database.
            log.warn("Could not find location for IP address {}: {}", ip, e.getMessage());
            return Optional.empty();
        }
    }
}
