package ru.don_polesie.back_end.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String name;
    private String imageUrl;

    private Double price;

    private Double fatGrams;
    private Double proteinGrams;
    private Double carbohydrateGrams;
    private Integer energyKcalPer100g;

    private String Volume;
    private Integer amount;

    private Integer storageTemperatureMin;
    private Integer storageTemperatureMax;

    private Boolean isWeighted;
    private String countryOfOrigin;

    public Product(Long id) {
        this.id = id;
    }
}
