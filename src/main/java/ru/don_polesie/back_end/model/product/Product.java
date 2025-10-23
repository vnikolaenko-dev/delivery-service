package ru.don_polesie.back_end.model.product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Double price;
    @Column(unique = true, nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private Double fatGrams;
    @Column(nullable = false)
    private Double proteinGrams;
    @Column(nullable = false)
    private Double carbohydrateGrams;
    @Column(nullable = false)
    private Integer energyKcalPer100g;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer minWeight;
    @Column(nullable = false)
    private Integer maxWeight;
    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer storageTemperatureMin;
    @Column(nullable = false)
    private Integer storageTemperatureMax;

    @Column(nullable = false)
    private Boolean isWeighted;
    @Column(nullable = false)
    private String countryOfOrigin;
}
