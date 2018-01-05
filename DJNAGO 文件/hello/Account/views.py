# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render,render_to_response

from Account.models import *
import json
from django.http import HttpResponse
import random,ast,time,shutil,math
from django.utils import timezone

import sys
sys.path.append('//root//hello//lib')
# sys.path.append('C:\\Users\\ANIGHTwod\\Desktop\\dj\\lib')
from igt_push import *
from igetui.template import *
from igetui.template.igt_base_template import *
from igetui.template.igt_transmission_template import *
from igetui.template.igt_link_template import *
from igetui.template.igt_notification_template import *
from igetui.template.igt_notypopload_template import *
from igetui.igt_message import *
from igetui.igt_target import *
from igetui.template import *
APPKEY = "MKZffIxiAaAwJIMS8e9Dl1"
APPID = "g2tqBDDTPr95li2VfKlRc3"
MASTERSECRET = "uUtAikwagz6LBMMqaHXfE4"
HOST = 'http://sdk.open.api.igexin.com/apiex.htm' 

push = IGeTui(HOST, APPKEY, MASTERSECRET)
# CID='bbfb98beffb384afef3c39c619148d00'
#createTest
def isPersonInTeam(request):
	personid=int(request.GET['PersonID'])
	teamid=int(request.GET['TeamID'])
	account=Account.objects.get(ID=personid)
	team=Team.objects.get(ID=teamid)
	if len(MatchOfPerson.objects.filter(Account=account,Team=team))!=0:
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":u""},ensure_ascii=False))
	else:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"不在队伍中"},ensure_ascii=False))
def Test1(request):
    template = TransmissionTemplate()
    template.transmissionType = 1
    template.appId = APPID
    template.appKey = APPKEY
    template.transmissionContent = '{"test":"helo"}'

    # begin = "2015-03-04 17:40:22";
    # end = "2015-03-04 17:47:24";
    # template.setDuration(begin, end)
    #template.setApnInfo(apn)
    HOST = 'http://sdk.open.api.igexin.com/apiex.htm' #http://sdk.open.api.igexin.com/apiex.htm
    push = IGeTui(HOST, APPKEY, MASTERSECRET)
    #push = IGeTui("",APPKEY,MASTERSECRET)#此方式可通过获取服务端地址列表判断最快域名后进行消息推送，每10分钟检查一次最快域名
    #消息模版：
    #TransmissionTemplate:透传功能模板，定义透传内容，应用启动形式
    # 定义"SingleMessage"消息体，设置是否离线，离线有效时间，模板设置
    message = IGtSingleMessage()
    message.isOffline = False
    message.offlineExpireTime = 0
    message.data = template
    message.pushNetWorkType = 0#设置是否根据WIFI推送消息，2为4G/3G/2G,1为wifi推送，0为不限制推送
    target = Target()
    target.appId = APPID
    target.clientId = CID
    #target.alias = ALIAS

    try:
        ret = push.pushMessageToSingle(message, target)
        return HttpResponse(json.dumps({
		"Result":ret
		})) 
    except RequestException, e:
        # 发生异常重新发送
        requstId = e.getRequestId()
        ret = push.pushMessageToSingle(message, target, requstId)
        return HttpResponse(json.dumps({
		"Result":ret
		})) 
def getMatchPassword(request):
	id=int(request.GET['MatchID'])
	match=Match.objects.get(ID=id)
	return HttpResponse(json.dumps({
		"Password":match.Password
		})) 
def getPersonDayMile(request):
	id=int(request.GET['PersonID'])
	if id==98966789 or id==38213675:
		Length=[5,4,7,3,2,6,8]
		days=["12-26", "12-25", "12-24", "12-23", "12-22", "12-21", "12-20"]
		return HttpResponse(json.dumps({
		"Time":days,
		"Length":Length
		})) 
		# {"Length": [0, 0, 0, 0, 0, 0, 0], "Time": ["12-24", "12-23", "12-22", "12-21", "12-20", "12-19", "12-18"]}
	account=Account.objects.get(ID=id)
	nt=time.strftime('%m-%d',time.localtime(time.time()))
	days=[time.strftime('%Y-%m-%d',time.localtime(time.time()-3600*24*i)) for i in range(7)]
	Length=[]
	matchofperson=account.matchofperson_set.all()
	for day in days:
		l=0
		for match in matchofperson:
			team=match.Team
			if team.EndTime.strftime("%Y-%m-%d")==day:
				l+=match.Length
		Length.append(int(l))
	days=[time.strftime('%m-%d',time.localtime(time.time()-3600*24*i)) for i in range(7)]
	return HttpResponse(json.dumps({
		"Time":days,
		"Length":Length
		})) 
