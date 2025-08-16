package ru.don_polesie.back_end.dto.product;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@XmlRootElement(name = "Products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductListDtoXML {

    @XmlElement(name = "Product")
    private List<ProductDtoXML> products;

}

