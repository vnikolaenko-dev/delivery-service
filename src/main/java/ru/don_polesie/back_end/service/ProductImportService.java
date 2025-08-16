package ru.don_polesie.back_end.service;

import jakarta.xml.bind.JAXBException;
import ru.don_polesie.back_end.dto.product.ProductListDtoXML;

import java.io.File;

public interface ProductImportService {

    ProductListDtoXML parse(File xmlFile) throws JAXBException;

    void importFromSharedFolder();
}
