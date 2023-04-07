package org.anderfolg.trainogram.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum ContentType {
    POST("POST",0),
    COMMENT("COMMENT",1),
    SPONSORED_POST("SPONSORED", 2);
    @Setter
    @Getter
    @Accessors(fluent = true)
    private Integer id;
    private String type;


    @JsonCreator
    public static ContentType fromId(Integer id){
        for (ContentType o: ContentType.values()){
            if ( o.id.compareTo(id) == 0 ){
                return o;
            }
        }
        return null;
    }

    ContentType(String type, int id) {
        this.id = id;
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }
}

//  TODO (Bogdan O.) 7/4/23: make it nested
class ContentConverter {
    @Converter(autoApply = true)
    public static class FieldConverter implements AttributeConverter<ContentType,Integer> {
        @Override
        public Integer convertToDatabaseColumn( ContentType type ) {
            return type.getId();
        }

        @Override
        public ContentType convertToEntityAttribute( Integer id ) {
            return ContentType.fromId(id);
        }
    }
}
