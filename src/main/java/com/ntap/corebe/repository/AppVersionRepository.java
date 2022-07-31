package com.ntap.corebe.repository;

import com.ntap.corebe.domain.AppVersion;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, UUID>, JpaSpecificationExecutor<AppVersion> {}
