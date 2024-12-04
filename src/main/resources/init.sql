CREATE TABLE `db_proxy` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `proxy` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                            `service` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                            `content` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `proxy_unique` (`proxy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci