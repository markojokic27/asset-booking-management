package de.bdr.asset.management.asset;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA Asset Repository
 */
public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {

    @EntityGraph(attributePaths = {"category"})
    @Query("SELECT a FROM Asset a WHERE a.id = :id")
    Optional<Asset> findById(@Param("id")Long id);

    @EntityGraph(attributePaths = {"category"})
    Page<Asset> findAll(Specification<Asset> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Optional<Asset> findByIdAndStatus(Long id, AssetStatusEnum status);

    boolean existsByCategoryId(Long categoryId);
}