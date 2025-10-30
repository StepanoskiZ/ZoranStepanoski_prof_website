package com.paradox.zswebsite.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IpGeolocationService {

    private static final Logger log = LoggerFactory.getLogger(IpGeolocationService.class);

    private final DatabaseReader databaseReader;

    public IpGeolocationService() {
        this.databaseReader = createDatabaseReader();
    }

    private DatabaseReader createDatabaseReader() {
        try (InputStream dbStream = getClass().getClassLoader().getResourceAsStream("geolite/GeoLite2-City.mmdb")) {
            if (dbStream == null) {
                log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.warn("!!! GeoLite2-City.mmdb not found. Geolocation will be disabled. !!!");
                log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return null;
            }

            // Create a temporary file to extract the database to.
            // This is necessary to use memory-mapping (mmap) instead of loading the whole file into RAM.
            File tempFile = File.createTempFile("geolite2-", ".mmdb");
            tempFile.deleteOnExit(); // Ensure the temp file is cleaned up on shutdown.

            // Copy the database from the JAR to the temporary file.
            Files.copy(dbStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("GeoLite2 Database extracted to temporary file: {}", tempFile.getAbsolutePath());

            // Build the DatabaseReader using the File object, which enables memory-mapping.
            DatabaseReader reader = new DatabaseReader.Builder(tempFile).build();
            log.info("GeoLite2 Database loaded successfully using memory-mapping. Geolocation is enabled.");
            return reader;
        } catch (IOException e) {
            log.error("Failed to load or read GeoLite2 database, geolocation will be disabled.", e);
            return null;
        }
    }

    public Optional<CityResponse> getLocation(String ip) {
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
