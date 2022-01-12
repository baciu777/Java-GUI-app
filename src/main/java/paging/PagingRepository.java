package paging;

import domain.Entity;
import repository.Repository;

public interface PagingRepository<ID ,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    PageR<E> findAllPage(Pageable pageable);   // Pageable e un fel de paginator
}