# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from Account.models import *
import json
from django.http import HttpResponse
import random,ast,time,shutil
dir='picofaccount'
#
#createTest
def Test1(request):
	# newMap=Map(ID=1111,Name=u"测试",Information=u"测试",Length=3.3) 
	# newMap.Points.list=[[1,2.5,3.5,4],[2,4.5,5.5,6]]
	# newMap.save()

	# newAccount=Account(ID=11111111,AccountNumber='13927111129',
	# 	Password='123456',Name=u'测试',Sex=True,Weight=3.4,Birthday='1970-1-2')
	# newAccount.save()

	# newMatch=Match(ID=11111,MapofMatch=Map.objects.get(ID=1111),
	# 	BeginTime='2017-01-01 12:00:00',EndTime='2017-01-01 14:00:00',
	# 	Name=u'测试',Password='123456',Creator=Account.objects.get(ID=11111111),
	# 	Type=True,Length=Map.objects.get(ID=1111).Length,Number=1)
	# newMatch.save()

	# newTeam=Team(ID=111111,Match=Match.objects.get(ID=11111),BeginTime='2017-1-1 12:00:00',
	# 	EndTime='2017-1-1 14:00:00',Name=u'测试')
	# newTeam.save()

	# newMOP=MatchOfPerson(Account=Account.objects.get(ID=11111111),
	# 	Match=Match.objects.get(ID=11111),
	# 	Team=Team.objects.get(ID=111111))
	# newMOP.save()
	return HttpResponse(json.dumps({
		"Result":"SUCCEED"
		},ensure_ascii=False))
#create account
def createAccount(request):
	accountnumber=request.GET['AccountNumber']
	password=request.GET['Password']
	name=request.GET['Name']
	sex=bool(int(request.GET['Sex']))
	birth=request.GET['Birthday']
	weight=float(request.GET['Weight'])
	if len(Account.objects.filter(AccountNumber=accountnumber))!=0:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":"HAVE SAME ACCOUNTNUMBER",
		"ID":0
		},ensure_ascii=False))
	id=random.randint(10000000,99999999)
	while len(Account.objects.filter(ID=id))!=0:
		id=random.randint(10000000,99999999)
	newAccount=Account(ID=id,AccountNumber=accountnumber,
		Password=password,Name=name,Sex=sex,Birthday=birth,
		Weight=weight)
	
	shutil.copy('/root/hello/h1.jpg','/home/root/hello/static/'+str(id)+'.jpg')
	newAccount.save()
	return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":"",
		"ID":id
		},ensure_ascii=False))
#change password
def changePassword(request):
	id=int(request.GET['ID'])
	op=request.GET['oldPassword']
	np=request.GET['newPassword']
	account=Account.objects.get(ID=id)
	if op==account.Password:
		account.Password=np
		account.save()
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":""
		}))
	else:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":"WRONG OLDPASSWORD"
		},ensure_ascii=False))
#change Info
def changeInfo(request):
	id=int(request.GET['ID'])
	name=request.GET['Name']
	sex=bool(int(request.GET['Sex']))
	birth=request.GET['Birthday']
	weight=float(request.GET['Weight'])
	account=Account.objects.get(ID=id)
	account.Name=name
	account.Sex=sex
	account.Birthday=birth
	account.Weight=weight
	account.save()
	return HttpResponse(json.dumps({
	"Result":"SUCCEED",
	"Reason":""
	},ensure_ascii=False))
#get Infomation
def getInfo(request):
	id=int(request.GET['ID'])
	account=Account.objects.get(ID=id)
	return HttpResponse(json.dumps({
	"Name":account.Name,
	"Sex":account.Sex,
	"Birthday":account.Birthday.strftime('%Y-%m-%d'),
	"Weight":account.Weight
	},ensure_ascii=False))

#get id
def getID(request):
	accountnumber=request.GET['AccountNumber']
	password=request.GET['Password']
	if len(Account.objects.filter(AccountNumber=accountnumber))!=0 and Account.objects.get(AccountNumber=accountnumber).Password==password:
		return HttpResponse(json.dumps({
	"Result":"SUCCEED",
	"Reason":"",
	"ID":Account.objects.get(AccountNumber=accountnumber).ID
	}))
	else:
		return HttpResponse(json.dumps({
	"Result":"FAIL",
	"Reason":"",
	"ID":0
	},ensure_ascii=False))
#get photo
#change photo
#get Match name/id/ list of someone
def getMatchOfPerson(request):
	id=int(request.GET['ID'])
	account=Account.objects.get(ID=id)
	matchs=account.matchofperson_set.all()
	matchs=[match.Match for match in matchs]
	names=[match.Name for match in matchs]
	ids=[match.ID for match in matchs]
	return HttpResponse(json.dumps({
	"Name":names,
	"ID":ids
	},ensure_ascii=False))
