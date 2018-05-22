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

import ru.emias.router.domain.entity.Parameter;
import ru.emias.router.domain.exception.ExceptionCodes;
import ru.emias.router.domain.exception.RouterException;
import ru.emias.router.domain.repository.ParameterRepository;
import ru.emias.router.domain.repository.RuleRepository;
import ru.emias.router.dto.ParameterDto;

@CrossOrigin
@RequestMapping("/rs/parameter")
@RestController
@Transactional
@Validated
public class ParameterApplicationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get All Parameters sorted by rank", notes = "")
    public List<ParameterDto> findAll() {
        return parameterRepository.findAll().stream().map(param -> convertToDto(param)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Parameter by id", notes = "")
    public ParameterDto findById(@PathVariable(name = "id", required = true) int id) {
        return convertToDto(parameterRepository.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save parameter", notes = "")
    public Integer save(@Valid @RequestBody ParameterDto parameterDto) throws RouterException {
        Parameter parameter = convertToEntity(parameterDto);
        // проверяем не поменяли ли тип правила
        if (parameter.getId() != null) {
            Parameter oldParam = parameterRepository.findById(parameter.getId());
            if (oldParam != null && !oldParam.getParameterType().equals(parameter.getParameterType())) {
                boolean parameterIsUsed = ruleRepository.checkIfParameterIsUsedInRules(parameter.getId());
                if (parameterIsUsed)
                    throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, "Непредвиденная ошибка. Изменять тип у используемого в правилах параметра - не допускается.");
            }
        }

        parameterRepository.save(parameter);
        return parameter.getId();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Delete Parameter by id", notes = "")
    public void remove(@PathVariable(name = "id", required = true) int id) throws RouterException {
        // проверка на удаление используемого в правилах параметра
        boolean parameterIsUsed = ruleRepository.checkIfParameterIsUsedInRules(id);
        Parameter parameter = parameterRepository.findById(id);
        if (parameterIsUsed)
            throw new RouterException(ExceptionCodes.PARAMETER_IS_USED_IN_RULES, String.format("Удаление параметра %s недопустимо, пока есть активное правило сформированное этим параметром.", parameter.getName()));

        parameterRepository.remove(parameter);
    }

    private ParameterDto convertToDto(Parameter entity) {
        return modelMapper.map(entity, ParameterDto.class);
    }

    private Parameter convertToEntity(ParameterDto dto) {
        return modelMapper.map(dto, Parameter.class);
    }
}
