package com.papers.paperspapeleria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import com.papers.paperspapeleria.dto.ProductDTO;
import com.papers.paperspapeleria.entity.Product; 
import com.papers.paperspapeleria.repository.ProductRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import com.papers.paperspapeleria.dto.ProductDetailDTO;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> listProducts() {
        List<Product> productos = productRepository.findAll();
        return productos.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToProductDTO(Product producto) {
        ProductDTO dto = new ProductDTO();
        dto.setId(producto.getId());
        dto.setName(producto.getName());
        dto.setImage(producto.getImage());
        return dto;
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        return convertToDetailDTO(product);
    }

    @Override
    public ProductDetailDTO createProduct(ProductDetailDTO productDTO) {
        Product productToSave = convertToEntity(productDTO);

        Product productoGuardado = productRepository.save(productToSave);

        return convertToDetailDTO(productoGuardado);
    }

    private ProductDetailDTO convertToDetailDTO(Product p) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(p.getId());
        dto.setReference(p.getReference());
        dto.setName(p.getName());
        dto.setPurchasePrice(p.getPurchasePrice());
        dto.setSalePrice(p.getSalePrice());
        dto.setActualStock(p.getActualStock());
        dto.setDescription(p.getDescription());
        dto.setBrand(p.getBrand());
        dto.setCategory(p.getCategory());
        dto.setImage(p.getImage());
        return dto;
    }

    private Product convertToEntity(ProductDetailDTO dto) {
        Product p = new Product();
        p.setReference(dto.getReference());
        p.setName(dto.getName());
        p.setPurchasePrice(dto.getPurchasePrice());
        p.setSalePrice(dto.getSalePrice());
        p.setActualStock(dto.getActualStock());
        p.setDescription(dto.getDescription());
        p.setBrand(dto.getBrand());
        p.setCategory(dto.getCategory());
        p.setImage(dto.getImage());
        return p;
    }

    @Override
    public ProductDetailDTO upDateProduct(Long id, ProductDetailDTO productDTO) {
        Product productoExistente = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        productoExistente.setReference(productDTO.getReference());
        productoExistente.setName(productDTO.getName());
        productoExistente.setPurchasePrice(productDTO.getPurchasePrice());
        productoExistente.setSalePrice(productDTO.getSalePrice());
        productoExistente.setActualStock(productDTO.getActualStock());
        productoExistente.setDescription(productDTO.getDescription());
        productoExistente.setBrand(productDTO.getBrand());
        productoExistente.setCategory(productDTO.getCategory());
        productoExistente.setImage(productDTO.getImage());

        Product upDatedProduct = productRepository.save(productoExistente);

        return convertToDetailDTO(upDatedProduct);
    }

}
