package de.bdr.asset.management.asset;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Asset Repository
 */
public interface AssetRepository extends JpaRepository<Asset, Long> {
}