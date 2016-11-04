/**
 * This source file is subject to the license that is bundled with this package in the file LICENSE.
 */
package com.codeup.pagination;

import java.util.List;

public class Pagination<T> {
    private PaginationStorage<T> storage;
    private int pageSize;
    private int currentPage;
    private long itemsCount = -1L;

    public Pagination(int pageSize, PaginationStorage<T> storage, int currentPage) {
        this.pageSize = pageSize;
        this.storage = storage;
        this.currentPage = currentPage;
    }

    public List<T> pageItems() {
        return storage.slice(offset(), pageSize);
    }

    public boolean hasPages() {
        return itemsCount() > pageSize;
    }

    public int nextPage() {
        return currentPage + 1;
    }

    public boolean hasNextPage() {
        return currentPage < pagesCount();
    }

    public int previousPage() {
        return currentPage - 1;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public int pagesCount() {
        return (int) Math.ceil((double) itemsCount() / pageSize);
    }

    private long itemsCount() {
        if (itemsCount == -1) {
            itemsCount = storage.itemsCount();
        }
        return itemsCount;
    }

    private int offset() {
        return (currentPage - 1) * pageSize;
    }
}
