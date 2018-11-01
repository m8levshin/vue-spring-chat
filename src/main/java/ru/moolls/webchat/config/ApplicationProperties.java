package ru.moolls.webchat.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:app.properties"})
@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperties {

    @Value("${lastEventCount}")
    private int lastEventCount;
}
