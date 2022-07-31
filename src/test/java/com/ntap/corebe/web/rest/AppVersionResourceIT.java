package com.ntap.corebe.web.rest;

import static com.ntap.corebe.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ntap.corebe.IntegrationTest;
import com.ntap.corebe.domain.AppVersion;
import com.ntap.corebe.domain.enumeration.AppVersionType;
import com.ntap.corebe.repository.AppVersionRepository;
import com.ntap.corebe.service.criteria.AppVersionCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppVersionResourceIT {

    private static final Integer DEFAULT_MAJOR = 1;
    private static final Integer UPDATED_MAJOR = 2;
    private static final Integer SMALLER_MAJOR = 1 - 1;

    private static final Integer DEFAULT_MINOR = 1;
    private static final Integer UPDATED_MINOR = 2;
    private static final Integer SMALLER_MINOR = 1 - 1;

    private static final Integer DEFAULT_PATCH = 1;
    private static final Integer UPDATED_PATCH = 2;
    private static final Integer SMALLER_PATCH = 1 - 1;

    private static final ZonedDateTime DEFAULT_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RELEASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final AppVersionType DEFAULT_TYPE = AppVersionType.PORTABLE;
    private static final AppVersionType UPDATED_TYPE = AppVersionType.INSTALLABLE;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/app-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppVersionMockMvc;

    private AppVersion appVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppVersion createEntity(EntityManager em) {
        AppVersion appVersion = new AppVersion()
            .major(DEFAULT_MAJOR)
            .minor(DEFAULT_MINOR)
            .patch(DEFAULT_PATCH)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .type(DEFAULT_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return appVersion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppVersion createUpdatedEntity(EntityManager em) {
        AppVersion appVersion = new AppVersion()
            .major(UPDATED_MAJOR)
            .minor(UPDATED_MINOR)
            .patch(UPDATED_PATCH)
            .releaseDate(UPDATED_RELEASE_DATE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return appVersion;
    }

    @BeforeEach
    public void initTest() {
        appVersion = createEntity(em);
    }

    @Test
    @Transactional
    void createAppVersion() throws Exception {
        int databaseSizeBeforeCreate = appVersionRepository.findAll().size();
        // Create the AppVersion
        restAppVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appVersion)))
            .andExpect(status().isCreated());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeCreate + 1);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getMajor()).isEqualTo(DEFAULT_MAJOR);
        assertThat(testAppVersion.getMinor()).isEqualTo(DEFAULT_MINOR);
        assertThat(testAppVersion.getPatch()).isEqualTo(DEFAULT_PATCH);
        assertThat(testAppVersion.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testAppVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAppVersion.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testAppVersion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAppVersion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAppVersion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppVersion.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAppVersion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createAppVersionWithExistingId() throws Exception {
        // Create the AppVersion with an existing ID
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeCreate = appVersionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appVersion)))
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppVersions() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appVersion.getId().toString())))
            .andExpect(jsonPath("$.[*].major").value(hasItem(DEFAULT_MAJOR)))
            .andExpect(jsonPath("$.[*].minor").value(hasItem(DEFAULT_MINOR)))
            .andExpect(jsonPath("$.[*].patch").value(hasItem(DEFAULT_PATCH)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get the appVersion
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, appVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appVersion.getId().toString()))
            .andExpect(jsonPath("$.major").value(DEFAULT_MAJOR))
            .andExpect(jsonPath("$.minor").value(DEFAULT_MINOR))
            .andExpect(jsonPath("$.patch").value(DEFAULT_PATCH))
            .andExpect(jsonPath("$.releaseDate").value(sameInstant(DEFAULT_RELEASE_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAppVersionsByIdFiltering() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        UUID id = appVersion.getId();

        defaultAppVersionShouldBeFound("id.equals=" + id);
        defaultAppVersionShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major equals to DEFAULT_MAJOR
        defaultAppVersionShouldBeFound("major.equals=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major equals to UPDATED_MAJOR
        defaultAppVersionShouldNotBeFound("major.equals=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major not equals to DEFAULT_MAJOR
        defaultAppVersionShouldNotBeFound("major.notEquals=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major not equals to UPDATED_MAJOR
        defaultAppVersionShouldBeFound("major.notEquals=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major in DEFAULT_MAJOR or UPDATED_MAJOR
        defaultAppVersionShouldBeFound("major.in=" + DEFAULT_MAJOR + "," + UPDATED_MAJOR);

        // Get all the appVersionList where major equals to UPDATED_MAJOR
        defaultAppVersionShouldNotBeFound("major.in=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major is not null
        defaultAppVersionShouldBeFound("major.specified=true");

        // Get all the appVersionList where major is null
        defaultAppVersionShouldNotBeFound("major.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major is greater than or equal to DEFAULT_MAJOR
        defaultAppVersionShouldBeFound("major.greaterThanOrEqual=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major is greater than or equal to UPDATED_MAJOR
        defaultAppVersionShouldNotBeFound("major.greaterThanOrEqual=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major is less than or equal to DEFAULT_MAJOR
        defaultAppVersionShouldBeFound("major.lessThanOrEqual=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major is less than or equal to SMALLER_MAJOR
        defaultAppVersionShouldNotBeFound("major.lessThanOrEqual=" + SMALLER_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsLessThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major is less than DEFAULT_MAJOR
        defaultAppVersionShouldNotBeFound("major.lessThan=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major is less than UPDATED_MAJOR
        defaultAppVersionShouldBeFound("major.lessThan=" + UPDATED_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMajorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where major is greater than DEFAULT_MAJOR
        defaultAppVersionShouldNotBeFound("major.greaterThan=" + DEFAULT_MAJOR);

        // Get all the appVersionList where major is greater than SMALLER_MAJOR
        defaultAppVersionShouldBeFound("major.greaterThan=" + SMALLER_MAJOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor equals to DEFAULT_MINOR
        defaultAppVersionShouldBeFound("minor.equals=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor equals to UPDATED_MINOR
        defaultAppVersionShouldNotBeFound("minor.equals=" + UPDATED_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor not equals to DEFAULT_MINOR
        defaultAppVersionShouldNotBeFound("minor.notEquals=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor not equals to UPDATED_MINOR
        defaultAppVersionShouldBeFound("minor.notEquals=" + UPDATED_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor in DEFAULT_MINOR or UPDATED_MINOR
        defaultAppVersionShouldBeFound("minor.in=" + DEFAULT_MINOR + "," + UPDATED_MINOR);

        // Get all the appVersionList where minor equals to UPDATED_MINOR
        defaultAppVersionShouldNotBeFound("minor.in=" + UPDATED_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor is not null
        defaultAppVersionShouldBeFound("minor.specified=true");

        // Get all the appVersionList where minor is null
        defaultAppVersionShouldNotBeFound("minor.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor is greater than or equal to DEFAULT_MINOR
        defaultAppVersionShouldBeFound("minor.greaterThanOrEqual=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor is greater than or equal to UPDATED_MINOR
        defaultAppVersionShouldNotBeFound("minor.greaterThanOrEqual=" + UPDATED_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor is less than or equal to DEFAULT_MINOR
        defaultAppVersionShouldBeFound("minor.lessThanOrEqual=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor is less than or equal to SMALLER_MINOR
        defaultAppVersionShouldNotBeFound("minor.lessThanOrEqual=" + SMALLER_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsLessThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor is less than DEFAULT_MINOR
        defaultAppVersionShouldNotBeFound("minor.lessThan=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor is less than UPDATED_MINOR
        defaultAppVersionShouldBeFound("minor.lessThan=" + UPDATED_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByMinorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where minor is greater than DEFAULT_MINOR
        defaultAppVersionShouldNotBeFound("minor.greaterThan=" + DEFAULT_MINOR);

        // Get all the appVersionList where minor is greater than SMALLER_MINOR
        defaultAppVersionShouldBeFound("minor.greaterThan=" + SMALLER_MINOR);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch equals to DEFAULT_PATCH
        defaultAppVersionShouldBeFound("patch.equals=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch equals to UPDATED_PATCH
        defaultAppVersionShouldNotBeFound("patch.equals=" + UPDATED_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch not equals to DEFAULT_PATCH
        defaultAppVersionShouldNotBeFound("patch.notEquals=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch not equals to UPDATED_PATCH
        defaultAppVersionShouldBeFound("patch.notEquals=" + UPDATED_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch in DEFAULT_PATCH or UPDATED_PATCH
        defaultAppVersionShouldBeFound("patch.in=" + DEFAULT_PATCH + "," + UPDATED_PATCH);

        // Get all the appVersionList where patch equals to UPDATED_PATCH
        defaultAppVersionShouldNotBeFound("patch.in=" + UPDATED_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch is not null
        defaultAppVersionShouldBeFound("patch.specified=true");

        // Get all the appVersionList where patch is null
        defaultAppVersionShouldNotBeFound("patch.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch is greater than or equal to DEFAULT_PATCH
        defaultAppVersionShouldBeFound("patch.greaterThanOrEqual=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch is greater than or equal to UPDATED_PATCH
        defaultAppVersionShouldNotBeFound("patch.greaterThanOrEqual=" + UPDATED_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch is less than or equal to DEFAULT_PATCH
        defaultAppVersionShouldBeFound("patch.lessThanOrEqual=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch is less than or equal to SMALLER_PATCH
        defaultAppVersionShouldNotBeFound("patch.lessThanOrEqual=" + SMALLER_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsLessThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch is less than DEFAULT_PATCH
        defaultAppVersionShouldNotBeFound("patch.lessThan=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch is less than UPDATED_PATCH
        defaultAppVersionShouldBeFound("patch.lessThan=" + UPDATED_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByPatchIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where patch is greater than DEFAULT_PATCH
        defaultAppVersionShouldNotBeFound("patch.greaterThan=" + DEFAULT_PATCH);

        // Get all the appVersionList where patch is greater than SMALLER_PATCH
        defaultAppVersionShouldBeFound("patch.greaterThan=" + SMALLER_PATCH);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate not equals to DEFAULT_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.notEquals=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate not equals to UPDATED_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.notEquals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the appVersionList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate is not null
        defaultAppVersionShouldBeFound("releaseDate.specified=true");

        // Get all the appVersionList where releaseDate is null
        defaultAppVersionShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate is greater than or equal to DEFAULT_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.greaterThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate is greater than or equal to UPDATED_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.greaterThanOrEqual=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate is less than or equal to DEFAULT_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.lessThanOrEqual=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate is less than or equal to SMALLER_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.lessThanOrEqual=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate is less than DEFAULT_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.lessThan=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate is less than UPDATED_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.lessThan=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByReleaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where releaseDate is greater than DEFAULT_RELEASE_DATE
        defaultAppVersionShouldNotBeFound("releaseDate.greaterThan=" + DEFAULT_RELEASE_DATE);

        // Get all the appVersionList where releaseDate is greater than SMALLER_RELEASE_DATE
        defaultAppVersionShouldBeFound("releaseDate.greaterThan=" + SMALLER_RELEASE_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description equals to DEFAULT_DESCRIPTION
        defaultAppVersionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the appVersionList where description equals to UPDATED_DESCRIPTION
        defaultAppVersionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description not equals to DEFAULT_DESCRIPTION
        defaultAppVersionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the appVersionList where description not equals to UPDATED_DESCRIPTION
        defaultAppVersionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAppVersionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the appVersionList where description equals to UPDATED_DESCRIPTION
        defaultAppVersionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description is not null
        defaultAppVersionShouldBeFound("description.specified=true");

        // Get all the appVersionList where description is null
        defaultAppVersionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description contains DEFAULT_DESCRIPTION
        defaultAppVersionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the appVersionList where description contains UPDATED_DESCRIPTION
        defaultAppVersionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where description does not contain DEFAULT_DESCRIPTION
        defaultAppVersionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the appVersionList where description does not contain UPDATED_DESCRIPTION
        defaultAppVersionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location equals to DEFAULT_LOCATION
        defaultAppVersionShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the appVersionList where location equals to UPDATED_LOCATION
        defaultAppVersionShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location not equals to DEFAULT_LOCATION
        defaultAppVersionShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the appVersionList where location not equals to UPDATED_LOCATION
        defaultAppVersionShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultAppVersionShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the appVersionList where location equals to UPDATED_LOCATION
        defaultAppVersionShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location is not null
        defaultAppVersionShouldBeFound("location.specified=true");

        // Get all the appVersionList where location is null
        defaultAppVersionShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location contains DEFAULT_LOCATION
        defaultAppVersionShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the appVersionList where location contains UPDATED_LOCATION
        defaultAppVersionShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where location does not contain DEFAULT_LOCATION
        defaultAppVersionShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the appVersionList where location does not contain UPDATED_LOCATION
        defaultAppVersionShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAppVersionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where type equals to DEFAULT_TYPE
        defaultAppVersionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the appVersionList where type equals to UPDATED_TYPE
        defaultAppVersionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where type not equals to DEFAULT_TYPE
        defaultAppVersionShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the appVersionList where type not equals to UPDATED_TYPE
        defaultAppVersionShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAppVersionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the appVersionList where type equals to UPDATED_TYPE
        defaultAppVersionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where type is not null
        defaultAppVersionShouldBeFound("type.specified=true");

        // Get all the appVersionList where type is null
        defaultAppVersionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy equals to DEFAULT_CREATED_BY
        defaultAppVersionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the appVersionList where createdBy equals to UPDATED_CREATED_BY
        defaultAppVersionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy not equals to DEFAULT_CREATED_BY
        defaultAppVersionShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the appVersionList where createdBy not equals to UPDATED_CREATED_BY
        defaultAppVersionShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAppVersionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the appVersionList where createdBy equals to UPDATED_CREATED_BY
        defaultAppVersionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy is not null
        defaultAppVersionShouldBeFound("createdBy.specified=true");

        // Get all the appVersionList where createdBy is null
        defaultAppVersionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy contains DEFAULT_CREATED_BY
        defaultAppVersionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the appVersionList where createdBy contains UPDATED_CREATED_BY
        defaultAppVersionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultAppVersionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the appVersionList where createdBy does not contain UPDATED_CREATED_BY
        defaultAppVersionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultAppVersionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the appVersionList where createdDate equals to UPDATED_CREATED_DATE
        defaultAppVersionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultAppVersionShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the appVersionList where createdDate not equals to UPDATED_CREATED_DATE
        defaultAppVersionShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultAppVersionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the appVersionList where createdDate equals to UPDATED_CREATED_DATE
        defaultAppVersionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where createdDate is not null
        defaultAppVersionShouldBeFound("createdDate.specified=true");

        // Get all the appVersionList where createdDate is null
        defaultAppVersionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAppVersionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the appVersionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultAppVersionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the appVersionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the appVersionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy is not null
        defaultAppVersionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the appVersionList where lastModifiedBy is null
        defaultAppVersionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAppVersionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the appVersionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAppVersionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the appVersionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAppVersionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAppVersionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the appVersionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAppVersionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAppVersionShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the appVersionList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultAppVersionShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultAppVersionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the appVersionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAppVersionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAppVersionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        // Get all the appVersionList where lastModifiedDate is not null
        defaultAppVersionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the appVersionList where lastModifiedDate is null
        defaultAppVersionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppVersionShouldBeFound(String filter) throws Exception {
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appVersion.getId().toString())))
            .andExpect(jsonPath("$.[*].major").value(hasItem(DEFAULT_MAJOR)))
            .andExpect(jsonPath("$.[*].minor").value(hasItem(DEFAULT_MINOR)))
            .andExpect(jsonPath("$.[*].patch").value(hasItem(DEFAULT_PATCH)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppVersionShouldNotBeFound(String filter) throws Exception {
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppVersion() throws Exception {
        // Get the appVersion
        restAppVersionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();

        // Update the appVersion
        AppVersion updatedAppVersion = appVersionRepository.findById(appVersion.getId()).get();
        // Disconnect from session so that the updates on updatedAppVersion are not directly saved in db
        em.detach(updatedAppVersion);
        updatedAppVersion
            .major(UPDATED_MAJOR)
            .minor(UPDATED_MINOR)
            .patch(UPDATED_PATCH)
            .releaseDate(UPDATED_RELEASE_DATE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAppVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppVersion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppVersion))
            )
            .andExpect(status().isOk());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testAppVersion.getMinor()).isEqualTo(UPDATED_MINOR);
        assertThat(testAppVersion.getPatch()).isEqualTo(UPDATED_PATCH);
        assertThat(testAppVersion.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testAppVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAppVersion.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testAppVersion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppVersion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppVersion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppVersion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppVersion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appVersion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appVersion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppVersionWithPatch() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();

        // Update the appVersion using partial update
        AppVersion partialUpdatedAppVersion = new AppVersion();
        partialUpdatedAppVersion.setId(appVersion.getId());

        partialUpdatedAppVersion.location(UPDATED_LOCATION).type(UPDATED_TYPE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restAppVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppVersion))
            )
            .andExpect(status().isOk());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getMajor()).isEqualTo(DEFAULT_MAJOR);
        assertThat(testAppVersion.getMinor()).isEqualTo(DEFAULT_MINOR);
        assertThat(testAppVersion.getPatch()).isEqualTo(DEFAULT_PATCH);
        assertThat(testAppVersion.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testAppVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAppVersion.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testAppVersion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppVersion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAppVersion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppVersion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppVersion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAppVersionWithPatch() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();

        // Update the appVersion using partial update
        AppVersion partialUpdatedAppVersion = new AppVersion();
        partialUpdatedAppVersion.setId(appVersion.getId());

        partialUpdatedAppVersion
            .major(UPDATED_MAJOR)
            .minor(UPDATED_MINOR)
            .patch(UPDATED_PATCH)
            .releaseDate(UPDATED_RELEASE_DATE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .type(UPDATED_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAppVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppVersion))
            )
            .andExpect(status().isOk());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
        AppVersion testAppVersion = appVersionList.get(appVersionList.size() - 1);
        assertThat(testAppVersion.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testAppVersion.getMinor()).isEqualTo(UPDATED_MINOR);
        assertThat(testAppVersion.getPatch()).isEqualTo(UPDATED_PATCH);
        assertThat(testAppVersion.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testAppVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAppVersion.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testAppVersion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppVersion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppVersion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppVersion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppVersion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appVersion))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppVersion() throws Exception {
        int databaseSizeBeforeUpdate = appVersionRepository.findAll().size();
        appVersion.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppVersionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appVersion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppVersion in the database
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppVersion() throws Exception {
        // Initialize the database
        appVersionRepository.saveAndFlush(appVersion);

        int databaseSizeBeforeDelete = appVersionRepository.findAll().size();

        // Delete the appVersion
        restAppVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, appVersion.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppVersion> appVersionList = appVersionRepository.findAll();
        assertThat(appVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
