package com.ntap.corebe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ntap.corebe.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AppVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppVersion.class);
        AppVersion appVersion1 = new AppVersion();
        appVersion1.setId(UUID.randomUUID());
        AppVersion appVersion2 = new AppVersion();
        appVersion2.setId(appVersion1.getId());
        assertThat(appVersion1).isEqualTo(appVersion2);
        appVersion2.setId(UUID.randomUUID());
        assertThat(appVersion1).isNotEqualTo(appVersion2);
        appVersion1.setId(null);
        assertThat(appVersion1).isNotEqualTo(appVersion2);
    }
}
