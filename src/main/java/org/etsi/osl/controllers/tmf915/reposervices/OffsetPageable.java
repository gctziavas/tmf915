package org.etsi.osl.controllers.tmf915.reposervices;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * A {@link Pageable} addressed by row offset instead of page number, matching the TMF
 * {@code offset}/{@code limit} query parameters. {@code PageRequest.of(offset / limit, limit)}
 * returns the wrong window when the offset is not a multiple of the limit.
 */
public final class OffsetPageable implements Pageable {

    public static final int DEFAULT_LIMIT = 50;

    private final long offset;
    private final int limit;
    private final Sort sort;

    private OffsetPageable(long offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    /**
     * @param offset zero-based row offset; null or negative means 0
     * @param limit  page size; null or non-positive means {@link #DEFAULT_LIMIT}
     * @param sort   sort order; use a stable one so consecutive pages do not overlap
     */
    public static OffsetPageable of(Integer offset, Integer limit, Sort sort) {
        long safeOffset = (offset != null && offset > 0) ? offset : 0;
        int safeLimit = (limit != null && limit > 0) ? limit : DEFAULT_LIMIT;
        return new OffsetPageable(safeOffset, safeLimit, sort);
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageable(offset + limit, limit, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new OffsetPageable(Math.max(0, offset - limit), limit, sort) : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageable(0, limit, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPageable((long) pageNumber * limit, limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }

    @Override
    public String toString() {
        return "OffsetPageable[offset=" + offset + ", limit=" + limit + ", sort=" + sort + "]";
    }
}
