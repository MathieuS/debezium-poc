package com.github.mathieus.agencyapp.adapter;

import com.github.mathieus.agencyapp.dto.CreditApplicationDTO;
import com.github.mathieus.agencyapp.dto.PageResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BackofficeRestClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BackofficeRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        // TODO : dynamiser pour docker compose/mvn
        this.baseUrl = "http://loan-backoffice-app:8280/credit-applications";
    }

    public CreditApplicationDTO getById(Long id) {
        return restTemplate.getForObject(baseUrl + "/" + id, CreditApplicationDTO.class);
    }

    public PageResponse<CreditApplicationDTO> getByAgency(Long agencyId, int page, int size, String sortBy, String sortDir) {
        String url = baseUrl + "?agencyId=" + agencyId + "&page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDir=" + sortDir;

        ResponseEntity<PageResponse<CreditApplicationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    public CreditApplicationDTO create(CreditApplicationDTO dto) {
        return restTemplate.postForObject(baseUrl, dto, CreditApplicationDTO.class);
    }

    public ResponseEntity<CreditApplicationDTO> update(Long id, CreditApplicationDTO dto) {
        HttpEntity<CreditApplicationDTO> entity = new HttpEntity<>(dto);
        return restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.PUT,
                entity,
                CreditApplicationDTO.class
        );
    }

}
