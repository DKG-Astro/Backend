package com.astro.entity.InventoryModule;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "goods_return")
public class GoodsReturn {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_return_note_no", nullable = false)
    private String goodsReturnNoteNo;

    @Column(name = "rejected_quantity", nullable = false)
    private Integer rejectedQuantity;

    @Column(name = "return_quantity", nullable = false)
    private Integer returnQuantity;

    @Column(name = "type_of_return", nullable = false)
    private String typeOfReturn;

    @Column(name = "reason_of_return", nullable = false)
    private String reasonOfReturn;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedDate = LocalDateTime.now();

}
