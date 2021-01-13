package com.techbase.interfaces.handle.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nguyentanh
 */
@Getter
public class PagerResponse<T> {

    private final List<T> content = new ArrayList<>();
    private long totalElements = 0;
    private long page = 0;
    private long size = 0;

    public PagerResponse(List<T> content, long totalElements, long page, long size) {
        this.content.addAll(content);
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }
}
