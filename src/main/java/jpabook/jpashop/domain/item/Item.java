package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
