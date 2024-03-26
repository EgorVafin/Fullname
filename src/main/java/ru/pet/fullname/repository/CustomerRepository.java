package ru.pet.fullname.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.pet.fullname.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM customer c WHERE c.first_name || c.last_name  like :filter", nativeQuery = true)
    public Page<Customer>  findFullNameLike(String filter, PageRequest pageRequest);

}
