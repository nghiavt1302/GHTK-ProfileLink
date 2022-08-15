//package com.example.ghtkprofilelink.controller;
//
//
//import com.example.ghtkprofilelink.model.dto.GeoIP;
//import com.example.ghtkprofilelink.service.GeoIPLocationService;
//import com.maxmind.geoip2.exception.GeoIp2Exception;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@RestController
//@CrossOrigin
//@RequestMapping("api/v1.0/geoip")
//public class GeoIPController {
//
//    private final GeoIPLocationService geoIPLocationService;
//
//    public GeoIPController(GeoIPLocationService geoIPLocationService) {
//        this.geoIPLocationService = geoIPLocationService;
//    }
//
//    @GetMapping("")
//    public GeoIP getLocation(HttpServletRequest request
//    ) throws IOException, GeoIp2Exception {
//        String clientIp = geoIPLocationService.getClientIp(request);
//        return geoIPLocationService.getIpLocation(clientIp, request);
//    }
//
//    @GetMapping("/ipAddress")
//	public String index(HttpServletRequest request) {
//		String clientIp = geoIPLocationService.getClientIp(request);
//		return clientIp;
//	}
//}
