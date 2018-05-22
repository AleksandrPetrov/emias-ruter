package ru.emias.router;

import java.util.Arrays;
import java.util.List;

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
import ru.emias.router.dto.RuleDto;
import ru.emias.router.dto.RuleParameterValueDto;
import ru.emias.router.service.ParameterApplicationService;
import ru.emias.router.service.RuleApplicationService;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(value = {ParameterApplicationService.class, RuleApplicationService.class, ParameterRepository.class,
        RuleRepository.class})
public class RuleServiceTests {

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
    }

    @Test
    public void testSave() throws Exception {
        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5", "20", "7", "41"));
        r.getParameterValues().add(new RuleParameterValueDto("newUI", false, EOperationType.EQ, "1"));
        r.setId(ruleService.save(r));

        List<RuleDto> rules = ruleService.findAll();
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(3, rules.get(0).getParameterValues().size());

        parameterService.save(new ParameterDto("p4", EParameterType.TEXT, "p4", 4));

        r = ruleService.findById(r.getId());
        r.getParameterValues().get(1).setValues(Arrays.asList("1", "2"));
        r.getParameterValues().get(1).setOperationType(EOperationType.NE);
        r.getParameterValues().add(new RuleParameterValueDto("p4", true, EOperationType.EQ));
        r.setId(ruleService.save(r));

        rules = ruleService.findAll();
        Assert.assertEquals(1, rules.size());
        Assert.assertEquals(4, rules.get(0).getParameterValues().size());
    }

    @Test
    public void testSaveWithNoValues() throws Exception {
        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", false, EOperationType.EQ));
        try {
            r.setId(ruleService.save(r));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.UNKNOWN_EXCEPTION, e.getCode());
        }
    }

    @Test
    public void testSaveWithTwoSameParameters() throws Exception {
        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", false, EOperationType.EQ, "5", "20", "7", "41"));

        try {
            r.setId(ruleService.save(r));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.TWO_SAME_PARAMETERS_IN_ONE_RULE, e.getCode());
        }

        Assert.assertEquals(0, ruleService.findAll().size());
    }

    @Test
    public void testSaveDoubleRule() throws Exception {
        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5", "20", "7", "41"));
        r.setId(ruleService.save(r));
        Assert.assertEquals(1, ruleService.findAll().size());

        RuleDto r2 = new RuleDto("r2", "/r3");
        r2.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r2.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "1", "2"));
        r2.setId(ruleService.save(r2));
        Assert.assertEquals(2, ruleService.findAll().size());

        RuleDto r3 = new RuleDto("r3", "/r3");
        r3.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r3.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5", "20", "41", "7"));
        try {
            r3.setId(ruleService.save(r3));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.RULES_WITH_SAME_VALUES, e.getCode());
        }

        RuleDto r4 = new RuleDto("r4", "/r4");
        r4.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r4.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "2", "1"));
        try {
            r4.setId(ruleService.save(r4));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.RULES_WITH_SAME_VALUES, e.getCode());
        }

        RuleDto r5 = new RuleDto("r5", "/r5");
        r5.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r5.getParameterValues().add(new RuleParameterValueDto("roleID", true, EOperationType.EQ, "1", "2"));
        r5.setId(ruleService.save(r5));
        Assert.assertEquals(3, ruleService.findAll().size());

        RuleDto r6 = new RuleDto("r6", "/r6");
        r6.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r6.getParameterValues().add(new RuleParameterValueDto("roleID", true, EOperationType.EQ));
        try {
            r6.setId(ruleService.save(r6));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.RULES_WITH_SAME_VALUES, e.getCode());
        }

    }

    @Test
    public void testDelete() throws Exception {
        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("medicalEntityID", true, EOperationType.EQ));
        r.getParameterValues().add(new RuleParameterValueDto("roleID", false, EOperationType.EQ, "5", "20", "7", "41"));
        r.getParameterValues().add(new RuleParameterValueDto("newUI", false, EOperationType.EQ, "1"));
        r.setId(ruleService.save(r));

        List<RuleDto> rules = ruleService.findAll();
        Assert.assertEquals(1, rules.size());

        ruleService.remove(r.getId());
        Assert.assertEquals(0, ruleService.findAll().size());
    }

}