def returnToGetui(info,cids):
    template = TransmissionTemplate()
    template.transmissionType = 1
    template.appId = APPID
    template.appKey = APPKEY
    template.transmissionContent = info
    # push = IGeTui(HOST, APPKEY, MASTERSECRET)
    message = IGtSingleMessage()
    message.isOffline = False
    message.offlineExpireTime = 0
    message.data = template
    message.pushNetWorkType = 0#设置是否根据WIFI推送消息，2为4G/3G/2G,1为wifi推送，0为不限制推送
    target = Target()
    target.appId = APPID
    for cid in cids:
	    target.clientId = cid
	    push.pushMessageToSingle(message, target)
def returnPoint(CID,teamid,p):
 	if len(CID)==0:
 		return
 	cids=ast.literal_eval(CID)
 	#http://sdk.open.api.igexin.com/apiex.htm
 	info=json.dumps({
		"makePoint":str(p)+'$'+str(teamid)+str(random.randint(10000,99999))})
 	#   long(p*math.pow(10,11)+teamid*math.pow(10,5)+random.randint(10000,99999))
 	returnToGetui(info,cids)
#point
def uploadPoint(request): #time
	teamid=int(request.GET['TeamID'])
	p=int(request.GET['Point'])
	team=Team.objects.get(ID=teamid)
	if timezone.now()<team.Match.BeginTime or timezone.now()>team.Match.EndTime:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"不在比赛时间内"},ensure_ascii=False))
	pointlist=ast.literal_eval(team.Match.MapofMatch.Points)[:-1]
	temp=pointlist[p]
	if team.Points=='':
		teampointlist=[]
	else:
		teampointlist=ast.literal_eval(team.Points)
	if temp not in teampointlist and temp in pointlist:
		teampointlist.append(temp)
		team.Points=str(teampointlist)
		if len(teampointlist)==1:
			team.BeginTime=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
		team.EndTime=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
		team.save()
	else:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"重复打点"},ensure_ascii=False))
	if len(teampointlist)==len(pointlist):
		returnPoint(team.CID,teamid,p)
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Complite":True,
		"Reason":""})) 
	else:
		returnPoint(team.CID,teamid,p)
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Complite":False,
		"Reason":""})) 
def getTeamTime(request):
	teamid=int(request.GET['TeamID'])
	team=Team.objects.get(ID=teamid)
	return HttpResponse(json.dumps({
		"Time":str(team.EndTime-team.BeginTime)
		})) 

def uploadCID(request):
	CID=request.GET['CID']
	teamid=request.GET['TeamID']
	team=Team.objects.get(ID=teamid)
	if len(team.CID)!=0:
		cids=ast.literal_eval(team.CID)
	else:
		cids=[]
	if CID not in cids:
		cids.append(CID)
	team.CID=str(cids)
	team.save()
	return HttpResponse(json.dumps({
			"Result":"SUCCEED"
			})) 

def uploadHead(request):
	if request.method=='POST':
		id=request.POST.get('id')
		head=request.FILES.get('head')
		#u'/home/root/hello/static/'
		path=u'/home/root/hello/static/'+id+u'.jpg'
		f=open(path,'wb')
		for chunk in head.chunks():
			f.write(chunk)
		f.close()
		return HttpResponse(json.dumps({
		"Result":'SUCCEED'
		},ensure_ascii=False))
	return render_to_response("uploadhead.html")
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
		"Reason":u"账号已注册",
		"ID":0
		},ensure_ascii=False))
	id=random.randint(10000000,99999999)
	while len(Account.objects.filter(ID=id))!=0:
		id=random.randint(10000000,99999999)
		#shutil.copy('/root/hello/h1.jpg','/home/root/hello/static/'+str(1)+'.jpg')
	shutil.copy('/root/hello/h1.jpg','/home/root/hello/static/'+str(id)+'.jpg')
	
	newAccount=Account(ID=id,AccountNumber=accountnumber,
	Password=password,Name=name,Sex=sex,Birthday=birth,
	Weight=weight)
	newAccount.save()
	return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":"",
		"ID":id
		},ensure_ascii=False))
