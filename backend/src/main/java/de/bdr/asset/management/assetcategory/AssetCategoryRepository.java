package de.bdr.asset.management.assetcategory;

import org.springframework.data.jpa.repository.JpaRepository;

/** JPA AssetCategory Repository */
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {

    /** Checks if an asset category exists with the given name. */
    boolean existsByName(String name);

    /** Checks if a name is used by any asset category other than the specified ID. */
    boolean existsByNameAndIdNot(String name, Long id);
}