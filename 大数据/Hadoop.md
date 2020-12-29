<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [一、大数据特点](#一大数据特点)
  - [大量（Volume）](#大量volume)
  - [高速（Velocity）](#高速velocity)
  - [多样（Variety）](#多样variety)
  - [低价值密度（Value）](#低价值密度value)
- [二、Hadoop是什么](#二hadoop是什么)
- [三、Hadoop三大发行版本](#三hadoop三大发行版本)
  - [1. Apache](#1-apache)
  - [2. Cloudera](#2-cloudera)
  - [3. Hortonworks](#3-hortonworks)
- [三、Hadoop的优势](#三hadoop的优势)
  - [1. 高可靠性](#1-高可靠性)
  - [2. 高扩展性](#2-高扩展性)
  - [3. 高效性](#3-高效性)
  - [4. 高容错性](#4-高容错性)
- [四、Hadoop的组成](#四hadoop的组成)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 一、大数据特点

### 大量（Volume）

### 高速（Velocity）

### 多样（Variety）

结构化数据，半结构化数据，非结构化数据

### 低价值密度（Value）

一堆数据里面有用的就那么一丢丢

## 二、Hadoop是什么

**狭义**：它就是一款开源的软件

**广义**：大数据技术的生态圈的代名词，围绕 Hadoop 衍生出来的技术和框架有很多

## 三、Hadoop三大发行版本

### 1. Apache

Apache 版本最原始（最基础）的版本，对于入门学习最好。
> [官网地址](http://hadoop.apache.org)
> [下载地址](https://hadoop.apache.org/releases.html)

### 2. Cloudera

Cloudera 内部集成了很多大数据框架。对应产品 CDH。
> [官网地址](https://www.cloudera.com/downloads/cdh)
> [下载地址](https://docs.cloudera.com/documentation/enterprise/6/release-notes/topics/rg_cdh_6_download.html)

### 3. Hortonworks

Hortonworks 文档较好。对应产品 HDP。
> [官网地址](https://hortonworks.com/products/data-center/hdp/)
> [下载地址](https://hortonworks.com/downloads/#data-platform)

> Hortonworks 在2018年已经被 Cloudera 公司收购，推出新的品牌 CDP。

## 三、Hadoop的优势

### 1. 高可靠性

Hadoop 底层维护多个数据副本，所以即使 Hadoop 某个计算元素或存储出现故障，也不会导致数据的丢失。

### 2. 高扩展性

在集群间分配任务数据，可方便的扩展数以千计的节点。

### 3. 高效性

在 MapReduce 的思想下，Hadoop 是并行工作的，以加快任务处理速度。

### 4. 高容错性

能够自动将失败的任务重新分配。

## 四、Hadoop的组成


