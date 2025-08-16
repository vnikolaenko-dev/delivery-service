package ru.don_polesie.back_end.service.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.product.ProductDtoXML;
import ru.don_polesie.back_end.dto.product.ProductListDtoXML;
import ru.don_polesie.back_end.mapper.ProductMapper;
import ru.don_polesie.back_end.model.Product;
import ru.don_polesie.back_end.repository.ProductRepository;
import ru.don_polesie.back_end.service.ProductImportService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductImportServiceImpl implements ProductImportService {

    @Value("${import.directory}")
    private String importDirectory;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public ProductListDtoXML parse(File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ProductListDtoXML.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ProductListDtoXML) unmarshaller.unmarshal(xmlFile);
    }

    @Transactional
    @Scheduled(fixedRate = 30 * 60 * 1000) // каждые 30 минут
    public void importFromSharedFolder() {
        File dir = new File(importDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
        if (files == null) return;
        for (File file : files) {
            try {
                ProductListDtoXML list = parse(file);
                for (ProductDtoXML dto : list.getProducts()) {
                    dto.setIsWeighted(dto.getVolume().contains("~"));
                    Optional<Product> existing = productRepository.findByBrandAndName(dto.getBrand(), dto.getName());
                    if (existing.isPresent()) {
                        productMapper.updateFromDto(dto, existing.get());
                        productRepository.save(existing.get());
                    } else {
                        Product newProd = productMapper.productDtoXMLtoProduct(dto);
                        productRepository.save(newProd);
                    }
                }

                // После успешной обработки — переместить или удалить
                Files.move(file.toPath(),
                        Paths.get(importDirectory, "processed", file.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
