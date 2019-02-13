package com.esp.ip.service.configure;

import com.esp.ip.service.exception.InvalidDatabaseException;
import com.esp.ip.service.io.Reader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author edward
 * @date 2019/1/7
 */
@Configuration
public class IPConfigure {

    @Value("${config.file-path}")
    private String filePath;

    @Bean
    public Reader Reader() throws InvalidDatabaseException {
        return new Reader(filePath);
    }
}
