package com.boot.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

	private long id;

	private Long userId;

	private double total;

	private List<CartEntryDTO> entries;
}
