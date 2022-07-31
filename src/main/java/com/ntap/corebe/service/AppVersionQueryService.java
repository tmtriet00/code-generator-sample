package com.ntap.corebe.service;

import com.ntap.corebe.domain.*; // for static metamodels
import com.ntap.corebe.domain.AppVersion;
import com.ntap.corebe.repository.AppVersionRepository;
import com.ntap.corebe.service.criteria.AppVersionCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppVersion} entities in the database.
 * The main input is a {@link AppVersionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppVersion} or a {@link Page} of {@link AppVersion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppVersionQueryService extends QueryService<AppVersion> {

    private final Logger log = LoggerFactory.getLogger(AppVersionQueryService.class);

    private final AppVersionRepository appVersionRepository;

    public AppVersionQueryService(AppVersionRepository appVersionRepository) {
        this.appVersionRepository = appVersionRepository;
    }

    /**
     * Return a {@link List} of {@link AppVersion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppVersion> findByCriteria(AppVersionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AppVersion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppVersion> findByCriteria(AppVersionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppVersionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppVersion> specification = createSpecification(criteria);
        return appVersionRepository.count(specification);
    }

    /**
     * Function to convert {@link AppVersionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppVersion> createSpecification(AppVersionCriteria criteria) {
        Specification<AppVersion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AppVersion_.id));
            }
            if (criteria.getMajor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMajor(), AppVersion_.major));
            }
            if (criteria.getMinor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinor(), AppVersion_.minor));
            }
            if (criteria.getPatch() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPatch(), AppVersion_.patch));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), AppVersion_.releaseDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AppVersion_.description));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), AppVersion_.location));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AppVersion_.type));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), AppVersion_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), AppVersion_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), AppVersion_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), AppVersion_.lastModifiedDate));
            }
        }
        return specification;
    }
}
