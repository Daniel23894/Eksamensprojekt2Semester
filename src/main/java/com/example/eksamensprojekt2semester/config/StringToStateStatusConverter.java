package com.example.eksamensprojekt2semester.config;

import com.example.eksamensprojekt2semester.model.StateStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStateStatusConverter implements Converter<String, StateStatus> {
    @Override
    public StateStatus convert(String source) {
        try {
            return StateStatus.fromValue(Integer.parseInt(source));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + source, e);
        }
    }
}
