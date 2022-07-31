package com.ntap.corebe.service;

import com.ntap.corebe.domain.AppVersion;
import com.ntap.corebe.repository.AppVersionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppVersion}.
 */
@Service
@Transactional
public class AppVersionService {

    private final Logger log = LoggerFactory.getLogger(AppVersionService.class);

    private final AppVersionRepository appVersionRepository;

    public AppVersionService(AppVersionRepository appVersionRepository) {
        this.appVersionRepository = appVersionRepository;
    }

    /**
     * Save a appVersion.
     *
     * @param appVersion the entity to save.
     * @return the persisted entity.
     */
    public AppVersion save(AppVersion appVersion) {
        log.debug("Request to save AppVersion : {}", appVersion);
        return appVersionRepository.save(appVersion);
    }

    /**
     * Partially update a appVersion.
     *
     * @param appVersion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppVersion> partialUpdate(AppVersion appVersion) {
        log.debug("Request to partially update AppVersion : {}", appVersion);

        return appVersionRepository
            .findById(appVersion.getId())
            .map(existingAppVersion -> {
                if (appVersion.getMajor() != null) {
                    existingAppVersion.setMajor(appVersion.getMajor());
                }
                if (appVersion.getMinor() != null) {
                    existingAppVersion.setMinor(appVersion.getMinor());
                }
                if (appVersion.getPatch() != null) {
                    existingAppVersion.setPatch(appVersion.getPatch());
                }
                if (appVersion.getReleaseDate() != null) {
                    existingAppVersion.setReleaseDate(appVersion.getReleaseDate());
                }
                if (appVersion.getDescription() != null) {
                    existingAppVersion.setDescription(appVersion.getDescription());
                }
                if (appVersion.getLocation() != null) {
                    existingAppVersion.setLocation(appVersion.getLocation());
                }
                if (appVersion.getType() != null) {
                    existingAppVersion.setType(appVersion.getType());
                }
                if (appVersion.getCreatedBy() != null) {
                    existingAppVersion.setCreatedBy(appVersion.getCreatedBy());
                }
                if (appVersion.getCreatedDate() != null) {
                    existingAppVersion.setCreatedDate(appVersion.getCreatedDate());
                }
                if (appVersion.getLastModifiedBy() != null) {
                    existingAppVersion.setLastModifiedBy(appVersion.getLastModifiedBy());
                }
                if (appVersion.getLastModifiedDate() != null) {
                    existingAppVersion.setLastModifiedDate(appVersion.getLastModifiedDate());
                }

                return existingAppVersion;
            })
            .map(appVersionRepository::save);
    }

    /**
     * Get all the appVersions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppVersion> findAll() {
        log.debug("Request to get all AppVersions");
        return appVersionRepository.findAll();
    }

    /**
     * Get one appVersion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppVersion> findOne(UUID id) {
        log.debug("Request to get AppVersion : {}", id);
        return appVersionRepository.findById(id);
    }

    /**
     * Delete the appVersion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete AppVersion : {}", id);
        appVersionRepository.deleteById(id);
    }
}
