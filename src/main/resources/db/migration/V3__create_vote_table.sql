CREATE TABLE VOTE (
    ID BIGSERIAL PRIMARY KEY,
    ID_VOTING_SESSION BIGSERIAL NOT NULL,
    ID_MEMBER BIGINT NOT NULL,
    VOTE_VALUE VARCHAR(3) NOT NULL,
    FOREIGN KEY (ID_VOTING_SESSION) REFERENCES VOTING_SESSION(id)
);
