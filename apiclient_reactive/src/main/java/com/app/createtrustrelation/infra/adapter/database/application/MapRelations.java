package com.app.createtrustrelation.infra.adapter.database.application;

import com.app.createtrustrelation.domain.model.TrustRelation;
import com.app.createtrustrelation.infra.adapter.database.domain.RelationshipData;


public final  class MapRelations {

    public static RelationshipData toData(TrustRelation relationship) {

        return  RelationshipData.builder()
                .cid(relationship.cid().getValue())
                .productId(relationship.productId().getValue())
                .product(relationship.product().getValue())
                .createAt(relationship.creteAt())
                .build();
    }
}
