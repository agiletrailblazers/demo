package com.atb.demo.stagger.mustache;


import com.atb.demo.stagger.DataTypeFilter;
import com.atb.demo.stagger.util.StringConversions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.model.Parameter;

public class MustacheParameter {
    private final String allowableValue;

    private final String access;

    private final String defaultValue;

    private String name;

    private final boolean required;

    private final String description;

    private final String type;

    private final String linkType;

    public MustacheParameter(Parameter para) {
        DataTypeFilter dataTypeFilter = new DataTypeFilter();
        StringConversions stringConversions = new StringConversions();

        this.name = para.name();
        this.linkType = dataTypeFilter.getTrueType(para.dataType());
        this.required = para.required();
        this.description = stringConversions.getStrInOption(para.description());
        this.type = para.dataType();
        this.defaultValue = stringConversions.getStrInOption(para.defaultValue());
        this.allowableValue = stringConversions.allowableValuesToString(para.allowableValues());
        this.access = stringConversions.getStrInOption(para.paramAccess());
    }

    String getDefaultValue() {
        return defaultValue;
    }

    public String getAllowableValue() {
        return allowableValue;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getAccess() {
        return access;
    }

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        try {
           return  om.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }

    }

    public void setName(String name) {
        this.name = name;
    }
}
