package com.example.ghtkprofilelink.controller;


import com.example.ghtkprofilelink.model.dto.GeoIP;
import com.example.ghtkprofilelink.service.GeoIPLocationService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class GeoIPController {

    private final GeoIPLocationService geoIPLocationService;

    public GeoIPController(GeoIPLocationService geoIPLocationService) {
        this.geoIPLocationService = geoIPLocationService;
    }

    @GetMapping("/geoIP/{ipAddress}")
    public GeoIP getLocation(@PathVariable String ipAddress, HttpServletRequest request
    ) throws IOException, GeoIp2Exception {
        String ipAddress1 = request.getHeader("X-FORWARDED-FOR");
        System.out.println("\n\n\n\n"+ipAddress1+"\n\n\n\n");
        return geoIPLocationService.getIpLocation(ipAddress, request);
    }

    @GetMapping("/ipAddress")
	public String index(HttpServletRequest request) {
		String clientIp = geoIPLocationService.getClientIp(request);
		return clientIp;
	}
}
