package ru.pet.fullname.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pet.fullname.dto.CustomerResponseDto;
import ru.pet.fullname.entity.Customer;
import ru.pet.fullname.entity.SortDirection;
import ru.pet.fullname.repository.CustomerRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class Controller {

    private static final ModelMapper modelMapper = new ModelMapper();

    private final CustomerRepository customerRepository;


    @GetMapping("")
    public ResponseEntity<List<CustomerResponseDto>> findAll(
            @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer offset,
            @Valid @Positive @RequestParam(required = false, defaultValue = "5") Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") SortDirection sortDirection,
            @RequestParam(required = false) String filter
    ) {
        PageRequest pageRequest = this.buildPageRequest(offset, limit, sortBy, sortDirection);

        Page<Customer> customers = null;
        if (filter == null) {
            customers = customerRepository.findAll(pageRequest);
        } else {

            pageRequest = PageRequest.of(offset, limit);
            customers = customerRepository.findFullNameLike(filter, pageRequest);

            System.out.println(1);
        }

        List<CustomerResponseDto> organizationResponseDtoList = customers
                .stream().map((element) -> modelMapper.map(element, CustomerResponseDto.class)).toList();

        return ResponseEntity.ok(organizationResponseDtoList);
    }

    private PageRequest buildPageRequest(Integer offset, Integer limit, String sortBy, ru.pet.fullname.entity.SortDirection sortDirection) {

        if (sortBy == null) {
            return PageRequest.of(offset, limit);
        }
        var sort = Sort.by(sortBy);
        if (sortDirection == ru.pet.fullname.entity.SortDirection.ASC) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }
        return PageRequest.of(offset, limit, sort);
    }

}
