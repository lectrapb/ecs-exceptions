package com.app.infra.adapter.database.domain;


import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "relationships")
public class RelationshipData {

    @Id
    @Column("relation_id")
    private Integer id;
    private String cid;
    private String product;
    @Column("product_id")
    private String productId;
    @Column("create_at")
    private Instant createAt;
}
