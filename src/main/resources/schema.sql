CREATE TABLE IF NOT EXISTS person (
                                     id SERIAL PRIMARY KEY,
                                     seq_user INTEGER NOT NULL ,
                                     birth_year INTEGER NOT NULL ,
                                     state VARCHAR NOT NULL ,
                                     country VARCHAR NOT NULL ,
                                     created_date TIMESTAMP NOT NULL,
                                     terms_version DATE NOT NULL,
                                     phone_country VARCHAR NOT NULL,
                                     kyc VARCHAR NOT NULL,
                                     has_email BOOLEAN NOT NULL,
                                     number_of_flags INTEGER NOT NULL,
                                     fraudster BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS country (
                                    code VARCHAR PRIMARY KEY,
                                    name VARCHAR NOT NULL ,
                                    code3 VARCHAR NOT NULL ,
                                    num_code INTEGER NOT NULL ,
                                    phone_code INTEGER NOT NULL
);