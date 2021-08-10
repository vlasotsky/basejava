package com.basejava.webapp;

import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.model.StringSection;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.OrganisationSection;
import com.basejava.webapp.model.Experience;
import com.basejava.webapp.model.Link;
import com.basejava.webapp.model.Period;
import java.time.YearMonth;

public class ResumeTestData {
    public static void main(String[] args) {
        System.out.println(makeTestResume("uuid50", "Marcus Simpson"));
    }

    public static Resume makeTestResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        resume.saveContact(ContactType.PHONE_NUMBER, "Тел.: +7(921) 855-0482");
        resume.saveContact(ContactType.MESSENGER_ACCOUNT, "Skype: grigory.kislin");
        resume.saveContact(ContactType.EMAIL_ADDRESS, "Почта: gkislin@yandex.ru");
        resume.saveContact(ContactType.LINKEDIN_ACCOUNT, "https://www.linkedin.com/in/gkislin");
        resume.saveContact(ContactType.GITHUB_ACCOUNT, "https://github.com/gkislin");
        resume.saveContact(ContactType.STACKOVERFLOW_ACCOUNT, "https://stackoverflow.com/users/548473");
        resume.saveContact(ContactType.PERSONAL_WEBPAGE, "http://gkislin.ru/");

        resume.saveSection(SectionType.OBJECTIVE, new StringSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.saveSection(SectionType.QUALIFICATIONS, new ListSection() {{
            saveToData("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
            saveToData("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
            saveToData("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle");
            saveToData("MySQL, SQLite, MS SQL, HSQLDB");
            saveToData("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        }});
        resume.saveSection(SectionType.PERSONAL, new StringSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        resume.saveSection(SectionType.ACHIEVEMENTS,
                new ListSection() {{
                    saveToData("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
                    saveToData("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
                    saveToData("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
                    saveToData("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
                    saveToData("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
                    saveToData("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
                }});
        resume.saveSection(SectionType.EXPERIENCE, new OrganisationSection() {{
            saveToData(new Experience(new Link("Java Online Projects", "http://javaops.ru/"), "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.", YearMonth.of(2013, 10), YearMonth.now()));
            saveToData(new Experience(new Link("Wrike", "https://www.wrike.com/"), "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.",
                    YearMonth.of(2014, 10), YearMonth.of(2016, 1)));
            saveToData(new Experience(new Link("RIT Center"), "Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python",
                    YearMonth.of(2012, 4), YearMonth.of(2014, 10)));
        }});
        resume.saveSection(SectionType.EDUCATION, new OrganisationSection() {{
            saveToData(new Experience(new Link("Coursera", "https://www.coursera.org/course/progfun"), "\"Functional Programming Principles in Scala\" by Martin Odersky", YearMonth.of(2013, 3), YearMonth.of(2013, 5)));
            saveToData(new Experience(new Link("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"), "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"", YearMonth.of(2011, 3), YearMonth.of(2011, 4)));
            saveToData(new Experience(new Link("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/")) {{
                savePeriod(new Period("Инженер (программист Fortran, C)", YearMonth.of(1987, 9), YearMonth.of(1993, 7)));
                savePeriod(new Period("Аспирантура (программист С, С++)", YearMonth.of(1993, 9), YearMonth.of(1996, 7)));
            }});
        }});
        return resume;
    }
}
