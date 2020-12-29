<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1. 大数据特点](#1-%E5%A4%A7%E6%95%B0%E6%8D%AE%E7%89%B9%E7%82%B9)
  - [1.1 大量（Volume）](#11-%E5%A4%A7%E9%87%8Fvolume)
  - [1.2 高速（Velocity）](#12-%E9%AB%98%E9%80%9Fvelocity)
  - [1.3 多样（Variety）](#13-%E5%A4%9A%E6%A0%B7variety)
  - [1.4 低价值密度（Value）](#14-%E4%BD%8E%E4%BB%B7%E5%80%BC%E5%AF%86%E5%BA%A6value)
- [2. Hadoop是什么](#2-hadoop%E6%98%AF%E4%BB%80%E4%B9%88)
  - [2.1 狭义](#21-%E7%8B%AD%E4%B9%89)
  - [2.2 广义](#22-%E5%B9%BF%E4%B9%89)
- [3. Hadoop三大发行版本](#3-hadoop%E4%B8%89%E5%A4%A7%E5%8F%91%E8%A1%8C%E7%89%88%E6%9C%AC)
  - [3.1 Apache](#31-apache)
  - [3.2 Cloudera](#32-cloudera)
  - [3.3 Hortonworks](#33-hortonworks)
- [4. Hadoop的优势](#4-hadoop%E7%9A%84%E4%BC%98%E5%8A%BF)
  - [4.1 高可靠性](#41-%E9%AB%98%E5%8F%AF%E9%9D%A0%E6%80%A7)
  - [4.2 高扩展性](#42-%E9%AB%98%E6%89%A9%E5%B1%95%E6%80%A7)
  - [4.3 高效性](#43-%E9%AB%98%E6%95%88%E6%80%A7)
  - [4.4 高容错性](#44-%E9%AB%98%E5%AE%B9%E9%94%99%E6%80%A7)
- [5. Hadoop的组成（面试重点）](#5-hadoop%E7%9A%84%E7%BB%84%E6%88%90%E9%9D%A2%E8%AF%95%E9%87%8D%E7%82%B9)
- [6. Hadoop的运行环境的搭建](#6-hadoop%E7%9A%84%E8%BF%90%E8%A1%8C%E7%8E%AF%E5%A2%83%E7%9A%84%E6%90%AD%E5%BB%BA)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 1. 大数据特点

### 1.1 大量（Volume）

### 1.2 高速（Velocity）

### 1.3 多样（Variety）

结构化数据，半结构化数据，非结构化数据

### 1.4 低价值密度（Value）

一堆数据里面有用的就那么一丢丢

## 2. Hadoop是什么

### 2.1 狭义

它就是一款开源的软件

### 2.2 广义

大数据技术的生态圈的代名词，围绕 Hadoop 衍生出来的技术和框架有很多

## 3. Hadoop三大发行版本

### 3.1 Apache

Apache 版本最原始（最基础）的版本，对于入门学习最好。
> [官网地址](http://hadoop.apache.org)
> [下载地址](https://hadoop.apache.org/releases.html)

### 3.2 Cloudera

Cloudera 内部集成了很多大数据框架。对应产品 CDH。
> [官网地址](https://www.cloudera.com/downloads/cdh)
> [下载地址](https://docs.cloudera.com/documentation/enterprise/6/release-notes/topics/rg_cdh_6_download.html)

### 3.3 Hortonworks

Hortonworks 文档较好。对应产品 HDP。
> [官网地址](https://hortonworks.com/products/data-center/hdp/)
> [下载地址](https://hortonworks.com/downloads/#data-platform)

> Hortonworks 在2018年已经被 Cloudera 公司收购，推出新的品牌 CDP。

## 4. Hadoop的优势

### 4.1 高可靠性

Hadoop 底层维护多个数据副本，所以即使 Hadoop 某个计算元素或存储出现故障，也不会导致数据的丢失。

### 4.2 高扩展性

在集群间分配任务数据，可方便的扩展数以千计的节点。

### 4.3 高效性

在 MapReduce 的思想下，Hadoop 是并行工作的，以加快任务处理速度。

### 4.4 高容错性

能够自动将失败的任务重新分配。

## 5. Hadoop的组成（面试重点）

![Hadoop各版本组成区别.png](https://i.loli.net/2020/12/29/n1vlZufaY5iW9Nw.png)

如上图所示，在 Hadoop1.x 时代，Hadoop 中的 MapReduce 同时处理业务逻辑运算和资源调度，耦合性较大。

在 Hadoop2.x 时代，增加了 Yarn。Yarn 只负责资源得调度，MapReduce 只负责运算。

Hadoop3.x 在组成上没有变化。

1. **HDFS**
   1. **NameNode**：管理真实数据块的元数据的，管理多个 DataNode（大哥）
   2. **DataNode**：对真实数据块进行存储管理（小弟）
   3. **SecondaryNameNode**：是 NameNode 的助手，帮助 NameNode 完成一些事情
2. **Yarn**
   1. **ResourceManager**：统筹管理每一个NodeManager，负责接收每一个作业请求
   2. **NodeManager**: 负责每一台服务器的资源调度，而且实时保证和ResourceManager 状态的汇报
   3. **ApplicationMaster**：只有Job提交的时候才会产生一个对象
   4. **Container**：对计算机的资源的一个抽象的封装对象

![Yarn架构.png](https://i.loli.net/2020/12/29/myRax5GkhqA6CQe.png)

3. **MapReduce**
   1. Map 阶段并行处理输入数据
   2. Reduce 阶段对 Map 结果进行汇总

![MapReduce示意图.png](https://i.loli.net/2020/12/29/T9186twScagCzkD.png)

## 6. Hadoop的运行环境的搭建

hhh
