package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ToString
public enum Role implements GrantedAuthority{

    ADMIN("ADMIN", 0),
    USER("USER", 1);
    @Getter
    @Accessors(fluent = true)
    private final Integer id;

    @Accessors(fluent = true)
    private final String authority;

    @JsonCreator
    public static Role fromId(Integer id){
        for (Role o: Role.values()){
            if ( o.id.compareTo(id) == 0 ){
                return o;
            }
        }
        return null;
    }

    Role(String authority, int id) {
        this.id = id;
        this.authority = authority;
    }

    @JsonValue
    @Override
    public String getAuthority() {
        return authority;
    }

    public Integer getId() {
        return id;
    }
    public static class RoleConverter {
        private RoleConverter(){
            throw new IllegalStateException("Utility class");
        }

        @Converter(autoApply = true)
        public static class FieldConverter implements AttributeConverter<Role,Integer> {
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
}
