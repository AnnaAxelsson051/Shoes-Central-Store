package com.shopme.admin.paging;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean //So spring wont create a new Bean for this interface
public interface SearchRepository <T, ID> extends PagingAndSortingRepository <T, ID> {
public List<T> findAll(String keywprd, Pageable pageable);
}
