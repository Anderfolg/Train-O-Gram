package org.anderfolg.trainogram.security;

import org.anderfolg.trainogram.entities.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//  TODO (Bogdan O.) 7/4/23: needs to be moved into Role.class
public class RoleConverter {
    @Converter(autoApply = true)
    public static class FieldConverter implements AttributeConverter<Role,Integer>{
        @Override
        public Integer convertToDatabaseColumn( Role role ) {
            return role.getId();
        }

        @Override
        public Role convertToEntityAttribute( Integer id ) {
            return Role.fromId(id);
        }
    }
}
