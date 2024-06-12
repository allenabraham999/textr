        package com.example.textr.config;

        import org.springframework.context.annotation.Configuration;
        import org.springframework.messaging.converter.MessageConverter;
        import org.springframework.messaging.simp.config.MessageBrokerRegistry;
        import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
        import org.springframework.stereotype.Component;
        import org.springframework.web.socket.config.annotation.EnableWebSocket;
        import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
        import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
        import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

        import java.util.List;

        @Configuration
        @EnableWebSocketMessageBroker
        //@EnableWebSocket
        public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

            @Override
            public void  configureMessageBroker(MessageBrokerRegistry config){
                config.enableSimpleBroker("/topic", "/queue");
                config.setApplicationDestinationPrefixes("/app");
                config.setUserDestinationPrefix("/user");
            }

            @Override
            public void  registerStompEndpoints(StompEndpointRegistry registry){
                registry.addEndpoint("/textr-socket")
                        .setAllowedOrigins("http://127.0.0.1:5500/");
            }
            @Override
            public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
                return true;
            }

        }