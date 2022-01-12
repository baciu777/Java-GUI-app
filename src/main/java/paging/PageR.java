package paging;

import java.util.stream.Stream;

public interface PageR<E> {
    Pageable getPageable();

    Pageable nextPageable();

    Stream<E> getContent();


}
