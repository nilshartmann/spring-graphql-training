package nh.publy.backend.graphql;

import org.springframework.data.domain.Page;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class PageResult {

  private final int pageNumber;
  private final long totalCount;
  private final int totalPageCount;
  private final boolean hasNextPage;
  private final boolean hasPreviousPage;

  public PageResult(int pageNumber, long totalCount, int totalPageCount, boolean hasNextPage, boolean hasPreviousPage) {
    this.pageNumber = pageNumber;
    this.totalCount = totalCount;
    this.totalPageCount = totalPageCount;
    this.hasNextPage = hasNextPage;
    this.hasPreviousPage = hasPreviousPage;
  }

  public static PageResult fromPage(Page<?> page) {
    return new PageResult(
      page.getNumber(),
      page.getTotalElements(),
      page.getTotalPages(),
      page.hasNext(),
      page.hasPrevious()
    );
  }

  @Override
  public String toString() {
    return "PageResult{" + "pageNumber=" + pageNumber + ", totalCount=" + totalCount + ", totalPageCount=" + totalPageCount
        + ", hasNextPage=" + hasNextPage + ", hasPreviousPage=" + hasPreviousPage + '}';
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public int getTotalPageCount() {
    return totalPageCount;
  }

  public boolean isHasNextPage() {
    return hasNextPage;
  }

  public boolean isHasPreviousPage() {
    return hasPreviousPage;
  }
}
