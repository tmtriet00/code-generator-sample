package com.ntap.corebe.service.criteria;

import com.ntap.corebe.domain.enumeration.AppVersionType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.ntap.corebe.domain.AppVersion} entity. This class is used
 * in {@link com.ntap.corebe.web.rest.AppVersionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-versions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppVersionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AppVersionType
     */
    public static class AppVersionTypeFilter extends Filter<AppVersionType> {

        public AppVersionTypeFilter() {}

        public AppVersionTypeFilter(AppVersionTypeFilter filter) {
            super(filter);
        }

        @Override
        public AppVersionTypeFilter copy() {
            return new AppVersionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private IntegerFilter major;

    private IntegerFilter minor;

    private IntegerFilter patch;

    private ZonedDateTimeFilter releaseDate;

    private StringFilter description;

    private StringFilter location;

    private AppVersionTypeFilter type;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public AppVersionCriteria() {}

    public AppVersionCriteria(AppVersionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.major = other.major == null ? null : other.major.copy();
        this.minor = other.minor == null ? null : other.minor.copy();
        this.patch = other.patch == null ? null : other.patch.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppVersionCriteria copy() {
        return new AppVersionCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public IntegerFilter getMajor() {
        return major;
    }

    public IntegerFilter major() {
        if (major == null) {
            major = new IntegerFilter();
        }
        return major;
    }

    public void setMajor(IntegerFilter major) {
        this.major = major;
    }

    public IntegerFilter getMinor() {
        return minor;
    }

    public IntegerFilter minor() {
        if (minor == null) {
            minor = new IntegerFilter();
        }
        return minor;
    }

    public void setMinor(IntegerFilter minor) {
        this.minor = minor;
    }

    public IntegerFilter getPatch() {
        return patch;
    }

    public IntegerFilter patch() {
        if (patch == null) {
            patch = new IntegerFilter();
        }
        return patch;
    }

    public void setPatch(IntegerFilter patch) {
        this.patch = patch;
    }

    public ZonedDateTimeFilter getReleaseDate() {
        return releaseDate;
    }

    public ZonedDateTimeFilter releaseDate() {
        if (releaseDate == null) {
            releaseDate = new ZonedDateTimeFilter();
        }
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTimeFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public AppVersionTypeFilter getType() {
        return type;
    }

    public AppVersionTypeFilter type() {
        if (type == null) {
            type = new AppVersionTypeFilter();
        }
        return type;
    }

    public void setType(AppVersionTypeFilter type) {
        this.type = type;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppVersionCriteria that = (AppVersionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(major, that.major) &&
            Objects.equals(minor, that.minor) &&
            Objects.equals(patch, that.patch) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(location, that.location) &&
            Objects.equals(type, that.type) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            major,
            minor,
            patch,
            releaseDate,
            description,
            location,
            type,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppVersionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (major != null ? "major=" + major + ", " : "") +
            (minor != null ? "minor=" + minor + ", " : "") +
            (patch != null ? "patch=" + patch + ", " : "") +
            (releaseDate != null ? "releaseDate=" + releaseDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
