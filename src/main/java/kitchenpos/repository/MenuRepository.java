package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Override
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}