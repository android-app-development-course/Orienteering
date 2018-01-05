# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
import datetime
import ast
 

# Create your models here.
class Account(models.Model):
	ID=models.IntegerField(primary_key=True)
	AccountNumber=models.CharField(max_length=30,default="")
	Password=models.CharField(max_length=30,default="")
	Name=models.CharField(max_length=30,default="")
	Sex=models.BooleanField()
	Birthday=models.DateField(default="1970-1-1")
	Weight=models.FloatField(default=0)
	def __unicode__(self):
		return self.Name+" "+self.AccountNumber
class List(object):
	def __init__(self,list=[]):
		self.list=list
class Map(models.Model):
	ID=models.IntegerField(primary_key=True)
	Name=models.CharField(max_length=30,default="")
	Information=models.CharField(max_length=300,default="")
	Length=models.FloatField(default=0)
	Points=models.CharField(max_length=500,default="")
	def __unicode__(self):
		return self.Name+" "+self.Information
class Match(models.Model):
	ID=models.IntegerField(primary_key=True)
	MapofMatch=models.ForeignKey(Map)
	BeginTime=models.DateTimeField()
	EndTime=models.DateTimeField()
	Name=models.CharField(max_length=30,default="")
	Password=models.CharField(max_length=30,default="")
	Creator=models.ForeignKey(Account)
	Type=models.BooleanField()
	Length=models.FloatField(default=0)
	Number=models.IntegerField(default=1)
	def __unicode__(self):
		return self.Name
class Team(models.Model):
	ID=models.IntegerField(primary_key=True)
	Match=models.ForeignKey(Match)
	BeginTime=models.DateTimeField()
	EndTime=models.DateTimeField()
	Name=models.CharField(max_length=30,default="")
	Number=models.IntegerField(default=1)
	Points=models.CharField(max_length=500,blank=True,default="")
	CID=models.CharField(max_length=3000,blank=True,default="")
	def __unicode__(self):
		return self.Name +" Match:"+ self.Match.Name
class MatchOfPerson(models.Model):
	Account=models.ForeignKey(Account)
	Match=models.ForeignKey(Match)
	Team=models.ForeignKey(Team)
	Points=models.CharField(max_length=50000,blank=True,default="")
	Length=models.FloatField(default=0)
	def __unicode__(self):
		return self.Account.Name +" Match:"+ self.Match.Name
class Question(models.Model):
	Question=models.CharField(max_length=300,default="")
	Answer=models.BooleanField()
	def __unicode__(self):
		return self.Question

