package li.dao.derby;

import li.annotation.Table;
import li.dao.Record;

@Table(value = "t_account")
public class Account extends Record<Account> {
    private static final long serialVersionUID = -5186608065079631307L;
}