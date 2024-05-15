package com.app.domain.model;



import java.time.Instant;

public record Relationship(String idRelation, String cid, String product,
                           String productId, Instant creteAt){

       public static Relationship of(String cid, String product, String productId){
           return new Relationship(null, cid ,product,
                   productId, Instant.now());
       }
}
