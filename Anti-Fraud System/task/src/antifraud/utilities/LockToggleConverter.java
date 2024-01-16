package antifraud.utilities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LockToggleConverter implements AttributeConverter<LockToggle, String> {

    @Override
    public String convertToDatabaseColumn(LockToggle attribute) {
        return attribute.getStringToggle();
    }

    @Override
    public LockToggle convertToEntityAttribute(String dbData) {
        return LockToggle.fetchByStringToggleNullable(dbData);
    }
}
