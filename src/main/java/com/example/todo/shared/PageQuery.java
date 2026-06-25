package com.example.todo.shared;

/**
 * Page query parameters.
 */
public record PageQuery(
    int page,
    int size,
    String sortBy,
    String sortDir
) {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIR = "desc";

    public PageQuery {
        if (page < 0) page = DEFAULT_PAGE;
        if (size < 1) size = DEFAULT_SIZE;
        if (sortBy == null || sortBy.isBlank()) sortBy = DEFAULT_SORT_BY;
        if (sortDir == null || sortDir.isBlank()) sortDir = DEFAULT_SORT_DIR;
        sortDir = sortDir.toLowerCase();
        if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
            sortDir = DEFAULT_SORT_DIR;
        }
    }

    public static PageQuery of(int page, int size, String sortBy, String sortDir) {
        return new PageQuery(page, size, sortBy, sortDir);
    }

    public static PageQuery defaultQuery() {
        return new PageQuery(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_DIR);
    }
}