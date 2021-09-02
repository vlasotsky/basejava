package com.basejava.webapp.storage.strategy;

import com.basejava.webapp.model.Link;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Organisation;
import com.basejava.webapp.model.OrganisationSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.TextSection;
import com.basejava.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerializer implements StreamSerializer {
    private XmlParser xmlParser;

    public XmlStreamSerializer() {
        xmlParser = new XmlParser(
                Resume.class, Organisation.class, Link.class,
                OrganisationSection.class, TextSection.class, ListSection.class, Organisation.Position.class);
    }

    @Override
    public void doWrite(OutputStream outputStream, Resume resume) throws IOException {
        try (Writer w = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            xmlParser.marshall(resume, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}