#get map id/name/length
def getMapList(request):
	maps=Map.objects.all()
	ids=[m.ID for m in maps]
	names=[m.Name for m in maps]
	lengths=[m.Length for m in maps]
	return HttpResponse(json.dumps({
	"Name":names,
	"ID":ids,
	"Length":lengths
	},ensure_ascii=False))
#get Map Point True:has scores
def getMapPoints(request):
	id=int(request.GET['ID'])
	t=bool(int(request.GET['Type']))
	points=Map.objects.get(ID=id).Points
	points=ast.literal_eval(points)
	if t:
		return HttpResponse(json.dumps({
	"Point":points[:-1],
	"Score":points[-1]
	}))
	else:
		return HttpResponse(json.dumps({
	"Point":points[:-1]
	},ensure_ascii=False))
#get march list
def getMatchList(request):
	word=request.GET['filter']
	matchs=Match.objects.all()
	matchsfilter=[]
	if word=='':
		matchsfilter=matchs
	else:
		for m in matchs:
			if m.Name.find(word)!=-1:
				matchsfilter.append(m)
	return HttpResponse(json.dumps({
	"ID":[m.ID for m in matchsfilter],
	"Name":[m.Name for m in matchsfilter],
	"Number":[m.Number for m in matchsfilter],
	"Length":[m.MapofMatch.Length for m in matchsfilter],
	"Type":[m.Type for m in matchsfilter],
	"Password":[m.Password for m in matchsfilter]
	},ensure_ascii=False))

#create team
def createTeam(request):
	creatorid=int(request.GET['CreatorID'])
	matchid=int(request.GET['MatchID'])
	account=Account.objects.get(ID=creatorid)
	match=Match.objects.get(ID=matchid)
	id=createNewTeam(match,account)
	match.Number=match.Number+1
	return HttpResponse(json.dumps({
		"ID":id}))
#join team
def joinTeam(request):
	teamid=int(request.GET['TeamID'])
	personid=int(request.GET['PersonID'])
	account=Account.objects.get(ID=personid)
	team=Team.objects.get(ID=teamid)
	if MatchOfPerson.objects.filter(Account=account,Match=team.Match):
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":"HAS JOIN IN OTHER TEAM"}))
	team.Number=team.Number+1
	team.save()
	team.Match.Number=team.Match.Number+1
	team.Match.save()
	newMatchOfPerson=MatchOfPerson(Account=account,Match=team.Match,Team=team)
	newMatchOfPerson.save()
	return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":""}))#########################

def createNewTeam(m,a,n=''):
	id=random.randint(100000,999999)
	while len(Team.objects.filter(ID=id))!=0:
		id=random.randint(100000,999999)
	n=n+str(len(m.team_set.all())) #auto
	newTeam=Team(ID=id,Match=m,Name=n,BeginTime='1970-1-1 00:00:00',
		EndTime='1970-1-1 00:00:00')
	newTeam.save()
	newMatchOfPerson=MatchOfPerson(Account=a,Match=m,Team=newTeam)
	newMatchOfPerson.save()
	return id
def createMatch(request):
	mapid=int(request.GET['MapID'])
	begintime=request.GET['beginTime']
	endtime=request.GET['endTime']
	name=request.GET['Name']
	password=request.GET['Password']
	creatorid=int(request.GET['CreatorID'])
	Type=bool(int(request.GET['Type']))
	id=random.randint(10000,99999)
	while len(Match.objects.filter(ID=id))!=0:
		id=random.randint(10000,99999)
	m=Map.objects.get(ID=mapid)
	newMatch=Match(ID=id,
		MapofMatch=m,
		BeginTime=begintime,
		EndTime=endtime,
		Name=name,
		Password=password,
		Creator=Account.objects.get(ID=creatorid),
		Type=Type,
		Length=m.Length,
		Number=1)
	newMatch.save()
	teamid=createNewTeam(newMatch,newMatch.Creator)
	return HttpResponse(json.dumps({
		"ID":id,
		"TeamID":teamid}))
#get team list
def getTeamList(request):
	matchid=int(request.GET['MatchID'])
	match=Match.objects.get(ID=matchid)
	teams=match.team_set.all()
	name=[t.Name for t in teams]
	id=[t.ID for t in teams]
	number=[t.Number for t in teams]
	return HttpResponse(json.dumps({
		"ID":id,
		"Name":name,
		"Number":number},ensure_ascii=False))
#point
def uploadPoint(request): #time
	teamid=int(request.GET['TeamID'])
	p=ast.literal_eval(request.GET['Point'])
	team=Team.objects.get(ID=teamid)
	pointlist=ast.literal_eval(team.Match.MapofMatch.Points)[:-1]
	if team.Points=='':
		teampointlist=[]
	else:
		teampointlist=ast.literal_eval(team.Points)
	if p not in teampointlist and p in pointlist:
		teampointlist.append(p)
		team.Points=str(teampointlist)
		if len(teampointlist)==1:
			team.BeginTime=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
		team.EndTime=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
		team.save()
	else:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":"HAS POINTED"}))
	if len(teampointlist)==len(pointlist):
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Complite":True,
		"Reason":""})) 
	else:
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Complite":False,
		"Reason":""})) 
