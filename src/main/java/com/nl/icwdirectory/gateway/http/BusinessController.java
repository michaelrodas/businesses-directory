package com.nl.icwdirectory.gateway.http;

import com.nl.icwdirectory.domain.Business;
import com.nl.icwdirectory.gateway.http.converter.BusinessToJsonConverter;
import com.nl.icwdirectory.gateway.http.converter.JsonToBusinessConverter;
import com.nl.icwdirectory.gateway.http.json.BusinessJson;
import com.nl.icwdirectory.gateway.http.json.CreateBusinessJson;
import com.nl.icwdirectory.gateway.http.mapping.URLMapping;
import com.nl.icwdirectory.usecase.CreateBusiness;
import com.nl.icwdirectory.usecase.DeleteBusiness;
import com.nl.icwdirectory.usecase.GetBusinesses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
final class BusinessController {

    private final JsonToBusinessConverter jsonToBusinessConverter;
    private final BusinessToJsonConverter businessToJsonConverter;
    private final DeleteBusiness deleteBusiness;
    private final CreateBusiness createBusiness;
    private final GetBusinesses getBusinesses;

    @Value("${elements.per.page}")
    private Integer elementsPerPage;

    public BusinessController(
            JsonToBusinessConverter jsonToBusinessConverter,
            BusinessToJsonConverter businessToJsonConverter,
            DeleteBusiness deleteBusiness,
            CreateBusiness createBusiness,
            GetBusinesses getBusinesses) {
        this.jsonToBusinessConverter = jsonToBusinessConverter;
        this.businessToJsonConverter = businessToJsonConverter;
        this.deleteBusiness = deleteBusiness;
        this.createBusiness = createBusiness;
        this.getBusinesses = getBusinesses;
    }

    @ApiOperation(value = "Delete a Business")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Executed business delete operation")
    })
    @DeleteMapping(value = URLMapping.DELETE_BUSINESS)
    public ResponseEntity<BusinessJson> deleteBusinessById(@PathVariable String id) {
        log.info("Deleting Business id {}", id);
        deleteBusiness.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ApiOperation(value = "Create a new Business")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Business successfully created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping(value = URLMapping.CREATE_NEW_BUSINESS,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessJson> createBusiness(
            @RequestBody @Valid final CreateBusinessJson createBusinessJson) {
        log.info("Creating business {}", createBusinessJson);
        final Business businessToBeCreated = jsonToBusinessConverter.convert(createBusinessJson);

        final Business businessCreated = createBusiness.createBusiness(businessToBeCreated);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(businessToJsonConverter.convert(businessCreated));
    }

    @GetMapping(value = URLMapping.GET_BUSINESSES)
    public ResponseEntity<List<BusinessJson>> getAllBusinesses(@RequestParam final int pageNumber) {
        final Page<Business> businesses = getBusinesses.getAllBusinesses(
                PageRequest.of(pageNumber, elementsPerPage, Sort.Direction.ASC, "business_name"));

        return ResponseEntity.ok(
                businesses.stream().map(businessToJsonConverter::convert).collect(Collectors.toUnmodifiableList()));
    }
}
