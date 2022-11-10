package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}