#get has points
def getHavePoint(request):
	teamid=int(request.GET['TeamID'])
	return HttpResponse(json.dumps({
		'Point':ast.literal_eval(Team.objects.get(ID=teamid).Points)
		})) 
#get recoding point
def getRecodingPoint(request):
	teamid=int(request.GET['TeamID'])
	personid=int(request.GET['PersonID'])
	team=Team.objects.get(ID=teamid)
	account=Account.objects.get(ID=personid)
	return HttpResponse(json.dumps({
		'Point':ast.literal_eval(MatchOfPerson.objects.get(Account=account,Team=team).Points)
		})) 
#upload recoding point
def uploadRecodingPoint(request):
	point=ast.literal_eval(request.GET['Point'])
	length=float(request.GET['Length'])
	teamid=int(request.GET['TeamID'])
	personid=int(request.GET['PersonID'])
	account=Account.objects.get(ID=personid)
	team=Team.objects.get(ID=teamid)
	mop=MatchOfPerson.objects.get(Account=account,Team=team)
	mop.Points=str(point)
	mop.Length=length
	mop.save()
	return HttpResponse(json.dumps({
		"Result":"SUCCEED"
		})) 
#get person rank
from operator import itemgetter
def getPersonRank(request):
	accounts=Account.objects.all()
	rank=[]
	for account in accounts:
		temp=[]
		temp.append(account.Name)
		temp.append(account.ID)
		marchs=account.matchofperson_set.all()
		temp.append(len(marchs))
		temp.append(sum([m.Length for m in marchs]))
		rank.append(temp)
	rank=sorted(rank,key=itemgetter(3))
	return HttpResponse(json.dumps({
		"Name": [r[0] for r in rank],
		"ID":[r[1] for r in rank],
		"Times":[r[2] for r in rank],
		"Length":[r[3] for r in rank]
		},ensure_ascii=False)) 
#get match rank
def getMatchRank(request):
	matchs=Match.objects.all()
	rank=[]
	for match in matchs:
		temp=[]
		temp.append(match.BeginTime)
		temp.append(match.Name)
		rank.append(temp)
	rank=sorted(rank,key=itemgetter(0))
	rank=[r[1] for r in rank]
	return HttpResponse(json.dumps({
		"Name": rank
		},ensure_ascii=False)) 
#get type Info
def getTypeInfo(request):
	Type=bool(int(request.GET['Type']))
	if Type:
		return HttpResponse(json.dumps({
		"Result": u"测试1"
		},ensure_ascii=False)) 
	else:
		return HttpResponse(json.dumps({
		"Result": u"测试2"
		},ensure_ascii=False)) 
#use match id get person id time
def PersonOfMatch(request):
	matchid=int(request.GET['MatchID'])
	match=Match.objects.get(ID=matchid)
	accounts=match.matchofperson_set.all()
	accounts=[m.Account for m in accounts]
	t=[]
	for account in accounts:
		matchofperson=MatchOfPerson.objects.get(Account=account,Match=match)
		if len(matchofperson.Team.Points)!=0 and len(ast.literal_eval(matchofperson.Team.Points
			))==len(ast.literal_eval(match.MapofMatch.Points)):
			#t.append(str(matchofperson.Team.EndTime))
			t.append(matchofperson.Team.EndTime.strftime("%Y-%m-%d %H:%M:%S"))
		else:
			t.append('')

	return HttpResponse(json.dumps({
		"Name": [a.Name for a in accounts],
		"ID":[a.ID for a in accounts],
		"Time":t
		},ensure_ascii=False)) 
#get question
def getQuestion(request):
	ques=Question.objects.all()
	ques=random.sample(ques,15)
	q=[x.Question for x in ques]
	a=[x.Answer for x in ques]
	return HttpResponse(json.dumps({
		"Question": q,
		"Answer":a
		},ensure_ascii=False)) 
#createMap
def createMap(request):
	id=random.randint(1000,9999)
	while len(Map.objects.filter(ID=id))!=0:
		id=random.randint(1000,9999)
	name=request.GET['Name']
	scores=ast.literal_eval(request.GET['Score'])
	points=ast.literal_eval(request.GET['Point'])
	info=request.GET['Information']
	length=request.GET['Length']
	newMap=Map(ID=id,Name=name,Information=info,
		Length=length)
	points.append(scores)
	newMap.Points=''+str(points)
	newMap.save()
	return HttpResponse(json.dumps({
		"ID":newMap.ID,
		"Name":newMap.Name,
		"Information":newMap.Information,
		"Length":newMap.Length,
		"Points":points,
		"Scores":scores},ensure_ascii=False))

