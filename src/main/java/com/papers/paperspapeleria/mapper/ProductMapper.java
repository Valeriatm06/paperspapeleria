package com.papers.paperspapeleria.mapper;

import com.papers.paperspapeleria.dto.ProductDetailDTO; // 👈 Importa ProductDetailDTO
import com.papers.paperspapeleria.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // ⚠️ Asegúrate de que este método mapee TODOS los campos de ProductDetailDTO
    public ProductDetailDTO toDetailDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setReference(product.getReference());
        dto.setPurchasePrice(product.getPurchasePrice()); // 👈 ¡ESTE ES EL CAMPO!
        dto.setSalePrice(product.getSalePrice());
        dto.setActualStock(product.getActualStock());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setImage(product.getImage());
        return dto;
    }
    
    // (Puedes tener otros métodos como toSimpleDTO si necesitas una versión más ligera)
}