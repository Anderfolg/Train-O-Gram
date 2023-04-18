package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum NotificationType {
    LIKE("LIKE", 0L),
    COMMENT("COMMENT", 1L),
    FOLLOW("FOLLOW", 2L),
    POST("POST", 3L);
    @Setter
    @Getter
    @Accessors(fluent = true)
    private Long id;
    private String type;


    @JsonCreator
    public static NotificationType fromId(Long id){
        for (NotificationType o: NotificationType.values()){
            if ( o.id.compareTo(id) == 0 ){
                return o;
            }
        }
        return null;
    }

    NotificationType(String type, Long id) {
        this.id = id;
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }
}

class NotificationConverter {

    private NotificationConverter() {
        throw new IllegalStateException("Utility class");
    }
    @Converter(autoApply = true)
    public static class FieldConverter implements AttributeConverter<NotificationType,Long> {
        @Override
        public Long convertToDatabaseColumn( NotificationType type ) {
            return type.getId();
        }

        @Override
        public NotificationType convertToEntityAttribute( Long id ) {
            return NotificationType.fromId(id);
        }
    }
}
