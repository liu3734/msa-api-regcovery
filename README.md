# msa-api-regcovery
[![Build Status](https://travis-ci.org/microcmpt/msa-api-regcovery.svg?branch=master)](https://travis-ci.org/microcmpt/msa-api-regcovery) [![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
### Introduction
api-regcovery是服务注册中心，主要提供服务的注册和发现两大功能，解决了传统的将url地址或者ip、端口号硬编码在代码中或者配置在配置文件中的问题，使用
服务注册中心更加灵活，能够动态增添服务，删除修改服务，使得整个架构具有良好的可伸缩性，服务注册中心的具体特点如下：
 - 灵活
 - 可伸缩
 - 高可用
 - 负载均衡(目前只使用随机负载均衡算法)