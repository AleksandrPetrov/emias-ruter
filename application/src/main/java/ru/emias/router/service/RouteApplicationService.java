package ru.emias.router.service;

import io.swagger.annotations.ApiOperation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.emias.router.domain.entity.Parameter;
import ru.emias.router.domain.entity.Rule;
import ru.emias.router.domain.exception.ExceptionCodes;
import ru.emias.router.domain.exception.RouterException;
import ru.emias.router.domain.repository.ParameterRepository;
import ru.emias.router.domain.repository.RuleRepository;
import ru.emias.router.dto.RequstParameterAndValueDto;
import ru.emias.router.dto.RouteUrlResponseDto;

@CrossOrigin
@RequestMapping("/rs/route")
@RestController
@Transactional
@Validated
public class RouteApplicationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Find Route", notes = "")
    public RouteUrlResponseDto findRoute(@RequestBody List<RequstParameterAndValueDto> routeRequestValues)
            throws RouterException {
        Map<Parameter, String> mapParameterToValue = new HashMap<Parameter, String>();
        for (RequstParameterAndValueDto paramAndValue : routeRequestValues) {

            Parameter parameter = parameterRepository.findByName(paramAndValue.getParamaterName());
            if (parameter == null)
                throw new RouterException(ExceptionCodes.UNKNOWN_PARAMETER, "Не найдено правило маршрутизации");

            if (mapParameterToValue.containsKey(parameter))
                throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, String.format("Непредвиденная ошибка. Параметр %s был указан несколько раз.", paramAndValue.getParamaterName()));

            if (!parameter.getParameterType().checkValueOfCurrentType(paramAndValue.getValue()))
                throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, //
                String.format("Непредвиденная ошибка. Значение %s указанное для параметра %s не сотвествуют его типу %s.", paramAndValue.getValue(), parameter.getName(), parameter.getParameterType()));

            mapParameterToValue.put(parameter, paramAndValue.getValue());
        }

        Map<Parameter, String> sortedByRankMapParameterToValue = new LinkedHashMap<Parameter, String>();
        mapParameterToValue.entrySet().stream().sorted(new Comparator<Entry<Parameter, String>>() {
            public int compare(Entry<Parameter, String> arg0, Entry<Parameter, String> arg1) {
                Integer r0 = arg0.getKey().getRank() == null ? Integer.MAX_VALUE : arg0.getKey().getRank();
                Integer r1 = arg1.getKey().getRank() == null ? Integer.MAX_VALUE : arg1.getKey().getRank();
                return r0.equals(r1) ? arg0.getKey().getId().compareTo(arg1.getKey().getId()) : r0.compareTo(r1);
            }
        }).forEach(entry -> sortedByRankMapParameterToValue.put(entry.getKey(), entry.getValue()));

        Rule rule = ruleRepository.findRuleForSpecifiedValues(sortedByRankMapParameterToValue);
        if (rule == null)
            throw new RouterException(ExceptionCodes.UNKNOWN_PARAMETER, "Не найдено правило маршрутизации");
        else
            return new RouteUrlResponseDto(routeRequestValues, rule.getTargetUrl());
    }
}
