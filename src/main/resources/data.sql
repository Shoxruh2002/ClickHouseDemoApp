CREATE TABLE test.user_jon
(
    id       String,
    username String,
    password String
)
    ENGINE = MergeTree()
        PRIMARY KEY (id);