package com.bulbul.es.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.bulbul.es.entity.Product;
import com.bulbul.es.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ElasticsearchClient elasticsearchClient;


    public Iterable<Product> getAllProducts() {
        return  productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, int id) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            return null;
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchByName(String name) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(sr -> sr
                .index("products")
                .query(q -> q
                        .match(m -> m
                                .field("name")
                                .query(name)
                        )
                )
        );

        System.out.println(searchRequest.toString());

        SearchResponse<Product> response = elasticsearchClient.search(searchRequest, Product.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }



}
