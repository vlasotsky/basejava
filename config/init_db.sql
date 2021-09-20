CREATE TABLE resume
    (
        uuid CHAR(36) NOT NULL PRIMARY KEY,
        full_name TEXT NOT NULL
    );

CREATE TABLE contact
    (
        id SERIAL,
        resume_uuid CHAR(36) NOT NULL REFERENCES resume(uuid) ON DELETE CASCADE,
        type TEXT NOT NULL,
        values TEXT NOT NULL
    );

-- ALTER TABLE resume OWNER TO postgres;
-- ALTER TABLE contact OWNER TO postgres;

CREATE UNIQUE INDEX contact_uuid_type_index ON contact (resume_uuid, type);


