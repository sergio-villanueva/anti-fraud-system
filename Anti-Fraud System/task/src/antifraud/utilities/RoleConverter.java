package antifraud.utilities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

/** This class is used to convert entity attribute state into database column representation and back again.
 * */
@Converter
public class RoleConverter implements AttributeConverter<Role,String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getRole();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Role.fetchByStringRoleNullable(dbData);
    }
}
