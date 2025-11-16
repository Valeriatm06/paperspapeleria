package com.papers.paperspapeleria.controller;

import com.papers.paperspapeleria.dto.ProductDTO;
import com.papers.paperspapeleria.dto.ProductDetailDTO;
import com.papers.paperspapeleria.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> listProducts() {
        return productService.listProducts();
    }

    @GetMapping("/{id}")
    public ProductDetailDTO getProductoById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailDTO crearProducto(
            @RequestBody ProductDetailDTO productDTO
    ) {
        return productService.createProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDetailDTO actualizarProducto(
        @PathVariable Long id,
        @RequestBody ProductDetailDTO productoDTO
) {
    return productService.upDateProduct(id, productoDTO);
    }
}
