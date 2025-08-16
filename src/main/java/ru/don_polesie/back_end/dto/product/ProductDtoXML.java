package ru.don_polesie.back_end.dto.product;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@Data
@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDtoXML {

    @XmlElement(name = "Id")
    private Long id;

    @XmlElement(name = "Brand")
    private String brand;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Price")
    private Double price;

    @XmlElement(name = "FatGrams")
    private Double fatGrams;

    @XmlElement(name = "ProteinGrams")
    private Double proteinGrams;

    @XmlElement(name = "CarbohydrateGrams")
    private Double carbohydrateGrams;

    @XmlElement(name = "EnergyKcalPer100g")
    private Integer energyKcalPer100g;

    @XmlElement(name = "Volume")
    private String Volume;

    @XmlElement(name = "StorageTemperatureMin")
    private Integer storageTemperatureMin;

    @XmlElement(name = "StorageTemperatureMax")
    private Integer storageTemperatureMax;

    @XmlElement(name = "CountryOfOrigin")
    private String countryOfOrigin;

    @XmlElement(name = "Amount")
    private String amount;

    private Boolean isWeighted;
}
