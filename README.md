# PARSER
[![Build Status](https://travis-ci.org/magidin91/job4j_grabber.svg?branch=master)](https://travis-ci.org/magidin91/job4j_grabber)
[![codecov](https://codecov.io/gh/magidin91/job4j_grabber/branch/master/graph/badge.svg)](https://codecov.io/gh/magidin91/job4j_grabber)

### Description

#### Technologies/Frameworks: JSOUP, Quartz, JDBC, SQL(PostgreSQL), Slf4j + Log4j, JUnit


Parser is an application that collects the desired data (JSOUP) from the specified resource with a certain frequency (Quartz).  
Data is saved in the database(JDBC). You can view the obtained data in the browser. (http://localhost:8080/)  
As an example of using the parser collects java jobs with sql.ru.

![alt text](https://github.com/magidin91/job4j_parser/blob/master/src/main/resources/parser.png)




