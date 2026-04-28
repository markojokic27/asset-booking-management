package de.bdr.asset.management.asset;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Asset Repository
 */
public interface AssetRepository extends JpaRepository<Asset, Long> {

    @EntityGraph(attributePaths = {"category"})
    Optional<Asset> findById(Long id);

    @EntityGraph(attributePaths = {"category"})
    Page<Asset> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Optional<Asset> findByIdAndStatusNot(Long id, AssetStatusEnum status);

    @EntityGraph(attributePaths = {"category"})
    Optional<Asset> findByIdAndStatus(Long id, AssetStatusEnum status);

    @EntityGraph(attributePaths = {"category"})
    Page<Asset> findAllByStatusNot(AssetStatusEnum status, Pageable pageable);
}