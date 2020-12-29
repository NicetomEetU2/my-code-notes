<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1. 大数据特点](#1-大数据特点)
  - [1.1 大量（Volume）](#11-大量volume)
  - [1.2 高速（Velocity）](#12-高速velocity)
  - [1.3 多样（Variety）](#13-多样variety)
  - [1.4 低价值密度（Value）](#14-低价值密度value)
- [2. Hadoop是什么](#2-hadoop是什么)
  - [2.1 狭义](#21-狭义)
  - [2.2 广义](#22-广义)
- [3. Hadoop三大发行版本](#3-hadoop三大发行版本)
  - [3.1 Apache](#31-apache)
  - [3.2 Cloudera](#32-cloudera)
  - [3.3 Hortonworks](#33-hortonworks)
- [4. Hadoop的优势](#4-hadoop的优势)
  - [4.1 高可靠性](#41-高可靠性)
  - [4.2 高扩展性](#42-高扩展性)
  - [4.3 高效性](#43-高效性)
  - [4.4 高容错性](#44-高容错性)
- [5. Hadoop的组成（面试重点）](#5-hadoop的组成面试重点)
- [6. Hadoop的运行环境的搭建](#6-hadoop的运行环境的搭建)

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
