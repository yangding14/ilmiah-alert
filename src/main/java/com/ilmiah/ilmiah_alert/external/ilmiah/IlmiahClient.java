package com.ilmiah.ilmiah_alert.external.ilmiah;

import com.ilmiah.ilmiah_alert.external.ilmiah.dto.GetProjectListResp;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.model.IlmiahApiException;

import io.micrometer.observation.ObservationRegistry;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Component
public class IlmiahClient {
    private static final int timeout = 5000;
    private final RestClient restClient;

    public IlmiahClient(ObservationRegistry observationRegistry) {
        this.restClient =
                RestClient.builder()
                        .observationRegistry(observationRegistry)
                        .requestFactory(getRequestFactory())
                        .build();
    }

    public GetProjectListResp getProjectList(Department department) throws IlmiahApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("department", String.valueOf(department.id()));
        queryParams.add("length", String.valueOf(200));

        String uri =
                UriComponentsBuilder.fromUriString(
                                "https://ilmiah.fsktm.um.edu.my/project/list/datatable")
                        .queryParams(queryParams)
                        .build()
                        .toUriString();

        try {
            return restClient.get().uri(uri).retrieve().body(GetProjectListResp.class);
        } catch (Exception e) {
            throw new IlmiahApiException(e.getMessage(), e.getCause());
        }
    }

    private HttpComponentsClientHttpRequestFactory getRequestFactory() {
        HttpClient httpClient =
                HttpClients.custom()
                        .setDefaultRequestConfig(
                                RequestConfig.custom()
                                        .setResponseTimeout(Timeout.of(Duration.ofMillis(timeout)))
                                        .build())
                        .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(timeout);
        return factory;
    }
}
