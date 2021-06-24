package com.demo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 分頁工具
 */
public final class PageUtils {

    private PageUtils() {
    }

    /**
     * 更新分頁資訊(防止查詢的頁數大於總頁數)
     *
     * @param pageable 目前的分頁資訊
     * @param total    總筆數
     * @return 新的分頁資訊
     */
    public static Pageable refresh(Pageable pageable, long total) {
        int maxPage = 0;
        Pageable newPageable = pageable;
        if (total > 0L) {
            maxPage = (int) Math.ceil(
                    (double) total / (double) pageable.getPageSize()
            ) - 1;
        }
        if (pageable.getPageNumber() > maxPage) {
            newPageable = PageRequest.of(
                    maxPage, pageable.getPageSize(), pageable.getSort()
            );
        }
        return newPageable;
    }


}
