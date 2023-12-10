package com.sunjy.maple.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author created by sunjy on 12/5/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_qq_character")
public class QqCharacter extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String qqNumber;

    @Column(nullable = false, unique = true)
    private String characterName;

}
