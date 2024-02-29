package com.example.textr.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:redis.properties")
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    @NotBlank
    private String host;

    private String password;

    @Min(1025)
    @Max(65536)
    private int port = 6379;

    @Min(1000)
    @Max(120000)
    private int timeout = 120000;

    @Min(1)
    @Max(400)
    private int max_connections = 400;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the max_connections
     */
    public int getMax_connections() {
        return max_connections;
    }

    /**
     * @param max_connections the max_connections to set
     */
    public void setMax_connections(int max_connections) {
        this.max_connections = max_connections;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JedisPool getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(timeout);
        jedisPoolConfig.setMaxTotal(max_connections);
        jedisPoolConfig.setMaxIdle(400);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout);
        return pool;
    }

}
