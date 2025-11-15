package com.papers.paperspapeleria.service;

import java.util.List;

import com.papers.paperspapeleria.dto.ProductDTO;
import com.papers.paperspapeleria.dto.ProductDetailDTO;

public class ProductServideImpl implements ProductService {

    @Override
    public List<ProductDTO> listProducts() {
        // TODO: En el futuro, esto llamará a:
        // productoRepository.findAll();
        return List.of();
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        // TODO: En el futuro, llamaremos a productoRepository.findById(id);
        ProductDetailDTO detail = new ProductDetailDTO();
        detail.setId(id);
        detail.setName("Producto Mock desde Servicio");
        return detail;
    }

    @Override
    public ProductDetailDTO createProduct(ProductDetailDTO producto) {
        // TODO: En el futuro, llamaremos a productoRepository.save(producto);
        producto.setId(1L);
        return producto;
    }

}
