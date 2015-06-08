package com.atb.demo.stagger.mustache;

import com.atb.demo.stagger.util.StringConversions;
import com.atb.demo.stagger.DataTypeFilter;
import com.wordnik.swagger.model.ModelProperty;

public class MustacheItem {
    private String name;

    private String type;

    private String linkType;

    private boolean required;

    private String access;

    private String description;

    private String notes;

    private String allowableValue;

    DataTypeFilter typeFilter = new DataTypeFilter();

    public MustacheItem(String name, ModelProperty documentationSchema) {
        StringConversions stringConversions = new StringConversions();

        this.name = name;
        this.type = documentationSchema.type();
        this.linkType = this.type;
        this.description = stringConversions.getStrInOption(documentationSchema.description());
        this.required = documentationSchema.required();
        this.notes = stringConversions.getStrInOption(documentationSchema.description());
        this.linkType = typeFilter.filterBasicTypes(this.linkType);
        this.allowableValue = stringConversions.allowableValuesToString(documentationSchema.allowableValues());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllowableValue() {
        return allowableValue;
    }

    public void setAllowableValue(String allowableValue) {
        this.allowableValue = allowableValue;
    }

    public void setTypeAsArray(String elementType) {
        this.type = typeFilter.AsArrayType(elementType);
        setLinkType(typeFilter.filterBasicTypes(elementType));
    }
}
