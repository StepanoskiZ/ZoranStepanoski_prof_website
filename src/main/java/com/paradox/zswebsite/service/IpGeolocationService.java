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

    public IpGeolocationService() {
        DatabaseReader tempReader; // Use a temporary, non-final variable for initialization logic.

        try (InputStream dbStream = getClass().getClassLoader().getResourceAsStream("geolite/GeoLite2-City.mmdb")) {
            if (dbStream == null) {
                log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.warn("!!! GeoLite2-City.mmdb not found. Geolocation will be disabled. !!!");
                log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                tempReader = null; // Assign null to the temporary variable.
            } else {
                tempReader = new DatabaseReader.Builder(dbStream).build();
                log.info("GeoLite2 Database loaded successfully. Geolocation is enabled.");
            }
        } catch (IOException e) {
            log.error("Failed to load or read GeoLite2 database, geolocation will be disabled.", e);
            tempReader = null; // Also assign null on any IO error.
        }

        // Assign to the final field exactly once at the end.
        this.databaseReader = tempReader;
    }

    /**
     * Get geolocation data for a given IP address.
     * @param ip The IP address string.
     * @return An Optional containing the CityResponse, or empty if not found or on error.
     */
    public Optional<CityResponse> getLocation(String ip) {
        // If the database failed to load, don't even try to look up.
        if (databaseReader == null) {
            return Optional.empty();
        }

        if (ip == null || ip.isBlank() || ip.equals("127.0.0.1") || ip.equals("0:0:0.0:0:0:0:1")) {
            return Optional.empty();
        }

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            return Optional.of(databaseReader.city(ipAddress));
        } catch (IOException | GeoIp2Exception e) {
            log.warn("Could not find location for IP address {}: {}", ip, e.getMessage());
            return Optional.empty();
        }
    }
}
