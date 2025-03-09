package com.ilmiah.ilmiah_alert.external.ilmiah;

import com.ilmiah.ilmiah_alert.external.ilmiah.dto.GetProjectListResp;
import com.ilmiah.ilmiah_alert.model.Department;
import com.ilmiah.ilmiah_alert.model.IlmiahApiException;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class IlmiahClient {
    private static final int readTimeout = 5000;
    private final RestClient restClient;

    public IlmiahClient() {
        this.restClient = RestClient.builder().requestFactory(getRequestFactory()).build();
    }

    public GetProjectListResp getProjectList(Department department) throws IlmiahApiException {
        String uri =
                UriComponentsBuilder.fromUriString(
                                "https://ilmiah.fsktm.um.edu.my/project/list/datatable")
                        .queryParam("department", department.id())
                        .build()
                        .toUriString();

        try {
            return restClient.get().uri(uri).retrieve().body(GetProjectListResp.class);
        } catch (RestClientException e) {
            throw new IlmiahApiException(e.getMessage(), e.getCause());
        }
    }

    private HttpComponentsClientHttpRequestFactory getRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(readTimeout);
        return factory;
    }
}
