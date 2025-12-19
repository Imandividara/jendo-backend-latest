package com.jendo.app.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Paginated response wrapper")
public class PaginationResponse<T> {
    
    @Schema(description = "List of items in current page")
    private List<T> content;
    
    @Schema(description = "Current page number (0-indexed)", example = "0")
    private int pageNumber;
    
    @Schema(description = "Number of items per page", example = "10")
    private int pageSize;
    
    @Schema(description = "Total number of items", example = "100")
    private long totalElements;
    
    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;
    
    @Schema(description = "Is this the first page", example = "true")
    private boolean first;
    
    @Schema(description = "Is this the last page", example = "false")
    private boolean last;
}
