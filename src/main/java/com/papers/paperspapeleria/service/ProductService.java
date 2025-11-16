package com.papers.paperspapeleria.service;

import java.util.List;
import com.papers.paperspapeleria.dto.ProductDTO;
import com.papers.paperspapeleria.dto.ProductDetailDTO;

public interface ProductService {

    List<ProductDTO> listProducts();

    ProductDetailDTO getProductById(Long id);

    ProductDetailDTO createProduct(ProductDetailDTO producto);

    ProductDetailDTO upDateProduct(Long id, ProductDetailDTO producto);
}
