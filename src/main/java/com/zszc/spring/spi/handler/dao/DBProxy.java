package com.zszc.spring.spi.handler.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Luiz
 * @version 1.0
 * Date: 2024/11/25 10:13
 * Description: No Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DBProxy {
    private Long id;
    private String service;
    private String proxy;
    private String content;
}
