package sample.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;

public interface Entity<T> {
    @XmlElement(name = "id")
    @ApiModelProperty(value = "Address' indentifier")
    Integer getId();

    void setId(Integer id);
}
