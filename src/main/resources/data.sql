CREATE TABLE test.user_jon
(
    id       UInt64,
    username String,
    password String
)
    ENGINE = MergeTree()
        PRIMARY KEY (id);