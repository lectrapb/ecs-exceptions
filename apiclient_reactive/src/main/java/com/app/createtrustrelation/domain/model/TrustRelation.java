package com.app.createtrustrelation.domain.model;



import com.app.createtrustrelation.domain.value.Cid;
import com.app.createtrustrelation.domain.value.Product;
import com.app.createtrustrelation.domain.value.ProductId;

import java.time.Instant;

public record TrustRelation(String idRelation, Cid cid, Product product,
                            ProductId productId, Instant creteAt){

       public static TrustRelation of(String cid, String product, String productId){
           return new TrustRelation(null, new Cid(cid), new Product(product),
                   new ProductId(productId),  Instant.now());
       }


}
