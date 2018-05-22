package ru.emias.router;

import java.util.List;

import org.junit.Assert;
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
public class ParameterServiceTests {

    @Autowired
    private ParameterApplicationService parameterService;

    @Autowired
    private RuleApplicationService ruleService;

    @Test
    public void testSave() throws Exception {
        ParameterDto p = new ParameterDto("par1", EParameterType.TEXT, "desc", 1);
        parameterService.save(p);
        List<ParameterDto> parameters = parameterService.findAll();
        Assert.assertEquals(1, parameters.size());
    }

    @Test
    public void testSaveNewParameterWithSameName() throws Exception {
        parameterService.save(new ParameterDto("p1", EParameterType.TEXT, "desc", 1));

        // test creating new with existing name
        try {
            parameterService.save(new ParameterDto("p1", EParameterType.TEXT, "desc", 1));
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.PARAMETER_NAME_IS_ALREADY_USED, e.getCode());
        }
        Assert.assertEquals(1, parameterService.findAll().size());

        // test changing name to existing one
        ParameterDto p2 = new ParameterDto("p2", EParameterType.TEXT, "desc", 1);
        p2.setId(parameterService.save(p2));
        Assert.assertNotNull(p2.getId());
        Assert.assertEquals(2, parameterService.findAll().size());

        p2.setName("p1");
        try {
            parameterService.save(p2);
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.PARAMETER_NAME_IS_ALREADY_USED, e.getCode());
        }
        Assert.assertEquals(2, parameterService.findAll().size());
    }

    @Test
    public void testEditParameterNameToAlreadyExisted() throws Exception {
        parameterService.save(new ParameterDto("p1", EParameterType.TEXT, "desc", 1));

        ParameterDto p2 = new ParameterDto("p2", EParameterType.TEXT, "desc", 1);
        p2.setId(parameterService.save(p2));
        Assert.assertNotNull(p2.getId());
        Assert.assertEquals(2, parameterService.findAll().size());

        p2.setName("p1");
        try {
            parameterService.save(p2);
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.PARAMETER_NAME_IS_ALREADY_USED, e.getCode());
        }
        Assert.assertEquals(2, parameterService.findAll().size());
    }

    @Test
    public void testDelete() throws Exception {
        ParameterDto p = new ParameterDto("p1", EParameterType.TEXT, "desc", 1);
        int id = parameterService.save(p);
        Assert.assertEquals(1, parameterService.findAll().size());

        parameterService.remove(id);
        Assert.assertEquals(0, parameterService.findAll().size());
    }

    @Test
    public void testDeletePameterWithLinkedRules() throws Exception {
        ParameterDto p = new ParameterDto("p1", EParameterType.TEXT, "desc", 1);
        p.setId(parameterService.save(p));
        Assert.assertEquals(1, parameterService.findAll().size());

        RuleDto r = new RuleDto("r1", "/r1");
        r.getParameterValues().add(new RuleParameterValueDto("p1", true, EOperationType.EQ));
        r.setId(ruleService.save(r));

        try {
            parameterService.remove(p.getId());
            Assert.fail();
        }
        catch (RouterException e) {
            Assert.assertEquals(ExceptionCodes.PARAMETER_IS_USED_IN_RULES, e.getCode());
        }
        Assert.assertEquals(1, parameterService.findAll().size());
    }

}
