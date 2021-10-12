package com.basejava.webapp;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.model.*;

import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_DUMMY = UUID.randomUUID().toString();
    public static final String UUID_TEST = UUID.randomUUID().toString();

    public static final Resume RESUME_1;
    public static final Resume RESUME_2;
    public static final Resume RESUME_3;
    public static final Resume RESUME_DUMMY;
    public static final Resume RESUME_TEST;

    static {
//        RESUME_1 = new Resume(UUID_1, "Mary");
//        RESUME_2 = new Resume(UUID_2, "David");
//        RESUME_3 = new Resume(UUID_3, "Zoe");
//        RESUME_DUMMY = new Resume(UUID_DUMMY, "dummy");
//        RESUME_TEST = new Resume(UUID_TEST, "Gabriel");
        RESUME_1 = makeTestResume(UUID_1, "Mary");
        RESUME_2 = makeTestResume(UUID_2, "David");
        RESUME_3 = makeTestResume(UUID_3, "Zoe");
        RESUME_DUMMY = makeTestResume(UUID_DUMMY, "dummy");
        RESUME_TEST = makeTestResume(UUID_TEST, "Gabriel");
    }

    private static Resume makeTestResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        resume.addContact(ContactType.PHONE_NUMBER, "Тел.: +7(921) 855-0482");
        resume.addContact(ContactType.MESSENGER_ACCOUNT, "Skype: grigory.kislin");
        resume.addContact(ContactType.EMAIL_ADDRESS, "Почта: gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN_ACCOUNT, "https://www.linkedin.com/in/gkislin");
        resume.addContact(ContactType.GITHUB_ACCOUNT, "https://github.com/gkislin");
        resume.addContact(ContactType.STACKOVERFLOW_ACCOUNT, "https://stackoverflow.com/users/548473");
        resume.addContact(ContactType.PERSONAL_WEBPAGE, "http://gkislin.ru/");

        resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle",
                "MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy"
        ));
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume.addSection(SectionType.ACHIEVEMENTS,
                new ListSection("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников."
                        , "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk."
                ));

        resume.addSection(SectionType.EXPERIENCE, new OrganizationSection(new Organization(new Link("Java Online Projects", "http://javaops.ru/"), new Organization.Position(2013, 10,
                "Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок.")),
                new Organization(new Link("Wrike", "https://www.wrike.com/"),
                        new Organization.Position(2014, 10, 2016, 1,
                                "Старший разработчик (backend)",
                                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.")
                )
                , new Organization(new Link("RIT Center"), new Organization.Position(2012, 4, 2014, 10,
                "Java архитектор",
                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python")
        )));

        resume.addSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization(new Link("Coursera",
                        "https://www.coursera.org/course/progfun"), new Organization.Position(2013, 3, 2013, 5,
                        "\"Functional Programming Principles in Scala\" by Martin Odersky"))
                , new Organization(new Link("Luxoft",
                "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"), new Organization.Position(2011, 3, 2011, 4,
                "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\""))
                , new Organization(new Link("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "http://www.ifmo.ru/"),
                new Organization.Position(1987, 9, 1993, 7,
                        "Инженер (программист Fortran, C)"),
                new Organization.Position(1993, 9, 1996, 7,
                        "Аспирантура (программист С, С++)"))));
        return resume;
    }
}