#change password
def changePassword(request):
	an=int(request.GET['AccountNumber'])
	op=request.GET['oldPassword']
	np=request.GET['newPassword']
	force=bool(int(request.GET['Force']))
	account=Account.objects.get(AccountNumber=an)
	if op==account.Password or force:
		account.Password=np
		account.save()
		return HttpResponse(json.dumps({
		"Result":"SUCCEED",
		"Reason":""
		}))
	else:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"密码错误"
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
	"Reason":u"账号或密码错误",
	"ID":0
	},ensure_ascii=False))
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
	"Password":[m.Password for m in matchsfilter],
	"beginTime":[m.BeginTime.strftime("%Y-%m-%d") for m in matchsfilter],
	"endTime":[m.EndTime.strftime("%Y-%m-%d") for m in matchsfilter],
	"mapName":[m.MapofMatch.Name for m in matchsfilter],
	"ImgUrl":[u'http://45.32.72.80/static/'+str(m.MapofMatch.ID)+'.jpg' for m in matchsfilter],
	},ensure_ascii=False))

#create team
def createTeam(request):
	creatorid=int(request.GET['CreatorID'])
	matchid=int(request.GET['MatchID'])
	account=Account.objects.get(ID=creatorid)
	match=Match.objects.get(ID=matchid)
	if  timezone.now()>match.EndTime:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"比赛以结束"},ensure_ascii=False))
	if MatchOfPerson.objects.filter(Account=account,Match=match):
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"已经加入其他队伍"},ensure_ascii=False))
	id=createNewTeam(match,account)
	match.Number=match.Number+1
	return HttpResponse(json.dumps({
		"ID":id,
		"Result":"SUCCEED",
		"Reason":""}))
#join team
def joinTeam(request):
	teamid=int(request.GET['TeamID'])
	personid=int(request.GET['PersonID'])
	account=Account.objects.get(ID=personid)
	team=Team.objects.get(ID=teamid)
	if  timezone.now()>team.Match.EndTime:
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"比赛已结束"},ensure_ascii=False))
	if MatchOfPerson.objects.filter(Account=account,Match=team.Match):
		return HttpResponse(json.dumps({
		"Result":"FAIL",
		"Reason":u"已经加入其他队伍"},ensure_ascii=False))
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
	shutil.copy('/root/hello/'+str(mapid)+'.jpg','/home/root/hello/static/'+str(id)+'.jpg')
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

#get has points
def getHavePoint(request):
	teamid=int(request.GET['TeamID'])
	return HttpResponse(json.dumps({
		'Point':ast.literal_eval(Team.objects.get(ID=teamid).Points)
		})) 
#upload recoding point
def uploadRecodingPoint(request):
	if request.method=='POST':
		point=request.POST.get('text')
		teamid=int(request.POST.get('teamid'))
		personid=int(request.POST.get('personid'))
		account=Account.objects.get(ID=personid)
		team=Team.objects.get(ID=teamid)
		mop=MatchOfPerson.objects.get(Account=account,Team=team)
		mop.Points=point
		mop.save()
		return HttpResponse(json.dumps({
			"Result":"SUCCEED"
			})) 
	return render_to_response("uploadpoint.html")
#get recoding point
def getRecodingPoint(request):
	personid=int(request.GET['PersonID'])
	matchid=int(request.GET['MatchID'])
	account=Account.objects.get(ID=personid)
	match=Match.objects.get(ID=matchid)
	return HttpResponse(
		str(MatchOfPerson.objects.get(Account=account,Match=match).Points)
		) 

#upload recoding length
def uploadRecodingLength(request):
	length=float(request.GET['Length'])
	teamid=int(request.GET['TeamID'])
	personid=int(request.GET['PersonID'])
	account=Account.objects.get(ID=personid)
	team=Team.objects.get(ID=teamid)
	mop=MatchOfPerson.objects.get(Account=account,Team=team)
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
		"Points":points[:-1],
		"Scores":scores},ensure_ascii=False))

#Getpersonoverview
def getPersonOverview(request):
	id=int(request.GET['ID'])
	if id==98966789 or id==38213675:
		return HttpResponse(json.dumps({
		"Times":11,
		"Length":37,
		"Duration":"23"+"(h)"
		},ensure_ascii=False))
	account=Account.objects.get(ID=id)
	matchlist=account.matchofperson_set.all()
	duration=[m.Team.EndTime-m.Team.BeginTime for m in matchlist]
	if len(duration)!=0:
		t=duration[0]
		for d in range(1,len(duration)):
			t=t+duration[d]
		return HttpResponse(json.dumps({
		"Times":len(matchlist),
		"Length":sum([m.Length for m in matchlist]),
		"Duration":"%.1f" % (t.total_seconds()/3600)+"(h)"
		},ensure_ascii=False))
	else:
		t='0(h)'
		return HttpResponse(json.dumps({
			"Times":len(matchlist),
			"Length":sum([m.Length for m in matchlist]),
			"Duration":t
			},ensure_ascii=False))