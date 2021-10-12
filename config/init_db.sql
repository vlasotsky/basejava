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


create table section
(
	id serial not null
		constraint section_pk
			primary key,
	resume_uuid char(36) not null
		constraint section_resume_uuid_fk
			references resume
				on delete cascade,
	type text not null,
	value text not null
);

alter table section owner to postgres;

create unique index section_type_index
	on section (resume_uuid, type);

