package com.app.domain.model;



import com.app.domain.model.valueobject.Cid;
import com.app.domain.model.valueobject.Product;
import com.app.domain.model.valueobject.ProductId;

import java.time.Instant;

public record Relationship(String idRelation, Cid cid, Product product,
                           ProductId productId, Instant creteAt){

       public static Relationship of(String cid, String product, String productId){
           return new Relationship(null, new Cid(cid), new Product(product),
                   new ProductId(productId),  Instant.now());
       }


}
