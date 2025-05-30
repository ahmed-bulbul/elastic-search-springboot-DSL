package com.bulbul.es.util;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.bulbul.es.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllServices() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Map> searchResponse = elasticsearchClient.search(s->s.query(supplier.get()),Map.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return searchResponse;
    }

    public SearchResponse<Product> matchAllProductsServices() throws IOException {
        Supplier<Query> supplier  = ElasticSearchUtil.supplier();
        SearchResponse<Product> searchResponse = elasticsearchClient
                .search(s->s.index("products").query(supplier.get()),Product.class);

        System.out.println("elasticsearch query is "+supplier.get().toString());
        return searchResponse;
    }

    public static Supplier<Query> supplierWithNameField(String fieldValue){
        Supplier<Query> supplier = ()->Query.of(q->q.match(matchQueryWithNameField(fieldValue)));
        return supplier;
    }

    public static MatchQuery matchQueryWithNameField(String fieldValue){
        var  matchQuery = new MatchQuery.Builder();
        return matchQuery.field("name").query(fieldValue).build();
    }

    public SearchResponse<Product> matchProductsWithName(String fieldValue) throws IOException {
        Supplier<Query> supplier  = ElasticSearchUtil.supplierWithNameField(fieldValue);
        SearchResponse<Product> searchResponse = elasticsearchClient.search(s->s.index("products").query(supplier.get()),Product.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return searchResponse;
    }
}
