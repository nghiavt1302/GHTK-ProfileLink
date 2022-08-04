//package com.example.ghtkprofilelink.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/secured/**").authenticated()
//                .anyMessage().authenticated();
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/secured/notification").setAllowedOrigins("http://localhost:4200").withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.setApplicationDestinationPrefixes("/app");
//        config.enableSimpleBroker( "/secured/queue","/secured/topic");
////        config.setUserDestinationPrefix("/secured");
//    }
//
//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//        registration
//                .setSendTimeLimit(15 * 1000)
//                .setSendBufferSizeLimit(512 * 1024)
//                .setMessageSizeLimit(128 * 1024);
//    }
//
//}
