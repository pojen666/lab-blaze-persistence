package com.demo.jpa.converter;

import com.demo.domain.Title;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TitleConverter implements AttributeConverter<Title, String> {
    @Override
    public String convertToDatabaseColumn(Title title) {
        return title.name();
    }

    @Override
    public Title convertToEntityAttribute(String s) {
        return Title.valueOf(s);
    }
}
