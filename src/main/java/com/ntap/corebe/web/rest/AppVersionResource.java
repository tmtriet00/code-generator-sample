package com.ntap.corebe.web.rest;

import com.ntap.corebe.domain.AppVersion;
import com.ntap.corebe.repository.AppVersionRepository;
import com.ntap.corebe.service.AppVersionQueryService;
import com.ntap.corebe.service.AppVersionService;
import com.ntap.corebe.service.criteria.AppVersionCriteria;
import com.ntap.corebe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ntap.corebe.domain.AppVersion}.
 */
@RestController
@RequestMapping("/api")
public class AppVersionResource {

    private final Logger log = LoggerFactory.getLogger(AppVersionResource.class);

    private static final String ENTITY_NAME = "corebeAppVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppVersionService appVersionService;

    private final AppVersionRepository appVersionRepository;

    private final AppVersionQueryService appVersionQueryService;

    public AppVersionResource(
        AppVersionService appVersionService,
        AppVersionRepository appVersionRepository,
        AppVersionQueryService appVersionQueryService
    ) {
        this.appVersionService = appVersionService;
        this.appVersionRepository = appVersionRepository;
        this.appVersionQueryService = appVersionQueryService;
    }

    /**
     * {@code POST  /app-versions} : Create a new appVersion.
     *
     * @param appVersion the appVersion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appVersion, or with status {@code 400 (Bad Request)} if the appVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-versions")
    public ResponseEntity<AppVersion> createAppVersion(@RequestBody AppVersion appVersion) throws URISyntaxException {
        log.debug("REST request to save AppVersion : {}", appVersion);
        if (appVersion.getId() != null) {
            throw new BadRequestAlertException("A new appVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppVersion result = appVersionService.save(appVersion);
        return ResponseEntity
            .created(new URI("/api/app-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-versions/:id} : Updates an existing appVersion.
     *
     * @param id the id of the appVersion to save.
     * @param appVersion the appVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appVersion,
     * or with status {@code 400 (Bad Request)} if the appVersion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-versions/{id}")
    public ResponseEntity<AppVersion> updateAppVersion(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AppVersion appVersion
    ) throws URISyntaxException {
        log.debug("REST request to update AppVersion : {}, {}", id, appVersion);
        if (appVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppVersion result = appVersionService.save(appVersion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appVersion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /app-versions/:id} : Partial updates given fields of an existing appVersion, field will ignore if it is null
     *
     * @param id the id of the appVersion to save.
     * @param appVersion the appVersion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appVersion,
     * or with status {@code 400 (Bad Request)} if the appVersion is not valid,
     * or with status {@code 404 (Not Found)} if the appVersion is not found,
     * or with status {@code 500 (Internal Server Error)} if the appVersion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/app-versions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppVersion> partialUpdateAppVersion(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AppVersion appVersion
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppVersion partially : {}, {}", id, appVersion);
        if (appVersion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appVersion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppVersion> result = appVersionService.partialUpdate(appVersion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appVersion.getId().toString())
        );
    }

    /**
     * {@code GET  /app-versions} : get all the appVersions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appVersions in body.
     */
    @GetMapping("/app-versions")
    public ResponseEntity<List<AppVersion>> getAllAppVersions(AppVersionCriteria criteria) {
        log.debug("REST request to get AppVersions by criteria: {}", criteria);
        List<AppVersion> entityList = appVersionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /app-versions/count} : count all the appVersions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/app-versions/count")
    public ResponseEntity<Long> countAppVersions(AppVersionCriteria criteria) {
        log.debug("REST request to count AppVersions by criteria: {}", criteria);
        return ResponseEntity.ok().body(appVersionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-versions/:id} : get the "id" appVersion.
     *
     * @param id the id of the appVersion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appVersion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-versions/{id}")
    public ResponseEntity<AppVersion> getAppVersion(@PathVariable UUID id) {
        log.debug("REST request to get AppVersion : {}", id);
        Optional<AppVersion> appVersion = appVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appVersion);
    }

    /**
     * {@code DELETE  /app-versions/:id} : delete the "id" appVersion.
     *
     * @param id the id of the appVersion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-versions/{id}")
    public ResponseEntity<Void> deleteAppVersion(@PathVariable UUID id) {
        log.debug("REST request to delete AppVersion : {}", id);
        appVersionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
