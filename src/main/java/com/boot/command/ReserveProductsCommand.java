package com.boot.command;

import com.boot.order.dto.OrderEntryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Accessors(chain = true)
public class ReserveProductsCommand{
    @TargetAggregateIdentifier
    private UUID orderId;
    private String email;
    private long userId;
    private List<OrderEntryDTO> entries;
}