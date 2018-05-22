package ru.emias.router.service;

import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.emias.router.domain.entity.Rule;
import ru.emias.router.domain.entity.RuleParameterValue;
import ru.emias.router.domain.exception.RouterException;
import ru.emias.router.domain.repository.ParameterRepository;
import ru.emias.router.domain.repository.RuleRepository;
import ru.emias.router.dto.RuleDto;
import ru.emias.router.dto.RuleParameterValueDto;

@CrossOrigin
@RequestMapping("/rs/rule")
@RestController
@Transactional
@Validated
public class RuleApplicationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get All Rules", notes = "")
    public List<RuleDto> findAll() {
        return ruleRepository.findAll().stream().map(rule -> convertToDto(rule)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get rule by id", notes = "")
    public RuleDto findById(@PathVariable(name = "id", required = true) int id) {
        return convertToDto(ruleRepository.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save rule", notes = "")
    public Integer save(@Valid @RequestBody RuleDto ruleDto) throws RouterException {
        Rule rule = convertToEntity(ruleDto);
        ruleRepository.save(rule);
        return rule.getId();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Delete rule by id", notes = "")
    public void remove(@PathVariable(name = "id", required = true) int id) throws RouterException {
        ruleRepository.remove(id);
    }

    private RuleDto convertToDto(Rule entity) {
        return modelMapper.map(entity, RuleDto.class);
    }

    private Rule convertToEntity(RuleDto dto) {
        Rule entity = modelMapper.map(dto, Rule.class);
        entity.getParameterValues().clear();
        for (RuleParameterValueDto pvDto : dto.getParameterValues()) {
            RuleParameterValue pv = modelMapper.map(pvDto, RuleParameterValue.class);
            pv.setParameter(parameterRepository.findByName(pvDto.getParameterName()));
            pv.setRule(entity);

            entity.getParameterValues().add(pv);
        }
        return entity;
    }
}
