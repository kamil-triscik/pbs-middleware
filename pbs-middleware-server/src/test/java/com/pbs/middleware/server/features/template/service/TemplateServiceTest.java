package com.pbs.middleware.server.features.template.service;

import com.pbs.middleware.server.features.template.domain.Template;
import com.pbs.middleware.server.features.template.storage.TemplateFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Template service tests")
class TemplateServiceTest {

    @Mock
    Template mockedTemplate;

    @Mock
    TemplateFactory factory;

//    @Mock
//    TemplateEventStorage eventStorage;

    @Mock
    TemplateMapper mapper;

//    @Test
//    @DisplayName("Dependencies test: factory is null")
//    void dependencies_factory_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(null, eventStorage, mapper));
//    }
//
//    @Test
//    @DisplayName("Dependencies test: storage is null")
//    void dependencies_storage_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, null, mapper));
//    }
//
//    @Test
//    @DisplayName("getAll(): test")
//    void getAll_test() {
//        Set<String> allTemplates = new HashSet<>() {{
//            add("templateX");
//        }};
//
//        when(eventStorage.streams()).thenReturn(allTemplates);
//
//        new TemplateService(factory, eventStorage, mapper).getAll();
//    }
//
//    @Test
//    @DisplayName("create(): template id is null test")
//    void create_template_id_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .create(null,"",  new ServiceTemplate(), new QsubParameters())
//        );
//    }
//
//    @Test
//    @DisplayName("create(): template id is blank test")
//    void create_template_id_is_blank_test() {
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .create("","",  new ServiceTemplate(), new QsubParameters())
//        );
//    }
//
//    @Test
//    @DisplayName("create(): service template is null test")
//    void create_service_template_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .create("templateID","",  null, new QsubParameters())
//        );
//    }
//
//    @Test
//    @DisplayName("create(): PBS is null test")
//    void create_PBS_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .create("templateID","",  new ServiceTemplate(), null)
//        );
//    }
//
//    @Test
//    @DisplayName("create(): Template create test")
//    void create_template_test() {
//        String templateId = "templateX";
//        val pbs = new QsubParameters();
//        val tmplService = new ServiceTemplate();
//
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        doNothing().when(mockedTemplate).alreadyExists();
//        doNothing().when(mockedTemplate).create("", tmplService, pbs);
//
//        val template = new TemplateService(factory, eventStorage, mapper).create(templateId,"",  tmplService, pbs);
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).alreadyExists();
//        verify(mockedTemplate, times(1)).create("", tmplService, pbs);
//        Assert.assertEquals(mockedTemplate, template);
//    }
//
//    @Test
//    @DisplayName("update(): template id is null test")
//    void update_template_id_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .update(null,"", new ServiceTemplate(), new QsubParameters())
//        );
//    }
//
//    @Test
//    @DisplayName("update(): service template is null test")
//    void update_service_template_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .update("templateID","",  null, new QsubParameters())
//        );
//    }
//
//    @Test
//    @DisplayName("update(): PBS is null test")
//    void update_PBS_is_null_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper)
//                        .update("templateID","",  new ServiceTemplate(), null)
//        );
//    }
//
//    @Test
//    @DisplayName("update(): Template update with service changes test")
//    void update_template_service_test() {
//        String templateId = "templateX";
//        val pbs = new QsubParameters();
//        val tmplService = new ServiceTemplate();
//        val pbs2 = new QsubParameters();
//        pbs.setJobName("job");
//        val tmplService2 = new ServiceTemplate();
//        tmplService.setGroup("group");
//
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        doNothing().when(mockedTemplate).exists();
//        when(mockedTemplate.getService()).thenReturn(tmplService2);
//        doNothing().when(mockedTemplate).update("", tmplService, pbs);
//
//        val template = new TemplateService(factory, eventStorage, mapper).update(templateId,"",  tmplService, pbs);
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).exists();
//        verify(mockedTemplate, times(1)).getService();
//        verify(mockedTemplate, times(0)).getPbs();
//        verify(mockedTemplate, times(1)).update("", tmplService, pbs);
//        Assert.assertEquals(mockedTemplate, template);
//    }
//
//    @Test
//    @DisplayName("update(): Template update with pbs changes test")
//    void update_template_pbs_test() {
//        String templateId = "templateX";
//        val pbs = new QsubParameters();
//        val tmplService = new ServiceTemplate();
//        val pbs2 = new QsubParameters();
//        pbs.setJobName("job");
//
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        doNothing().when(mockedTemplate).exists();
//        when(mockedTemplate.getService()).thenReturn(tmplService);
//        when(mockedTemplate.getPbs()).thenReturn(pbs2);
//        doNothing().when(mockedTemplate).update("", tmplService, pbs);
//
//        val template = new TemplateService(factory, eventStorage, mapper).update(templateId, "", tmplService, pbs);
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).exists();
//        verify(mockedTemplate, times(1)).getService();
//        verify(mockedTemplate, times(1)).getPbs();
//        verify(mockedTemplate, times(1)).update("", tmplService, pbs);
//        Assert.assertEquals(mockedTemplate, template);
//    }
//
//    @Test
//    @DisplayName("update(): Template update without changes test")
//    void update_template_without_changes_test() {
//        String templateId = "templateX";
//        val pbs = new QsubParameters();
//        val tmplService = new ServiceTemplate();
//
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        doNothing().when(mockedTemplate).exists();
//        when(mockedTemplate.getService()).thenReturn(tmplService);
//        when(mockedTemplate.getPbs()).thenReturn(pbs);
//
//        val service = spy(new TemplateService(factory, eventStorage, mapper));
//
//        val template = service.update(templateId, "", tmplService, pbs);
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).exists();
//        verify(mockedTemplate, times(1)).getService();
//        verify(mockedTemplate, times(1)).getPbs();
//        verify(mockedTemplate, times(0)).update("", tmplService, pbs);
//        Assert.assertEquals(mockedTemplate, template);
//    }
//
//    @Test
//    @DisplayName("get(): NULL argument test test")
//    void get_with_null_argument_test() {
//        assertThrows(
//                NullPointerException.class,
//                () -> new TemplateService(factory, eventStorage, mapper).get(null)
//        );
//    }
//
//    @Test
//    @DisplayName("get(): Template does not exists test")
//    void get_template_does_not_exists_test() {
//        String templateId = "templateX";
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        val exception = new TemplateNotFoundException(templateId);
//        doThrow(exception)
//                .when(mockedTemplate).exists();
//
//        assertThrows(
//                TemplateNotFoundException.class,
//                () -> new TemplateService(factory, eventStorage, mapper).get(templateId)
//        );
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).exists();
//
//    }
//
//    @Test
//    @DisplayName("get(): Template exists test")
//    void get_template_does_exists_test() {
//        String templateId = "templateX";
//        when(factory.get(templateId)).thenReturn(Optional.of(mockedTemplate));
//        doNothing().when(mockedTemplate).exists();
//
//        val template = new TemplateService(factory, eventStorage, mapper).get(templateId);
//
//        verify(factory, times(1)).get(templateId);
//        verify(mockedTemplate, times(1)).exists();
//        assertEquals(template, mockedTemplate);
//    }
}