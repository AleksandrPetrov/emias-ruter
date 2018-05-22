package ru.emias.router;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import ru.emias.router.domain.entity.EOperationType;
import ru.emias.router.domain.entity.EParameterType;
import ru.emias.router.domain.exception.ExceptionCodes;
import ru.emias.router.domain.exception.RouterException;
import ru.emias.router.domain.repository.ParameterRepository;
import ru.emias.router.domain.repository.RuleRepository;
import ru.emias.router.dto.ParameterDto;
import ru.emias.router.dto.RequstParameterAndValueDto;
import ru.emias.router.dto.RuleDto;
import ru.emias.router.dto.RuleParameterValueDto;
import ru.emias.router.service.ParameterApplicationService;
import ru.emias.router.service.RouteApplicationService;
import ru.emias.router.service.RuleApplicationService;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(value = {RouteApplicationService.class, ParameterApplicationService.class, RuleApplicationService.class,
        ParameterRepository.class, RuleRepository.class})
public class RouteServiceTests {

    @Autowired
    private RouteApplicationService routeService;

    @Autowired
    private ParameterApplicationService parameterService;

    @Autowired
    private RuleApplicationService ruleService;

    @Before
    public void initParameters() throws Exception {
        ParameterDto p_medicalEntityID = new ParameterDto("medicalEntityID", EParameterType.TEXT, "ИД ЛПУ", 1);
        p_medicalEntityID.setId(parameterService.save(p_medicalEntityID));

        ParameterDto p_roleID = new ParameterDto("roleID", EParameterType.NUMERIC, "ИД роли пользователя", 2);
        p_roleID.setId(parameterService.save(p_roleID));

        ParameterDto p_newUI = new ParameterDto("newUI", EParameterType.TEXT, "p_newUI", 3);
        p_newUI.setId(parameterService.save(p_newUI));

        RuleDto r1 = new RuleDto("r1", "/r1");
        r1.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r1.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5", "20", "7", "41"));
        r1.getParameterValues().add(new RuleParameterValueDto("newUI", false, EOperationType.NE, "1"));
        r1.setId(ruleService.save(r1));

        RuleDto r2 = new RuleDto("r2", "/r2");
        r2.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", false, EOperationType.EQ, "1"));
        r2.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5"));
        r2.setId(ruleService.save(r2));

        RuleDto r3 = new RuleDto("r3", "/r3");
        r3.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r3.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5"));
        r3.setId(ruleService.save(r3));
    }

    @Test
    public void testRouteWithUnknownParameter() throws Exception {
        try {
            routeService.findRoute(Arrays.asList(new RequstParameterAndValueDto("unknownParam", "1")));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.UNKNOWN_PARAMETER, e.getCode());
        }
    }

    @Test
    public void testRouteWithSameParameter() throws Exception {
        try {
            routeService.findRoute(Arrays.asList( //
            new RequstParameterAndValueDto("medicalEntityID", "1"), //
                new RequstParameterAndValueDto("medicalEntityID", "2")));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.UNKNOWN_EXCEPTION, e.getCode());
        }
    }

    @Test
    public void testRouteWithIncorrectValueForParam() throws Exception {
        try {
            routeService.findRoute(Arrays.asList(new RequstParameterAndValueDto("roleID", "test")));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.UNKNOWN_EXCEPTION, e.getCode());
        }
    }

    @Test
    public void testSuccess() throws Exception {
        Assert.assertEquals("/r1", routeService.findRoute(Arrays.asList(//
        new RequstParameterAndValueDto("medicalEntityID", "99"),//
            new RequstParameterAndValueDto("roleID", "5"),//
            new RequstParameterAndValueDto("newUI", "99")//
        )).getTargetUrl());

        Assert.assertEquals("/r2", routeService.findRoute(Arrays.asList(//
        new RequstParameterAndValueDto("medicalEntityID", "1"),//
            new RequstParameterAndValueDto("roleID", "5")//
        )).getTargetUrl());

        Assert.assertEquals("/r3", routeService.findRoute(Arrays.asList(//
        new RequstParameterAndValueDto("medicalEntityID", "99"),//
            new RequstParameterAndValueDto("roleID", "5")//
        )).getTargetUrl());
    }
}
