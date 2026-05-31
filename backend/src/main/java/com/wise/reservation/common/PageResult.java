package com.wise.reservation.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> records;
    private long total;
    private long current;
    private long pages;

    public static <T> PageResult<T> of(List<T> records, long total, long current, long pages) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setCurrent(current);
        result.setPages(pages);
        return result;
    }
}
